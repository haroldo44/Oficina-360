package edu.ifpb.oficina360.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.ifpb.oficina360.model.OrdemServico;

public interface OrdemServicoRepository extends JpaRepository<OrdemServico, Long> {
}
