const token = 'Bearer '+localStorage.getItem('token');
const urlBase = 'http://localhost:8080/api/';
const insertButtonCoin = document.getElementById('insert-button-coin');
const insertButtonProd = document.getElementById('insert-button-prod');
const insertButtonAdmin = document.getElementById('insert-button-admin');
const message = document.getElementById('message-h3');
var idVendingMachine;
var globalItem;
var selected;
var USDollar;

function init(){
    insertButtonCoin.addEventListener('click', function() {changeButtonCoin(globalItem);});
    insertButtonProd.addEventListener('click', function() {changeButtonProd(globalItem);});
    insertButtonAdmin.addEventListener('click', function() {changeButtonVendingMachine(globalItem);});

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

// VendingMachines

function getVendingMachines() {
    const url = urlBase + 'vending-machines';
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
                console.log('Get Vending Machines Error:', data);
                message.innerText = data.error.message;
            } else {
                console.log('Get Vending Machines:', data);
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
function clearVendingMachineList(){
    var table = document.getElementById("data-table-admin").getElementsByTagName('tbody')[0];
    table.innerHTML = '';
}
function loadVendingMachineList(){
    const table = document.getElementById("data-table-admin").getElementsByTagName('tbody')[0];
    clearVendingMachineList();
    getVendingMachines()
    .then(list => {
        list.data.vendingMachineList.forEach((item, index) => {
            const row = table.insertRow(-1);
            row.innerHTML = `<td>${item.name}</td>`;
            const actionsCell = row.insertCell(1);
            const editButton = document.createElement("i");
            editButton.classList.add("actions-left");
            editButton.classList.add("fa");
            editButton.classList.add("fa-pencil");
            editButton.addEventListener("click", function() {editButtonVendingMachine(item);});
            const deleteButton = document.createElement("i");
            deleteButton.classList.add("actions-right");
            deleteButton.classList.add("fa");
            deleteButton.classList.add("fa-trash");
            deleteButton.addEventListener("click", function() {deleteVendingMachine(item.id);});
            actionsCell.appendChild(editButton);
            actionsCell.appendChild(deleteButton);

            row.addEventListener("mouseover", function () {
                const actions = this.getElementsByClassName("actions-right")[0];
                actions.style.display = "block";
                const actions2 = this.getElementsByClassName("actions-left")[0];
                actions2.style.display = "block";
            });
            row.addEventListener("mouseout", function () {
                const actions = this.getElementsByClassName("actions-right")[0];
                actions.style.display = "none";
                const actions2 = this.getElementsByClassName("actions-left")[0];
                actions2.style.display = "none";
            });
        });
    });
}
function editButtonVendingMachine(item){
    document.getElementById('name-input-admin').value = item.name;
    insertButtonAdmin.innerText = 'Update';
    globalItem = item;
}
function changeButtonVendingMachine(item){
    if (item == null){
        postVendingMachine()
    } else {
        putVendingMachine(item.id);
    }
    globalItem = null;
}

loadVendingMachineList();

function postVendingMachine(){
    const newVendingMachineUrl = urlBase + 'vending-machines';

    const nameNewVendingMachine = document.getElementById('name-input-admin').value;
    document.getElementById('name-input-admin').value = '';
    const dat = {
      name: nameNewVendingMachine
    };

    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      },
      body: JSON.stringify(dat)
    };

    fetch(newVendingMachineUrl, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                        console.log('Post VendingMachine Error:', data);
                        message.innerText = data.error.message;
                    } else {
                        console.log('Post VendingMachine:', data);
                        loadVendingMachineList();
                        message.innerText = data.message;
                    }

                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });

}
function putVendingMachine(id){
    insertButtonAdmin.innerText = 'Insert';
    const putVendingMachineUrl = urlBase + 'vending-machines/' +id;

    const nameNewVendingMachine = document.getElementById('name-input-admin').value;
    document.getElementById('name-input-admin').value = '';
    const dat = {
      name: nameNewVendingMachine
    };

    const options = {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      },
      body: JSON.stringify(dat)
    };

    fetch(putVendingMachineUrl, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                        console.log('Put VendingMachine Error:', data);
                        message.innerText = data.error.message;
                    } else {
                        console.log('Put VendingMachine:', data);
                        loadVendingMachineList();
                        message.innerText = data.message;
                    }
                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });

}
function deleteVendingMachine(id){
    const deleteVendingMachineUrl = urlBase + 'vending-machines/' +id;

    const options = {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      }
    };

    fetch(deleteVendingMachineUrl, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                        console.log('Delete VendingMachine Error:', data);
                        message.innerText = data.error.message;
                    } else {
                        console.log('Delete VendingMachine:', data);
                        loadVendingMachineList();
                        message.innerText = data.message;
                    }
                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });

}

//Coins

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
function clearCoinList(){
    var table = document.getElementById("data-table-coin").getElementsByTagName('tbody')[0];
    table.innerHTML = '';
}
function editButtonCoin(item){
    document.getElementById('name-input-coin').value = item.name;
    document.getElementById('value-input-coin').value = item.value;
    insertButtonCoin.innerText = 'Update';
    globalItem = item;
}
function changeButtonCoin(item){
    if (item == null){
        postCoin()
    } else {
        putCoin(item.id);
    }
    globalItem = null;
}
function loadCoinsList(){
    const table = document.getElementById("data-table-coin").getElementsByTagName('tbody')[0];
    clearCoinList();
    getCoins()
    .then(list => {
        list.data.coinList.forEach((item, index) => {
            const row = table.insertRow(-1);
            row.innerHTML = `<td>${item.name}</td><td>${USDollar.format(item.value)}</td>`;
            const actionsCell = row.insertCell(2);
            const editButton = document.createElement("i");
            editButton.classList.add("actions-left");
            editButton.classList.add("fa");
            editButton.classList.add("fa-pencil");
            editButton.addEventListener("click", function() {editButtonCoin(item);});
            const deleteButton = document.createElement("i");
            deleteButton.classList.add("actions-right");
            deleteButton.classList.add("fa");
            deleteButton.classList.add("fa-trash");
            deleteButton.addEventListener("click", function() {deleteCoin(item.id);});
            actionsCell.appendChild(editButton);
            actionsCell.appendChild(deleteButton);

            row.addEventListener("mouseover", function () {
                const actions = this.getElementsByClassName("actions-right")[0];
                actions.style.display = "block";
                const actions2 = this.getElementsByClassName("actions-left")[0];
                actions2.style.display = "block";
            });
            row.addEventListener("mouseout", function () {
                const actions = this.getElementsByClassName("actions-right")[0];
                actions.style.display = "none";
                const actions2 = this.getElementsByClassName("actions-left")[0];
                actions2.style.display = "none";
            });
        });
    });
}

loadCoinsList();

function postCoin(){
    const newCoinUrl = urlBase + 'coins';

    const nameNewCoin = document.getElementById('name-input-coin').value;
    const valueNewCoin = document.getElementById('value-input-coin').value;
    const dat = {
      name: nameNewCoin,
      value: valueNewCoin
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
                        message.innerText = data.error.message;
                    } else {
                        console.log('Post Coin:', data);
                        loadCoinsList();
                        message.innerText = data.message;
                    }
                    document.getElementById('name-input-coin').value = '';
                    document.getElementById('value-input-coin').value = '';
                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });

}
function putCoin(id){
    insertButtonCoin.innerText = 'Insert';
    const putCoinUrl = urlBase + 'coins/' +id;

    const nameNewCoin = document.getElementById('name-input-coin').value;
    const valueNewCoin = document.getElementById('value-input-coin').value;
    document.getElementById('name-input-coin').value = '';
    document.getElementById('value-input-coin').value = '';
    const dat = {
      name: nameNewCoin,
      value: valueNewCoin
    };

    const options = {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      },
      body: JSON.stringify(dat)
    };

    fetch(putCoinUrl, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                        console.log('Put Coin Error:', data);
                        message.innerText = data.error.message;
                    } else {
                        console.log('Put Coin:', data);
                        loadCoinsList();
                        message.innerText = data.message;
                    }
                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });

}
function deleteCoin(id){
    const putCoinUrl = urlBase + 'coins/' +id;

    const options = {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      }
    };

    fetch(putCoinUrl, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                        console.log('Delete Coin Error:', data);
                        message.innerText = data.error.message;
                    } else {
                        console.log('Delete Coin:', data);
                        loadCoinsList();
                        message.innerText = data.message;
                    }
                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });

}

//Products

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
function getAvailableProducts() {
    const url = urlBase + 'products/available';
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
                console.log('Get available Products Error:', data);
                message.innerText = data.error.message;
            } else {
                console.log('Get available Products:', data);
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
    var table = document.getElementById("data-table-prod").getElementsByTagName('tbody')[0];
    table.innerHTML = '';
}
function loadProductsList(){
    const table = document.getElementById("data-table-prod").getElementsByTagName('tbody')[0];
    clearProductsList();
    getProducts()
    .then(list => {
        list.data.productList.forEach((item, index) => {
            const row = table.insertRow(-1);
            row.innerHTML = `<td>${item.code}</td><td>${item.name}</td><td>${USDollar.format(item.price)}</td>`;
            const actionsCell = row.insertCell(3);
            const editButton = document.createElement("i");
            editButton.classList.add("actions-left");
            editButton.classList.add("fa");
            editButton.classList.add("fa-pencil");
            editButton.addEventListener("click", function() {editButtonProd(item);});
            const deleteButton = document.createElement("i");
            deleteButton.classList.add("actions-right");
            deleteButton.classList.add("fa");
            deleteButton.classList.add("fa-trash");
            deleteButton.addEventListener("click", function() {deleteProduct(item.id);});
            actionsCell.appendChild(editButton);
            actionsCell.appendChild(deleteButton);

            row.addEventListener("mouseover", function () {
                const actions = this.getElementsByClassName("actions-right")[0];
                actions.style.display = "block";
                const actions2 = this.getElementsByClassName("actions-left")[0];
                actions2.style.display = "block";
            });
            row.addEventListener("mouseout", function () {
                const actions = this.getElementsByClassName("actions-right")[0];
                actions.style.display = "none";
                const actions2 = this.getElementsByClassName("actions-left")[0];
                actions2.style.display = "none";
            });
        });
    });
    loadAvailableProductsList();
}
function clearAvailableProductsList(){
     var select = document.getElementById("prod-select");
     var length = select.options.length;
     for (i = length-1; i >= 0; i--) {
       select.options[i] = null;
     }
}
function loadAvailableProductsList(){
    const selectElement = document.getElementById("prod-select");
    clearAvailableProductsList();
    getAvailableProducts()
    .then(list => {
        list.data.availableProducts.forEach((item, index) => {
            var option = document.createElement("option");
            option.text = item;
            selectElement.appendChild(option);
            selectElement.addEventListener("change", function() {
              selected = selectElement.value;
            });
        });
        selected = list.data.availableProducts[0];
    });
}

loadProductsList();

function postProduct(){
    const newProductUrl = urlBase + 'products';

    const nameNewProduct = selected;
    const valueNewProduct = document.getElementById('value-input-prod').value;
    const codeNewProduct = document.getElementById('code-input-prod').value;
    document.getElementById('value-input-prod').value = '';
    document.getElementById('code-input-prod').value = '';
    const dat = {
      name: nameNewProduct,
      price: valueNewProduct,
      code: codeNewProduct
    };

    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      },
      body: JSON.stringify(dat)
    };

    fetch(newProductUrl, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                        console.log('Post Product Error:', data);
                        message.innerText = data.error.message;
                    } else {
                        console.log('Post Product:', data);
                        loadProductsList();
                        message.innerText = data.message;
                    }

                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });

}
function putProduct(id){
    insertButtonProd.innerText = 'Insert';
    const putProductUrl = urlBase + 'products/' +id;

    const valueNewProduct = document.getElementById('value-input-prod').value;
    const codeNewProduct = document.getElementById('code-input-prod').value;
    document.getElementById('value-input-prod').value = '';
                        document.getElementById('code-input-prod').value = '';
    const dat = {
      price: valueNewProduct,
      code: codeNewProduct
    };

    const options = {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      },
      body: JSON.stringify(dat)
    };

    fetch(putProductUrl, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                        console.log('Put Product Error:', data);
                        message.innerText = data.error.message;
                    } else {
                        console.log('Put Product:', data);
                        loadProductsList();
                        message.innerText = data.message;
                    }

                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            insertButtonProd.innerText = 'Insert';
            console.error('Request Error:', error);
          });

}
function deleteProduct(id){
    const putProductUrl = urlBase + 'products/' +id;

    const options = {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': token
      }
    };

    fetch(putProductUrl, options)
          .then(response => {
             return response.json();
           })
          .then(data => {
                if (data) {
                    if (data.error){
                        console.log('Delete Product Error:', data);
                        message.innerText = data.error.message;
                    } else {
                        console.log('Delete Product:', data);
                        loadProductsList();
                        message.innerText = data.message;
                    }
                } else {
                    console.warn('Empty Response');
                }
          })
          .catch(error => {
            console.error('Request Error:', error);
          });

}
function editButtonProd(item){
    document.getElementById('value-input-prod').value = item.price;
    document.getElementById('code-input-prod').value = item.code;
    insertButtonProd.innerText = 'Update';
    globalItem = item;
}
function changeButtonProd(item){
    if (item == null){
        postProduct()
    } else {
        putProduct(item.id);
    }
    globalItem = null;
}

