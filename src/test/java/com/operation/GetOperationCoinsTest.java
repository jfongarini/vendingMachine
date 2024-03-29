package com.operation;

import com.CommonTest;
import com.dao.CoinDao;
import com.dao.OperationDao;
import com.dao.UserDao;
import com.dao.VendingMachineDao;
import com.domain.operation.response.OperationGetCoinsResponse;
import com.model.*;
import com.security.JwtService;
import com.service.OperationService;
import com.util.enums.MessagesEnum;
import com.util.enums.StatusEnum;
import com.util.enums.UserEnum;
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

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class GetOperationCoinsTest {

    @SpyBean
    private OperationService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/operations/coins";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private VendingMachineDao vendingMachineDao;

    @Autowired
    private CoinDao coinDao;

    @MockBean
    private OperationDao operationDao;

    @MockBean
    private UserDao userDao;

    @MockBean
    private JwtService jwtService;

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
    public void getCoinsOperationOk(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Operation operation = setOperation(vendingMachine);
        Set<Operation> operations = new HashSet<>();
        operations.add(operation);
        Mockito.when(vendingMachineDao.findById(Mockito.anyInt())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.findByUser(Mockito.any())).thenReturn(Optional.of(operation));
        ResponseEntity<OperationGetCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.GET, getUser(vendingMachine),OperationGetCoinsResponse.class,5,1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.OPERATION_GET_COINS_OK.getText(), response.getBody().getMessage());
    }

    @Test
    public void getCoinsOperationVmNull(){
        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");
        ResponseEntity<OperationGetCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(headers),OperationGetCoinsResponse.class,5,1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void getCoinsOperationOperationNull(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Operation operation = setOperation(vendingMachine);

        Mockito.when(vendingMachineDao.findById(Mockito.anyInt())).thenReturn(Optional.of(vendingMachine));

        ResponseEntity<OperationGetCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.GET, getUser(vendingMachine),OperationGetCoinsResponse.class,5,1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void getCoinsOperationException(){
        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");
        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.anyInt());

        ResponseEntity<OperationGetCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(headers),OperationGetCoinsResponse.class,5,1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_GET_COINS_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void getCoinsOperationControllerException(){
        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");
        Mockito.doThrow(new RuntimeException()).when(service).getCoinsOperation(Mockito.anyString());

        ResponseEntity<OperationGetCoinsResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(headers),OperationGetCoinsResponse.class,5,1);
        Mockito.doCallRealMethod().when(service).getCoinsOperation(Mockito.anyString());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private Operation setOperation(VendingMachine vendingMachine){

        Product product = new Product();
        product.setProductId(1);
        product.setName("Apple");
        product.setPrice(0.5);
        product.setCode("001");
        List<Product> products = new ArrayList<>();
        products.add(product);

        Coin coin1 = new Coin();
        coin1.setCoinId(1);
        coin1.setName("50 cents");
        coin1.setValue(0.5);
        coinDao.save(coin1);
        Coin coin2 = new Coin();
        coin2.setCoinId(2);
        coin2.setName("5 cents");
        coin2.setValue(0.05);
        coinDao.save(coin2);
        List<Coin> coins = new ArrayList<>();
        coins.add(coin1);
        coins.add(coin2);

        Operation operation = new Operation();
        operation.setOperationId(1);
        operation.setCoins(coins);
        operation.setProducts(products);
        operation.setValue(0.5);
        operation.setStatus(StatusEnum.OPEN.name());
        operation.setDate(new Date());

        return operation;
    }

    public HttpEntity<HttpHeaders> getUser(VendingMachine vendingMachine){
        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);

        User user = new User();
        user.setRole(UserEnum.USER.name());
        user.setVendingMachine(vendingMachine);
        Mockito.when(userDao.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");

        return new HttpEntity(headers);
    }
}
