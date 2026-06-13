package com.example.chatroom;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class StaticPageTest {

    @Test
    void rendersCloudAndLocalTextAreas() throws Exception {
        String html = new String(Objects.requireNonNull(
            getClass().getResourceAsStream("/static/index.html")
        ).readAllBytes(), StandardCharsets.UTF_8);

        assertThat(html)
            .contains("id=\"cloudText\"")
            .contains("id=\"localText\"")
            .contains("id=\"localFile\"")
            .contains("id=\"uploadLocalFileButton\"")
            .contains("/api/files")
            .contains("https://chatroom-271063202893.asia-northeast3.run.app")
            .contains("const isCloudRunPage = window.location.hostname === cloudRunHost")
            .contains("[data-local-only]")
            .contains("class=\"panel\" data-local-only")
            .contains("class=\"bridge-actions\" data-local-only")
            .contains("class=\"file-transfer\" data-local-only")
            .contains("classList.add(\"remote-only\")")
            .contains("원격서버 API")
            .contains("원격서버 텍스트")
            .contains("로컬서버 텍스트")
            .contains("로컬서버 파일 전송")
            .contains("원격서버 -&gt; 로컬서버 복사")
            .contains("로컬서버 -&gt; 원격서버 복사")
            .doesNotContain("id=\"cloudApiBase\"")
            .doesNotContain("id=\"localApiBase\"")
            .doesNotContain("id=\"refreshBothButton\"")
            .doesNotContain("양쪽 읽기")
            .doesNotContain("Cloud Run")
            .doesNotContain(">Local ");
    }
}
