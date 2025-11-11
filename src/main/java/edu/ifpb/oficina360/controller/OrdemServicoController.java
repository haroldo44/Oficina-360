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


    @GetMapping
    public List<OrdemServico> listar() {
        return ordemServicoService.listar(); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdemServico> buscar(@PathVariable Long id) {
        OrdemServico os = ordemServicoService.buscarPorId(id);
        return os != null ? ResponseEntity.ok(os) : ResponseEntity.notFound().build();
    }
    

    @PostMapping("/{idCliente}/{idMecanico}")
    public ResponseEntity<OrdemServico> criarOrdemServico(
            @RequestBody OrdemServico ordemServico,
            @PathVariable Long idCliente,
            @PathVariable Long idMecanico) {
        
        OrdemServico novaOrdem = ordemServicoService.salvar(ordemServico, idCliente, idMecanico);
        return new ResponseEntity<>(novaOrdem, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public OrdemServico atualizar(@PathVariable Long id, @RequestBody OrdemServico ordemServico) {
        return ordemServicoService.atualizar(id, ordemServico); 
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        ordemServicoService.deletar(id);
    }
}