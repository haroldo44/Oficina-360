package edu.ifpb.oficina360.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.ifpb.oficina360.model.Urgencia;
import edu.ifpb.oficina360.service.UrgenciaService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/urgencia")
public class UrgenciaController {

    @Autowired
    private UrgenciaService urgenciaService;

    @GetMapping("/solicitar")
    public String abrirFormulario(ModelMap model) {
        model.addAttribute("urgencia", new Urgencia());
        return "/cliente/urgencia-form";
    }

    @PostMapping("/salvar")
    public String salvarUrgencia(
        @Valid Urgencia urgencia,
        BindingResult result,
        ModelMap model
    ) {
        if (result.hasErrors()) {
            return "/cliente/urgencia-form";
        }

        urgenciaService.salvar(urgencia);
        model.addAttribute("sucesso", "Urgência enviada à oficina!");
        return "/cliente/urgencia-sucesso";
    }
}

