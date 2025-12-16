package edu.ifpb.oficina360.controller;

import edu.ifpb.oficina360.model.Mecanico;
import edu.ifpb.oficina360.model.MecanicoCadastroDTO;
import edu.ifpb.oficina360.model.Oficina;
import edu.ifpb.oficina360.model.OficinaCadastroDTO;
import edu.ifpb.oficina360.repository.ClienteRepository; 
import edu.ifpb.oficina360.repository.MecanicoRepository; 
import edu.ifpb.oficina360.repository.OficinaRepository;
import edu.ifpb.oficina360.service.MecanicoService;
import edu.ifpb.oficina360.service.OficinaService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/oficinas")
public class OficinaController {

    @Autowired
    private OficinaService oficinaService;

    @Autowired
    private OficinaRepository oficinaRepository;
    
    @Autowired 
    private MecanicoService mecanicoService;

    @Autowired
    private ClienteRepository clienteRepository; 

    @Autowired
    private MecanicoRepository mecanicoRepository; 

    
    // ETAPA 1
    @GetMapping("/cadastro/etapa1")
    public String etapa1(HttpSession session, Model model) {
        OficinaCadastroDTO dto = (OficinaCadastroDTO) session.getAttribute("cadastroOficina");
        if (dto == null) {
            dto = new OficinaCadastroDTO();
            session.setAttribute("cadastroOficina", dto);
        }
        model.addAttribute("dto", dto);
        return "oficinas/cadastro-etapa1";
    }

    @PostMapping("/cadastro/etapa1")
    public String salvarEtapa1(
            @ModelAttribute("dto") OficinaCadastroDTO dto, 
            BindingResult result,
            HttpSession session) {

        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            result.rejectValue("email", "erro", "O e-mail é obrigatório.");
        }
        if (dto.getSenha() == null || dto.getSenha().trim().isEmpty()) {
            result.rejectValue("senha", "erro", "A senha é obrigatória.");
        }

        // --- 2. VERIFICAÇÃO DE EMAIL DUPLICADO ---
        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            boolean emailExiste = oficinaRepository.existsByEmail(dto.getEmail()) ||
                                  clienteRepository.existsByEmail(dto.getEmail()) ||
                                  mecanicoRepository.existsByEmail(dto.getEmail());

            if (emailExiste) {
                result.rejectValue("email", "erro", "Este e-mail já está em uso.");
            }
        }

        // --- 3. CONFIRMAÇÃO DE SENHA ---
        if (dto.getSenha() != null && !dto.getSenha().equals(dto.getConfirmarSenha())) {
            result.rejectValue("confirmarSenha", null, "As senhas não conferem.");
        }

        if (result.hasErrors()) {
            return "oficinas/cadastro-etapa1";
        }

        session.setAttribute("cadastroOficina", dto);
        return "redirect:/oficinas/cadastro/etapa2";
    }


    // ETAPA 2
    @GetMapping("/cadastro/etapa2")
    public String etapa2(HttpSession session, Model model) {
        OficinaCadastroDTO dto = (OficinaCadastroDTO) session.getAttribute("cadastroOficina");
        if (dto == null) {
            return "redirect:/oficinas/cadastro/etapa1";
        }
        model.addAttribute("dto", dto);
        return "oficinas/cadastro-etapa2";
    }

    @PostMapping("/cadastro/etapa2")
    public String processarEtapa2(
            @Valid @ModelAttribute("dto") OficinaCadastroDTO dto,
            BindingResult result,
            @RequestParam("imagemOficinaArquivo") MultipartFile imagemOficina,
            @RequestParam("fotoProprietarioArquivo") MultipartFile fotoProprietario,
            HttpSession session,
            RedirectAttributes attr) {

        if (result.hasErrors()) {
            return "oficinas/cadastro-etapa2";
        }

        OficinaCadastroDTO etapa1 = (OficinaCadastroDTO) session.getAttribute("cadastroOficina");
        if (etapa1 == null) return "redirect:/oficinas/cadastro/etapa1"; // Segurança caso a sessão expire

        dto.setEmail(etapa1.getEmail());
        dto.setSenha(etapa1.getSenha());
        dto.setConfirmarSenha(etapa1.getConfirmarSenha());

        try {
            Oficina oficina = oficinaService.finalizarCadastro(dto, imagemOficina, fotoProprietario);

            session.removeAttribute("cadastroOficina");
            attr.addFlashAttribute("sucesso", "Oficina criada com sucesso!");

            return "redirect:/oficinas/home/" + oficina.getId();
        } catch (RuntimeException e) {
             attr.addFlashAttribute("erro", "Erro ao criar oficina: " + e.getMessage());
             return "redirect:/oficinas/cadastro/etapa2";
        }
    }


    // Redireciona para a página home de empresa.
    @GetMapping("/home/{id}")
    public String home(@PathVariable Long id, Model model) {
        Oficina oficina = oficinaService.buscarPorId(id); 
        
        List<Mecanico> mecanicos = mecanicoService.buscarMecanicosPorOficina(id);
        
        model.addAttribute("oficina", oficina);
        model.addAttribute("mecanicos", mecanicos); 
        model.addAttribute("mecanicoDto", new MecanicoCadastroDTO()); 
        
        return "oficinas/oficina-home";
    }


    // Atualiza informações de oficina.
    @PostMapping("/atualizar/{id}")
    public String atualizarOficina(
            @PathVariable Long id,
            @ModelAttribute Oficina oficinaAtualizada,
            RedirectAttributes attr) {

        oficinaService.atualizarInformacoes(id, oficinaAtualizada);

        attr.addFlashAttribute("sucesso", "Informações atualizadas com sucesso!");
        return "redirect:/oficinas/home/" + id;
    }


    // Alterar foto de oficina.
    @PostMapping("/alterar-foto/{id}")
    public String alterarFotoOficina(
            @PathVariable Long id,
            @RequestParam("novaFoto") MultipartFile novaFoto,
            RedirectAttributes attr) {

        try {
            oficinaService.atualizarFoto(id, novaFoto);
            attr.addFlashAttribute("sucesso", "Foto da oficina atualizada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("erro", "Erro ao atualizar foto da oficina: " + e.getMessage());
        }

        return "redirect:/oficinas/home/" + id;
    }


    // Alterar foto de proprietário.
    @PostMapping("/alterar-foto-proprietario/{id}")
    public String alterarFotoProprietario(
            @PathVariable Long id,
            @RequestParam("novaFotoProprietario") MultipartFile novaFotoProprietario,
            RedirectAttributes attr) {

        try {
            oficinaService.atualizarFotoProprietario(id, novaFotoProprietario);
            attr.addFlashAttribute("sucesso", "Foto do proprietário atualizada com sucesso!");
        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("erro", "Erro ao atualizar foto do proprietário: " + e.getMessage());
        }

        return "redirect:/oficinas/home/" + id;
    }


    // Redireciona para a página de perfil de usuário empresa.
    @GetMapping("/perfil/{id}")
    public String perfil(@PathVariable Long id, Model model) {

        Oficina oficina = oficinaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Oficina não encontrada"));

        model.addAttribute("oficina", oficina);

        return "oficinas/perfil";
    }
    

    //  Excluir conta
    @PostMapping("/excluir/{id}")
    public String excluirOficina(@PathVariable Long id, RedirectAttributes attr) {
        try {
            oficinaService.excluir(id); 
             
            attr.addFlashAttribute("sucesso", "Oficina excluída com sucesso!");
        } catch (Exception e) {
            attr.addFlashAttribute("erro", "Erro ao excluir oficina: " + e.getMessage());
         }

        return "redirect:/"; 
    }
}