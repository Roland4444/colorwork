package ru.com.avs.dao;

import java.util.List;

import ru.com.avs.model.Scale;

public interface ScaleDao extends Dao<Scale> {

    List<Scale> getByMode(int modeId);
}
