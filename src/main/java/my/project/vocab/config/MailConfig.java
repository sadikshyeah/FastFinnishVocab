package my.project.vocab.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfig {

    @Bean
    @Primary
    public JavaMailSender javaMailSender(
            @Value("${spring.mail.host}") String host,
            @Value("${spring.mail.port:587}") int port,
            @Value("${spring.mail.username:}") String username,
            @Value("${spring.mail.password:}") String password,
            @Value("${spring.mail.properties.mail.smtp.auth:true}") String smtpAuth,
            @Value("${spring.mail.properties.mail.smtp.starttls.enable:true}") String startTls,
            @Value("${spring.mail.properties.mail.smtp.starttls.required:true}") String startTlsRequired) {
        JavaMailSenderImpl sender = new JavaMailSenderImpl();
        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        if (password != null && !password.isEmpty()) {
            // Google shows app passwords with spaces, but SMTP expects one plain value.
            sender.setPassword(password.replaceAll("\\s+", ""));
        }

        Properties props = new Properties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", startTls);
        props.put("mail.smtp.starttls.required", startTlsRequired);
        sender.setJavaMailProperties(props);
        return sender;
    }
}
