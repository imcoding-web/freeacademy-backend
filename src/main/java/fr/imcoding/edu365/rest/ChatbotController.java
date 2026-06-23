package fr.imcoding.edu365.rest;

<<<<<<< HEAD
import fr.imcoding.edu365.business.services.ChatbotService;
=======
>>>>>>> 8a0dde93d83bffe390e50cf66821e2ecbdb7d62c
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/chatbot")
@RequiredArgsConstructor
public class ChatbotController {

<<<<<<< HEAD
    private final ChatbotService chatbotService;

    @PostMapping("/message")
    public ResponseEntity<?> chat(@RequestBody Map<String, String> request) {
        try {
            String message = request.get("message");
            String response = chatbotService.chat(message);
            Map<String, String> result = new HashMap<>();
            result.put("response", response);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }
}
=======
    @PostMapping("/message")
    public ResponseEntity<?> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String response = "Bonjour ! Je suis le chatbot de FreeAcademy. Vous avez dit : " + message;
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return ResponseEntity.ok(result);
    }
}
>>>>>>> 8a0dde93d83bffe390e50cf66821e2ecbdb7d62c
