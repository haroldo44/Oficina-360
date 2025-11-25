package edu.ifpb.oficina360.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.ifpb.oficina360.model.Urgencia;

@Repository
public interface UrgenciaRepository extends JpaRepository<Urgencia, Long> {

    List<Urgencia> findByOficinaIdAndResolvidoFalse(Long oficinaId);

    List<Urgencia> findByClienteId(Long clienteId);
}

