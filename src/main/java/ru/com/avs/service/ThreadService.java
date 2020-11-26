package ru.com.avs.service;

import ru.com.avs.model.ThreadModel;

public interface ThreadService {
    void addThread(ThreadModel threadModel);

    void stopThreads();

    ThreadModel getThread(String threadType, int scaleId);
}
