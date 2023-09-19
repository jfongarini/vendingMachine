package com.operation;

import com.dao.OperationDao;
import com.dao.ProductDao;
import com.dao.VendingMachineDao;

import com.domain.operation.request.OperationSelectProductRequest;
import com.domain.operation.response.OperationSelectProductResponse;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class AddProductsOperationTest {

    @SpyBean
    private OperationService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingMachine/{id}/operation/product";

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

        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.findById(Mockito.any())).thenReturn(Optional.of(operation));

        OperationSelectProductRequest data = new OperationSelectProductRequest();
        data.setCode("001");

        HttpEntity<OperationSelectProductRequest> request = new HttpEntity<>(data);

        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationSelectProductResponse.class,5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.OPERATION_INS_PRODUCT_OK.getText(), response.getBody().getMessage());

    }

    @Test
    public void operationAddProductsRequestFail(){
        OperationSelectProductRequest data = new OperationSelectProductRequest();

        HttpEntity<OperationSelectProductRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationSelectProductResponse.class,5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PARAM_VALID_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddProductsVmNull(){
        OperationSelectProductRequest data = new OperationSelectProductRequest();
        data.setCode("001");

        HttpEntity<OperationSelectProductRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationSelectProductResponse.class,5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddProductsOperationNull(){

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");

        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        OperationSelectProductRequest data = new OperationSelectProductRequest();
        data.setCode("001");

        HttpEntity<OperationSelectProductRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationSelectProductResponse.class,5);

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

        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.findById(Mockito.any())).thenReturn(Optional.of(operation));

        OperationSelectProductRequest data = new OperationSelectProductRequest();
        data.setCode("001");

        HttpEntity<OperationSelectProductRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationSelectProductResponse.class,5);

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

        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.findById(Mockito.any())).thenReturn(Optional.of(operation));

        OperationSelectProductRequest data = new OperationSelectProductRequest();
        data.setCode("001");

        HttpEntity<OperationSelectProductRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationSelectProductResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.PRODUCT_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddProductsException(){

        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.anyInt());

        OperationSelectProductRequest data = new OperationSelectProductRequest();
        data.setCode("001");

        HttpEntity<OperationSelectProductRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationSelectProductResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_INS_PRODUCT_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationAddProductsControllerException(){

        Mockito.doThrow(new RuntimeException()).when(service).addProductOperation(Mockito.anyString(),Mockito.any(), Mockito.any());
        OperationSelectProductRequest data = new OperationSelectProductRequest();
        HttpEntity<OperationSelectProductRequest> request = new HttpEntity<>(data);
        ResponseEntity<OperationSelectProductResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,OperationSelectProductResponse.class,5);
        Mockito.doCallRealMethod().when(service).addProductOperation(Mockito.anyString(),Mockito.any(), Mockito.any());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
