const token = 'Bearer '+localStorage.getItem('token');
const urlBase = 'http://localhost:8080/api/';

var idVendingMachine;
var userList = [];
var coinsList = [];
var productsList = [];

var USDollar;

function init(){

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

// Operations

function getOperations() {
    const url = urlBase + 'vending-machines/operations';
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
              console.log('Get Operations Error:', data);
            } else {
              console.log('Get Operations:', data);
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
async function loadOperations(){
    await getOperations()
    .then(list => {
        list.data.operations.forEach((item, index) => {
            if (item.status == "INSERT_COINS" || item.status == "EXTRACT_COINS"){
                coinsList.push(item);
            } else if (item.status == "INSERT_PRODUCTS" || item.status == "EXTRACT_PRODUCTS") {
                productsList.push(item);
            } else {
                userList.push(item);
            }
        });
    });
}

function loadCoinOperations(){
    const table = document.getElementById("data-table-coin").getElementsByTagName('tbody')[0];
    let allMoneyCoin = 0;
    let coin;
    let value;
    coinsList.forEach((item, index) => {
        const row = table.insertRow(-1);
        let qty = item.coins.length;
        if (item.status == "EXTRACT_COINS"){
            row.classList.add("coral");
            value = "-";
            coin = "All";
            allMoneyCoin = 0;
        } else {
            row.classList.add("lightgreen");
            value = USDollar.format(item.coins[0].value);
            coin = item.coins[0].name;
            item.coins.forEach((itemC, index) => {
                allMoneyCoin = allMoneyCoin + itemC.value;
            });
        }
        row.innerHTML = `<td>${item.date}</td>
                        <td>${item.status}</td>
                        <td>${coin}</td>
                        <td>${value}</td>
                        <td>${qty}</td>
                        <td>${USDollar.format(item.value)}</td>
                        <td>${USDollar.format(allMoneyCoin)}</td>`;
    });
}
function loadProdOperations(){
    const table = document.getElementById("data-table-prod").getElementsByTagName('tbody')[0];
    let allMoneyProd = 0;
    let product;
    let value;
    let total;
    productsList.forEach((item, index) => {
        const row = table.insertRow(-1);
        let qty = item.products.length;
        if (item.status == "EXTRACT_PRODUCTS"){
            row.classList.add("coral");
            value = "-";
            product = "All";
            allMoneyProd = 0;
            total = item.value;
        } else {
            row.classList.add("lightgreen");
            value = USDollar.format(item.products[0].price);
            product = item.products[0].name;
            item.products.forEach((itemC, index) => {
                allMoneyProd = allMoneyProd + itemC.price;
            });
            total = item.value * qty;
        }
        row.innerHTML = `<td>${item.date}</td>
                        <td>${item.status}</td>
                        <td>${product}</td>
                        <td>${value}</td>
                        <td>${qty}</td>
                        <td>${USDollar.format(total)}</td>
                        <td>${USDollar.format(allMoneyProd)}</td>`;
    });
}
function loadUserOperations(){
    const table = document.getElementById("data-table-user").getElementsByTagName('tbody')[0];
    let totalProfit = 0;
    userList.forEach((item, index) => {
        const row = table.insertRow(-1);
        totalProfit = totalProfit + item.value;
        row.innerHTML = `<td>${item.date}</td>
                        <td>${item.status}</td>
                        <td>${USDollar.format(item.value)}</td>
                        <td>${item.name}</td>`;
    });
    document.getElementById('profit').innerText = USDollar.format(totalProfit);
}
async function executeAsync(){
    await loadOperations();
    loadCoinOperations();
    loadProdOperations();
    loadUserOperations();
}

executeAsync();
















