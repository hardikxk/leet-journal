package com.hardik.gateway;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class LoginRedirectController {
    
    @GetMapping("/login")
    public RedirectView login() {
        return new RedirectView("/oauth2/authorization/proxy-client-oidc");
    }
}
