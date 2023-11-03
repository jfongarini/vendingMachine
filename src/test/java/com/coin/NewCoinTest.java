package com.coin;

import com.CommonTest;
import com.dao.CoinDao;
import com.domain.coin.request.CoinNewRequest;
import com.domain.coin.response.CoinNewResponse;
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
import org.springframework.http.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class NewCoinTest extends CommonTest {

    @SpyBean
    private CoinService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/coins";

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
    public void coinNewOk(){
        CoinNewRequest data = new CoinNewRequest();
        data.setName("1 cent");
        data.setValue(0.01);

        Coin coin = new Coin();
        coin.setName(data.getName());
        coin.setValue(data.getValue());
        coin.setExist(true);
        Mockito.when(coinDao.save(Mockito.any())).thenReturn(coin);

        ResponseEntity<CoinNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, getAdmin(data),CoinNewResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.COIN_NEW_OK.getText(), response.getBody().getMessage());
        assertEquals("1 cent", response.getBody().getData().getName());
    }

    @Test
    public void coinNewRequestFail(){
        CoinNewRequest data = new CoinNewRequest();
        data.setName("1 cent");

        ResponseEntity<CoinNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, getAdmin(data),CoinNewResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PARAM_VALID_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void coinNewException(){
        CoinNewRequest data = new CoinNewRequest();
        data.setName("1 cent");
        data.setValue(0.01);

        Mockito.doThrow(new RuntimeException()).when(coinDao).save(Mockito.any());
        ResponseEntity<CoinNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, getAdmin(data),CoinNewResponse.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.COIN_NEW_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void coinNewControllerException(){
        CoinNewRequest data = new CoinNewRequest();
        data.setName("1 cent");

        Mockito.doThrow(new RuntimeException()).when(service).newCoin(Mockito.any(), Mockito.any());

        ResponseEntity<CoinNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, getAdmin(data),CoinNewResponse.class);
        Mockito.doCallRealMethod().when(service).newCoin(Mockito.any(), Mockito.any());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
