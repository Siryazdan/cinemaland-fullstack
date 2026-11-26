package at.Cinemaland.Security;

import at.Cinemaland.Model.AppUser;
import at.Cinemaland.Repository.AppUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class AppUserService implements UserDetailsService {

    public static final int MIN_PASSWORD_LENGTH = 8;

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return appUserRepository.findByEmailIgnoreCase(normalizeEmail(email))
                .map(AppUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException("No user with this email"));
    }

    /**
     * Creates a new account. Throws {@link SignupException} with a user-friendly message
     * if the input is invalid or the email is already taken.
     */
    public AppUser register(String displayName, String email, String password) {
        String name = displayName == null ? "" : displayName.trim();
        String mail = normalizeEmail(email);

        if (name.isEmpty()) {
            throw new SignupException("Please enter your name.");
        }
        if (name.length() > 100) {
            throw new SignupException("Your name can be at most 100 characters long.");
        }
        if (mail.isEmpty() || !mail.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$") || mail.length() > 255) {
            throw new SignupException("Please enter a valid email address.");
        }
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new SignupException("Your password must be at least " + MIN_PASSWORD_LENGTH + " characters long.");
        }
        if (password.length() > 72) { // BCrypt only uses the first 72 bytes
            throw new SignupException("Your password can be at most 72 characters long.");
        }
        if (appUserRepository.existsByEmailIgnoreCase(mail)) {
            throw new SignupException("An account with this email already exists. Please log in instead.");
        }

        AppUser user = new AppUser();
        user.setDisplayName(name);
        user.setEmail(mail);
        user.setPasswordHash(passwordEncoder.encode(password));
        return appUserRepository.save(user);
    }

    private static String normalizeEmail(String email) {
        return email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
    }

    public static class SignupException extends RuntimeException {
        public SignupException(String message) {
            super(message);
        }
    }
}
