package ru.com.avs.service;

import java.util.List;

import ru.com.avs.model.Mode;

public interface ModeService {
    List<Mode> getList();

    Mode getById(int id);

    Mode getByCode(List<Mode> modes, String modeCode);

    Mode getActiveMode();

    void save(Mode mode);

    void delete(Mode mode);
}
