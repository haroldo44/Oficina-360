package edu.ifpb.oficina360.controller;

import edu.ifpb.oficina360.model.MecanicoCadastroDTO;
import edu.ifpb.oficina360.service.MecanicoService;
import edu.ifpb.oficina360.repository.OficinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

    // ===================== ADICIONAR MECÂNICO =====================
    @PostMapping("/adicionar/{oficinaId}")
    public String adicionarMecanico(
            @PathVariable Long oficinaId,
            @ModelAttribute("mecanicoDto") @Valid MecanicoCadastroDTO dto,
            BindingResult result,
            @RequestParam("fotoPerfil") MultipartFile fotoPerfil,
            RedirectAttributes attr) {

        // Log inicial
        System.out.println("=== Tentando adicionar mecânico ===");
        System.out.println("DTO recebido: " + dto.getNomeCompleto() + " | " + dto.getEmail());
        System.out.println("Horários: " + dto.getTurnoManhaInicioString() + " - " + dto.getTurnoManhaFimString()
                + " / " + dto.getTurnoTardeInicioString() + " - " + dto.getTurnoTardeFimString());

        dto.setFotoArquivo(fotoPerfil);

        // Verifica erros de validação
        if (result.hasErrors()) {
            System.out.println(">>> Erros de validação detectados:");
            result.getAllErrors().forEach(err -> System.out.println(" - " + err));
            attr.addFlashAttribute("erro", "Verifique os campos do formulário de mecânico.");
            attr.addFlashAttribute("org.springframework.validation.BindingResult.mecanicoDto", result);
            attr.addFlashAttribute("mecanicoDto", dto);
            return "redirect:/oficinas/home/" + oficinaId;
        }

        try {
            mecanicoService.salvarMecanico(dto, oficinaId);
            attr.addFlashAttribute("sucesso", "Mecânico adicionado com sucesso!");
            System.out.println(">>> Mecânico salvo com sucesso!");
        } catch (Exception e) {
            System.out.println(">>> Erro ao salvar mecânico:");
            e.printStackTrace();
            attr.addFlashAttribute("erro", "Erro ao salvar mecânico: " + e.getMessage());
        }

        return "redirect:/oficinas/home/" + oficinaId;
    }

    // ===================== REMOVER MECÂNICO =====================
    @PostMapping("/remover/{mecanicoId}/{oficinaId}")
    public String removerMecanico(
            @PathVariable Long mecanicoId,
            @PathVariable Long oficinaId,
            RedirectAttributes attr) {

        try {
            mecanicoService.removerMecanico(mecanicoId);
            attr.addFlashAttribute("sucesso", "Mecânico removido com sucesso!");
            System.out.println(">>> Mecânico removido: ID " + mecanicoId);
        } catch (Exception e) {
            System.out.println(">>> Erro ao remover mecânico:");
            e.printStackTrace();
            attr.addFlashAttribute("erro", "Erro ao remover mecânico.");
        }

        return "redirect:/oficinas/home/" + oficinaId;
    }
    
    // ===================== EDITAR MECÂNICO =====================
    @PostMapping("/editar/{id}")
    public String editarMecanico(@PathVariable Long id,
                                 @ModelAttribute MecanicoCadastroDTO dto,
                                 @RequestParam("fotoPerfil") MultipartFile fotoPerfil,
                                 RedirectAttributes attr) {
        dto.setFotoArquivo(fotoPerfil); // atualiza a foto
        mecanicoService.editarMecanico(id, dto);
        attr.addFlashAttribute("sucesso", "Mecânico atualizado com sucesso!");
        return "redirect:/oficinas/home/" + dto.getOficinaId();
    }

}
