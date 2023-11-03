package com.coin;

import com.CommonTest;
import com.dao.CoinDao;
import com.domain.coin.request.CoinUpdateRequest;
import com.domain.coin.response.CoinUpdateResponse;
import com.security.JwtService;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class UpdateCoinTest extends CommonTest {

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
    public void coinUpdateOk(){
        Coin coin = new Coin();
        coin.setCoinId(10);
        coin.setName("ten");
        coin.setValue(10.0);
        Mockito.when(coinDao.findById(Mockito.anyInt())).thenReturn(Optional.of(coin));

        CoinUpdateRequest data = new CoinUpdateRequest();
        data.setName("ten10");

        coin.setName(data.getName());
        Mockito.when(coinDao.save(Mockito.any())).thenReturn(coin);

        ResponseEntity<CoinUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, getAdmin(data),CoinUpdateResponse.class,10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.COIN_UPDATE_OK.getText(), response.getBody().getMessage());
    }

    @Test
    public void coinUpdateRequestFail(){
        CoinUpdateRequest data = new CoinUpdateRequest();

        ResponseEntity<CoinUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, getAdmin(data),CoinUpdateResponse.class,10);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.COIN_PARAMETERS_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void coinUpdateCoinNull(){
        CoinUpdateRequest data = new CoinUpdateRequest();
        data.setName("ten10");

        Mockito.when(coinDao.findById(Mockito.anyInt())).thenReturn(null);

        ResponseEntity<CoinUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, getAdmin(data),CoinUpdateResponse.class,10);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.COIN_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void coinUpdateException(){
        CoinUpdateRequest data = new CoinUpdateRequest();
        data.setName("ten10");

        Mockito.doThrow(new RuntimeException()).when(coinDao).findById(Mockito.anyInt());
        ResponseEntity<CoinUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, getAdmin(data),CoinUpdateResponse.class,10);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.COIN_UPDATE_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void coinUpdateControllerException(){
        CoinUpdateRequest data = new CoinUpdateRequest();
        data.setName("ten10");

        Mockito.doThrow(new RuntimeException()).when(service).updateCoin(Mockito.any(), Mockito.anyInt());

        ResponseEntity<CoinUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, getAdmin(data),CoinUpdateResponse.class,10);
        Mockito.doCallRealMethod().when(service).updateCoin(Mockito.any(), Mockito.anyInt());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
