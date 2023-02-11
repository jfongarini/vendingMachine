package com.operation;

import com.dao.CoinDao;
import com.dao.OperationDao;
import com.dao.VendingMachineDao;
import com.domain.operation.request.OperationAddCoinRequest;
import com.domain.operation.request.OperationAddCoinsRequest;
import com.domain.operation.response.OperationAddCoinsResponse;
import com.model.Coin;
import com.model.Operation;
import com.model.VendingMachine;
import com.service.OperationService;
import com.util.enums.MessagesEnum;
import com.util.enums.StatusEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockserver.integration.ClientAndServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class AddCoinsOperationTest {

    @SpyBean
    private OperationService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingMachine/{id}/operation/coins";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private VendingMachineDao vendingMachineDao;

    @Autowired
    private CoinDao coinDao;

    @MockBean
    private OperationDao operationDao;

    private ClientAndServer mockServer;

    @BeforeEach
    void init(){
        mockServer = startClientAndServer(1080);
        URL = "http://localhost:"+localServerPort+URL;
    }

    @AfterEach
    void stop(){
        mockServer.stop();
    }

    @Test
    public void operationAddCoinsOk(){

        Coin coin1 = new Coin();
        coin1.setCoinId(1);
        coin1.setName("5 cents");
        coin1.setValue(0.05);
        coinDao.save(coin1);

        Coin coin2 = new Coin();
        coin2.setCoinId(10);
        coin2.setName("50 cents");
        coin2.setValue(0.5);
        coinDao.save(coin2);

        List<Coin> coins = new ArrayList<>();
        coins.add(coin1);
        coins.add(coin2);

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");

        Operation operation = new Operation();
        operation.setOperationId(1);
        operation.setVendingMachine(vendingMachine);
        operation.setCoins(coins);
        operation.setValue(0.5);
        operation.setStatus(StatusEnum.OPEN.name());
        operation.setDate(new Date());

        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.findById(Mockito.any())).thenReturn(Optional.of(operation));

        OperationAddCoinsRequest data = new OperationAddCoinsRequest();
        data.setOperation(1);
        OperationAddCoinRequest coinData1 = new OperationAddCoinRequest();
        coinData1.setName(coin1.getName());
        coinData1.setQuantity(2L);
        OperationAddCoinRequest coinData2 = new OperationAddCoinRequest();
        coinData2.setName(coin2.getName());
        coinData2.setQuantity(2L);
        List<OperationAddCoinRequest> operationAddCoinRequestList = new ArrayList<>();
        operationAddCoinRequestList.add(coinData1);
        operationAddCoinRequestList.add(coinData2);
        data.setCoins(operationAddCoinRequestList);
        HttpEntity<OperationAddCoinsRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationAddCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationAddCoinsResponse.class,5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.OPERATION_INS_COIN_OK.getText(), response.getBody().getMessage());

    }

    @Test
    public void operationAddCoinsRequestFail(){
        OperationAddCoinsRequest data = new OperationAddCoinsRequest();
        data.setOperation(1);
        List<OperationAddCoinRequest> operationAddCoinRequestList = new ArrayList<>();
        data.setCoins(operationAddCoinRequestList);
        HttpEntity<OperationAddCoinsRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationAddCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationAddCoinsResponse.class,5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PARAM_VALID_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddCoinsVmNull(){
        OperationAddCoinsRequest data = new OperationAddCoinsRequest();
        data.setOperation(1);
        OperationAddCoinRequest coinData1 = new OperationAddCoinRequest();
        coinData1.setName("test");
        coinData1.setQuantity(2L);
        List<OperationAddCoinRequest> operationAddCoinRequestList = new ArrayList<>();
        operationAddCoinRequestList.add(coinData1);
        data.setCoins(operationAddCoinRequestList);
        HttpEntity<OperationAddCoinsRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationAddCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationAddCoinsResponse.class,5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddCoinsOperationNull(){

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");

        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        OperationAddCoinsRequest data = new OperationAddCoinsRequest();
        data.setOperation(22);
        OperationAddCoinRequest coinData1 = new OperationAddCoinRequest();
        coinData1.setName("test");
        coinData1.setQuantity(2L);
        List<OperationAddCoinRequest> operationAddCoinRequestList = new ArrayList<>();
        operationAddCoinRequestList.add(coinData1);
        data.setCoins(operationAddCoinRequestList);
        HttpEntity<OperationAddCoinsRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationAddCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationAddCoinsResponse.class,5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddCoinsOperationClosed(){
        Coin coin1 = new Coin();
        coin1.setCoinId(1);
        coin1.setName("5 cents");
        coin1.setValue(0.05);
        coinDao.save(coin1);

        List<Coin> coins = new ArrayList<>();
        coins.add(coin1);

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");

        Operation operation = new Operation();
        operation.setOperationId(1);
        operation.setVendingMachine(vendingMachine);
        operation.setCoins(coins);
        operation.setValue(0.5);
        operation.setStatus(StatusEnum.CLOSE_OK.name());
        operation.setDate(new Date());

        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.findById(Mockito.any())).thenReturn(Optional.of(operation));

        OperationAddCoinsRequest data = new OperationAddCoinsRequest();
        data.setOperation(1);
        OperationAddCoinRequest coinData1 = new OperationAddCoinRequest();
        coinData1.setName(coin1.getName());
        coinData1.setQuantity(2L);
        List<OperationAddCoinRequest> operationAddCoinRequestList = new ArrayList<>();
        operationAddCoinRequestList.add(coinData1);
        data.setCoins(operationAddCoinRequestList);
        HttpEntity<OperationAddCoinsRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationAddCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationAddCoinsResponse.class,5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_CLOSED.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddCoinsCoinNull(){
        Coin coin1 = new Coin();
        coin1.setCoinId(1);
        coin1.setName("5 cents");
        coin1.setValue(0.05);

        List<Coin> coins = new ArrayList<>();
        coins.add(coin1);

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");

        Operation operation = new Operation();
        operation.setOperationId(1);
        operation.setVendingMachine(vendingMachine);
        operation.setCoins(coins);
        operation.setValue(0.5);
        operation.setStatus(StatusEnum.OPEN.name());
        operation.setDate(new Date());

        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.findById(Mockito.any())).thenReturn(Optional.of(operation));

        OperationAddCoinsRequest data = new OperationAddCoinsRequest();
        data.setOperation(1);
        OperationAddCoinRequest coinData1 = new OperationAddCoinRequest();
        coinData1.setName(coin1.getName());
        coinData1.setQuantity(2L);
        List<OperationAddCoinRequest> operationAddCoinRequestList = new ArrayList<>();
        operationAddCoinRequestList.add(coinData1);
        data.setCoins(operationAddCoinRequestList);
        HttpEntity<OperationAddCoinsRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationAddCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationAddCoinsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.COIN_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddCoinsException(){

        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.anyInt());

        OperationAddCoinsRequest data = new OperationAddCoinsRequest();
        data.setOperation(1);
        OperationAddCoinRequest coinData1 = new OperationAddCoinRequest();
        coinData1.setName("test");
        coinData1.setQuantity(2L);
        List<OperationAddCoinRequest> operationAddCoinRequestList = new ArrayList<>();
        operationAddCoinRequestList.add(coinData1);
        data.setCoins(operationAddCoinRequestList);
        HttpEntity<OperationAddCoinsRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationAddCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationAddCoinsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_INS_COIN_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddCoinsControllerException(){

        Mockito.doThrow(new RuntimeException()).when(service).addCoinsOperation(Mockito.anyInt(),Mockito.any(), Mockito.any());
        OperationAddCoinsRequest data = new OperationAddCoinsRequest();
        HttpEntity<OperationAddCoinsRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationAddCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationAddCoinsResponse.class,5);
        Mockito.doCallRealMethod().when(service).addCoinsOperation(Mockito.anyInt(),Mockito.any(), Mockito.any());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
