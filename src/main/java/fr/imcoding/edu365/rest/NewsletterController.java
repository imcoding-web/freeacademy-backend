package fr.imcoding.edu365.rest;

import fr.imcoding.edu365.business.services.NewsletterService;
import fr.imcoding.edu365.client.dtos.response.NewsLetterResponse;
import fr.imcoding.edu365.dtos.NewsLetterDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author Bassem
 * @Date 19/10/2022
 */

@RestController
@CrossOrigin
@RequestMapping("/newsletter")
@RequiredArgsConstructor
public class NewsletterController {
    private final NewsletterService newLetterServices;

    /**
     * save w new subscription
     * @param data that contain  user email
     * @return ResponseEntity
     */
    @PostMapping()
    public void addNewSubscribtionUser(@RequestBody NewsLetterDto data) {
        newLetterServices.addNewSubscribtionUser(data);

    }

    @GetMapping
    public List<NewsLetterResponse> getAllSubscription(){
        return newLetterServices.getAllSubscription();
    }

}
