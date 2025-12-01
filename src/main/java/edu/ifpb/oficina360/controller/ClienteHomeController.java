package edu.ifpb.oficina360.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.Oficina;
import edu.ifpb.oficina360.model.Servico;
import edu.ifpb.oficina360.service.ClienteService;
import edu.ifpb.oficina360.service.OficinaService;
import edu.ifpb.oficina360.service.ServicoService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/clientes")
public class ClienteHomeController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private OficinaService oficinaService;

    @Autowired
    private ServicoService servicoService;

    // ===================== HOME =====================
    @GetMapping("/home/{id}")
    public String home(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id);
        if (cliente == null) {
            return "redirect:/";
        }

        model.addAttribute("cliente", cliente);

        // Adiciona a oficina para o HTML
        model.addAttribute("oficina", cliente.getOficina());

        // Protege contra lista nula
        List<Servico> servicos = cliente.getServicos() != null ? cliente.getServicos() : List.of();
        model.addAttribute("servicosAgendados",
                servicos.stream().filter(s -> "PENDENTE".equals(s.getStatus())).toList());
        model.addAttribute("servicosFinalizados",
                servicos.stream().filter(s -> "FINALIZADO".equals(s.getStatus())).toList());

        return "clientes/tela-cliente";
    }

    // ===================== VINCULAR OFICINA =====================
    @PostMapping("/vincular-oficina")
    public String vincularOficina(@RequestParam Long oficinaId,
                                  HttpSession session,
                                  RedirectAttributes attr) {
        Cliente cliente = (Cliente) session.getAttribute("usuarioLogado");
        if (cliente == null) {
            attr.addFlashAttribute("erro", "É necessário estar logado.");
            return "redirect:/login";
        }

        Oficina oficina = oficinaService.buscarPorId(oficinaId);
        cliente.setOficina(oficina);
        clienteService.salvar(cliente);

        attr.addFlashAttribute("sucesso", "Oficina vinculada com sucesso!");
        return "redirect:/clientes/home/" + cliente.getId();
    }
}
