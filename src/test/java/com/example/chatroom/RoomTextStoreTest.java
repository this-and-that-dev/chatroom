package com.example.chatroom;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoomTextStoreTest {

    @Test
    void clearsStoredRoomTextsOncePerNewDay() {
        MutableClock clock = new MutableClock("2026-06-18T10:00:00Z");
        RoomTextStore store = new RoomTextStore(clock);

        store.saveText("alpha", "전날 글");

        clock.setInstant("2026-06-19T00:00:00Z");

        assertThat(store.getText("alpha")).isEmpty();

        store.saveText("alpha", "오늘 글");

        assertThat(store.getText("alpha")).isEqualTo("오늘 글");
    }

    private static class MutableClock extends Clock {
        private Instant instant;

        MutableClock(String instant) {
            this.instant = Instant.parse(instant);
        }

        void setInstant(String instant) {
            this.instant = Instant.parse(instant);
        }

        @Override
        public ZoneId getZone() {
            return ZoneId.of("UTC");
        }

        @Override
        public Clock withZone(ZoneId zone) {
            return Clock.fixed(instant, zone);
        }

        @Override
        public Instant instant() {
            return instant;
        }
    }
}
