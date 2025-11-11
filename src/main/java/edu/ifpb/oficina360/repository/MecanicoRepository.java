package edu.ifpb.oficina360.repository;

import java.util.Optional; // Importe Optional

import org.springframework.data.jpa.repository.JpaRepository;
import edu.ifpb.oficina360.model.Mecanico;

// A chave primária de Mecanico é Long (ID_MECANICO)
public interface MecanicoRepository extends JpaRepository<Mecanico, Long> {
    
    // ⚠️ NOVO MÉTODO: Permite buscar o Mecânico pelo ID do Usuário (ID_USUARIO)
    Optional<Mecanico> findByUsuario_IdUsuario(Long idUsuario);
}