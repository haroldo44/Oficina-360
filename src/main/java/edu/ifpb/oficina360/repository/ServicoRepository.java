package edu.ifpb.oficina360.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByClienteAndStatus(Cliente cliente, String status);
}

