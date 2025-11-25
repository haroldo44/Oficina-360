<script>
function fazerLogin(event) {
    event.preventDefault();

    fetch("http://localhost:8080/usuarios/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
            email: document.getElementById("email").value,
            senha: document.getElementById("senha").value
        })
    })
    .then(r => r.text())
    .then(msg => alert(msg))
    .catch(err => alert("Erro ao se comunicar com o servidor"));
}
</script>
