package at.Cinemaland.Controller.web;

import at.Cinemaland.Security.AppUserService;
import at.Cinemaland.Security.AuthSessionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** Login and sign-up pages. The login form itself is processed by Spring Security (POST /login). */
@Controller
public class AuthController {

    private final AppUserService appUserService;
    private final AuthSessionService authSessionService;

    public AuthController(AppUserService appUserService, AuthSessionService authSessionService) {
        this.appUserService = appUserService;
        this.authSessionService = authSessionService;
    }

    @GetMapping("/login")
    public String login(Authentication authentication, CsrfToken csrfToken) {
        if (isLoggedIn(authentication)) {
            return "redirect:/administration";
        }
        // Create the CSRF token (and its session) now: the landing page is long, so by the time
        // Thymeleaf reaches the form the response is already being sent and no session can be made.
        csrfToken.getToken();
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signup(Authentication authentication, Model model, CsrfToken csrfToken) {
        if (isLoggedIn(authentication)) {
            return "redirect:/administration";
        }
        csrfToken.getToken();
        model.addAttribute("minPasswordLength", AppUserService.MIN_PASSWORD_LENGTH);
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String signupSubmit(@RequestParam("displayName") String displayName,
                               @RequestParam("email") String email,
                               @RequestParam("password") String password,
                               @RequestParam("confirmPassword") String confirmPassword,
                               Model model,
                               HttpServletRequest request,
                               HttpServletResponse response) {
        try {
            if (!password.equals(confirmPassword)) {
                throw new AppUserService.SignupException("The two passwords don't match.");
            }
            appUserService.register(displayName, email, password);
        } catch (AppUserService.SignupException e) {
            // show the form again with the error; never send the password back
            model.addAttribute("error", e.getMessage());
            model.addAttribute("displayName", displayName);
            model.addAttribute("email", email);
            model.addAttribute("minPasswordLength", AppUserService.MIN_PASSWORD_LENGTH);
            return "auth/signup";
        }
        // log the new user in right away and take them to their own (empty) collection
        authSessionService.signIn(email, password, request, response);
        return "redirect:/administration?welcome";
    }

    private static boolean isLoggedIn(Authentication authentication) {
        return authentication != null && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken);
    }
}
