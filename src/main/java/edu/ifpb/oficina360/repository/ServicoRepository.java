package edu.ifpb.oficina360.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.ifpb.oficina360.model.Cliente;
import edu.ifpb.oficina360.model.Mecanico;
import edu.ifpb.oficina360.model.Servico;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, Long> {
    
    List<Servico> findByOficinaIdAndStatus(Long oficinaId, String status);
    List<Servico> findByClienteAndStatus(Cliente cliente, String status);
    List<Servico> findByMecanicoAndStatus(Mecanico mecanico, String status);
    
    // Método para validar disponibilidade do mecânico
    boolean existsByMecanicoAndDataAgendamentoAndHoraAgendamentoAndStatusNot(
            Mecanico mecanico, LocalDate data, LocalTime hora, String statusIgnorado);
}