package com.example.service;

import okhttp3.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.*;

import java.io.IOException;
import java.time.Duration;

public class OllamaClient {
    // timeout để tránh treo UI nếu server chậm
    private final OkHttpClient http = new OkHttpClient.Builder()
            .callTimeout(Duration.ofSeconds(120))
            .connectTimeout(Duration.ofSeconds(10))
            .readTimeout(Duration.ofSeconds(120))
            .writeTimeout(Duration.ofSeconds(120))
            .build();

    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl;   // vd: http://localhost:11434  hoặc http://host.docker.internal:11434 (nếu app chạy trong container)
    private final String model;     // vd: llama3.1:8b, llama3.2:3b-instruct

    public OllamaClient(String baseUrl, String model) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
        this.model = model;
    }

    /** Gọi /api/chat của Ollama (non-stream) và trả content đầy đủ. */
    public String chat(String systemPrompt, String userMessage, double temperature) throws IOException {
        ObjectNode root = mapper.createObjectNode();
        root.put("model", model);
        root.put("stream", false);

        ObjectNode options = mapper.createObjectNode();
        options.put("temperature", temperature);
        root.set("options", options);

        ArrayNode messages = mapper.createArrayNode();
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            ObjectNode sys = mapper.createObjectNode();
            sys.put("role", "system");
            sys.put("content", systemPrompt);
            messages.add(sys);
        }
        ObjectNode user = mapper.createObjectNode();
        user.put("role", "user");
        user.put("content", userMessage);
        messages.add(user);
        root.set("messages", messages);

        String json = mapper.writeValueAsString(root);

        Request req = new Request.Builder()
                .url(baseUrl + "/api/chat")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(json, MediaType.get("application/json; charset=utf-8")))
                .build();

        try (Response res = http.newCall(req).execute()) {
            String body = res.body() != null ? res.body().string() : "";
            if (!res.isSuccessful()) {
                throw new IOException("Ollama HTTP " + res.code() + " - " + body);
            }
            JsonNode node = mapper.readTree(body);
            return node.path("message").path("content").asText();
        }
    }
}
