package com.product;

import com.dao.ProductDao;
import com.domain.product.response.ProductGetResponse;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class GetProductTest {

    @SpyBean
    private ProductService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/product/{id}";

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
    public void productGetOk(){
        Product product = new Product();
        product.setProductId(10);
        product.setName("Apple");
        product.setCode("001");
        product.setPrice(1.0);
        Mockito.when(productDao.save(Mockito.any())).thenReturn(product);
        Mockito.when(productDao.findById(Mockito.any())).thenReturn(Optional.of(product));

        ResponseEntity<ProductGetResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),ProductGetResponse.class,10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.PRODUCT_GET_OK.getText(), response.getBody().getMessage());
        assertEquals("Apple", response.getBody().getData().getName());
    }

    @Test
    public void productGetProductNull(){
        Mockito.when(productDao.findById(Mockito.any())).thenReturn(null);

        ResponseEntity<ProductGetResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),ProductGetResponse.class,10);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PRODUCT_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void productGetException(){
        Mockito.doThrow(new RuntimeException()).when(productDao).findById(Mockito.any());

        ResponseEntity<ProductGetResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),ProductGetResponse.class,10);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.PRODUCT_GET_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void productGetControllerException(){
        Mockito.doThrow(new RuntimeException()).when(service).getProduct(Mockito.anyInt());

        ResponseEntity<ProductGetResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),ProductGetResponse.class,10);
        Mockito.doCallRealMethod().when(service).getProduct(Mockito.anyInt());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
