package fr.imcoding.edu365.business.services;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.dialogflow.v2.*;
import fr.imcoding.edu365.persistence.entities.ChatMessage;
import fr.imcoding.edu365.persistence.repositories.ChatMessageRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
public class ChatbotService {

    private final String projectId = "chatbot-rayen-kkc9";

    private final ChatMessageRepository chatMessageRepository;

    public ChatbotService(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }
    public List<ChatMessage> getHistory() {
        return chatMessageRepository.findAll();
    }
    public String chat(String userMessage, String sessionId) throws Exception {

        InputStream inputStream = new ClassPathResource(
                "config/chatbot-rayen-kkc9-2c78c478981b.json")
                .getInputStream();

        GoogleCredentials credentials = GoogleCredentials.fromStream(inputStream);

        SessionsSettings settings = SessionsSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        try (SessionsClient sessionsClient = SessionsClient.create(settings)) {

            SessionName session = SessionName.of(projectId, sessionId);

            TextInput textInput = TextInput.newBuilder()
                    .setText(userMessage)
                    .setLanguageCode("fr-FR")
                    .build();

            QueryInput queryInput = QueryInput.newBuilder()
                    .setText(textInput)
                    .build();

            DetectIntentResponse response =
                    sessionsClient.detectIntent(session, queryInput);

            String botResponse = response.getQueryResult().getFulfillmentText();

            // Sauvegarde en base
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setUserMessage(userMessage);
            chatMessage.setBotResponse(botResponse);
            chatMessage.setSessionId(sessionId);

            chatMessageRepository.save(chatMessage);

            return botResponse;
        }
    }
}