function previewAndSubmit(event, form) {
    const reader = new FileReader();
    reader.onload = function() {
        document.getElementById("previewFotoOficina").src = reader.result;
    };
    reader.readAsDataURL(event.target.files[0]);
    setTimeout(() => form.submit(), 300);
}

// ===============================
// BOTÕES EDITAR / SALVAR / CANCELAR
// ===============================

const btnEditar   = document.getElementById("btnEditar");
const btnSalvar   = document.getElementById("btnSalvar");
const btnCancelar = document.getElementById("btnCancelar");

// botão dentro do form
const btnSalvarForm = document.getElementById("btnSalvarForm");

const campos = document.querySelectorAll(".campo-editavel");

let valoresOriginais = {};

// EDITAR
btnEditar.addEventListener("click", () => {
    valoresOriginais = {};

    campos.forEach(campo => {
        valoresOriginais[campo.name] = campo.value;
        campo.removeAttribute("readonly");
        campo.classList.add("editando");
    });

    btnEditar.style.display = "none";
    btnSalvar.style.display = "inline-block";
    btnCancelar.style.display = "inline-block";
});

// CANCELAR
btnCancelar.addEventListener("click", () => {
    campos.forEach(campo => {
        campo.value = valoresOriginais[campo.name];
        campo.setAttribute("readonly", true);
        campo.classList.remove("editando");
    });

    btnEditar.style.display = "inline-block";
    btnSalvar.style.display = "none";
    btnCancelar.style.display = "none";
});

// SALVAR
btnSalvar.addEventListener("click", () => {
    // clica automaticamente no submit REAL
    btnSalvarForm.click();
});


// ADICIONAR MECANICO

// Fecha a modal se o usuário clicar fora dela
window.onclick = function(event) {
    const modal = document.getElementById('modalAdicionarMecanico');
    if (event.target === modal) {
        modal.style.display = "none";
    }
}





