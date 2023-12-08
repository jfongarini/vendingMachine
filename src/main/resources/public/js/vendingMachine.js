const token = 'Bearer '+localStorage.getItem('token');
const urlBase = 'http://localhost:8080/api/';
const insertButtonCoins = document.getElementById('insert-button-coins');
const extractButtonCoins = document.getElementById('extract-button-coins');
const insertButtonProd = document.getElementById('insert-button-prod');
const extractButtonProd = document.getElementById('extract-button-prod');
const message = document.getElementById('message-h3');
var selectedCoin;
var selectedProd;
var idVendingMachine;
var globalItem;

function init(){

    insertButtonCoins.addEventListener('click', postCoin);
    extractButtonCoins.addEventListener('click', extractAllCoins);

    insertButtonProd.addEventListener('click', postProduct);
    extractButtonProd.addEventListener('click', extractAllProducts);

    const vmName = localStorage.getItem('name');
    document.getElementById('vm-name').innerText = "Vending Machine: "+vmName;

    const goBackButton = document.getElementById('go-back-button');
    goBackButton.addEventListener('click', function() {
        window.location.href = "login.html";
    });
}

init();

// Coins

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
          return response.json();
        })
        .then(data => {
          if (data) {
            if (data.error){
                console.log('Get Coins Error:', data);
                message.innerText = data.error.message;
            } else {
                console.log('Get Coins:', data);
            }
            return data;
          } else {
            console.warn('Empty Response');
          }
        })
      .catch(error => {
        console.error('Request Error:', error);
      });
}
function clearCoinsList(){
     var select = document.getElementById("coin-select");
     var length = select.options.length;
     for (i = length-1; i >= 0; i--) {
       select.options[i] = null;
     }
}
function loadCoinsList(){
    const selectElement = document.getElementById("coin-select");
    clearCoinsList();
    getCoins()
    .then(list => {
        list.data.coinList.forEach((item, index) => {
            var option = document.createElement("option");
            option.text = item.name;
            selectElement.appendChild(option);
            selectElement.addEventListener("change", function() {
              selectedCoin = selectElement.value;
            });
        });
        selectedCoin = list.data.coinList[0].name;
    });
}
loadCoinsList();

// Vending Machine Coins

function getVmCoins() {
    const url = urlBase + 'vending-machines/coins';
    const options = {

      method: 'GET',
      headers: {
        'Content-type': 'application/json',
        'Authorization': token
      }
    };

    return fetch(url, options)
      .then(response => {
          return response.json();
        })
        .then(data => {
          if (data) {
            if (data.error){
              console.log('Get Coins Error:', data);
              message.innerText = data.error.message;
            } else {
              console.log('Get Coins:', data);
            }
            return data;
          } else {
            console.warn('Empty Response');
          }
        })
      .catch(error => {
        console.error('Request Error:', error);
      });
}
function loadVmCoinsList(){
    const table = document.getElementById("data-table-coins").getElementsByTagName('tbody')[0];
    const totalCoins = document.getElementById("total-coins");
    const totalValue = document.getElementById("total-value-coins");
    clearVmCoinList();
    getVmCoins()
    .then(list => {
        list.data.coins.forEach((item, index) => {
            const row = table.insertRow(-1);
            row.innerHTML = `<td>${item.name}</td><td>${item.value}</td><td>${item.quantity}</td>`;
        });
        totalCoins.innerText = list.data.totalCoins;
        totalValue.innerText = "$"+list.data.totalValue;
    });
}
function clearVmCoinList(){
    var table = document.getElementById("data-table-coins").getElementsByTagName('tbody')[0];
    table.innerHTML = '';
}
loadVmCoinsList();
function deleteCoin(){
    const url = urlBase + 'vending-machines/coins';
    const options = {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      }
    };

    return fetch(url, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                      console.log('Delete Coins Error:', data);
                      message.innerText = data.error.message;
                    } else {
                      console.log('Delete Coins:', data);
                      message.innerText = data.message;
                    }
                    return data;
                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });

}
async function extractAllCoins(){
    const extractedCoins = document.getElementById("extracted-coins");
    const extractedValue = document.getElementById("extracted-value-coins");
    var coinCount = 0;
    var valueCount = 0;
    await deleteCoin()
    .then(list => {
        list.data.deletedCoins.forEach((item, index) => {
            coinCount = coinCount + item.quantityToDelete;
            valueCount = valueCount + (item.value * item.quantityToDelete);
        });
        extractedCoins.innerText = coinCount;
        extractedValue.innerText = "$"+valueCount.toFixed(2);
    });
    loadCoinsList();
    loadVmCoinsList();
}
function postCoin(){
    const url = urlBase + 'vending-machines/coins';
    const qtyNewCoin = document.getElementById('qty-input-coins').value;
    document.getElementById('qty-input-coins').value = '';
    const nameNewCoin = selectedCoin;
    const dat = {
        coins:[
        {
            name: nameNewCoin,
            quantity: qtyNewCoin
        }]
    };

    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      },
      body: JSON.stringify(dat)
    };

    fetch(url, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                      console.log('Post Coins Error:', data);
                      message.innerText = data.error.message;
                    } else {
                      console.log('Post Coins:', data);
                    }
                    loadCoinsList();
                    loadVmCoinsList();
                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });

}

// Products

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
          return response.json();
        })
        .then(data => {
          if (data) {
            if (data.error){
                console.log('Get Product Error:', data);
                message.innerText = data.error.message;
            } else {
                console.log('Get Product:', data);
            }
            return data;
          } else {
            console.warn('Empty Response');
          }
        })
      .catch(error => {
        console.error('Request Error:', error);
      });
}
function clearProductsList(){
     var select = document.getElementById("prod-select");
     var length = select.options.length;
     for (i = length-1; i >= 0; i--) {
       select.options[i] = null;
     }
}
function loadProductsList(){
    const selectElement = document.getElementById("prod-select");
    clearProductsList();
    getProducts()
    .then(list => {
        list.data.productList.forEach((item, index) => {
            var option = document.createElement("option");
            option.text = item.name;
            selectElement.appendChild(option);
            selectElement.addEventListener("change", function() {
              selectedProd = selectElement.value;
            });
        });
        selectedProd = list.data.productList[0].name;
    });
}
loadProductsList();

// Vending Machine Products

function getVmProducts() {
    const url = urlBase + 'vending-machines/products';
    const options = {

      method: 'GET',
      headers: {
        'Content-type': 'application/json',
        'Authorization': token
      }
    };

    return fetch(url, options)
      .then(response => {
          return response.json();
        })
        .then(data => {
          if (data) {
            if (data.error){
              console.log('Get Products Error:', data);
              message.innerText = data.error.message;
            } else {
              console.log('Get Products:', data);
            }
            return data;
          } else {
            console.warn('Empty Response');
          }
        })
      .catch(error => {
        console.error('Request Error:', error);
      });
}
function loadVmProductsList(){
    const table = document.getElementById("data-table-prod").getElementsByTagName('tbody')[0];
    const totalCoins = document.getElementById("total-prod");
    const totalValue = document.getElementById("total-value-prod");
    clearVmProductList();
    getVmProducts()
    .then(list => {
        list.data.products.forEach((item, index) => {
            const row = table.insertRow(-1);
            row.innerHTML = `<td>${item.code}</td><td>${item.name}</td><td>${item.price}</td><td>${item.quantity}</td>`;
        });
        totalCoins.innerText = list.data.quantity;
        totalValue.innerText = "$"+list.data.totalValue;
    });
}
function clearVmProductList(){
    var table = document.getElementById("data-table-prod").getElementsByTagName('tbody')[0];
    table.innerHTML = '';
}
loadVmProductsList();
function deleteProduct(){
    const url = urlBase + 'vending-machines/products';
    const options = {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      }
    };

    return fetch(url, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                      console.log('Delete Products Error:', data);
                      message.innerText = data.error.message;
                    } else {
                      console.log('Delete Products:', data);
                      message.innerText = data.message;
                    }
                    return data;
                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });

}
async function extractAllProducts(){
    const extractedCoins = document.getElementById("extracted-prod");
    const extractedValue = document.getElementById("extracted-value-prod");
    var coinCount = 0;
    var valueCount = 0;
    await deleteProduct()
    .then(list => {
        list.data.deletedProducts.forEach((item, index) => {
            coinCount = coinCount + item.quantityToDelete;
            valueCount = valueCount + (item.price * item.quantityToDelete);
        });
        extractedCoins.innerText = coinCount;
        extractedValue.innerText = "$"+valueCount.toFixed(2);
    });
    loadProductsList();
    loadVmProductsList();
}
function postProduct(){
    const url = urlBase + 'vending-machines/products';
    const qtyNewCoin = document.getElementById('qty-input-prod').value;
    document.getElementById('qty-input-prod').value = '';
    const nameNewCoin = selectedProd;
    const dat = {
        products:[
        {
            name: nameNewCoin,
            quantity: qtyNewCoin
        }]
    };

    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      },
      body: JSON.stringify(dat)
    };

    fetch(url, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                      console.log('Post Products Error:', data);
                      message.innerText = data.error.message;
                    } else {
                      console.log('Post Products:', data);
                      message.innerText = data.message;
                    }
                    loadProductsList();
                    loadVmProductsList();
                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });

}