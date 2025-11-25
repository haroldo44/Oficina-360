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
import edu.ifpb.oficina360.model.Oficina;
import edu.ifpb.oficina360.service.ClienteService;
import edu.ifpb.oficina360.service.OficinaService;
import edu.ifpb.oficina360.service.ServicoService;

@Controller
@RequestMapping("/clientes")
public class ClienteHomeController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private OficinaService oficinaService;

    @Autowired
    private ServicoService servicoService;

    // ... (Outros métodos) ...

    @PostMapping("/vincular-oficina")
    public String vincularOficina(
            @ModelAttribute("cliente") Cliente clienteForm,
            RedirectAttributes attr
    ) {
        Cliente cliente = clienteService.buscarPorId(1L); // TEMPORÁRIO

        // pega o ID informado na etapa 3
        Long idOficina = clienteForm.getOficina().getId();

        // Chamada CORRIGIDA: Usa o serviço que já resolve o Optional
        Oficina oficina = oficinaService.buscarPorId(idOficina);

        cliente.setOficina(oficina);
        clienteService.salvar(cliente);

        attr.addFlashAttribute("sucesso", "Oficina vinculada com sucesso!");
        return "redirect:/clientes/home";
    }
}