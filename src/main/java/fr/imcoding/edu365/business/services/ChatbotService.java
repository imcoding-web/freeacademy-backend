package fr.imcoding.edu365.business.services;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.io.InputStream;

@Service
public class ChatbotService {

    private final String projectId = "chatbot-rayen-kkc9";

    public String chat(String userMessage, String sessionId) throws Exception {
        // Chargement du fichier JSON (Placez-le dans src/main/resources/config/)
        InputStream inputStream = new ClassPathResource("config/chatbot-rayen-kkc9-2c78c478981b.json").getInputStream();
        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);

        SessionsSettings settings = SessionsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        try (SessionsClient sessionsClient = SessionsClient.create(settings)) {
            SessionName session = SessionName.of(projectId, sessionId);
            TextInput.Builder textInput = TextInput.newBuilder().setText(userMessage).setLanguageCode("fr-FR");
            QueryInput queryInput = QueryInput.newBuilder().setText(textInput).build();

            DetectIntentResponse response = sessionsClient.detectIntent(session, queryInput);
            return response.getQueryResult().getFulfillmentText();
        }
    }
}