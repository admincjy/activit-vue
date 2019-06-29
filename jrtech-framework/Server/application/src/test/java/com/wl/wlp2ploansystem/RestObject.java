package com.wl.wlp2ploansystem;

import java.time.LocalDateTime;

public class RestObject {

    public RestObject setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    private LocalDateTime timestamp;

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}