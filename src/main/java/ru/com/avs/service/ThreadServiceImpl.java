package ru.com.avs.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import ru.com.avs.model.ThreadModel;
import ru.com.avs.thread.CameraThread;
import ru.com.avs.thread.ScaleThread;

@Service("ThreadService")
public class ThreadServiceImpl implements ThreadService {

    private List<ThreadModel> threadModelList;

    public ThreadServiceImpl() {
        threadModelList = new ArrayList<>();
    }

    @Override
    public void addThread(ThreadModel threadModel) {
        threadModelList.add(threadModel);
    }

    @Override
    public void stopThreads() {
        for (ThreadModel threadModel : threadModelList) {
            switch (threadModel.getType()) {
                case ThreadModel.CAMERA_THREAD:
                    CameraThread cameraThread = (CameraThread) threadModel.getThread();
                    cameraThread.stopThread();
                    break;
                case ThreadModel.SCALE_THREAD:
                    ScaleThread scaleThread = (ScaleThread) threadModel.getThread();
                    scaleThread.stopThread();
                    break;
                default:
                    break;
            }
        }
        threadModelList.removeIf(e -> (!e.getThread().isAlive()));
    }

    @Override
    public ThreadModel getThread(String threadType, int scaleId) {
        return threadModelList.stream()
                .filter(threadModel -> scaleId == threadModel.getScaleId()
                        && threadType.equals(threadModel.getType()))
                .findFirst()
                .orElse(null);
    }
}
