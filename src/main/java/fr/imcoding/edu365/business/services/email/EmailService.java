package fr.imcoding.edu365.business.services.email;

import static fr.imcoding.edu365.utils.Constants.BACKEND_BASE_URL;
import static fr.imcoding.edu365.utils.Constants.CODE;
import static fr.imcoding.edu365.utils.Constants.FRONT_BASE_URL;

import fr.imcoding.edu365.dtos.EmailTraceabilityDto;
import fr.imcoding.edu365.enumeration.EmailContext;
import fr.imcoding.edu365.persistence.entities.NotSentEmailTraceability;
import java.util.List;

import java.util.Map;
import java.util.Optional;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import fr.imcoding.edu365.dtos.EmailDto;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EmailService {

	@Value("${edu365.front.base.url}")
	private String frontBaseUrl;

	@Value("${edu365.front.admin.base.url}")
	private String frontAdminBaseUrl;

	@Value("${edu365.backend.base.url}")
	private String backendBaseUrl;

	@Value("${spring.mail.username}")
	private String smtpMailUsername;

	@Autowired
	private JavaMailSender mailSender;

	@Value("${edu365.app.base-url}")
	private String baseUrl;

	@Autowired
	private NotSentEmailService notSentEmailService;

	@Autowired
	private SpringTemplateEngine thymeleafTemplateEngine;

	public EmailService() {
	}

	/**
	 * send email
	 *
	 * @param emailDto: email content and config
	 */
	@Async
	public void sendMail(EmailDto emailDto, List<String> destinations) {
		destinations.stream().forEach(destination -> {
			NotSentEmailTraceability notSentEmailTraceability=notSentEmailService.getEmailTraceability(destination,emailDto.getEmailContext());

			emailDto.getMaps().put(FRONT_BASE_URL, frontBaseUrl);
			emailDto.getMaps().put(BACKEND_BASE_URL, backendBaseUrl);
			try {

				Context thymeleafContext = new Context();
				thymeleafContext.setVariables(emailDto.getMaps());
				String htmlBody = thymeleafTemplateEngine.process(emailDto.getTemplateName(), thymeleafContext);

				MimeMessage message = mailSender.createMimeMessage();
				MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
				// helper.setTo(emailDto.getTo());
				helper.setTo(destination);
				helper.setFrom(smtpMailUsername);
				helper.setSubject(emailDto.getSubject());
				helper.setText(htmlBody, true);

				// Add exist attachments to email
				emailDto.getAttachments().entrySet().forEach(attachment -> {
					try {
						helper.addAttachment(attachment.getKey(), new ByteArrayResource(attachment.getValue()));
					} catch (MessagingException e) {
						log.error(e.getMessage());
						e.printStackTrace();
					}
				});

				mailSender.send(message);
				if(notSentEmailTraceability!=null){
					notSentEmailService.deleteEmailTaceability(notSentEmailTraceability);
				}
			} catch (Exception e) {
				if(emailDto.getEmailContext().equals(EmailContext.WELCOME_USER)){
					String code=emailDto.getMaps().entrySet().stream()
							.filter(k -> k.getKey().equals("UUID"))
							.map(Map.Entry::getValue)
							.findFirst()
							.orElse(null).toString();
						if(notSentEmailTraceability==null){
							EmailTraceabilityDto emailTraceability=new EmailTraceabilityDto(destination,emailDto.getEmailContext(),emailDto.getSubject(),emailDto.getTemplateName(),code);
							notSentEmailService.saveEmailTraceability(emailTraceability);
						}
				else{
							notSentEmailService.updateAttemptNumber(notSentEmailTraceability);

						}
				}
				e.printStackTrace();
				log.error("Exception when send Email: {}", e.getMessage());
			}
		});

	}

}
