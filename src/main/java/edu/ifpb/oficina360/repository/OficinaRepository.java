package edu.ifpb.oficina360.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.ifpb.oficina360.model.Oficina;

@Repository
public interface OficinaRepository extends JpaRepository<Oficina, Long> {
    // JpaRepository já traz métodos como save, findById, deleteById, etc.
}
