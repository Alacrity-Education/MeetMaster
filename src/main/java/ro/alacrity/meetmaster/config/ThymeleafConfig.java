package ro.alacrity.meetmaster.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;

@Configuration
public class ThymeleafConfig {

    @Bean
    public SpringResourceTemplateResolver htmlTemplateResolver() {
        var r = new SpringResourceTemplateResolver();
        r.setPrefix("classpath:/templates/");
        r.setSuffix(".html");
        r.setCharacterEncoding("UTF-8");
        r.setCheckExistence(true);
        r.setOrder(1);
        return r;
    }

    @Bean
    public SpringResourceTemplateResolver exactNameTemplateResolver() {
        var r = new SpringResourceTemplateResolver();
        r.setPrefix("classpath:/templates/");
        r.setSuffix("");
        r.setCharacterEncoding("UTF-8");
        r.setCheckExistence(true);
        r.setOrder(0);
        return r;
    }
}
