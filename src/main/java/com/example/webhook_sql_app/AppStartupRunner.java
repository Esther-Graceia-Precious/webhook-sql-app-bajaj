package com.example.webhooksqlapp.runner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Component
public class AppStartupRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AppStartupRunner.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void run(String... args) {
        // Confirm that the runner is executing
        System.out.println("AppStartupRunner is running!");
        log.info("AppStartupRunner is running!");

        try {
            // Step 1: Generate webhook
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
            Map<String, String> body = Map.of(
                    "name", "Esther Graceia Precious A",
                    "regNo", "22BCE3053",
                    "email", "esther.graceiaprecious@gmail.com"
            );

            ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);
            String webhookUrl = (String) response.getBody().get("webhook");
            String accessToken = (String) response.getBody().get("accessToken");

            log.info("Webhook URL: {}", webhookUrl);
            log.info("Access Token: {}", accessToken);

            // Step 2: Prepare your final SQL query
            String finalQuery = """
                SELECT
                    p.AMOUNT AS SALARY,
                    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
                    TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,
                    d.DEPARTMENT_NAME
                FROM PAYMENTS p
                JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
                JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
                WHERE DAY(p.PAYMENT_TIME) <> 1
                ORDER BY p.AMOUNT DESC
                LIMIT 1;
            """;

            // Step 3: Submit final SQL query with JWT token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", accessToken);
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, String> answerBody = Map.of("finalQuery", finalQuery);
            HttpEntity<Map<String, String>> request = new HttpEntity<>(answerBody, headers);

            ResponseEntity<String> submitResponse =
                    restTemplate.postForEntity(webhookUrl, request, String.class);

            log.info("Submission response: {}", submitResponse.getBody());

        } catch (Exception e) {
            log.error("Error during webhook process", e);
        }
    }
}
