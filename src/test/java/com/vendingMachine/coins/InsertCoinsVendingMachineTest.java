package com.vendingMachine.coins;

import com.dao.CoinDao;
import com.dao.VendingMachineDao;
import com.domain.vendingMachine.request.VmCoinRequest;
import com.domain.vendingMachine.request.VmInsertCoinsRequest;
import com.domain.vendingMachine.response.VmInsertCoinsResponse;
import com.model.Coin;
import com.model.VendingMachine;
import com.service.VendingMachineService;
import com.util.enums.MessagesEnum;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class InsertCoinsVendingMachineTest {

    @SpyBean
    private VendingMachineService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingmachine/{id}/insertCoins";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private VendingMachineDao vendingMachineDao;

    @MockBean
    private CoinDao coinDao;

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
    public void vendingMachineInsertCoinsOk(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        Coin coin = new Coin();
        coin.setCoinId(10);
        coin.setName("ten");
        coin.setValue(10.0);
        Mockito.when(coinDao.findByName(Mockito.any())).thenReturn(Optional.of(coin));

        VmCoinRequest vmCoinRequest = new VmCoinRequest();
        vmCoinRequest.setName("ten");
        vmCoinRequest.setQuantity(1L);

        List<VmCoinRequest> coins = new ArrayList<>();
        coins.add(vmCoinRequest);

        VmInsertCoinsRequest data = new VmInsertCoinsRequest();
        data.setCoins(coins);

        HttpEntity<VmInsertCoinsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmInsertCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VmInsertCoinsResponse.class,5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.VMC_POST_OK.getText(), response.getBody().getMessage());

    }

    @Test
    public void vendingMachineInsertCoinsRequestFail(){

        List<VmCoinRequest> coins = new ArrayList<>();

        VmInsertCoinsRequest data = new VmInsertCoinsRequest();
        data.setCoins(coins);

        HttpEntity<VmInsertCoinsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmInsertCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VmInsertCoinsResponse.class,5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PARAM_VALID_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineInsertCoinsCoinNull(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        VmCoinRequest vmCoinRequest = new VmCoinRequest();
        vmCoinRequest.setName("ten");
        vmCoinRequest.setQuantity(1L);

        List<VmCoinRequest> coins = new ArrayList<>();
        coins.add(vmCoinRequest);

        VmInsertCoinsRequest data = new VmInsertCoinsRequest();
        data.setCoins(coins);

        HttpEntity<VmInsertCoinsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmInsertCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VmInsertCoinsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.COIN_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineInsertCoinsVMNull(){

        VmCoinRequest vmCoinRequest = new VmCoinRequest();
        vmCoinRequest.setName("ten");
        vmCoinRequest.setQuantity(1L);

        List<VmCoinRequest> coins = new ArrayList<>();
        coins.add(vmCoinRequest);

        VmInsertCoinsRequest data = new VmInsertCoinsRequest();
        data.setCoins(coins);

        HttpEntity<VmInsertCoinsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmInsertCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VmInsertCoinsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineInsertCoinsException(){

        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.anyInt());

        VmCoinRequest vmCoinRequest = new VmCoinRequest();
        vmCoinRequest.setName("ten");
        vmCoinRequest.setQuantity(1L);

        List<VmCoinRequest> coins = new ArrayList<>();
        coins.add(vmCoinRequest);

        VmInsertCoinsRequest data = new VmInsertCoinsRequest();
        data.setCoins(coins);

        HttpEntity<VmInsertCoinsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmInsertCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VmInsertCoinsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VMC_POST_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineInsertCoinsControllerException(){

        VmInsertCoinsRequest data = new VmInsertCoinsRequest();

        HttpEntity<VmInsertCoinsRequest> request = new HttpEntity<>(data);

        Mockito.doThrow(new RuntimeException()).when(service).insertCoinsVendingMachine(Mockito.anyInt(),Mockito.any(), Mockito.any());

        ResponseEntity<VmInsertCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VmInsertCoinsResponse.class,5);
        Mockito.doCallRealMethod().when(service).insertCoinsVendingMachine(Mockito.anyInt(),Mockito.any(), Mockito.any());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
