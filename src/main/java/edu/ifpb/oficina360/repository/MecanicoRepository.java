package edu.ifpb.oficina360.repository;

import java.util.Optional; // Importe Optional

import org.springframework.data.jpa.repository.JpaRepository;
import edu.ifpb.oficina360.model.Mecanico;

public interface MecanicoRepository extends JpaRepository<Mecanico, Long> {
    
    Optional<Mecanico> findByUsuario_IdUsuario(Long idUsuario);
}