package com.vendingMachine.products;

import com.dao.ProductDao;
import com.dao.VendingMachineDao;
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
public class ExtractProductsVendingMachineTest {

    @SpyBean
    private VendingMachineService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingmachine/{id}/extractProducts";

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
    public void vendingMachineExtractProductsOk(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");


        Product product = new Product();
        product.setProductId(10);
        product.setName("ten");
        List<Product> productList = new ArrayList<>();
        productList.add(product);
        productList.add(product);
        vendingMachine.setProducts(productList);
        Mockito.when(productDao.findAll()).thenReturn(productList);
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));
        ResponseEntity<VmExtractProductsResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),VmExtractProductsResponse.class,5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.VMP_DELETE_OK.getText(), response.getBody().getMessage());

    }

    @Test
    public void vendingMachineExtractProductsVMNull(){

        ResponseEntity<VmExtractProductsResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),VmExtractProductsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());

    }

    @Test
    public void vendingMachineExtractProductsException(){

        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.anyInt());

        ResponseEntity<VmExtractProductsResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),VmExtractProductsResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VMP_DELETE_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineExtractProductsControllerException(){

        Mockito.doThrow(new RuntimeException()).when(service).extractProductsVendingMachine(Mockito.anyInt());

        ResponseEntity<VmExtractProductsResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),VmExtractProductsResponse.class,5);
        Mockito.doCallRealMethod().when(service).extractProductsVendingMachine(Mockito.anyInt());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
