package com.operation;

import com.dao.OperationDao;
import com.dao.ProductDao;
import com.dao.VendingMachineDao;
import com.domain.operation.response.OperationGetSelectedProductsResponse;
import com.model.Coin;
import com.model.Operation;
import com.model.Product;
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
import org.springframework.http.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class GetOperationProductsTest {

    @SpyBean
    private OperationService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingMachine/{id}/operation/product?operation={operation}";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private VendingMachineDao vendingMachineDao;

    @Autowired
    private ProductDao productDao;

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
    public void getProductsOperationOk(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Operation operation = setOperation(vendingMachine);
        Set<Operation> operations = new HashSet<>();
        operations.add(operation);
        vendingMachine.setOperations(operations);
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.findById(Mockito.any())).thenReturn(Optional.of(operation));

        ResponseEntity<OperationGetSelectedProductsResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),OperationGetSelectedProductsResponse.class,5,1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.OPERATION_GET_PRODUCT_OK.getText(), response.getBody().getMessage());
    }

    @Test
    public void getProductsOperationVmNull(){
        ResponseEntity<OperationGetSelectedProductsResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),OperationGetSelectedProductsResponse.class,5,1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void getProductsOperationOperationNull(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Operation operation = setOperation(vendingMachine);

        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        ResponseEntity<OperationGetSelectedProductsResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),OperationGetSelectedProductsResponse.class,5,1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void getProductsOperationException(){
        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.any());

        ResponseEntity<OperationGetSelectedProductsResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),OperationGetSelectedProductsResponse.class,5,1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_GET_PRODUCT_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void getProductsOperationControllerException(){
        Mockito.doThrow(new RuntimeException()).when(service).getProductOperation(Mockito.anyInt(),Mockito.anyInt());

        ResponseEntity<OperationGetSelectedProductsResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),OperationGetSelectedProductsResponse.class,5,1);
        Mockito.doCallRealMethod().when(service).getProductOperation(Mockito.anyInt(),Mockito.anyInt());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private Operation setOperation(VendingMachine vendingMachine){

        Product product = new Product();
        product.setProductId(1);
        product.setName("Apple");
        product.setPrice(0.5);
        product.setCode("001");

        productDao.save(product);
        List<Product> products = new ArrayList<>();
        products.add(product);
        products.add(product);

        Coin coin1 = new Coin();
        coin1.setCoinId(1);
        coin1.setName("50 cents");
        coin1.setValue(0.5);

        Coin coin2 = new Coin();
        coin2.setCoinId(2);
        coin2.setName("5 cents");
        coin2.setValue(0.05);

        List<Coin> coins = new ArrayList<>();
        coins.add(coin1);
        coins.add(coin2);

        Operation operation = new Operation();
        operation.setOperationId(1);
        operation.setVendingMachine(vendingMachine);
        operation.setCoins(coins);
        operation.setProducts(products);
        operation.setValue(0.5);
        operation.setStatus(StatusEnum.OPEN.name());
        operation.setDate(new Date());

        return operation;
    }
}
