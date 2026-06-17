package com.example.chatroom;

import java.time.Clock;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Component;

@Component
public class RoomTextStore {

    private final ConcurrentMap<String, String> roomTexts = new ConcurrentHashMap<>();
    private final Clock clock;
    private LocalDate lastResetDate;

    public RoomTextStore() {
        this(Clock.systemDefaultZone());
    }

    RoomTextStore(Clock clock) {
        this.clock = clock;
        this.lastResetDate = LocalDate.now(clock);
    }

    public String getText(String roomId) {
        clearIfNewDay();
        return roomTexts.getOrDefault(roomId, "");
    }

    public String saveText(String roomId, String text) {
        clearIfNewDay();
        String safeText = text == null ? "" : text;
        roomTexts.put(roomId, safeText);
        return safeText;
    }

    private synchronized void clearIfNewDay() {
        LocalDate today = LocalDate.now(clock);
        if (!today.equals(lastResetDate)) {
            roomTexts.clear();
            lastResetDate = today;
        }
    }
}
