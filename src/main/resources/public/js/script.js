document.getElementById("usuarioButton").addEventListener("click", function() {
    // Llamar al método del sistema Java para usuarios
    // Puedes usar AJAX para hacer la solicitud al sistema Java
    // Aquí hay un ejemplo básico de cómo podrías hacerlo con fetch:
    fetch('/ruta-al-metodo-java-de-usuario')
    .then(response => {
        if (response.status === 200) {
            // Manejar la respuesta exitosa
        } else {
            // Manejar errores
        }
    })
    .catch(error => {
        // Manejar errores de conexión
    });
});

document.getElementById("adminButton").addEventListener("click", function() {
        const data = {
            // Puedes agregar datos aquí
            //username: document.getElementById('usernameInput').value,
            //password: document.getElementById('passwordInput').value,
            //key1: 'value1',
            //key2: 'value2',
        };

        const requestOptions = {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json', // Ajusta el tipo de contenido según tus necesidades
            },
            body: JSON.stringify(data), // Convierte los datos a JSON si es necesario
        };
    fetch('/api/vending-machines/login/1', requestOptions)
    .then(response => {
        console.log(response);
        if (response.status === 200) {
            // Manejar la respuesta exitosa
        } else {
            // Manejar errores
        }
    })
    .catch(error => {
        // Manejar errores de conexión
    });
});