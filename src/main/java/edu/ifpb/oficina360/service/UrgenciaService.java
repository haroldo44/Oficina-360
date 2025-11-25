package edu.ifpb.oficina360.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ifpb.oficina360.model.Urgencia;
import edu.ifpb.oficina360.repository.UrgenciaRepository;

@Service
public class UrgenciaService {

    @Autowired
    private UrgenciaRepository urgenciaRepository;

    public Urgencia salvar(Urgencia urgencia) {
        return urgenciaRepository.save(urgencia);
    }
}
