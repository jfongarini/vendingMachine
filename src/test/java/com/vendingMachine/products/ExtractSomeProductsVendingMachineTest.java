package com.vendingMachine.products;

import com.dao.ProductDao;
import com.dao.VendingMachineDao;
import com.domain.vendingMachine.request.VmProductRequest;
import com.domain.vendingMachine.request.VmExtractSomeProductsRequest;
import com.domain.vendingMachine.response.VmExtractProductsResponse;
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
import org.springframework.http.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ExtractSomeProductsVendingMachineTest {

    @SpyBean
    private VendingMachineService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingmachine/{id}/extractSomeProducts";

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
    public void vendingMachineExtractSomeProductsOk(){

        Product product = new Product();
        product.setProductId(10);
        product.setName("ten");
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Mockito.when(productDao.findByName(Mockito.any())).thenReturn(Optional.of(product));

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        vendingMachine.setProducts(productList);
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        VmProductRequest vmProductRequest = new VmProductRequest();
        vmProductRequest.setName("ten");
        vmProductRequest.setQuantity(1L);

        List<VmProductRequest> products = new ArrayList<>();
        products.add(vmProductRequest);

        VmExtractSomeProductsRequest data = new VmExtractSomeProductsRequest();
        data.setProducts(products);
        HttpEntity<VmExtractSomeProductsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmExtractProductsResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, request,VmExtractProductsResponse.class,5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.VMP_DELETE_SOME_OK.getText(), response.getBody().getMessage());

    }

    @Test
    public void vendingMachineExtractSomeProductsRequestFail(){

        List<VmProductRequest> products = new ArrayList<>();

        VmExtractSomeProductsRequest data = new VmExtractSomeProductsRequest();
        data.setProducts(products);

        HttpEntity<VmExtractSomeProductsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmExtractProductsResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, request,VmExtractProductsResponse.class,5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PARAM_VALID_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineExtractSomeProductsVMNull(){

        VmProductRequest vmProductRequest = new VmProductRequest();
        vmProductRequest.setName("ten");
        vmProductRequest.setQuantity(1L);

        List<VmProductRequest> products = new ArrayList<>();
        products.add(vmProductRequest);

        VmExtractSomeProductsRequest data = new VmExtractSomeProductsRequest();
        data.setProducts(products);

        HttpEntity<VmExtractSomeProductsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmExtractProductsResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, request,VmExtractProductsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineExtractSomeProductsProductsNull(){

        Product product = new Product();
        product.setProductId(10);
        product.setName("ten");
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        Mockito.when(productDao.findByName(Mockito.any())).thenReturn(null);

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        vendingMachine.setProducts(productList);
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        VmProductRequest vmProductRequest = new VmProductRequest();
        vmProductRequest.setName("ten");
        vmProductRequest.setQuantity(1L);

        List<VmProductRequest> products = new ArrayList<>();
        products.add(vmProductRequest);

        VmExtractSomeProductsRequest data = new VmExtractSomeProductsRequest();
        data.setProducts(products);
        HttpEntity<VmExtractSomeProductsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmExtractProductsResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, request,VmExtractProductsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.PRODUCT_NOT_EXIST.getText(), response.getBody().getError().getMessage());

    }

    @Test
    public void vendingMachineExtractSomeProductsException(){

        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.anyInt());

        VmProductRequest vmProductRequest = new VmProductRequest();
        vmProductRequest.setName("ten");
        vmProductRequest.setQuantity(1L);

        List<VmProductRequest> products = new ArrayList<>();
        products.add(vmProductRequest);

        VmExtractSomeProductsRequest data = new VmExtractSomeProductsRequest();
        data.setProducts(products);

        HttpEntity<VmExtractSomeProductsRequest> request = new HttpEntity<>(data);
        ResponseEntity<VmExtractProductsResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, request,VmExtractProductsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VMP_DELETE_SOME_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineExtractSomeProductsControllerException(){

        VmExtractSomeProductsRequest data = new VmExtractSomeProductsRequest();

        HttpEntity<VmExtractSomeProductsRequest> request = new HttpEntity<>(data);

        Mockito.doThrow(new RuntimeException()).when(service).extractSomeProductsVendingMachine(Mockito.anyInt(),Mockito.any(),Mockito.any());

        ResponseEntity<VmExtractProductsResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, request,VmExtractProductsResponse.class,5);
        Mockito.doCallRealMethod().when(service).extractSomeProductsVendingMachine(Mockito.anyInt(),Mockito.any(),Mockito.any());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
