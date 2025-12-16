package edu.ifpb.oficina360.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.ClienteCadastroDTO;
import edu.ifpb.oficina360.model.Oficina;
import edu.ifpb.oficina360.repository.ClienteRepository;
import edu.ifpb.oficina360.repository.MecanicoRepository; // Importe o Repo Mecanico
import edu.ifpb.oficina360.repository.OficinaRepository; // Importe o Repo Oficina
import edu.ifpb.oficina360.service.ClienteService;
import edu.ifpb.oficina360.service.OficinaService;
import edu.ifpb.oficina360.service.ServicoService;
import jakarta.servlet.http.HttpSession;
// Removi o import do @Valid pois faremos manualmente

@Controller
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private OficinaService oficinaService;

    @Autowired
    private ServicoService servicoService;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private OficinaRepository oficinaRepository; // Injeção da Oficina

    @Autowired
    private MecanicoRepository mecanicoRepository; // Injeção do Mecanico


    // ETAPA 1
    @GetMapping("/cadastro/etapa1")
    public String etapa1(Model model) {
        if (!model.containsAttribute("dto")) {
            model.addAttribute("dto", new ClienteCadastroDTO());
        }
        return "clientes/cadastro-etapa1";
    }

    @PostMapping("/cadastro/etapa1")
    public String salvarEtapa1(@ModelAttribute("dto") ClienteCadastroDTO dto, // SEM @Valid aqui
                               BindingResult result,
                               HttpSession session) {

        if (dto.getEmail() == null || dto.getEmail().trim().isEmpty()) {
            result.rejectValue("email", "erro", "O e-mail é obrigatório.");
        }
        if (dto.getSenha() == null || dto.getSenha().trim().isEmpty()) {
            result.rejectValue("senha", "erro", "A senha é obrigatória.");
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            boolean emailExiste = clienteRepository.existsByEmail(dto.getEmail()) ||
                                  oficinaRepository.existsByEmail(dto.getEmail()) ||
                                  mecanicoRepository.existsByEmail(dto.getEmail());

            if (emailExiste) {
                result.rejectValue("email", "erro", "Este e-mail já está em uso.");
            }
        }

        // Se tiver erro (vazio ou duplicado), volta
        if (result.hasErrors()) {
            return "clientes/cadastro-etapa1";
        }

        // --- 3. CONFIRMAÇÃO DE SENHA ---
        if (!dto.getSenha().equals(dto.getConfirmarSenha())) {
            result.rejectValue("confirmarSenha", null, "As senhas não conferem.");
            return "clientes/cadastro-etapa1";
        }

        session.setAttribute("cadastroCliente", dto);
        return "redirect:/clientes/cadastro/etapa2";
    }

    // ETAPA 2
    @GetMapping("/cadastro/etapa2")
    public String etapa2(HttpSession session, Model model) {
        ClienteCadastroDTO dto = (ClienteCadastroDTO) session.getAttribute("cadastroCliente");
        if (dto == null) return "redirect:/clientes/cadastro/etapa1";
        model.addAttribute("dto", dto);
        return "clientes/cadastro-etapa2";
    }

    @PostMapping("/cadastro/etapa2")
    public String salvarEtapa2(
            @ModelAttribute("dto") ClienteCadastroDTO dto, // SEM @Valid aqui também
            BindingResult result,
            @RequestParam(value = "fotoClienteArquivo", required = false) MultipartFile fotoCliente,
            HttpSession session,
            RedirectAttributes attr) {

        // --- VALIDAÇÃO MANUAL BÁSICA ---
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            result.rejectValue("nome", "erro", "O nome é obrigatório.");
        }
        if (dto.getCpf() == null || dto.getCpf().trim().isEmpty()) {
            result.rejectValue("cpf", "erro", "O CPF é obrigatório.");
        }

        if (result.hasErrors()) {
            return "clientes/cadastro-etapa2";
        }

        // Recupera dados da Etapa 1
        ClienteCadastroDTO etapa1 = (ClienteCadastroDTO) session.getAttribute("cadastroCliente");
        if (etapa1 == null) return "redirect:/clientes/cadastro/etapa1";

        // Mantém os dados da etapa 1
        dto.setEmail(etapa1.getEmail());
        dto.setSenha(etapa1.getSenha());
        dto.setConfirmarSenha(etapa1.getConfirmarSenha());

        // Processa Foto
        if (fotoCliente != null && !fotoCliente.isEmpty()) {
            String nomeArquivo = clienteService.salvarArquivoTemporario(fotoCliente);
            session.setAttribute("fotoClienteNomeTemp", nomeArquivo);
        }

        session.setAttribute("cadastroCliente", dto);
        return "redirect:/clientes/cadastro/etapa3";
    }

    // ETAPA 3
    @GetMapping("/cadastro/etapa3")
    public String etapa3(HttpSession session, Model model) {
        ClienteCadastroDTO dto = (ClienteCadastroDTO) session.getAttribute("cadastroCliente");
        if (dto == null) return "redirect:/clientes/cadastro/etapa1";
        model.addAttribute("dto", dto);
        model.addAttribute("oficinas", oficinaService.buscarTodas());
        return "clientes/cadastro-etapa3";
    }

    @PostMapping("/cadastro/etapa3")
    public String salvarEtapa3(@ModelAttribute("dto") ClienteCadastroDTO dto,
                               HttpSession session) {

        if (dto.getOficinaId() == null) {
            // Se quiser validar aqui também pode, mas geralmente o select obriga
            return "clientes/cadastro-etapa3";
        }

        ClienteCadastroDTO dtoSessao = (ClienteCadastroDTO) session.getAttribute("cadastroCliente");
        dtoSessao.setOficinaId(dto.getOficinaId());
        session.setAttribute("cadastroCliente", dtoSessao);

        return "forward:/clientes/salvar-final";
    }

    // SALVAR FINAL
    @PostMapping("/salvar-final")
    public String salvarFinal(HttpSession session, RedirectAttributes attr) {
        ClienteCadastroDTO dto = (ClienteCadastroDTO) session.getAttribute("cadastroCliente");

        if (dto == null) {
            attr.addFlashAttribute("erro", "Sessão expirada. Refaça o cadastro.");
            return "redirect:/clientes/cadastro/etapa1";
        }

        String fotoNome = (String) session.getAttribute("fotoClienteNomeTemp");

        try {
            Cliente cliente = clienteService.finalizarCadastro(dto, fotoNome);

            session.removeAttribute("cadastroCliente");
            session.removeAttribute("fotoClienteNomeTemp");

            return "redirect:/clientes/home/" + cliente.getId();
        } catch (RuntimeException e) {
            attr.addFlashAttribute("erro", "Erro ao finalizar cadastro: " + e.getMessage());
            return "redirect:/clientes/cadastro/etapa3";
        }
    }

    // HOME DO CLIENTE
    @GetMapping("/home/{idCliente}")
    public String homeCliente(@PathVariable Long idCliente, Model model) {
        Cliente cliente = clienteService.buscarPorId(idCliente);
        if (cliente == null) return "redirect:/";
        Oficina oficina = cliente.getOficina();

        model.addAttribute("cliente", cliente);
        model.addAttribute("oficina", oficina);

        model.addAttribute("servicosAgendados",
                servicoService.buscarPorClienteEStatus(cliente, "PENDENTE"));
        model.addAttribute("servicosFinalizados",
                servicoService.buscarPorClienteEStatus(cliente, "FINALIZADO"));

        return "clientes/tela-cliente";
    }


    // PERFIL
    @GetMapping("/perfil/{id}")
    public String perfilCliente(@PathVariable Long id, Model model) {
        Cliente cliente = clienteService.buscarPorId(id);
        if (cliente == null) return "redirect:/";
        model.addAttribute("cliente", cliente);
        return "clientes/perfil";
    }

    // ALTERAR FOTO DE PERFIL
    @PostMapping("/alterar-foto/{id}")
    public String alterarFotoCliente(
            @PathVariable Long id,
            @RequestParam("fotoPerfilArquivo") MultipartFile arquivo) {

        try {
            Cliente cliente = clienteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

            if (!arquivo.isEmpty()) {
                String uploadDir = "uploads/";
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String nomeArquivo = UUID.randomUUID() + "_" + arquivo.getOriginalFilename();
                Path destino = Paths.get(uploadDir + nomeArquivo);

                Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

                cliente.setFoto(nomeArquivo);
                clienteRepository.save(cliente);
            }
            return "redirect:/clientes/perfil/" + id;

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/erro";
        }
    }

    // EXCLUIR
    @PostMapping("/excluir/{id}")
    public String excluirCliente(@PathVariable Long id, RedirectAttributes attr) {
        try {
            clienteService.excluir(id);
            attr.addFlashAttribute("sucesso", "Conta excluída com sucesso!");
        } catch (Exception e) {
            attr.addFlashAttribute("erro", "Erro ao excluir conta: " + e.getMessage());
        }
        return "redirect:/";
    }
}