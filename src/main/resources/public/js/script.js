
var idVendingMachine;
function setElements() {
    const url = 'http://localhost:8080/api/vending-machines';
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

const containerList = document.getElementById('containerList');
chargeVendingMachineList();

function chargeVendingMachineList(){
    setElements()
    .then(list => {
        list.data.vendingMachineList.forEach((element, index) => {
            const li = document.createElement('li');
            li.textContent = element.name;
            li.classList.add('square');

            li.addEventListener('click', () => {
                const squares = document.querySelectorAll('.square');
                squares.forEach(square => square.classList.remove('selected'));
                li.classList.add('selected');
                sendNumberToRectangle(element.id);
            });

            containerList.appendChild(li);
        });
    });
}

function clearVendingMachineList(){
    var list = document.getElementById('containerList');
    list.innerHTML = '';
}

function sendNumberToRectangle(number) {
    console.log('Selected number:', number);
    idVendingMachine = number;
}

const userButton = document.getElementById('userButton');
userButton.addEventListener('click', () => {
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
});

const adminButton = document.getElementById('adminButton');

adminButton.addEventListener('click', () => {
    const url = 'http://localhost:8080/api/vending-machines/login/{id}';
    const endpointUrl = url.replace('{id}', idVendingMachine);

    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'accept': '*/*'
      }
    };

    fetch(endpointUrl, options)
    .then(response => {
        if (!response.ok) {
            throw new Error('Exception error: ' + response.statusText);
        }
        return response.json();
    })
    .then(data => {
        if (data) {
            console.log('Post Login Administrator:', data);
            localStorage.setItem('token', data.data.token);
            const newURL = `vms.html`;
            window.location.href = newURL;
        } else {
            console.warn('Empty Response');
        }
    })
    .catch(error => {
        console.error('Request Error:', error);
    });
});

document.getElementById('squareAdd').addEventListener('click', function() {

    const square2 = document.getElementById('square2Add');
    document.getElementById('name-input').value='';
    square2.style.display = 'inline-block';

});

function enterDetect(event) {
    if (event.key === 'Enter') {
        const newVmUrl = 'http://localhost:8080/api/vending-machines';

        const inputNewVm = document.getElementById('name-input').value;
        const dat = {
          name: inputNewVm
        };

        const options = {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'accept': '*/*'
          },
          body: JSON.stringify(dat)
        };

        fetch(newVmUrl, options)
              .then(response => {
                 if (!response.ok) {
                   throw new Error('Exception error: ' + response.statusText);
                 }
                 return response.json();
               })
              .then(data => {
                    if (data) {
                        console.log('Post Vending Machines:', data);
                        clearVendingMachineList();
                        chargeVendingMachineList();
                        var square2 = document.getElementById('square2Add');
                        square2.style.display = 'none';
                        localStorage.setItem('token', data.token);
                        localStorage.setItem('id', data.id);
                    } else {
                        console.warn('Empty Response');
                    }
              })
              .catch(error => {
                console.error('Request Error:', error);
              });
    }
}
