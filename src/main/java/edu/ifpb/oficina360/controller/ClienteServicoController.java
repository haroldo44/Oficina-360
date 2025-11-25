package edu.ifpb.oficina360.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.Servico;
import edu.ifpb.oficina360.service.ClienteService;
import edu.ifpb.oficina360.service.ServicoService;

@Controller
@RequestMapping("/clientes")
public class ClienteServicoController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private ServicoService servicoService;

    @GetMapping("/solicitar-servico")
    public String solicitar(ModelMap model) {
        model.addAttribute("servico", new Servico());
        return "/clientes/solicitar-servico";
    }

    @PostMapping("/solicitar-servico")
    public String salvar(
        @ModelAttribute("servico") Servico servico,
        RedirectAttributes attr
    ) {
        Cliente cliente = clienteService.buscarPorId(1L); // TEMPORÁRIO
        servico.setCliente(cliente);
        servico.setOficina(cliente.getOficina());
        servico.setStatus("PENDENTE");

        servicoService.salvar(servico);

        attr.addFlashAttribute("sucesso", "Serviço solicitado com sucesso!");
        return "redirect:/clientes/home";
    }
}

