package edu.ifpb.oficina360.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import edu.ifpb.oficina360.model.Empresa;
import edu.ifpb.oficina360.model.Usuario;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    Optional<Empresa> findByUsuario(Usuario usuario);
}
