package com.vendingMachine.operations;

import com.dao.OperationDao;
import com.dao.VendingMachineDao;
import com.domain.vendingMachine.operation.response.VmGetOperationsResponse;
import com.model.Coin;
import com.model.Operation;
import com.model.Product;
import com.model.VendingMachine;
import com.service.VendingMachineService;
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
public class GetOperationsVendingMachineTest {

    @SpyBean
    private VendingMachineService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingmachine/{id}/getOperations";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private VendingMachineDao vendingMachineDao;

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
    public void vendingMachineGetOperationsOk(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        Operation operation = setOperation(vendingMachine);
        List<Operation> operations = new ArrayList<>();
        operations.add(operation);
        Mockito.when(operationDao.findAll()).thenReturn(operations);
        ResponseEntity<VmGetOperationsResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),VmGetOperationsResponse.class,5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.VMO_GET_ALL_OK.getText(), response.getBody().getMessage());

    }

    @Test
    public void vendingMachineGetOperationsWithDatesOk(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        Operation operation = setOperation(vendingMachine);
        List<Operation> operations = new ArrayList<>();
        operations.add(operation);
        Mockito.when(operationDao.getAllBetweenDates(Mockito.any(),Mockito.any())).thenReturn(operations);

        String URL_FROM = "/api/vendingmachine/{id}/getOperations?from=01/01/2023";
        ResponseEntity<VmGetOperationsResponse> response = restTemplate.exchange(URL_FROM, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),VmGetOperationsResponse.class,5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.VMO_GET_ALL_OK.getText(), response.getBody().getMessage());
    }

    @Test
    public void vendingMachineGetOperationsVMNull(){

        ResponseEntity<VmGetOperationsResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),VmGetOperationsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());

    }

    @Test
    public void vendingMachineGetOperationsException(){
        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.anyInt());

        ResponseEntity<VmGetOperationsResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),VmGetOperationsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VMO_GET_ALL_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineGetOperationsControllerException(){
        Mockito.doThrow(new RuntimeException()).when(service).getOperationsVendingMachine(Mockito.anyString(),Mockito.eq(null),Mockito.eq(null));

        ResponseEntity<VmGetOperationsResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),VmGetOperationsResponse.class,5);
        Mockito.doCallRealMethod().when(service).getOperationsVendingMachine(Mockito.anyString(),Mockito.eq(null),Mockito.eq(null));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    private Operation setOperation(VendingMachine vendingMachine){

        Product product = new Product();
        product.setProductId(10);
        product.setName("Apple");
        product.setPrice(0.5);
        product.setCode("001");
        List<Product> products = new ArrayList<>();
        products.add(product);

        Coin coin = new Coin();
        coin.setCoinId(10);
        coin.setName("50 cents");
        coin.setValue(0.5);
        List<Coin> coins = new ArrayList<>();
        coins.add(coin);

        Operation operation = new Operation();
        operation.setOperationId(1);
        operation.setCoins(coins);
        operation.setProducts(products);
        operation.setValue(0.5);
        operation.setStatus(StatusEnum.OPEN.name());
        operation.setDate(new Date());

        return operation;
    }
}
