package com.example.chatroom;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.stereotype.Component;

@Component
public class RoomTextStore {

    private final ConcurrentMap<String, String> roomTexts = new ConcurrentHashMap<>();

    public String getText(String roomId) {
        return roomTexts.getOrDefault(roomId, "");
    }

    public String saveText(String roomId, String text) {
        String safeText = text == null ? "" : text;
        roomTexts.put(roomId, safeText);
        return safeText;
    }
}
