package edu.ifpb.oficina360.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";  // carrega templates/home.html
    }
    
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // limpa a sessão
        return "redirect:/?logout=true"; // volta para a tela de login (home.html)
    }

}

