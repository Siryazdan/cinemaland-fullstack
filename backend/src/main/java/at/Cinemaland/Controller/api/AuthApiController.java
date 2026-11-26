package at.Cinemaland.Controller.api;

import at.Cinemaland.Security.AppUserPrincipal;
import at.Cinemaland.Security.AppUserService;
import at.Cinemaland.Security.AuthSessionService;
import at.Cinemaland.Security.CurrentUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

/** Login / sign-up / logout for the Vue app. The session cookie is set by the server. */
@RestController
@RequestMapping("/api/auth")
public class AuthApiController {

    public record LoginRequest(String email, String password) {
    }

    public record SignupRequest(String displayName, String email, String password) {
    }

    public record UserResponse(String id, String email, String displayName) {
        static UserResponse of(AppUserPrincipal p) {
            return new UserResponse(p.getId(), p.getEmail(), p.getDisplayName());
        }
    }

    private final AuthSessionService authSessionService;
    private final AppUserService appUserService;
    private final CurrentUser currentUser;

    public AuthApiController(AuthSessionService authSessionService, AppUserService appUserService,
                             CurrentUser currentUser) {
        this.authSessionService = authSessionService;
        this.appUserService = appUserService;
        this.currentUser = currentUser;
    }

    public record ErrorResponse(String message) {
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest body, HttpServletRequest request, HttpServletResponse response) {
        return UserResponse.of(authSessionService.signIn(body.email(), body.password(), request, response));
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse signup(@RequestBody SignupRequest body, HttpServletRequest request, HttpServletResponse response) {
        appUserService.register(body.displayName(), body.email(), body.password());
        return UserResponse.of(authSessionService.signIn(body.email(), body.password(), request, response));
    }

    // Spring Boot hides error messages by default - these are safe to show, so send them to the Vue app

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> wrongCredentials() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("Wrong email or password."));
    }

    @ExceptionHandler(AppUserService.SignupException.class)
    public ResponseEntity<ErrorResponse> invalidSignup(AppUserService.SignupException e) {
        return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
    }

    @GetMapping("/me")
    public UserResponse me() {
        return UserResponse.of(currentUser.get());
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        authSessionService.signOut(request, response);
    }
}
