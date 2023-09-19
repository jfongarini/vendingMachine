package com.operation;

import com.dao.CoinDao;
import com.dao.OperationDao;
import com.dao.ProductDao;
import com.dao.VendingMachineDao;
import com.domain.operation.response.OperationCancelResponse;
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
public class CancelOperationTest {


    @SpyBean
    private OperationService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingMachine/{id}/operation/cancel?operation={operation}";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private VendingMachineDao vendingMachineDao;

    @Autowired
    private CoinDao coinDao;

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
    public void cancelOperationOk(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Product product = new Product();
        product.setProductId(10);
        product.setName("Apple");
        product.setPrice(0.5);
        product.setCode("001");
        productDao.save(product);

        Operation operation = setOperation(vendingMachine,product);
        Set<Operation> operations = new HashSet<>();
        operations.add(operation);
        List<Product> products = new ArrayList<>();
        products.add(product);
        vendingMachine.setProducts(products);
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.findById(Mockito.any())).thenReturn(Optional.of(operation));

        ResponseEntity<OperationCancelResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<Void>(new HttpHeaders()),OperationCancelResponse.class,5,1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.OPERATION_CANCEL_OK.getText(), response.getBody().getMessage());
    }

    @Test
    public void cancelOperationVmNull(){
        ResponseEntity<OperationCancelResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<Void>(new HttpHeaders()),OperationCancelResponse.class,5,1);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void cancelOperationOperationNull(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");

        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        ResponseEntity<OperationCancelResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<Void>(new HttpHeaders()),OperationCancelResponse.class,5,1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void cancelOperationException(){

        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.any());
        ResponseEntity<OperationCancelResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<Void>(new HttpHeaders()),OperationCancelResponse.class,5,1);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_CANCEL_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void cancelOperationControllerException(){

        Mockito.doThrow(new RuntimeException()).when(service).cancelOperation(Mockito.anyString());
        ResponseEntity<OperationCancelResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<Void>(new HttpHeaders()),OperationCancelResponse.class,5,1);
        Mockito.doCallRealMethod().when(service).cancelOperation(Mockito.anyString());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private Operation setOperation(VendingMachine vendingMachine, Product product){

        List<Product> products = new ArrayList<>();
        products.add(product);

        Operation operation = new Operation();
        operation.setOperationId(1);
        operation.setProducts(products);
        operation.setValue(0.5);
        operation.setStatus(StatusEnum.OPEN.name());
        operation.setDate(new Date());

        return operation;
    }
}
