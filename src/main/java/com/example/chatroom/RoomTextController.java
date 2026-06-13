package com.example.chatroom;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/rooms")
public class RoomTextController {

    private final RoomTextStore roomTextStore;

    public RoomTextController(RoomTextStore roomTextStore) {
        this.roomTextStore = roomTextStore;
    }

    @GetMapping("/{roomId}/text")
    public RoomTextResponse getText(@PathVariable String roomId) {
        return new RoomTextResponse(roomId, roomTextStore.getText(roomId));
    }

    @PutMapping("/{roomId}/text")
    public RoomTextResponse saveText(@PathVariable String roomId, @RequestBody TextRequest request) {
        String savedText = roomTextStore.saveText(roomId, request.getText());
        return new RoomTextResponse(roomId, savedText);
    }

    public static class TextRequest {
        private String text;

        public TextRequest() {
        }

        public TextRequest(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public static class RoomTextResponse {
        private final String roomId;
        private final String text;

        public RoomTextResponse(String roomId, String text) {
            this.roomId = roomId;
            this.text = text;
        }

        public String getRoomId() {
            return roomId;
        }

        public String getText() {
            return text;
        }
    }
}
