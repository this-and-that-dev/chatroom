package com.example.chatroom;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RoomTextControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void savesAndReadsTextByRoom() throws Exception {
        mockMvc.perform(put("/api/rooms/alpha/text")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new TextRequest("첫 번째 브라우저에서 쓴 글"))))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.roomId", is("alpha")))
            .andExpect(jsonPath("$.text", is("첫 번째 브라우저에서 쓴 글")));

        mockMvc.perform(get("/api/rooms/alpha/text"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.roomId", is("alpha")))
            .andExpect(jsonPath("$.text", is("첫 번째 브라우저에서 쓴 글")));
    }

    @Test
    void keepsRoomsSeparated() throws Exception {
        mockMvc.perform(put("/api/rooms/alpha/text")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new TextRequest("알파 방"))))
            .andExpect(status().isOk());

        mockMvc.perform(put("/api/rooms/beta/text")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new TextRequest("베타 방"))))
            .andExpect(status().isOk());

        mockMvc.perform(get("/api/rooms/alpha/text"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.text", is("알파 방")));

        mockMvc.perform(get("/api/rooms/beta/text"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.text", is("베타 방")));
    }

    @Test
    void returnsEmptyTextForNewRoom() throws Exception {
        mockMvc.perform(get("/api/rooms/new-room/text"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.roomId", is("new-room")))
            .andExpect(jsonPath("$.text", is("")));
    }

    private record TextRequest(String text) {
    }
}
