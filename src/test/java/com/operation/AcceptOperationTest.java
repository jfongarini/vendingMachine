package com.operation;

import com.CommonTest;
import com.dao.*;
import com.domain.operation.response.OperationAcceptResponse;
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
public class AcceptOperationTest {

    @SpyBean
    private OperationService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/operations/accept";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private VendingMachineDao vendingMachineDao;

    @Autowired
    private CoinDao coinDao;

    @Autowired
    private ProductDao productDao;

    @MockBean
    private UserDao userDao;

    @MockBean
    private JwtService jwtService;

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
    public void acceptOperationOk(){

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setName("first");
        Product product = new Product();
        product.setProductId(1);
        product.setName("Apple");
        product.setPrice(0.5);
        product.setCode("001");
        productDao.save(product);

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

        User user = new User();
        user.setRole(UserEnum.USER.name());
        user.setVendingMachine(vendingMachine);
        Mockito.when(userDao.findById(Mockito.anyInt())).thenReturn(Optional.of(user));

        Operation operation = setOperation(user,vendingMachine,product);
        Set<Operation> operations = new HashSet<>();
        operations.add(operation);
        List<Product> products = new ArrayList<>();
        products.add(product);
        vendingMachine.setProducts(products);
        vendingMachine.setCoins(coins);

        Mockito.when(vendingMachineDao.findById(Mockito.anyInt())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.findByUser(Mockito.any())).thenReturn(Optional.of(operation));
        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");

        ResponseEntity<OperationAcceptResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<Void>(headers),OperationAcceptResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.OPERATION_ACCEPT_OK.getText(), response.getBody().getMessage());
    }

    @Test
    public void acceptOperationVmNull(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setName("first");
        Product product = new Product();
        product.setProductId(1);
        product.setName("Apple");
        product.setPrice(0.5);
        product.setCode("001");
        productDao.save(product);

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

        User user = new User();
        user.setRole(UserEnum.USER.name());
        user.setVendingMachine(vendingMachine);
        Mockito.when(userDao.findById(Mockito.anyInt())).thenReturn(Optional.of(user));

        Operation operation = setOperation(user,vendingMachine,product);
        Set<Operation> operations = new HashSet<>();
        operations.add(operation);
        List<Product> products = new ArrayList<>();
        products.add(product);
        vendingMachine.setProducts(products);
        vendingMachine.setCoins(coins);

        Mockito.when(operationDao.findByUser(Mockito.any())).thenReturn(Optional.of(operation));
        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");

        ResponseEntity<OperationAcceptResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<Void>(headers),OperationAcceptResponse.class);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void acceptOperationOperationNull(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");

        Mockito.when(vendingMachineDao.findById(Mockito.anyInt())).thenReturn(Optional.of(vendingMachine));

        ResponseEntity<OperationAcceptResponse> response = restTemplate.exchange(URL, HttpMethod.POST,getUser(vendingMachine),OperationAcceptResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void acceptOperationOperationClosed(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Product product = new Product();
        product.setProductId(1);
        product.setName("Apple");
        product.setPrice(0.5);
        product.setCode("001");
        productDao.save(product);

        User user = new User();
        user.setRole(UserEnum.USER.name());
        user.setVendingMachine(vendingMachine);
        Mockito.when(userDao.findById(Mockito.anyInt())).thenReturn(Optional.of(user));

        Operation operation = setOperation(user,vendingMachine,product);
        Set<Operation> operations = new HashSet<>();
        operations.add(operation);
        operation.setStatus(StatusEnum.CLOSE_OK.name());
        List<Product> products = new ArrayList<>();
        products.add(product);
        vendingMachine.setProducts(products);
        Mockito.when(operationDao.findByUser(Mockito.any())).thenReturn(Optional.of(operation));
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");

        ResponseEntity<OperationAcceptResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<Void>(headers),OperationAcceptResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_CLOSED.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void acceptOperationIncompleteCoin(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Product product = new Product();
        product.setProductId(1);
        product.setName("Apple");
        product.setPrice(0.5);
        product.setCode("001");
        productDao.save(product);

        User user = new User();
        user.setRole(UserEnum.USER.name());
        user.setVendingMachine(vendingMachine);
        Mockito.when(userDao.findById(Mockito.anyInt())).thenReturn(Optional.of(user));

        Operation operation = setOperation(user,vendingMachine,product);
        Set<Operation> operations = new HashSet<>();
        operations.add(operation);
        operation.setCoins(null);
        List<Product> products = new ArrayList<>();
        products.add(product);
        vendingMachine.setProducts(products);
        Mockito.when(operationDao.findByUser(Mockito.any())).thenReturn(Optional.of(operation));
        Mockito.when(vendingMachineDao.findById(Mockito.anyInt())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");

        ResponseEntity<OperationAcceptResponse> response = restTemplate.exchange(URL, HttpMethod.POST,new HttpEntity<Void>(headers),OperationAcceptResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_INCOMPLETE_COIN.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void acceptOperationIncompleteProduct(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Product product = new Product();
        product.setProductId(1);
        product.setName("Apple");
        product.setPrice(0.5);
        product.setCode("001");
        productDao.save(product);

        User user = new User();
        user.setRole(UserEnum.USER.name());
        user.setVendingMachine(vendingMachine);
        Mockito.when(userDao.findById(Mockito.anyInt())).thenReturn(Optional.of(user));

        Operation operation = setOperation(user,vendingMachine,product);
        Set<Operation> operations = new HashSet<>();
        operations.add(operation);
        operation.setProducts(null);
        List<Product> products = new ArrayList<>();
        products.add(product);
        vendingMachine.setProducts(products);
        Mockito.when(operationDao.findByUser(Mockito.any())).thenReturn(Optional.of(operation));
        Mockito.when(vendingMachineDao.findById(Mockito.anyInt())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");

        ResponseEntity<OperationAcceptResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<Void>(headers),OperationAcceptResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_INCOMPLETE_PRODUCT.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void acceptOperationMoneyNotEnough(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Product product = new Product();
        product.setProductId(1);
        product.setName("Apple");
        product.setPrice(10.5);
        product.setCode("001");
        productDao.save(product);

        User user = new User();
        user.setRole(UserEnum.USER.name());
        user.setVendingMachine(vendingMachine);
        Mockito.when(userDao.findById(Mockito.anyInt())).thenReturn(Optional.of(user));

        Operation operation = setOperation(user, vendingMachine,product);
        Set<Operation> operations = new HashSet<>();
        operations.add(operation);
        List<Product> products = new ArrayList<>();
        products.add(product);
        vendingMachine.setProducts(products);

        Mockito.when(vendingMachineDao.findById(Mockito.anyInt())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.findByUser(Mockito.any())).thenReturn(Optional.of(operation));

        ResponseEntity<OperationAcceptResponse> response = restTemplate.exchange(URL, HttpMethod.POST, getUser(vendingMachine),OperationAcceptResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_ACCEPT_NOT_ENOUGH.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void acceptOperationException(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Mockito.doThrow(new RuntimeException()).when(operationDao).findByUser(Mockito.any());
        ResponseEntity<OperationAcceptResponse> response = restTemplate.exchange(URL, HttpMethod.POST,getUser(vendingMachine),OperationAcceptResponse.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_GET_PRODUCT_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void acceptOperationControllerException(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Mockito.doThrow(new RuntimeException()).when(service).acceptOperation(Mockito.anyString());
        ResponseEntity<OperationAcceptResponse> response = restTemplate.exchange(URL, HttpMethod.POST, getUser(vendingMachine),OperationAcceptResponse.class);
        Mockito.doCallRealMethod().when(service).acceptOperation(Mockito.anyString());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private Operation setOperation(User user,VendingMachine vendingMachine, Product product){

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
        operation.setUser(user);

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
