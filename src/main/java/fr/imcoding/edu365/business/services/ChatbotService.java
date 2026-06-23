package fr.imcoding.edu365.business.services;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ChatbotService {

    @Value("${groq.api.key}")
    private String apiKey;

    private static final String GROQ_URL =
            "https://api.groq.com/openai/v1/chat/completions";

    public String chat(String userMessage) throws Exception {
        OkHttpClient client = new OkHttpClient();

        String json = "{"
                + "\"model\": \"llama-3.3-70b-versatile\","
                + "\"messages\": [{"
                + "\"role\": \"system\","
                + "\"content\": \"Tu es un assistant virtuel de FreeAcademy, une plateforme d education en ligne. Reponds en francais.\""
                + "},{"
                + "\"role\": \"user\","
                + "\"content\": \"" + userMessage + "\""
                + "}]"
                + "}";

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"), json
        );

        Request request = new Request.Builder()
                .url(GROQ_URL)
                .addHeader("Authorization", "Bearer " + apiKey)
                .addHeader("Content-Type", "application/json")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        String responseBody = response.body().string();

        System.out.println("Groq response: " + responseBody);

        int contentIndex = responseBody.lastIndexOf("\"content\":\"");
        int start = contentIndex + "\"content\":\"".length();
        String remaining = responseBody.substring(start);
        int end = remaining.indexOf("\"},\"logprobs\"");
        return remaining.substring(0, end)
                .replace("\\n", "\n")
                .replace("\\u0027", "'")
                .replace("\\*", "*");
    }
}