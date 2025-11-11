package edu.ifpb.oficina360.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ifpb.oficina360.model.OrdemServico;
import edu.ifpb.oficina360.service.OrdemServicoService;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService ordemServicoService;

    // --- Endpoints Básicos ---

    @GetMapping
    public List<OrdemServico> listar() {
        // Assume que OrdemServicoService possui um método listar()
        return ordemServicoService.listar(); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServico> buscar(@PathVariable Long id) {
        // Assume que OrdemServicoService possui um método buscarPorId(Long id) que retorna Optional ou a entidade
        OrdemServico os = ordemServicoService.buscarPorId(id);
        return os != null ? ResponseEntity.ok(os) : ResponseEntity.notFound().build();
    }
    
    // --- Endpoints de Criação/Atualização (Adaptados) ---

    /**
     * POST: Cria uma nova Ordem de Serviço, ligando-a a um Cliente e um Mecânico.
     * URI: /ordens-servico/{idCliente}/{idMecanico}
     */
    @PostMapping("/{idCliente}/{idMecanico}")
    public ResponseEntity<OrdemServico> criarOrdemServico(
            @RequestBody OrdemServico ordemServico,
            @PathVariable Long idCliente,
            @PathVariable Long idMecanico) {
        
        OrdemServico novaOrdem = ordemServicoService.salvar(ordemServico, idCliente, idMecanico);
        return new ResponseEntity<>(novaOrdem, HttpStatus.CREATED);
    }
    
    /**
     * PUT: Atualiza uma Ordem de Serviço existente.
     * URI: /ordens-servico/{id}
     */
    @PutMapping("/{id}")
    public OrdemServico atualizar(@PathVariable Long id, @RequestBody OrdemServico ordemServico) {
        // Assume que o método atualizar fará o trabalho de busca e merge no Service
        return ordemServicoService.atualizar(id, ordemServico); 
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        ordemServicoService.deletar(id);
    }
}