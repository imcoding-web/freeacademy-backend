package fr.imcoding.edu365.business.services;

import fr.imcoding.edu365.business.mappers.NewsletterMapper;
import fr.imcoding.edu365.client.dtos.response.NewsLetterResponse;
import fr.imcoding.edu365.dtos.NewsLetterDto;
import fr.imcoding.edu365.persistence.entities.SubscribtionUser;
import fr.imcoding.edu365.persistence.repositories.SubscribtionUserRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NewsletterService {
    private final SubscribtionUserRepository subscribtionUserRepository;
    private final NewsletterMapper newsletterMapper;


    public void addNewSubscribtionUser(NewsLetterDto data) {
        SubscribtionUser subscription = new SubscribtionUser();
        subscription.setEmail(data.getEmail());
        subscribtionUserRepository.save(subscription);
    }

    public List<NewsLetterResponse> getAllSubscription(){
        return subscribtionUserRepository.findAll().stream().map(newsletterMapper::toNewsLetterResponse).collect(Collectors.toList());
    }
}
