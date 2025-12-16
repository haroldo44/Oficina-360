package edu.ifpb.oficina360.controller;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.Mecanico;
import edu.ifpb.oficina360.model.Oficina;
import edu.ifpb.oficina360.service.ClienteService;
import edu.ifpb.oficina360.service.MecanicoService;
import edu.ifpb.oficina360.service.OficinaService;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {

    @Autowired
    private OficinaService oficinaService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private MecanicoService mecanicoService; 

    // --- CORREÇÃO AQUI ---
    // A URL no navegador será "/login"
    @GetMapping("/login")
    public String abrirTelaLogin() {
        // O Spring vai procurar o arquivo templates/home.html
        return "home"; 
    }
    // ---------------------

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String senha,
                        HttpSession session,
                        RedirectAttributes attr) {

        // 1. TENTA LOGIN COMO OFICINA
        Oficina oficina = oficinaService.buscarPorEmail(email);
        if (oficina != null && oficina.getSenha().equals(senha)) {
            session.setAttribute("usuarioLogado", oficina);
            session.setAttribute("tipoUsuario", "OFICINA");
            return "redirect:/oficinas/home/" + oficina.getId();
        }

        // 2. TENTA LOGIN COMO CLIENTE
        Cliente cliente = clienteService.buscarPorEmail(email);
        if (cliente != null && cliente.getSenha().equals(senha)) {
            session.setAttribute("usuarioLogado", cliente);
            session.setAttribute("tipoUsuario", "CLIENTE");
            return "redirect:/clientes/home/" + cliente.getId();
        }

        // 3. TENTA LOGIN COMO MECÂNICO
        Mecanico mecanico = mecanicoService.buscarPorEmail(email);
        if (mecanico != null && mecanico.getSenha().equals(senha)) {
            session.setAttribute("usuarioLogado", mecanico);
            session.setAttribute("tipoUsuario", "MECANICO");
            return "redirect:/mecanicos/home/" + mecanico.getId();
        }

        // 4. FALHA NO LOGIN
        attr.addFlashAttribute("erro", "Email/senha inválidos ou essa conta não existe!");
        return "redirect:/login";
    }
}