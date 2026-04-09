package my.project.vocab.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Connects Spring Security login to DB-based UserDetailsService.
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http, AuthenticationProvider authenticationProvider)
            throws Exception {

        http
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/css/**").permitAll()
                        .requestMatchers("/registration").permitAll()
                        .requestMatchers("/storeuser").permitAll()
                        .requestMatchers("/signup").permitAll()
                        .requestMatchers("/saveuser").permitAll()
                        .requestMatchers("/forgot_password").permitAll()
                        .requestMatchers("/reset_password/**").permitAll()
                        .requestMatchers("/reset_password").permitAll()
                        .requestMatchers("/verify_email/**").permitAll()
                        .requestMatchers("/verify_email").permitAll()
                        .anyRequest().authenticated())
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable()))
                .formLogin(formlogin -> formlogin
                        .loginPage("/login")
                        .defaultSuccessUrl("/wordlist", true)
                        .failureHandler((request, response, exception) -> {
                            String path = request.getContextPath() + "/login?error";
                            if (exception instanceof DisabledException) {
                                path = request.getContextPath() + "/login?error=unverified";
                            }
                            response.sendRedirect(path);
                        })
                        .permitAll())
                .logout(logout -> logout
                        .permitAll());

        return http.build();
    }
}
