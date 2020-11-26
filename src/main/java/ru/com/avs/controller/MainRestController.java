package ru.com.avs.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import ru.com.avs.model.Metal;
import ru.com.avs.model.Mode;
import ru.com.avs.model.Tare;
import ru.com.avs.model.TempTare;
import ru.com.avs.model.ThreadModel;
import ru.com.avs.model.Waybill;
import ru.com.avs.model.Weighing;
import ru.com.avs.service.MetalService;
import ru.com.avs.service.ModeService;
import ru.com.avs.service.PropertyService;
import ru.com.avs.service.TareService;
import ru.com.avs.service.TempTareService;
import ru.com.avs.service.ThreadService;
import ru.com.avs.service.WaybillService;
import ru.com.avs.service.WeighingService;
import ru.com.avs.thread.CameraThread;
import ru.com.avs.thread.ScaleThread;
import ru.com.avs.util.SpringLoader;

@RestController
@RequestMapping("/api")
public class MainRestController extends AbstractController {

    @Autowired
    private MetalService metalService;

    @Autowired
    private WeighingService weighingService;

    @Autowired
    private WaybillService waybillService;

    @Autowired
    private TareService tareService;

    @Autowired
    private PropertyService property;

    @Autowired
    private TempTareService tempTareService;

    private ThreadService threadService;

    private ModeService modeService;

    /**
     * Send list of metals service to client.
     *
     * @return result
     * @throws IOException exception
     */
    @RequestMapping(value = "/list-metals", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public String getListMetals() throws IOException {
        List<Metal> list = metalService.getAll();
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(list);
    }

    /**
     * Get list tare.
     *
     * @return String json
     */
    @RequestMapping(value = "/list-tares", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public String getListTares() throws IOException {
        List<Tare> tares = tareService.getList();
        ObjectMapper objectMapper = new ObjectMapper();

        return objectMapper.writeValueAsString(tares);
    }

    /**
     * Get weight from scales.
     *
     * @param scaleId Scale.id
     * @return String weight
     */
    @RequestMapping(value = "/get-weight", method = RequestMethod.GET, produces = "text/plain; charset=utf-8")
    public String getWeight(@RequestParam(value = "scaleId") Integer scaleId) {
        threadService = (ThreadService) SpringLoader.getBean("ThreadService");
        ScaleThread scaleThread = (ScaleThread) threadService.getThread(ThreadModel.SCALE_THREAD, scaleId).getThread();
        return scaleThread.getWeight();
    }

    /**
     * When weighing if over.
     *
     * @param state     String state of weighting
     * @param waybillId Waybill.id
     * @return status
     */
    @RequestMapping(value = "/end-weighing", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public String endWeighing(@RequestParam(value = "state") String state,
                              @RequestParam(value = "waybillId") Integer waybillId) {

        Waybill waybill = waybillService.getById(waybillId);
        switch (state) {
            case "complete":
                for (TempTare tempTare : waybill.getTempTares()) {
                    tempTareService.delete(tempTare);
                }
                waybill.setComplete(true);
                waybillService.save(waybill);
                new Thread(() -> {
                    waybillService.exportWaybill(waybill);
                }).start();

                Platform.runLater(() -> {
                    PrintController controller
                            = (PrintController) runController("printForm", "Печать чека", true);
                    controller.initData(waybill, true);
                });
                break;
            case "cancel":
                waybillService.delete(waybill);
                break;
            default:
                break;
        }
        return "ok";
    }

    /**
     * Save weighing from client.
     *
     * @param json Json Weighing
     * @return waybill id
     * @throws IOException exception
     */
    @RequestMapping(value = "/set-weighing", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity setWeighing(@RequestBody() String json) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        Waybill waybill = objectMapper.readValue(json, Waybill.class);

        if (waybill.getId() == 0) {
            waybill = createWaybillModel(waybill);
            waybillService.save(waybill);
        }

        Weighing weighing = waybill.getWeighings().stream().findFirst().get();
        weighing.setWaybill(waybill);
        if (weighing.getBrutto().longValue() < weighing.getTare().longValue()) {
            BigDecimal tmp = weighing.getBrutto();
            weighing.setBrutto(weighing.getTare());
            weighing.setTare(tmp);
        }
        weighingService.save(weighing);

        threadService = (ThreadService) SpringLoader.getBean("ThreadService");
        CameraThread cameraThread =
                (CameraThread) threadService.getThread(ThreadModel.CAMERA_THREAD, waybill.getScaleId()).getThread();
        cameraThread.getSnapshot(String.valueOf(weighing.getId()));

        waybill = waybillService.getById(waybill.getId());
        String retJson = objectMapper.writeValueAsString(waybill);
        return ResponseEntity.ok(retJson);
    }

    /**
     * Get settings from server to android app.
     *
     * @return ResponseEntity
     */
    @RequestMapping(value = "/get-settings", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity getSettings(@RequestParam(value = "modeId") Integer modeId) throws IOException {
        modeService = (ModeService) SpringLoader.getBean("ModeService");
        Mode mode = modeService.getActiveMode();

        if (mode != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writeValueAsString(mode);
            return new ResponseEntity<>(json, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Не выбран режим", HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * Delete weighing by id.
     *
     * @param weighingId Weighing.id
     * @return ResponseEntity
     */
    @RequestMapping(value = "/weighing", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    public ResponseEntity weighingDelete(@RequestParam(value = "weighingId") Integer weighingId) {
        Weighing weighing = weighingService.getById(weighingId);
        weighingService.delete(weighing);
        return ResponseEntity.ok("ok");
    }

    /**
     * Get list of not completed waybills.
     *
     * @return ResponseEntity
     */
    @RequestMapping(value = "/get-waybills", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    public ResponseEntity getWaybills() throws IOException {
        List<Waybill> waybills = waybillService.getNotCompleted();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(waybills);
        return ResponseEntity.ok(json);
    }

    /**
     * Save TempTare to base.
     *
     * @param json Waybill
     * @return Waybill with id;
     */
    @RequestMapping(value = "set-tare", method = RequestMethod.PUT, produces = "application/json; charset=utf-8")
    public ResponseEntity createTempTare(@RequestBody() String json) throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        Waybill waybill = objectMapper.readValue(json, Waybill.class);

        if (waybill.getId() == 0) {
            waybill = createWaybillModel(waybill);
            waybillService.save(waybill);
        }

        TempTare tempTare = waybill.getTempTares().stream().findFirst().get();
        tempTare.setWaybill(waybill);
        tempTareService.save(tempTare);

        waybill = waybillService.getById(waybill.getId());
        List<TempTare> tareList = new ArrayList<>();
        tareList.add(tempTare);
        waybill.setTempTares(tareList);
        String retJson = objectMapper.writeValueAsString(waybill);
        return ResponseEntity.ok(retJson);
    }

    /**
     * Delete TempTare.
     *
     * @param tareId TempTare.id
     * @return status
     */
    @RequestMapping(value = "tare", method = RequestMethod.DELETE, produces = "application/json; charset=utf-8")
    public ResponseEntity deleteTempTare(@RequestParam(value = "tareId") Integer tareId) {
        TempTare tare = tempTareService.get(tareId);
        tempTareService.delete(tare);
        return ResponseEntity.ok("ok");
    }

    /**
     * Stop creating waybill. Check for empty waybill.
     *
     * @param waybillId Waybill.id
     * @return status
     */
    @RequestMapping(value = "stop", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity stop(@RequestParam(value = "waybillId") Integer waybillId) {
        Waybill waybill = waybillService.getById(waybillId);

        if (waybill.getWeighings().size() == 0 && waybill.getTempTares().size() == 0) {
            waybillService.delete(waybill);
        }
        return ResponseEntity.ok("ok");
    }

    private Waybill createWaybillModel(Waybill waybill) {
        waybill.setDateCreate(LocalDate.now());
        waybill.setTimeCreate(LocalTime.now());
        waybill.setDepartmentId(property.getIntProperty("department.id"));
        waybill.setWaybill(waybillService.getLastWaybillNumber());
        modeService = (ModeService) SpringLoader.getBean("ModeService");
        waybill.setMode(modeService.getActiveMode().getModeCode());
        return waybill;
    }
}
