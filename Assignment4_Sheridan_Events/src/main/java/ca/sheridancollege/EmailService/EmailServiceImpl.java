package ca.sheridancollege.EmailService;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class EmailServiceImpl {

	@Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	public JavaMailSender emailSender;
	
	public void sendSimpleMessage(
			String to,String subject,String text) {
		SimpleMailMessage message=new SimpleMailMessage();
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		emailSender.send(message);
	}
	
public void sendMailWithInline(String name,String messageBody, String footer,String to, String subject) throws MessagingException {
	//prepare the evaluation content
	final Context ctx=new Context();
	ctx.setVariable("name",name);
	ctx.setVariable("message",messageBody);
	ctx.setVariable("footer", footer);

	//prepare message using a Spring helper
	final  MimeMessage mimeMessage =this.emailSender.createMimeMessage();
	final MimeMessageHelper message=new MimeMessageHelper(mimeMessage,true,"UTF-8"); // true= multipart
	message.setSubject(subject);
	message.setFrom("java3samplemail@gmail.com");
	message.setTo(to);
	
	//Create the HTMl body using Thymeleaf
	final String htmlContent =this.templateEngine.process("/users/emailTemplate.html", ctx);
	message.setText(htmlContent,true); //true=isHTML
	
	//send mail
	this.emailSender.send(mimeMessage);
 }
}
