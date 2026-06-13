package com.example.chatroom;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LocalFileControllerTest {

    @TempDir
    static Path tempDir;

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void configureUploadDir(DynamicPropertyRegistry registry) {
        registry.add("chatroom.upload-dir", () -> tempDir.resolve("uploads").toString());
    }

    @Test
    void uploadsFileToConfiguredLocalDirectory() throws Exception {
        Path uploadDir = tempDir.resolve("uploads");
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "memo.txt",
            "text/plain",
            "업무컴퓨터에서 보낸 파일".getBytes()
        );

        mockMvc.perform(multipart("/api/files").file(file))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.fileName", is("memo.txt")))
            .andExpect(jsonPath("$.size", is(35)));

        Path savedFile = uploadDir.resolve("memo.txt");
        org.assertj.core.api.Assertions.assertThat(savedFile).exists();
        org.assertj.core.api.Assertions.assertThat(Files.readString(savedFile)).isEqualTo("업무컴퓨터에서 보낸 파일");
    }
}
