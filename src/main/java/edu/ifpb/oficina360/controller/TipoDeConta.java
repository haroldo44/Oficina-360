package edu.ifpb.oficina360.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.ifpb.oficina360.model.Cliente;

@Controller
@RequestMapping("/tipoDeContas")
public class TipoDeConta {

	@GetMapping("/escolhaTipo")
    public String etapa1(Cliente cliente) {
        return "/tipoConta/perfilConta";
    }
}
