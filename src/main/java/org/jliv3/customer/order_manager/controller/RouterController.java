package org.jliv3.customer.order_manager.controller;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Controller
public class RouterController implements WebMvcConfigurer {
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/orders")
    public String orders() {
        return index();
    }

    @GetMapping("/users")
    public String users() {
        return "users";
    }

    @GetMapping("/403")
    public String accessDenied() {
        return "403";
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        registry.addInterceptor(lci).addPathPatterns("/*");
    }

    @Bean
    public LocaleResolver localeResolver() {
        CookieLocaleResolver ckr = new CookieLocaleResolver();
        ckr.setCookieMaxAge(60 * 60);
        return ckr;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
        messageResource.setBasename("classpath:i18n/messages");
        messageResource.setDefaultEncoding("UTF-8");
        return messageResource;
    }
}
