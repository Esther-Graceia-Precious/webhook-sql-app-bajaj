package com.example.webhooksqlapp;

import jakarta.persistence.*;

@Entity
public class WebhookData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String payload;

    public WebhookData() {}

    public WebhookData(String payload) {
        this.payload = payload;
    }

    // Getters and setters
    public Long getId() { return id; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
}
