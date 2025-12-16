package edu.ifpb.oficina360.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter; // Importante para formatar a data
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.ifpb.oficina360.model.*;
import edu.ifpb.oficina360.repository.ServicoRepository;
import edu.ifpb.oficina360.service.*;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/clientes")
public class ClienteServicoController {

    @Autowired private ClienteService clienteService;
    @Autowired private ServicoService servicoService;
    @Autowired private MecanicoService mecanicoService;
    @Autowired private ServicoRepository servicoRepository;


    // ETAPA 1: ESCOLHER PROBLEMA 
    @GetMapping("/solicitar-servico/{idCliente}")
    public String etapa1Solicitacao(@PathVariable Long idCliente, HttpSession session, Model model) {
        Cliente cliente = clienteService.buscarPorId(idCliente);
        if (cliente == null) return "redirect:/";

        ServicoAgendamentoDTO dto = new ServicoAgendamentoDTO();
        dto.setIdCliente(idCliente);
        session.setAttribute("agendamentoTemp", dto);
        
        model.addAttribute("cliente", cliente);
        
        return "servicos/servico-etapa1";
    }

    @PostMapping("/solicitar-servico/etapa1")
    public String processarEtapa1(
            @RequestParam(value = "opcaoSelecionada", required = false) String titulo,
            @RequestParam("descricao") String descricao,
            HttpSession session, RedirectAttributes attr) {
        
        ServicoAgendamentoDTO dto = (ServicoAgendamentoDTO) session.getAttribute("agendamentoTemp");
        if (dto == null) return "redirect:/";

        if ((titulo == null || titulo.isEmpty()) && (descricao == null || descricao.trim().isEmpty())) {
            attr.addFlashAttribute("erro", "Selecione uma opção ou descreva o problema.");
            return "redirect:/clientes/solicitar-servico/" + dto.getIdCliente();
        }

        dto.setTitulo((titulo != null && !titulo.isEmpty()) ? titulo : "Relato Personalizado");
        dto.setDescricao(descricao);
        session.setAttribute("agendamentoTemp", dto);

        return "redirect:/clientes/solicitar-servico/etapa2";
    }

    // ETAPA 2: ESCOLHER MECÂNICO 
    @GetMapping("/solicitar-servico/etapa2")
    public String etapa2Solicitacao(HttpSession session, Model model) {
        ServicoAgendamentoDTO dto = (ServicoAgendamentoDTO) session.getAttribute("agendamentoTemp");
        if (dto == null) return "redirect:/";

        Cliente cliente = clienteService.buscarPorId(dto.getIdCliente());
        
        List<Mecanico> mecanicos = mecanicoService.buscarMecanicosPorOficina(cliente.getOficina().getId());
        
        model.addAttribute("mecanicos", mecanicos);
        model.addAttribute("cliente", cliente);
        
        return "servicos/servico-etapa2";
    }

    @PostMapping("/solicitar-servico/etapa2")
    public String processarEtapa2(@RequestParam("idMecanico") Long idMecanico, HttpSession session) {
        ServicoAgendamentoDTO dto = (ServicoAgendamentoDTO) session.getAttribute("agendamentoTemp");
        if (dto == null) return "redirect:/";

        dto.setIdMecanico(idMecanico);
        session.setAttribute("agendamentoTemp", dto);

        return "redirect:/clientes/solicitar-servico/etapa3";
    }

    // ETAPA 3: DATA E HORÁRIO
    @GetMapping("/solicitar-servico/etapa3")
    public String etapa3Solicitacao(HttpSession session, Model model) {
        ServicoAgendamentoDTO dto = (ServicoAgendamentoDTO) session.getAttribute("agendamentoTemp");
        if (dto == null) return "redirect:/";
        
        Cliente cliente = clienteService.buscarPorId(dto.getIdCliente());
        Mecanico mecanico = mecanicoService.buscarPorId(dto.getIdMecanico());

        model.addAttribute("cliente", cliente);
        model.addAttribute("mecanico", mecanico);
        
        return "servicos/servico-etapa3";
    }

    // FINALIZAR E SALVAR NO BANCO
    @PostMapping("/solicitar-servico/finalizar")
    public String finalizarSolicitacao(
            @RequestParam("data") String dataString,
            @RequestParam("hora") String horaString,
            HttpSession session, RedirectAttributes attr) {
        
        ServicoAgendamentoDTO dto = (ServicoAgendamentoDTO) session.getAttribute("agendamentoTemp");
        if (dto == null) return "redirect:/";

        try {
            LocalDate data = LocalDate.parse(dataString);
            LocalTime hora = LocalTime.parse(horaString);
            
            // Validação de data passada
            if (data.isBefore(LocalDate.now())) {
                attr.addFlashAttribute("erro", "Não é possível agendar para datas passadas.");
                return "redirect:/clientes/solicitar-servico/etapa3";
            }
            
            Mecanico mecanico = mecanicoService.buscarPorId(dto.getIdMecanico());
            
            // Verifica conflito de horário no banco (exceto cancelados)
            boolean ocupado = servicoRepository.existsByMecanicoAndDataAgendamentoAndHoraAgendamentoAndStatusNot(
                    mecanico, data, hora, "CANCELADO");
            
            if (ocupado) {
                attr.addFlashAttribute("erro", "Horário indisponível. Escolha outro.");
                return "redirect:/clientes/solicitar-servico/etapa3";
            }

            // Cria e Salva o Serviço
            Cliente cliente = clienteService.buscarPorId(dto.getIdCliente());
            Servico servico = new Servico();
            
            servico.setTitulo(dto.getTitulo());
            servico.setDescricao(dto.getDescricao());
            servico.setCliente(cliente);
            servico.setOficina(cliente.getOficina());
            servico.setMecanico(mecanico);
            servico.setDataAgendamento(data);
            servico.setHoraAgendamento(hora);
            servico.setStatus("PENDENTE");

            servicoService.salvar(servico);

            session.removeAttribute("agendamentoTemp");
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String dataFormatada = data.format(formatter);
            
            attr.addFlashAttribute("sucesso", "Agendamento realizado para " + dataFormatada + " às " + hora);
            return "redirect:/clientes/home/" + cliente.getId();

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("erro", "Erro ao processar agendamento.");
            return "redirect:/clientes/solicitar-servico/etapa3";
        }
    }
    
    // ROTA PARA CANCELAR SERVIÇO
    @PostMapping("/servicos/cancelar/{idServico}")
    public String cancelarServico(@PathVariable Long idServico, RedirectAttributes attr) {
        try {
            Servico servico = servicoRepository.findById(idServico)
                    .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

            servico.setStatus("CANCELADO"); 
            servicoRepository.save(servico); 

            attr.addFlashAttribute("sucesso", "Agendamento cancelado com sucesso.");
            return "redirect:/clientes/home/" + servico.getCliente().getId();

        } catch (Exception e) {
            e.printStackTrace();
            attr.addFlashAttribute("erro", "Erro ao cancelar serviço.");
            return "redirect:/clientes/home/" + 1;
        }
    }
}