package com.vendingMachine.products;

import com.dao.ProductDao;
import com.dao.VendingMachineDao;
import com.domain.vendingMachine.request.VmProductRequest;
import com.domain.vendingMachine.request.VmInsertProductsRequest;
import com.domain.vendingMachine.response.VmInsertProductsResponse;
import com.model.Product;
import com.model.VendingMachine;
import com.service.VendingMachineService;
import com.util.enums.MessagesEnum;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class InsertProductsVendingMachineTest {

    @SpyBean
    private VendingMachineService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingmachine/{id}/insertProducts";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private VendingMachineDao vendingMachineDao;

    @MockBean
    private ProductDao productDao;

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
    public void vendingMachineInsertProductsOk(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        Product product = new Product();
        product.setProductId(10);
        product.setName("ten");
        Mockito.when(productDao.findByName(Mockito.any())).thenReturn(Optional.of(product));

        VmProductRequest vmProductRequest = new VmProductRequest();
        vmProductRequest.setName("ten");
        vmProductRequest.setQuantity(1L);

        List<VmProductRequest> Products = new ArrayList<>();
        Products.add(vmProductRequest);

        VmInsertProductsRequest data = new VmInsertProductsRequest();
        data.setProducts(Products);

        HttpEntity<VmInsertProductsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmInsertProductsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VmInsertProductsResponse.class,5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.VMP_POST_OK.getText(), response.getBody().getMessage());

    }

    @Test
    public void vendingMachineInsertProductsRequestFail(){

        List<VmProductRequest> products = new ArrayList<>();

        VmInsertProductsRequest data = new VmInsertProductsRequest();
        data.setProducts(products);

        HttpEntity<VmInsertProductsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmInsertProductsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VmInsertProductsResponse.class,5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PARAM_VALID_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineInsertProductsProductNull(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        VmProductRequest vmProductRequest = new VmProductRequest();
        vmProductRequest.setName("ten");
        vmProductRequest.setQuantity(1L);

        List<VmProductRequest> products = new ArrayList<>();
        products.add(vmProductRequest);

        VmInsertProductsRequest data = new VmInsertProductsRequest();
        data.setProducts(products);

        HttpEntity<VmInsertProductsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmInsertProductsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VmInsertProductsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.PRODUCT_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineInsertProductsVMNull(){

        VmProductRequest vmProductRequest = new VmProductRequest();
        vmProductRequest.setName("ten");
        vmProductRequest.setQuantity(1L);

        List<VmProductRequest> products = new ArrayList<>();
        products.add(vmProductRequest);

        VmInsertProductsRequest data = new VmInsertProductsRequest();
        data.setProducts(products);

        HttpEntity<VmInsertProductsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmInsertProductsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VmInsertProductsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineInsertProductsException(){

        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.anyInt());

        VmProductRequest vmProductRequest = new VmProductRequest();
        vmProductRequest.setName("ten");
        vmProductRequest.setQuantity(1L);

        List<VmProductRequest> products = new ArrayList<>();
        products.add(vmProductRequest);

        VmInsertProductsRequest data = new VmInsertProductsRequest();
        data.setProducts(products);

        HttpEntity<VmInsertProductsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmInsertProductsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VmInsertProductsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VMP_POST_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineInsertProductsControllerException(){

        VmInsertProductsRequest data = new VmInsertProductsRequest();

        HttpEntity<VmInsertProductsRequest> request = new HttpEntity<>(data);

        Mockito.doThrow(new RuntimeException()).when(service).insertProductsVendingMachine(Mockito.anyInt(),Mockito.any(), Mockito.any());

        ResponseEntity<VmInsertProductsResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VmInsertProductsResponse.class,5);
        Mockito.doCallRealMethod().when(service).insertProductsVendingMachine(Mockito.anyInt(),Mockito.any(), Mockito.any());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
