package at.Cinemaland.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Who may open what:
 * - login, sign-up, CSS/JS and images are public
 * - everything else (the site, /administration and the /api used by the Vue app) needs a login,
 *   and every user only ever sees their own movies, categories and actors.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityContextRepository securityContextRepository() {
        return new HttpSessionSecurityContextRepository();
    }

    /** Uses our AppUserService (a UserDetailsService) + the BCrypt encoder above. */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   SecurityContextRepository securityContextRepository) throws Exception {
        http
                // uses the CORS rules from WebConfig (the Vue app runs on another port)
                .cors(Customizer.withDefaults())
                // The Thymeleaf forms get a CSRF token automatically. The JSON API is protected by
                // CORS (only the configured frontend origins) and SameSite session cookies instead.
                .csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/api/**")))
                .securityContext(context -> context.securityContextRepository(securityContextRepository))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/login", "/signup", "/error").permitAll()
                        .requestMatchers("/css/**", "/js/**", "/favicon.ico").permitAll()
                        // images: file IDs are random UUIDs, and <img> tags can't always send a login
                        .requestMatchers(HttpMethod.GET, "/files/*", "/api/files/*/_stream").permitAll()
                        .requestMatchers("/api/auth/login", "/api/auth/signup").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("email")
                        .passwordParameter("password")
                        // after logging in, every user lands in their own collection
                        .defaultSuccessUrl("/administration", true)
                        .failureUrl("/login?error")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll())
                // the API answers 401 instead of redirecting to the HTML login page
                .exceptionHandling(ex -> ex.defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**")));
        return http.build();
    }
}
