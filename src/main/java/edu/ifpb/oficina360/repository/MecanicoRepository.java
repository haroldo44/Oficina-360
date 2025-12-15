package edu.ifpb.oficina360.repository;

import edu.ifpb.oficina360.model.Mecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MecanicoRepository extends JpaRepository<Mecanico, Long> {
    List<Mecanico> findByOficinaId(Long oficinaId);
    Optional<Mecanico> findByEmail(String email);
    
    List<Mecanico> findByOficinaIdAndAtivoTrue(Long oficinaId);
}
