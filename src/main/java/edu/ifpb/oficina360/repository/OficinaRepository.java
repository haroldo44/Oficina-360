package edu.ifpb.oficina360.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.ifpb.oficina360.model.Oficina;

@Repository
public interface OficinaRepository extends JpaRepository<Oficina, Long> {
	Optional<Oficina> findByEmail(String email);
	boolean existsByEmail(String email);
}
