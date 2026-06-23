package fr.imcoding.edu365.rest;

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

    @PostMapping("/message")
    public ResponseEntity<?> chat(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String response = "Bonjour ! Je suis le chatbot de FreeAcademy. Vous avez dit : " + message;
        Map<String, String> result = new HashMap<>();
        result.put("response", response);
        return ResponseEntity.ok(result);
    }
}
