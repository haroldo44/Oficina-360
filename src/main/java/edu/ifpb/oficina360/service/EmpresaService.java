package edu.ifpb.oficina360.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ifpb.oficina360.model.Empresa;
import edu.ifpb.oficina360.model.Usuario;
import edu.ifpb.oficina360.repository.EmpresaRepository;
import edu.ifpb.oficina360.repository.UsuarioRepository;
import jakarta.transaction.Transactional;

@Service
public class EmpresaService {

    @Autowired
    private EmpresaRepository empresaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional
    public Empresa salvar(Long idUsuario, Empresa empresa) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado: " + idUsuario));

        empresa.setIdUsuario(null);
        empresa.setUsuario(usuario);

        return empresaRepository.save(empresa);
    }


    public List<Empresa> listar() {
        return empresaRepository.findAll();
    }

    public Empresa buscarPorId(Long id) {
        return empresaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Empresa não encontrada! ID: " + id));
    }

    public Empresa atualizar(Long id, Empresa novosDados) {
        Empresa empresaExistente = buscarPorId(id);

        empresaExistente.setNomeOficina(novosDados.getNomeOficina());
        empresaExistente.setCnpj(novosDados.getCnpj());
        empresaExistente.setCidadeOficina(novosDados.getCidadeOficina());
        empresaExistente.setBairroOficina(novosDados.getBairroOficina());
        empresaExistente.setRuaOficina(novosDados.getRuaOficina());
        empresaExistente.setTelefone(novosDados.getTelefone());
        empresaExistente.setImagemOficina(novosDados.getImagemOficina());
        empresaExistente.setCidadeProprietario(novosDados.getCidadeProprietario());
        empresaExistente.setBairroProprietario(novosDados.getBairroProprietario());
        empresaExistente.setRuaProprietario(novosDados.getRuaProprietario());

        return empresaRepository.save(empresaExistente);
    }

    public void deletar(Long id) {
        Empresa empresa = buscarPorId(id);
        empresaRepository.delete(empresa);
    }
}
