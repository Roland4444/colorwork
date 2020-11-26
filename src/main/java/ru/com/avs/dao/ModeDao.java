package ru.com.avs.dao;

import java.util.List;

import ru.com.avs.model.Mode;

public interface ModeDao extends Dao<Mode> {

    List<Mode> getList();
}
