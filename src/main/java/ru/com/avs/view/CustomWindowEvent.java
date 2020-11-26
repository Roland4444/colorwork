package ru.com.avs.view;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class CustomWindowEvent extends WindowEvent {
    public static final EventType<WindowEvent> CANCEL =
            new EventType<>(CustomEvent.CANCEL, "CANCEL");

    public static final EventType<WindowEvent> SAVE =
            new EventType<>(CustomEvent.SAVE, "SAVE");


    /**
     * Construct a new {@code Event} with the specified event source, target
     * and type. If the source or target is set to {@code null}, it is replaced
     * by the {@code NULL_SOURCE_TARGET} value.
     *
     * @param source    the event source which sent the event
     * @param eventType the event type
     */
    public CustomWindowEvent(Window source, EventType<? extends Event> eventType) {
        super(source, eventType);
    }
}
