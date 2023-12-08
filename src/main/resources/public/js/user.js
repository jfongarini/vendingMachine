const token = 'Bearer '+localStorage.getItem('token');
const operation = localStorage.getItem('operation');
const status = localStorage.getItem('status');
const idVendingMachine = localStorage.getItem('idVendingMachine');

const urlBase = 'http://localhost:8080/api/';

let totalAmount = 0;
var selectedCoin = [];
var USDollar;
var products;
const insertButton = document.getElementById('insert-button');
const acceptButton = document.getElementById('accept-button');
const cancelButton = document.getElementById('cancel-button');
const selectedProduct = document.getElementById('selected-product-name');
const coinsInserted = document.getElementById('coins-inserted-amount');
const changeAmount = document.getElementById('change-amount');
const purchaseMessage = document.getElementById('purchase-message');
const purchaseSummary = document.getElementById('purchase-summary');
const newOperationButton = document.getElementById('new-operation-button');

function init(){
    insertButton.addEventListener('click', insertCoin);
    acceptButton.addEventListener('click', acceptOperation);
    cancelButton.addEventListener('click', cancelOperation);


    const vmName = localStorage.getItem('name');
    document.getElementById('vm-name').innerText = "Vending Machine: "+vmName;

    newOperationButton.addEventListener('click', function() {
        newOperation();
    });

    const goBackButton = document.getElementById('go-back-button');
    goBackButton.addEventListener('click', function() {
        window.location.href = "login.html";
    });

    USDollar = new Intl.NumberFormat('en-US', {
        style: 'currency',
        currency: 'USD',
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
            option.text = item.name + " - " +USDollar.format(item.value);
            selectedCoin[index] = item;
            selectElement.appendChild(option);
        });
    });
}
loadCoinsList();

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
async function loadVmProductsList(){
    const table = document.getElementById("data-table-prod").getElementsByTagName('tbody')[0];
    clearVmProductList();
    products = await getVmProducts();
    products.data.products.forEach((item, index) => {
        const row = table.insertRow(-1);
        row.innerHTML = `<td>${item.code}</td><td>${item.name}</td><td>${USDollar.format(item.price)}</td><td>${item.quantity}</td>`;
        row.addEventListener("click", function () {
            document.getElementById('code-input').value = item.code;
        });
    });
}
function clearVmProductList(){
    var table = document.getElementById("data-table-prod").getElementsByTagName('tbody')[0];
    table.innerHTML = '';
}
loadVmProductsList();

// Operations

function postOperationCoin(){
    const newCoinUrl = urlBase + 'operations/coins';
    var selectElement = document.getElementById("coin-select");
    const nameNewCoin = selectedCoin[selectElement.selectedIndex].name;
    const dat = {
      coins:[
          {
              name: nameNewCoin,
              quantity: 1
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

    fetch(newCoinUrl, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                        console.log('Post Coin Error:', data);
                    } else {
                        console.log('Post Coin:', data);
                    }
                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });
}
async function postOperationProduct(){

    const newCoinUrl = urlBase + 'operations/product';

    const codeProd = document.getElementById('code-input').value;
    const dat = {
        code: codeProd
    };

    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      },
      body: JSON.stringify(dat)
    };

    await fetch(newCoinUrl, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                        console.log('Post Product Error:', data);
                    } else {
                        console.log('Post Product:', data);
                    }
                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });
}
async function postOperationAccept(){
    const newCoinUrl = urlBase + 'operations/accept';

    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      }
    };

    return fetch(newCoinUrl, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                        console.log('Post Accept Operation Error:', data);
                    } else {
                        console.log('Post Accept Operation:', data);
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
function postOperationCancel(){
    const newCoinUrl = urlBase + 'operations/cancel';

    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      }
    };

    fetch(newCoinUrl, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                        console.log('Post Cancel Operation Error:', data);
                    } else {
                        console.log('Post Cancel Operation:', data);
                    }
                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });
}

//

function insertCoin(){
    postOperationCoin();
    var selectElement = document.getElementById("coin-select");
    totalAmount += selectedCoin[selectElement.selectedIndex].value;
    const amount = document.getElementById('total-amount');
    amount.textContent = USDollar.format(totalAmount);
}
async function acceptOperation(){
    let selectedProductData = null;
    const selectedProductCode = document.getElementById('code-input').value;
    products.data.products.forEach((item, index) => {
        if (item.code == selectedProductCode) {
            selectedProductData = item;
        }
    });
    if (selectedProductData == null){
        return;
    }

    const totalInserted = totalAmount;

    await postOperationProduct();
    let acceptStatus = await postOperationAccept()

    if (acceptStatus.error == null) {
        selectedProduct.textContent = selectedProductData.name;
        coinsInserted.textContent = USDollar.format(totalInserted);
        changeAmount.textContent = USDollar.format((totalInserted - selectedProductData.price));
        purchaseMessage.textContent = "Purchase successful!";
        newOperationButton.style.display = 'block';

        insertButton.disabled = true;
        acceptButton.disabled = true;
        cancelButton.disabled = true;
    } else {
        selectedProduct.textContent = "";
        coinsInserted.textContent = "";
        purchaseMessage.textContent = "Insufficient funds. Please insert more coins.";
    }

    purchaseSummary.style.display = 'block';
    await loadVmProductsList();
}
function cancelOperation(){
    postOperationCancel();
    selectedProduct.textContent = "-";
    coinsInserted.textContent = "-";
    changeAmount.textContent = USDollar.format(totalAmount);
    purchaseMessage.textContent = "Operation Cancelled!";
    newOperationButton.style.display = 'block';

    insertButton.disabled = true;
    acceptButton.disabled = true;
    cancelButton.disabled = true;
    purchaseSummary.style.display = 'block';
}
function newOperation(){
    const newOpUrl = 'http://localhost:8080/api/operations?vendingMachine='+idVendingMachine;
    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'accept': '*/*'
      }
    };

    fetch(newOpUrl, options)
    .then(response => {
     if (!response.ok) {
       throw new Error('Exception error: ' + response.statusText);
     }
     return response.json();
    })
    .then(data => {
        if (data) {
            console.log('Post New Operation:', data);
            localStorage.setItem('token', data.data.token);
            localStorage.setItem('operation', data.data.operation);
            localStorage.setItem('status', data.data.status);
            const newURL = `user.html`;
            window.location.href = newURL;
        } else {
            console.warn('Empty Response');
        }
    })
    .catch(error => {
    console.error('Request Error:', error);
    });
}