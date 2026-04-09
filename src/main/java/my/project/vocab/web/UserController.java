package my.project.vocab.web;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import my.project.vocab.domain.ResetPasswordForm;
import my.project.vocab.domain.SignupForm;
import my.project.vocab.domain.User;
import my.project.vocab.domain.UserNotFoundException;
import my.project.vocab.domain.UserRepository;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository urepository;

    @Autowired
    private JavaMailSender mailSender;

    /** Optional explicit From; if blank, falls back to spring.mail.username, then noreply@localhost. */
    @Value("${app.mail.from:}")
    private String appMailFrom;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    /** When true, prints verification/reset URLs to the server log (useful when SMTP is not configured locally). */
    @Value("${app.mail.log-links:false}")
    private boolean logMailLinks;

    @RequestMapping(value = "signup")
    public String signup(Model model) {
        model.addAttribute("signupform", new SignupForm());
        return "signup";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.GET)
    public String registrationRedirect() {
        return "redirect:/signup";
    }

    @RequestMapping(value = "/login")
    public String login() {
        return "login";
    }

    // Sign-up: create user, keep disabled, then send verification email.
    @RequestMapping(value = { "saveuser", "storeuser" }, method = RequestMethod.POST)
    public String save(@Valid @ModelAttribute("signupform") SignupForm signupForm, BindingResult bindingResult,
            HttpServletRequest request, Model model) throws MessagingException {
        if (!bindingResult.hasErrors()) {
            if (signupForm.getPassword().equals(signupForm.getPasswordCheck())) {
                String pwd = signupForm.getPassword();
                BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
                String hashPwd = bc.encode(pwd);

                UUID uuid = UUID.randomUUID();
                String verificationToken = uuid.toString().replaceAll("-", "");

                User newUser = new User();
                newUser.setPassword(hashPwd);
                newUser.setUsername(signupForm.getUsername());
                newUser.setEmail(signupForm.getEmail());
                newUser.setRole("ROLE_USER");
                newUser.setVerificationToken(verificationToken);
                newUser.setEnabled(false);

                if (urepository.findByUsername(signupForm.getUsername()) == null) {
                    if (urepository.findByEmail(signupForm.getEmail()) == null) {
                        urepository.save(newUser);

                        String url = request.getRequestURL().toString();
                        String verificationLink = url.replace(request.getServletPath(), "") + "/verify_email?token="
                                + verificationToken;
                        sendVerificationEmail(newUser.getEmail(), verificationLink);
                        model.addAttribute("message",
                                "We have sent you a verification email. Please check your email.");
                    } else {
                        bindingResult.rejectValue("email", "err.email", "Email is already in use");
                        return "signup";
                    }
                } else {
                    bindingResult.rejectValue("username", "err.username", "Username already exists");
                    return "signup";
                }
            } else {
                bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords does not match");
                return "signup";
            }
        } else {
            return "signup";
        }
        return "signup";
    }

    // User opens link from email; valid token activates the account.
    @RequestMapping(value = "/verify_email", method = RequestMethod.GET)
    public String verifyEmail(@RequestParam(value = "token") String token) {
        User user = urepository.findByVerificationToken(token);

        if (user != null) {
            user.setEnabled(true);
            user.setVerificationToken(null);
            urepository.save(user);

            return "verify_email";
        } else {
            return "token_error";
        }
    }

    @RequestMapping(value = "/forgot_password", method = RequestMethod.GET)
    public String forgotPassword(Model model) {
        return "forgotpassword";
    }

    // Generates reset token and sends reset link to user email.
    @RequestMapping(value = "/forgot_password", method = RequestMethod.POST)
    public String processForgotPassword(HttpServletRequest request, Model model) throws MessagingException {
        try {
            String email = request.getParameter("email");
            UUID uuid = UUID.randomUUID();
            String token = uuid.toString().replaceAll("-", "");

            User appUser = urepository.findByEmail(email);

            if (appUser == null) {
                throw new UserNotFoundException("Could not find the user with this email.");
            } else if (!appUser.isEnabled()) {
                throw new UserNotFoundException(
                        "The user is not verified. Please check your email for verification link");
            } else {
                appUser.setResetToken(token);
                urepository.save(appUser);
            }

            String url = request.getRequestURL().toString();
            String passwordResetLink = url.replace(request.getServletPath(), "") + "/reset_password?token=" + token;

            sendResetEmail(email, passwordResetLink);

            model.addAttribute("message", "We have sent you a reset link. Please check your email.");

        } catch (UserNotFoundException exception) {
            model.addAttribute("error", exception.getMessage());
        } catch (MessagingException exception) {
            model.addAttribute("error", "Error while sending email");
        }
        return "forgotpassword";
    }

    @RequestMapping(value = "/reset_password", method = RequestMethod.GET)
    public String showResetPasswordForm(@RequestParam(value = "token") String token, Model model) {
        User user = urepository.findByResetToken(token);
        model.addAttribute("token", token);
        model.addAttribute("resetform", new ResetPasswordForm());

        if (user == null) {
            return "token_error";
        }

        return "reset_password";
    }

    // Saves the new password and clears the reset token.
    @RequestMapping(value = "/reset_password", method = RequestMethod.POST)
    public String processResetPassword(@RequestParam(value = "token") String token,
            @Valid @ModelAttribute("resetform") ResetPasswordForm resetForm, BindingResult bindingResult, Model model) {
        User appUser = urepository.findByResetToken(token);
        model.addAttribute("token", token);

        if (appUser == null) {
            return "token_error";
        }

        if (!bindingResult.hasErrors()) {
            if (resetForm.getPassword().equals(resetForm.getPasswordCheck())) {
                String pwd = resetForm.getPassword();
                BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
                String hashPwd = bc.encode(pwd);

                appUser.setPassword(hashPwd);
                appUser.setResetToken(null);

                urepository.save(appUser);
            } else {
                bindingResult.rejectValue("passwordCheck", "err.passCheck", "Passwords does not match");
                return "reset_password";
            }
        } else {
            return "reset_password";
        }

        return "redirect:/login";
    }

    private void sendVerificationEmail(String email, String verificationLink) throws MessagingException {
        if (logMailLinks) {
            log.info("Verification link (copy into browser if email is not delivered): {}", verificationLink);
        }
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress());
        helper.setTo(email);

        String content = "<p>Hello,</p>"
                + "<p>Thank you for registering. Please verify your email by clicking the link below:</p>"
                + "<p><a href=\"" + verificationLink + "\">Verify my email</a></p>";

        helper.setSubject("Email verification — Fast Finnish Vocab");
        helper.setText(content, true);

        mailSender.send(message);
    }

    private void sendResetEmail(String email, String passwordResetLink) throws MessagingException {
        if (logMailLinks) {
            log.info("Password reset link (copy into browser if email is not delivered): {}", passwordResetLink);
        }
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress());
        helper.setTo(email);

        String content = "<p>Hello,</p>" + "<p>You have requested to reset your password</p>"
                + "<p>Click the link below to reset your password</p>" + "<p><a href=\"" + passwordResetLink
                + "\">Change my password</a></p>";

        helper.setSubject("Password reset — Fast Finnish Vocab");
        helper.setText(content, true);

        mailSender.send(message);
    }

    /**
    * setFrom() cannot use an empty email address.
    * If mail username is blank, we use a safe fallback address.
    */
    private String fromAddress() {
        if (appMailFrom != null && !appMailFrom.isBlank()) {
            return appMailFrom.trim();
        }
        if (mailUsername != null && !mailUsername.isBlank()) {
            return mailUsername.trim();
        }
        return "noreply@localhost";
    }
}
