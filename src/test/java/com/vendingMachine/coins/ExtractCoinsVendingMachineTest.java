package com.vendingMachine.coins;

import com.dao.CoinDao;
import com.dao.VendingMachineDao;
import com.domain.vendingMachine.coin.response.VmExtractCoinsResponse;
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
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ExtractCoinsVendingMachineTest {

    @SpyBean
    private VendingMachineService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingmachine/{id}/extractCoins";

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
    public void vendingMachineExtractCoinsOk(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");


        Coin coin = new Coin();
        coin.setCoinId(10);
        coin.setName("ten");
        coin.setValue(10.0);
        List<Coin> coinList = new ArrayList<>();
        coinList.add(coin);
        coinList.add(coin);
        vendingMachine.setCoins(coinList);
        Mockito.when(coinDao.findAll()).thenReturn(coinList);
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));
        ResponseEntity<VmExtractCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),VmExtractCoinsResponse.class,5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.VMC_DELETE_OK.getText(), response.getBody().getMessage());

    }

    @Test
    public void vendingMachineExtractCoinsVMNull(){

        ResponseEntity<VmExtractCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),VmExtractCoinsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());

    }

    @Test
    public void vendingMachineExtractCoinsException(){

        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.anyInt());

        ResponseEntity<VmExtractCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),VmExtractCoinsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VMC_DELETE_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineExtractCoinsControllerException(){

        Mockito.doThrow(new RuntimeException()).when(service).extractCoinsVendingMachine(Mockito.anyString());

        ResponseEntity<VmExtractCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),VmExtractCoinsResponse.class,5);
        Mockito.doCallRealMethod().when(service).extractCoinsVendingMachine(Mockito.anyString());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
