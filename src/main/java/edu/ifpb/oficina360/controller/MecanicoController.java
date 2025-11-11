package edu.ifpb.oficina360.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.ifpb.oficina360.model.Mecanico;
import edu.ifpb.oficina360.service.MecanicoService;

@RestController
@RequestMapping("/mecanicos")
public class MecanicoController {

    @Autowired
    private MecanicoService mecanicoService;

    @PostMapping("/{idUsuario}/{idEmpresa}")
    public ResponseEntity<Mecanico> salvar(
            @PathVariable Long idUsuario,
            @PathVariable Long idEmpresa,
            @RequestBody Mecanico mecanico) {

        Mecanico novo = mecanicoService.salvar(idUsuario, idEmpresa, mecanico);
        return ResponseEntity.ok(novo);
    }

    // ✅ Listar todos os mecânicos
    @GetMapping
    public ResponseEntity<List<Mecanico>> listar() {
        return ResponseEntity.ok(mecanicoService.listar());
    }

    // ✅ Buscar mecânico por ID
    @GetMapping("/{id}")
    public ResponseEntity<Mecanico> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(mecanicoService.buscarPorId(id));
    }
}