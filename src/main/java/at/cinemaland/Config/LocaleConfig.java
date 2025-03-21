package at.cinemaland.Config;

import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;
import org.slf4j.Logger;


@Configuration
public class LocaleConfig implements WebMvcConfigurer {


    private static final Logger logger = LoggerFactory.getLogger(LocaleConfig.class);


    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver clr = new CookieLocaleResolver();
        clr.setDefaultLocale(Locale.ENGLISH);
        clr.setCookieName("localeInfo");
        clr.setCookieMaxAge(3600);
        return clr;
    }


    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        // System.out.println("LocaleChangeInterceptor Initialized - Parameter Name: " + lci.getParamName());
        logger.info("LocaleChangeInterceptor Initialized - Parameter Name: {}", lci.getParamName());
        return lci;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}