package ru.com.avs.view;

import javafx.event.Event;
import javafx.event.EventType;

public class CustomEvent extends Event {

    public static final EventType<Event> CANCEL = EventType.ROOT;
    public static final EventType<Event> SAVE = EventType.ROOT;

    public CustomEvent(EventType<? extends Event> eventType) {
        super(eventType);
    }
}
