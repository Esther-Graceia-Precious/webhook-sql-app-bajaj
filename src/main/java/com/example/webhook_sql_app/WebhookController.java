package com.example.webhooksqlapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    @Autowired
    private com.example.webhooksqlapp.WebhookRepository repository;

    @PostMapping
    public String receiveWebhook(@RequestBody String payload) {
        com.example.webhooksqlapp.WebhookData data = new com.example.webhooksqlapp.WebhookData(payload);
        repository.save(data);
        return "Webhook received and saved!";
    }
}
