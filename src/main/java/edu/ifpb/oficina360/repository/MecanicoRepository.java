package edu.ifpb.oficina360.repository;

import edu.ifpb.oficina360.model.Mecanico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MecanicoRepository extends JpaRepository<Mecanico, Long> {
    
    // Adicione um método para buscar todos os mecânicos de uma oficina específica.
    List<Mecanico> findByOficinaId(Long oficinaId);
}
