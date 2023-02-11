package com.product;

import com.dao.ProductDao;
import com.domain.product.response.ProductGetAllResponse;
import com.util.enums.MessagesEnum;
import com.model.Product;
import com.service.ProductService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class GetAllProductTest {

    @SpyBean
    private ProductService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/product/all";

    @Autowired
    private TestRestTemplate restTemplate;

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
    public void productGetAllOk(){
        Product product = new Product();
        product.setProductId(10);
        product.setName("Apple");
        product.setCode("001");
        product.setPrice(1.0);
        List<Product> products = new ArrayList<>();
        products.add(product);
        Mockito.when(productDao.save(Mockito.any())).thenReturn(product);
        Mockito.when(productDao.findAll()).thenReturn(products);

        ResponseEntity<ProductGetAllResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()), ProductGetAllResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.PRODUCT_GET_ALL_OK.getText(), response.getBody().getMessage());
    }

    @Test
    public void productGetAllProductListNull(){
        Mockito.when(productDao.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<ProductGetAllResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()), ProductGetAllResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PRODUCT_LIST_EMPTY.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void productGetAllException(){
        Mockito.doThrow(new RuntimeException()).when(productDao).findAll();

        ResponseEntity<ProductGetAllResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()), ProductGetAllResponse.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.PRODUCT_GET_ALL_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void productGetAllControllerException(){
        Mockito.doThrow(new RuntimeException()).when(service).getAllProduct();

        ResponseEntity<ProductGetAllResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()), ProductGetAllResponse.class);
        Mockito.doCallRealMethod().when(service).getAllProduct();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
