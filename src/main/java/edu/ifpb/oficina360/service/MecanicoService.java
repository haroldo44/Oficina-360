package edu.ifpb.oficina360.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ifpb.oficina360.model.Empresa;
import edu.ifpb.oficina360.model.Mecanico;
import edu.ifpb.oficina360.model.Usuario;
import edu.ifpb.oficina360.repository.EmpresaRepository;
import edu.ifpb.oficina360.repository.MecanicoRepository;
import edu.ifpb.oficina360.repository.UsuarioRepository;

@Service
public class MecanicoService {

    @Autowired
    private MecanicoRepository mecanicoRepository;

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Mecanico salvar(Long idUsuario, Long idEmpresa, Mecanico mecanico) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + idUsuario));

        Empresa empresa = empresaRepository.findById(idEmpresa)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada: " + idEmpresa));

        mecanico.setUsuario(usuario);
        mecanico.setEmpresa(empresa);

        usuario.setTipo("MECANICO");
        usuarioRepository.save(usuario);

        return mecanicoRepository.save(mecanico);
    }

    public List<Mecanico> listar() {
        return mecanicoRepository.findAll();
    }

    public Mecanico buscarPorId(Long id) {
        return mecanicoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Mecânico não encontrado: " + id));
    }
}
