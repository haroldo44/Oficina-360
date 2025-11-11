package edu.ifpb.oficina360.repository;

import edu.ifpb.oficina360.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

// Usamos Long para o tipo do ID (ID_USUARIO)
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    
    // Removida a necessidade do findByUsuario, pois findById(idUsuario) já resolve.
}