package com.coin;

import com.dao.CoinDao;
import com.domain.coin.response.CoinGetAllResponse;
import com.util.enums.MessagesEnum;
import com.model.Coin;
import com.service.CoinService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class GetAllCoinTest {

    @SpyBean
    private CoinService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/coin/all";

    @Autowired
    private TestRestTemplate restTemplate;

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
    public void coinGetAllOk(){
        Coin coin = new Coin();
        coin.setCoinId(10);
        coin.setName("ten");
        coin.setValue(10.0);
        List<Coin> coins = new ArrayList<>();
        coins.add(coin);
        Mockito.when(coinDao.save(Mockito.any())).thenReturn(coin);
        Mockito.when(coinDao.findAll()).thenReturn(coins);

        ResponseEntity<CoinGetAllResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()), CoinGetAllResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.COIN_GET_ALL_OK.getText(), response.getBody().getMessage());
    }

    @Test
    public void coinGetAllCoinListNull(){
        Mockito.when(coinDao.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<CoinGetAllResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()), CoinGetAllResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.COIN_LIST_EMPTY.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void coinGetAllException(){
        Mockito.doThrow(new RuntimeException()).when(coinDao).findAll();

        ResponseEntity<CoinGetAllResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()), CoinGetAllResponse.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.COIN_GET_ALL_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void coinGetAllControllerException(){
        Mockito.doThrow(new RuntimeException()).when(service).getAllCoin();

        ResponseEntity<CoinGetAllResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()), CoinGetAllResponse.class);
        Mockito.doCallRealMethod().when(service).getAllCoin();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
