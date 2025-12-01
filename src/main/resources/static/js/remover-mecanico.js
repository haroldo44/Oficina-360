// Ativa o modo de seleção: mostra ou esconde os botões "Remover" de cada card
(function() {
    const btnRemoverGlobal = document.getElementById("btnRemoverMecanico");
    if (btnRemoverGlobal) {
        btnRemoverGlobal.addEventListener("click", () => {
            const botoes = document.querySelectorAll(".btn-remover-individual");
            const mostrandoAlgum = Array.from(botoes).some(btn => btn.style.display !== "none");
            const novoDisplay = mostrandoAlgum ? "none" : "inline-block";
            botoes.forEach(btn => btn.style.display = novoDisplay);
        });
    }
})();

// Função que envia o POST para remover o mecânico
function removerMecanico(mecanicoId, oficinaId) {
    const form = document.createElement("form");
    form.method = "post";
    form.action = `/mecanicos/remover/${mecanicoId}/${oficinaId}`;
    document.body.appendChild(form);
    form.submit();
}

// Função que abre a modal de edição e preenche os campos com os dados do card clicado
function abrirModalEdicao(card) {
    const mecanico = {
        id: card.getAttribute("data-id"),
        nomeCompleto: card.getAttribute("data-nome"),
        email: card.getAttribute("data-email"),
        senha: card.getAttribute("data-senha"),
        numeroTelefone: card.getAttribute("data-telefone"),
        turnoManhaInicioString: card.getAttribute("data-manha-inicio"),
        turnoManhaFimString: card.getAttribute("data-manha-fim"),
        turnoTardeInicioString: card.getAttribute("data-tarde-inicio"),
        turnoTardeFimString: card.getAttribute("data-tarde-fim")
    };

    // Preenche os campos da modal
    document.getElementById("editId").value = mecanico.id;
    document.getElementById("editNome").value = mecanico.nomeCompleto;
    document.getElementById("editEmail").value = mecanico.email;
    document.getElementById("editSenha").value = mecanico.senha;
    document.getElementById("editTelefone").value = mecanico.numeroTelefone;
    document.getElementById("editManhaInicio").value = mecanico.turnoManhaInicioString;
    document.getElementById("editManhaFim").value = mecanico.turnoManhaFimString;
    document.getElementById("editTardeInicio").value = mecanico.turnoTardeInicioString;
    document.getElementById("editTardeFim").value = mecanico.turnoTardeFimString;

    // Define a action do formulário de edição e abre a modal
    document.getElementById("formEditarMecanico").action = `/mecanicos/editar/${mecanico.id}`;
    document.getElementById("modalEditarMecanico").style.display = "block";
}