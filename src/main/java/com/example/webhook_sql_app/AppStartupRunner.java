package com.example.webhooksqlapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@Component
public class AppStartupRunner implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(AppStartupRunner.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void run(String... args) {
        System.out.println("AppStartupRunner is running!");
        log.info("AppStartupRunner is running!");

        try {
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";
            Map<String, String> body = Map.of(
                    "name", "Esther Graceia Precious A",
                    "regNo", "22BCE3053",
                    "email", "esther.graceiaprecious@gmail.com"
            );

            ResponseEntity<Map> response = restTemplate.postForEntity(url, body, Map.class);
            if (response.getBody() != null) {
                String webhookUrl = (String) response.getBody().get("webhook");
                String accessToken = (String) response.getBody().get("accessToken");

                log.info("Webhook URL: {}", webhookUrl);
                log.info("Access Token: {}", accessToken);

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

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", accessToken);
                headers.setContentType(MediaType.APPLICATION_JSON);

                Map<String, String> answerBody = Map.of("finalQuery", finalQuery);
                HttpEntity<Map<String, String>> request = new HttpEntity<>(answerBody, headers);

                ResponseEntity<String> submitResponse =
                        restTemplate.postForEntity(webhookUrl, request, String.class);

                log.info("Submission response: {}", submitResponse.getBody());
            } else {
                log.error("No body in webhook generation response");
            }

        } catch (Exception e) {
            log.error("Error during webhook process", e);
        }
    }
}
