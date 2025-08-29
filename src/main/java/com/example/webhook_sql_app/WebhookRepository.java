package com.example.webhooksqlapp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WebhookRepository extends JpaRepository<WebhookData, Long> {
}
