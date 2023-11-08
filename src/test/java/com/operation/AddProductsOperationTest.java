package com.operation;

import com.CommonTest;
import com.dao.OperationDao;
import com.dao.ProductDao;
import com.dao.UserDao;
import com.dao.VendingMachineDao;

import com.domain.operation.request.OperationSelectProductRequest;
import com.domain.operation.response.OperationSelectProductResponse;
import com.model.Operation;
import com.model.Product;
import com.model.User;
import com.model.VendingMachine;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class AddProductsOperationTest{

    @SpyBean
    private OperationService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/operations/product";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private VendingMachineDao vendingMachineDao;

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
    public void operationAddProductsOk(){

        Product product = new Product();
        product.setProductId(1);
        product.setName("Apple");
        product.setPrice(0.5);
        product.setCode("001");
        productDao.save(product);
        List<Product> products = new ArrayList<>();
        products.add(product);

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        vendingMachine.setProducts(products);

        Operation operation = new Operation();
        operation.setOperationId(1);
        operation.setProducts(products);
        operation.setValue(0.5);
        operation.setStatus(StatusEnum.OPEN.name());
        operation.setDate(new Date());

        Mockito.when(vendingMachineDao.findById(Mockito.anyInt())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.findByUser(Mockito.any())).thenReturn(Optional.of(operation));

        OperationSelectProductRequest data = new OperationSelectProductRequest();
        data.setCode("001");

        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");

        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST,new HttpEntity<OperationSelectProductRequest>(data,headers),OperationSelectProductResponse.class,5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.OPERATION_INS_PRODUCT_OK.getText(), response.getBody().getMessage());

    }

    @Test
    public void operationAddProductsRequestFail(){

        OperationSelectProductRequest data = new OperationSelectProductRequest();

        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");

        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<OperationSelectProductRequest>(data,headers),OperationSelectProductResponse.class,5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PARAM_VALID_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddProductsVmNull(){
        OperationSelectProductRequest data = new OperationSelectProductRequest();
        data.setCode("001");

        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");

        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<OperationSelectProductRequest>(data,headers),OperationSelectProductResponse.class,5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddProductsOperationNull(){

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");

        Mockito.when(vendingMachineDao.findById(Mockito.anyInt())).thenReturn(Optional.of(vendingMachine));

        OperationSelectProductRequest data = new OperationSelectProductRequest();
        data.setCode("001");

        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, getUser(data,vendingMachine),OperationSelectProductResponse.class,5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddProductsOperationClosed(){
        Product product = new Product();
        product.setProductId(1);
        product.setName("Apple");
        product.setPrice(0.5);
        product.setCode("001");
        productDao.save(product);
        List<Product> products = new ArrayList<>();
        products.add(product);

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");

        Operation operation = new Operation();
        operation.setOperationId(1);
        operation.setProducts(products);
        operation.setValue(0.5);
        operation.setStatus(StatusEnum.CLOSE_OK.name());
        operation.setDate(new Date());

        Mockito.when(vendingMachineDao.findById(Mockito.anyInt())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.findByUser(Mockito.any())).thenReturn(Optional.of(operation));

        OperationSelectProductRequest data = new OperationSelectProductRequest();
        data.setCode("001");

        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");

        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<OperationSelectProductRequest>(data,headers),OperationSelectProductResponse.class,5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_CLOSED.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddProductsCoinNull(){
        Product product = new Product();
        product.setProductId(1);
        product.setName("Apple");
        product.setPrice(0.5);
        product.setCode("001");

        List<Product> products = new ArrayList<>();
        products.add(product);

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");

        Operation operation = new Operation();
        operation.setOperationId(1);
        operation.setProducts(products);
        operation.setValue(0.5);
        operation.setStatus(StatusEnum.OPEN.name());
        operation.setDate(new Date());

        Mockito.when(vendingMachineDao.findById(Mockito.anyInt())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.findByUser(Mockito.any())).thenReturn(Optional.of(operation));

        OperationSelectProductRequest data = new OperationSelectProductRequest();
        data.setCode("001");

        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");

        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<OperationSelectProductRequest>(data,headers),OperationSelectProductResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.PRODUCT_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddProductsException(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.anyInt());

        OperationSelectProductRequest data = new OperationSelectProductRequest();
        data.setCode("001");

        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");

        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, getUser(data,vendingMachine),OperationSelectProductResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_INS_PRODUCT_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddProductsControllerException(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Mockito.doThrow(new RuntimeException()).when(service).addProductOperation(Mockito.anyString(),Mockito.any(), Mockito.any());
        OperationSelectProductRequest data = new OperationSelectProductRequest();

        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, getUser(data,vendingMachine),OperationSelectProductResponse.class,5);
        Mockito.doCallRealMethod().when(service).addProductOperation(Mockito.anyString(),Mockito.any(), Mockito.any());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    public HttpEntity<HttpHeaders> getUser(OperationSelectProductRequest data,VendingMachine vendingMachine){
        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);

        User user = new User();
        user.setRole(UserEnum.USER.name());
        user.setVendingMachine(vendingMachine);
        Mockito.when(userDao.findById(Mockito.anyInt())).thenReturn(Optional.of(user));
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");

        return new HttpEntity(data,headers);
    }
}
