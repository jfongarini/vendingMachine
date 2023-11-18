const token = localStorage.getItem('token');
const operation = localStorage.getItem('operation');
const status = localStorage.getItem('status');
const urlBase = 'http://localhost:8080/api/';

function getProducts() {
    const url = urlBase + 'products';
    const options = {

      method: 'GET',
      headers: {
        'Content-type': 'application/json'
      }
    };

    return fetch(url, options)
      .then(response => {
          if (!response.ok) {
            throw new Error('Exception error: ' + response.statusText);
          }
          return response.json();
        })
        .then(data => {
          if (data) {
            console.log('Get Vending Machines:', data);
            return data;
          } else {
            console.warn('Empty Response');
          }
        })
      .catch(error => {
        console.error('Request Error:', error);
      });
}

function getCoins() {
    const url = urlBase + 'coins';
    const options = {

      method: 'GET',
      headers: {
        'Content-type': 'application/json'
      }
    };

    return fetch(url, options)
      .then(response => {
          if (!response.ok) {
            throw new Error('Exception error: ' + response.statusText);
          }
          return response.json();
        })
        .then(data => {
          if (data) {
            console.log('Get Vending Machines:', data);
            return data;
          } else {
            console.warn('Empty Response');
          }
        })
      .catch(error => {
        console.error('Request Error:', error);
      });
}

function loadProducts(){
    const productList = document.getElementById('product-list');
    getProducts()
    .then(list => {
        console.log(list.data.vendingMachineList);
        list.data.vendingMachineList.forEach((element, index) => {
            const row = document.createElement('tr');
            const codeCell = document.createElement('td');
            codeCell.textContent = product.code;
            const nameCell = document.createElement('td');
            nameCell.textContent = product.name;
            const priceCell = document.createElement('td');
            priceCell.textContent = `$${product.price.toFixed(2)}`;
            const quantityCell = document.createElement('td');
            quantityCell.textContent = product.quantity;

            row.appendChild(codeCell);
            row.appendChild(nameCell);
            row.appendChild(priceCell);
            row.appendChild(quantityCell);
            productList.appendChild(row);
        });
    });
}

loadProducts();



// Ejemplo de datos de monedas (reemplaza con los datos reales de tu API)
const coinData = [
    { name: 'Ten dollar', value: 10.00 },
    { name: 'Quarter', value: 0.25 },
    { name: 'Dime', value: 0.10 },
    { name: 'Nickel', value: 0.05 },
];

// Función para cargar monedas desde datos simulados (en lugar de una API)
function loadCoins() {
    const coinSelect = document.getElementById('coin-select');
    coinSelect.innerHTML = '';

    coinData.forEach((coin, index) => {
        const option = document.createElement('option');
        option.value = index;
        option.textContent = `${coin.name} - $${coin.value.toFixed(2)}`;
        coinSelect.appendChild(option);
    });
}

// Función para calcular y actualizar el total
function updateTotal(amount) {
    const totalAmount = document.getElementById('total-amount');
    totalAmount.textContent = amount.toFixed(2);
}

// Inicialización al cargar la página
document.addEventListener('DOMContentLoaded', function() {
    loadCoins();
    let totalAmount = 0;

    const selectedProduct = document.getElementById('selected-product-name');
    const coinsInserted = document.getElementById('coins-inserted-amount');
    const changeAmount = document.getElementById('change-amount');
    const purchaseMessage = document.getElementById('purchase-message');
    const purchaseSummary = document.getElementById('purchase-summary');
    const insertButton = document.getElementById('insert-button');
    const acceptButton = document.getElementById('accept-button');
    const cancelButton = document.getElementById('cancel-button');
    const newOperationButton = document.getElementById('new-operation-button');


    // Evento click en el botón "Insert"
    document.getElementById('insert-button').addEventListener('click', function() {
        const coinSelect = document.getElementById('coin-select');
        const selectedCoinIndex = coinSelect.value;

        if (selectedCoinIndex !== "") {
            const selectedCoin = coinData[selectedCoinIndex];
            totalAmount += selectedCoin.value;
            updateTotal(totalAmount);
        }
    });

    // Evento click en el botón "Accept"
    document.getElementById('accept-button').addEventListener('click', function() {
        // Implementa la lógica para aceptar la selección y realizar una llamada a la API si es necesario.
        console.log('Producto aceptado');
        // Por ejemplo, puedes restar el precio del producto del total y realizar una llamada a la API para procesar la compra.
        // totalAmount -= productoSeleccionado.price;
        // Realiza una llamada a la API para comprar el producto.
            // Muestra el resumen de la compra
        
        const selectedProductData = productData[0]; // Debes obtener el producto seleccionado de tu lógica
        const totalInserted = totalAmount;

        if (selectedProductData && totalInserted >= selectedProductData.price) {
            // Mostrar el producto seleccionado y las monedas ingresadas
            selectedProduct.textContent = selectedProductData.name;
            coinsInserted.textContent = "$"+totalInserted.toFixed(2);
            changeAmount.textContent = "$"+(totalInserted - selectedProductData.price).toFixed(2);
            purchaseMessage.textContent = "Purchase successful!";
            newOperationButton.style.display = 'block';
                // Deshabilitar el botón "Insert"
            insertButton.disabled = true;

            // Deshabilitar el botón "Accept"
            acceptButton.disabled = true;

            // Deshabilitar el botón "Cancel"
            cancelButton.disabled = true;
        } else {
            // Mostrar un mensaje de falta de dinero
            selectedProduct.textContent = "";
            coinsInserted.textContent = "";
            purchaseMessage.textContent = "Insufficient funds. Please insert more coins.";
        }

        // Mostrar el resumen de la compra
        purchaseSummary.style.display = 'block';


    });

    // Evento click en el botón "Cancel"
    document.getElementById('cancel-button').addEventListener('click', function() {
        // Implementa la lógica para cancelar la selección.
        console.log('Selección cancelada');
        // Por ejemplo, puedes reiniciar el valor total y deseleccionar cualquier producto seleccionado.
        // totalAmount = 0;
        selectedProduct.textContent = "-";
        coinsInserted.textContent = "-";
        changeAmount.textContent = "$"+totalAmount.toFixed(2);
        purchaseMessage.textContent = "Operation Cancelled!";
        newOperationButton.style.display = 'block';




        // Desmarca cualquier producto seleccionado.
            // Deshabilitar el botón "Insert"
        insertButton.disabled = true;

        // Deshabilitar el botón "Accept"
        acceptButton.disabled = true;

        // Deshabilitar el botón "Cancel"
        cancelButton.disabled = true;




        purchaseSummary.style.display = 'block';
    });
});
const newOperationButton = document.getElementById('new-operation-button');
newOperationButton.addEventListener('click', function() {
    location.reload();
});


// Evento click en el botón "Go Back"
const goBackButton = document.getElementById('go-back-button');
goBackButton.addEventListener('click', function() {
    window.location.href = "vm.html";
});