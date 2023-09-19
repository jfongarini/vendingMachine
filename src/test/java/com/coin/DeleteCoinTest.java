package com.coin;

import com.dao.CoinDao;
import com.domain.coin.response.CoinDeleteResponse;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class DeleteCoinTest {

    @SpyBean
    private CoinService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/coins/{id}";

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
    public void coinDeleteOk(){
        Coin coin = new Coin();
        coin.setCoinId(10);
        coin.setName("ten");
        coin.setValue(10.0);
        Mockito.when(coinDao.findById(Mockito.any())).thenReturn(Optional.of(coin));

        ResponseEntity<CoinDeleteResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),CoinDeleteResponse.class,10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.COIN_DELETE_OK.getText(), response.getBody().getMessage());
    }

    @Test
    public void coinDeleteCoinNull(){

        Mockito.when(coinDao.findById(Mockito.any())).thenReturn(null);
        ResponseEntity<CoinDeleteResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),CoinDeleteResponse.class,10);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.COIN_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void coinDeleteException(){
        Coin coin = new Coin();
        coin.setCoinId(11);
        coin.setName("eleven");
        coin.setValue(11.0);
        Mockito.when(coinDao.findById(Mockito.any())).thenReturn(Optional.of(coin));

        Mockito.doThrow(new RuntimeException()).when(coinDao).delete(Mockito.any());
        ResponseEntity<CoinDeleteResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),CoinDeleteResponse.class,11);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.COIN_DELETE_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void coinDeleteControllerException(){

        Mockito.doThrow(new RuntimeException()).when(service).deleteCoin(Mockito.anyInt());

        ResponseEntity<CoinDeleteResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),CoinDeleteResponse.class,10);
        Mockito.doCallRealMethod().when(service).deleteCoin(Mockito.anyInt());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
