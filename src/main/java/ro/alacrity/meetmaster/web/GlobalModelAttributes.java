package ro.alacrity.meetmaster.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @Value("${app.icon-url:}")
    private String iconUrl;

    @Value("${app.favicon-url:}")
    private String faviconUrl;

    @ModelAttribute("iconUrl")
    public String iconUrl() {
        return iconUrl;
    }

    @ModelAttribute("faviconUrl")
    public String faviconUrl() {
        return faviconUrl;
    }
}
