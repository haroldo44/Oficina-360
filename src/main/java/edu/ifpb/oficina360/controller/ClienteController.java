package edu.ifpb.oficina360.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.ClienteCadastroDTO;
import edu.ifpb.oficina360.model.Oficina;
import edu.ifpb.oficina360.service.ClienteService;
import edu.ifpb.oficina360.service.OficinaService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private OficinaService oficinaService;

    // ===================== ETAPA 1 =====================
    
    @GetMapping("/cadastro/etapa1")
    public String etapa1(Model model) {
        if (!model.containsAttribute("dto")) {
            model.addAttribute("dto", new ClienteCadastroDTO());
        }
        return "clientes/cadastro-etapa1";
    }
    
    @PostMapping("/cadastro/etapa1")
    public String salvarEtapa1(@ModelAttribute("dto") @Valid ClienteCadastroDTO dto,
                               BindingResult result,
                               HttpSession session) {
        if (result.hasErrors()) {
            return "clientes/cadastro-etapa1";
        }

        if (!dto.getSenha().equals(dto.getConfirmarSenha())) {
            result.rejectValue("confirmarSenha", null, "As senhas não conferem.");
            return "clientes/cadastro-etapa1";
        }

        session.setAttribute("cadastroCliente", dto);

        return "redirect:/clientes/cadastro/etapa2";
    }


    // ===================== ETAPA 2 =====================
    @GetMapping("/cadastro/etapa2")
    public String etapa2(HttpSession session, Model model) {
        ClienteCadastroDTO dto = (ClienteCadastroDTO) session.getAttribute("cadastroCliente");
        if (dto == null) {
            return "redirect:/clientes/cadastro/etapa1";
        }
        model.addAttribute("dto", dto);
        return "clientes/cadastro-etapa2";
    }

    @PostMapping("/cadastro/etapa2")
    public String salvarEtapa2(@ModelAttribute("dto") @Valid ClienteCadastroDTO dto,
                               BindingResult result,
                               HttpSession session) {

        if (result.hasFieldErrors("nome") || result.hasFieldErrors("cpf")) {
            return "clientes/cadastro-etapa2";
        }

        session.setAttribute("cadastroCliente", dto);

        return "redirect:/clientes/cadastro/etapa3";
    }


    // ===================== ETAPA 3 =====================
    @GetMapping("/cadastro/etapa3")
    public String etapa3(HttpSession session, Model model) {
        ClienteCadastroDTO dto = (ClienteCadastroDTO) session.getAttribute("cadastroCliente");

        if (dto == null) {
            return "redirect:/clientes/cadastro/etapa1";
        }

        model.addAttribute("dto", dto);
        return "clientes/cadastro-etapa3";
    }

    @PostMapping("/cadastro/etapa3")
    public String salvarEtapa3(@ModelAttribute("dto") ClienteCadastroDTO dto,
                               HttpSession session) {

        if (dto.getOficinaId() == null) {
            return "clientes/cadastro-etapa3";
        }

        ClienteCadastroDTO dtoSessao =
                (ClienteCadastroDTO) session.getAttribute("cadastroCliente");

        dtoSessao.setOficinaId(dto.getOficinaId());

        session.setAttribute("cadastroCliente", dtoSessao);

        // 🔥 Aqui estava o erro. Agora chamamos POST → POST (sem 405)
        return "forward:/clientes/salvar-final";
    }


    // ===================== SALVAR FINAL =====================
    @PostMapping("/salvar-final")
    public String salvarFinal(HttpSession session) {

        ClienteCadastroDTO dto = (ClienteCadastroDTO)
                session.getAttribute("cadastroCliente");

        if (dto == null) {
            return "redirect:/clientes/cadastro/etapa1";
        }

        Cliente cliente = new Cliente();
        cliente.setEmail(dto.getEmail());
        cliente.setSenha(dto.getSenha());
        cliente.setNome(dto.getNome());
        cliente.setCpf(dto.getCpf());
        cliente.setCidade(dto.getCidade());
        cliente.setBairro(dto.getBairro());
        cliente.setRua(dto.getRua());

        Oficina oficina = oficinaService.buscarPorId(dto.getOficinaId());
        cliente.setOficina(oficina);

        clienteService.salvar(cliente);

        session.removeAttribute("cadastroCliente");

        return "redirect:/login";
    }


    // ===================== LOGIN =====================
    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String senha,
                        HttpSession session,
                        RedirectAttributes attr) {

        Cliente cliente = clienteService.buscarPorEmail(email);

        if (cliente != null && cliente.getSenha().equals(senha)) {
            session.setAttribute("usuarioLogado", cliente);
            return "redirect:/clientes/home/" + cliente.getId();
        }

        attr.addFlashAttribute("erro", "Email ou senha inválidos!");
        return "redirect:/login";
    }


    // ===================== PERFIL =====================
    @GetMapping("/perfil/{id}")
    public String perfilCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id);
        if (cliente == null) {
            return "redirect:/";
        }
        model.addAttribute("cliente", cliente);
        return "clientes/perfil";
    }


    // ===================== EXCLUIR =====================
    @PostMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable Long id, RedirectAttributes attr) {
        try {
            clienteService.deletarCliente(id);
            attr.addFlashAttribute("sucesso", "Conta excluída com sucesso!");
        } catch (Exception e) {
            attr.addFlashAttribute("erro", "Erro ao excluir conta: " + e.getMessage());
        }
        return "redirect:/";
    }
}
