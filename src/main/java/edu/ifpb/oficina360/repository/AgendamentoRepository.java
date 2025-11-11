package edu.ifpb.oficina360.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.ifpb.oficina360.model.Agendamento;

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
	
}
