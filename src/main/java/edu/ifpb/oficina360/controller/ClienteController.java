package edu.ifpb.oficina360.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.Oficina;
import edu.ifpb.oficina360.service.ClienteService;
import edu.ifpb.oficina360.service.OficinaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
@SessionAttributes("cliente") // mantém o cliente entre etapas
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private OficinaService oficinaService;


    // ===================== ETAPA 1 =====================
    @GetMapping("/cadastro/etapa1")
    public String etapa1(ModelMap model) {
        if (!model.containsAttribute("cliente")) {
            model.addAttribute("cliente", new Cliente());
        }
        return "/clientes/cadastro-etapa1";
    }

    @PostMapping("/cadastro/etapa2")
    public String salvarEtapa1(
            @Valid @ModelAttribute("cliente") Cliente cliente,
            BindingResult result,
            RedirectAttributes attr) {

        // valida campos obrigatórios
        if (result.hasFieldErrors("email") || result.hasFieldErrors("senha")) {
            return "/clientes/cadastro-etapa1";
        }

        if (!cliente.getSenha().equals(cliente.getConfirmarSenha())) {
            result.rejectValue("confirmarSenha", "error.cliente", "As senhas não conferem.");
            return "/clientes/cadastro-etapa1";
        }

        return "redirect:/clientes/cadastro/etapa2";
    }


    // ===================== ETAPA 2 =====================
    @GetMapping("/cadastro/etapa2")
    public String etapa2(@ModelAttribute("cliente") Cliente cliente) {

        if (cliente.getEmail() == null) {
            return "redirect:/clientes/cadastro/etapa1";
        }

        return "/clientes/cadastro-etapa2";
    }

    @PostMapping("/cadastro/etapa3")
    public String salvarEtapa2(
            @Valid @ModelAttribute("cliente") Cliente cliente,
            BindingResult result) {

        if (result.hasFieldErrors("nome") || result.hasFieldErrors("cpf")) {
            return "/clientes/cadastro-etapa2";
        }

        return "redirect:/clientes/cadastro/etapa3";
    }


    // ===================== ETAPA 3 =====================
    @GetMapping("/cadastro/etapa3")
    public String etapa3(@ModelAttribute("cliente") Cliente cliente) {

        if (cliente.getEmail() == null) {
            return "redirect:/clientes/cadastro/etapa1";
        }

        return "/clientes/cadastro-etapa3";
    }

    @PostMapping("/salvar-final")
    public String salvarFinal(
            @ModelAttribute("cliente") Cliente cliente,
            RedirectAttributes attr) {
        try {

            // 1. Verifica se a oficina foi selecionada e obtém o ID
            if (cliente.getOficina() == null || cliente.getOficina().getId() == null) {
                throw new IllegalArgumentException("O ID da Oficina não pode ser nulo.");
            }
            Long idOficina = cliente.getOficina().getId();

            // 2. Busca a entidade Oficina (o service lança a exceção se não encontrar)
            Oficina oficina = oficinaService.buscarPorId(idOficina);

            // 3. Define a oficina no cliente (associação)
            cliente.setOficina(oficina);

            // 4. Salva o cliente
            clienteService.cadastrarCliente(cliente);

            attr.addFlashAttribute("sucesso", "Conta criada com sucesso!");
            
            // Retorna ao início do cadastro ou à página de login
            return "redirect:/clientes/cadastro/etapa1"; 

        } catch (Exception e) {
            attr.addFlashAttribute("erro", e.getMessage());
            return "redirect:/clientes/cadastro/etapa3";
        }
    }
}