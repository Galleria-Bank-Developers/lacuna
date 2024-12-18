package com.api.lacunaapi.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WebhookPayload {
    private String id;
    private String status;

    @Override
    public String toString() {
        return "WebhookPayload{id='" + id + "', status='" + status + "'}";
    }
}
