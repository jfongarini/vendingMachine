const token = localStorage.getItem('token');
const urlBase = 'http://localhost:8080/api/';
var idVendingMachine;

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

function loadVendingMachineList(){
    const table = document.getElementById("data-table").getElementsByTagName('tbody')[0];
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
            const deleteButton = document.createElement("i");
            deleteButton.classList.add("actions-right");
            deleteButton.classList.add("fa");
            deleteButton.classList.add("fa-trash");
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

function clearVendingMachineList(){
    var table = document.getElementById("data-table").getElementsByTagName('tbody')[0];
    table.innerHTML = '';
}

loadVendingMachineList();