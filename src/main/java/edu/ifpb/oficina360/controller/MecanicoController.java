package edu.ifpb.oficina360.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ifpb.oficina360.model.MecanicoCadastroDTO;
import edu.ifpb.oficina360.repository.OficinaRepository;
import edu.ifpb.oficina360.service.MecanicoService;
import edu.ifpb.oficina360.service.OficinaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/mecanicos") // Mapeamento base para /mecanicos
public class MecanicoController {

	@Autowired
	private OficinaRepository oficinaRepository; 

	@Autowired
	private MecanicoService mecanicoService;
	
	@Autowired
	private OficinaService oficinaService;

	// ===================== ADICIONAR MECÂNICO (POST) =====================
	// Rota final esperada: /mecanicos/adicionar/{oficinaId}
	@PostMapping("/adicionar/{oficinaId}") 
	public String adicionarMecanico(
	        @PathVariable Long oficinaId,
            @ModelAttribute("mecanicoDto") @Valid MecanicoCadastroDTO dto, // Usa DTO
	        BindingResult result,
            @RequestParam("fotoPerfil") MultipartFile fotoPerfil, // Recebe o arquivo
	        RedirectAttributes attr) { // Usaremos 'attr'

        // 1. ANEXA A FOTO AO DTO ANTES DA VALIDAÇÃO (O Service precisa do objeto)
        // Se a foto for obrigatória, adicione uma validação de .isEmpty() aqui.
        dto.setFotoArquivo(fotoPerfil); 
        
        // 2. Verifica erros de validação (Campos de texto/horário)
	    if (result.hasErrors()) {
	        attr.addFlashAttribute("erro", "Verifique os campos do formulário de mecânico.");
            attr.addFlashAttribute("org.springframework.validation.BindingResult.mecanicoDto", result);
            attr.addFlashAttribute("mecanicoDto", dto); 
	        return "redirect:/oficinas/home/" + oficinaId;
	    }
        
	    try {
	        // Chama o método que converte DTO, lida com horários, foto e salva
	        mecanicoService.salvarMecanico(dto, oficinaId); 
	        attr.addFlashAttribute("sucesso", "Mecânico adicionado com sucesso!");
	    } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("erro", "Erro ao salvar mecânico: " + e.getMessage());
        }

	    return "redirect:/oficinas/home/" + oficinaId;
	}


	// ===================== REMOVER MECÂNICO (POST) =====================
    // Rota final esperada: /mecanicos/remover/{mecanicoId}/{oficinaId}
	@PostMapping("/remover/{mecanicoId}/{oficinaId}") 
	public String removerMecanico(
	        @PathVariable Long mecanicoId,
	        @PathVariable Long oficinaId,
	        RedirectAttributes attr) {
	    
	    try {
	        mecanicoService.removerMecanico(mecanicoId);
	        attr.addFlashAttribute("sucesso", "Mecânico removido com sucesso!");
	    } catch (Exception e) {
	        attr.addFlashAttribute("erro", "Erro ao remover mecânico.");
	    }
	    
	    return "redirect:/oficinas/home/" + oficinaId;
	}
}