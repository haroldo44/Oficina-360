package edu.ifpb.oficina360.controller;

import java.time.LocalDate;
import edu.ifpb.oficina360.model.Mecanico;
import edu.ifpb.oficina360.model.MecanicoCadastroDTO;
import edu.ifpb.oficina360.model.Servico;
import edu.ifpb.oficina360.service.MecanicoService;
import edu.ifpb.oficina360.service.ServicoService;
import edu.ifpb.oficina360.repository.ClienteRepository; // Novo Import
import edu.ifpb.oficina360.repository.MecanicoRepository; // Novo Import
import edu.ifpb.oficina360.repository.OficinaRepository;
import edu.ifpb.oficina360.repository.ServicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/mecanicos")
public class MecanicoController {

    @Autowired
    private OficinaRepository oficinaRepository;

    @Autowired
    private MecanicoService mecanicoService;
    
    @Autowired
    private ServicoService servicoService;
    
    @Autowired
    private ServicoRepository servicoRepository;

    @Autowired
    private ClienteRepository clienteRepository; 

    @Autowired
    private MecanicoRepository mecanicoRepository; 

    // Redireciona mecanico para sua página home.
    @GetMapping("/home/{id}")
    public String home(@PathVariable Long id, Model model) {
        Mecanico mecanico = mecanicoService.buscarPorId(id);
        if (mecanico == null) return "redirect:/login";

        model.addAttribute("mecanico", mecanico);
        model.addAttribute("servicosAgendados", servicoService.buscarPorMecanicoEStatus(mecanico, "PENDENTE"));
        model.addAttribute("servicosRealizados", servicoService.listarHistoricoMecanico(mecanico));

        return "mecanicos/mecanico-home";
    }

    // Redireciona mecanico para a página de finalizar servico.
    @GetMapping("/finalizar/{id}")
    public String paginaFinalizarServico(@PathVariable Long id, Model model) {
        Servico servico = servicoRepository.findById(id).orElse(null);
        if (servico == null) return "redirect:/";
        model.addAttribute("servico", servico);
        model.addAttribute("mecanico", servico.getMecanico()); 
        return "servicos/finalizar-servico";
    }

    // Finaliza o servico e o manda para email.
    @PostMapping("/concluir/{id}")
    public String concluirServico(
            @PathVariable Long id,
            @RequestParam("veiculoModelo") String veiculoModelo,
            @RequestParam("veiculoPlaca") String veiculoPlaca,
            @RequestParam("veiculoMarca") String veiculoMarca,
            @RequestParam(value = "veiculoChassi", required = false) String veiculoChassi,
            @RequestParam("diagnosticoMecanico") String diagnostico,
            @RequestParam("solucaoAplicada") String solucao,
            @RequestParam(value = "pecasTrocadas", required = false) String pecas,
            @RequestParam(value = "valorPecas", defaultValue = "0") Double valorPecas,
            @RequestParam("valorMaoDeObra") Double valorMaoDeObra,
            @RequestParam("oficinaId") Long oficinaId, 
            RedirectAttributes attr) {

        try {
            servicoService.finalizarServicoComDados(
                id, 
                veiculoModelo, 
                veiculoPlaca, 
                veiculoMarca, 
                veiculoChassi,
                diagnostico, 
                solucao, 
                pecas, 
                valorPecas, 
                valorMaoDeObra
            );

            System.out.println(">>> Serviço " + id + " finalizado e PDF enviado.");
            attr.addFlashAttribute("sucesso", "Serviço finalizado e comprovante enviado!");

        } catch (Exception e) {
            System.err.println("Erro ao finalizar serviço: " + e.getMessage());
            e.printStackTrace();
            attr.addFlashAttribute("erro", "Erro ao finalizar: " + e.getMessage());
        }

        Servico s = servicoRepository.findById(id).orElse(null);
        if (s != null) {
            return "redirect:/mecanicos/home/" + s.getMecanico().getId();
        }
        return "redirect:/"; 
    }

    // Adicionar mecanico a oficina.
    @PostMapping("/adicionar/{oficinaId}")
    public String adicionarMecanico(
            @PathVariable Long oficinaId, 
            @ModelAttribute("mecanicoDto") @Valid MecanicoCadastroDTO dto, 
            BindingResult result, 
            @RequestParam("fotoPerfil") MultipartFile fotoPerfil, 
            RedirectAttributes attr) {

        boolean emailExiste = clienteRepository.existsByEmail(dto.getEmail()) ||
                              oficinaRepository.existsByEmail(dto.getEmail()) ||
                              mecanicoRepository.existsByEmail(dto.getEmail());

        if (emailExiste) {
            attr.addFlashAttribute("erro", "Erro: Este e-mail já está em uso por outro usuário.");
            return "redirect:/oficinas/home/" + oficinaId;
        }
        // ---------------------------------------------------

        dto.setFotoArquivo(fotoPerfil);
        
        if (result.hasErrors()) {
            attr.addFlashAttribute("erro", "Verifique os campos obrigatórios.");
            return "redirect:/oficinas/home/" + oficinaId;
        }
        
        try {
            mecanicoService.salvarMecanico(dto, oficinaId);
            attr.addFlashAttribute("sucesso", "Mecânico adicionado!");
        } catch (Exception e) {
            attr.addFlashAttribute("erro", "Erro: " + e.getMessage());
        }
        return "redirect:/oficinas/home/" + oficinaId;
    }
    
    // remover mecanico da oficina.
    @PostMapping("/remover/{mecanicoId}/{oficinaId}")
    public String removerMecanico(@PathVariable Long mecanicoId, @PathVariable Long oficinaId, RedirectAttributes attr) {
        try {
            mecanicoService.removerMecanico(mecanicoId);
            attr.addFlashAttribute("sucesso", "Mecânico removido!");
        } catch (Exception e) {
            attr.addFlashAttribute("erro", "Erro ao remover.");
        }
        return "redirect:/oficinas/home/" + oficinaId;
    }
    
    // editar informações de mecanico.
    @PostMapping("/editar/{id}")
    public String editarMecanico(@PathVariable Long id, @ModelAttribute MecanicoCadastroDTO dto, @RequestParam("fotoPerfil") MultipartFile fotoPerfil, RedirectAttributes attr) {
        dto.setFotoArquivo(fotoPerfil);
        mecanicoService.editarMecanico(id, dto);
        attr.addFlashAttribute("sucesso", "Mecânico atualizado!");
        return "redirect:/oficinas/home/" + dto.getOficinaId();
    }
    
    // Confirma edição feita em mecanicos.
    @PostMapping("/{id}/status")
    public String alternarStatus(
            @PathVariable Long id,
            @RequestParam("oficinaId") Long oficinaId,
            RedirectAttributes attr
    ) {
        mecanicoService.alternarStatus(id);
        attr.addFlashAttribute("sucesso", "Status do mecânico atualizado!");
        return "redirect:/oficinas/home/" + oficinaId;
    }
}