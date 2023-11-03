package com.product;

import com.CommonTest;
import com.dao.ProductDao;
import com.domain.product.request.ProductNewRequest;
import com.domain.product.response.ProductNewResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.service.ApiProductService;
import com.util.enums.MessagesEnum;
import com.model.Product;
import com.service.ProductService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class NewProductTest extends CommonTest {

    @SpyBean
    private ProductService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/products";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private ApiProductService apiProductService;

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
    public void productNewOk() throws IOException {

        ProductNewRequest data = new ProductNewRequest();
        data.setName("Apple");
        data.setCode("001");
        data.setPrice(1.0);

        Mockito.when(apiProductService.getFruit(Mockito.anyString())).thenReturn("Apple");

        Product product = new Product();
        product.setName(data.getName());
        product.setCode(data.getCode());
        product.setPrice(data.getPrice());
        Mockito.when(productDao.save(Mockito.any())).thenReturn(product);

        ResponseEntity<ProductNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, getAdmin(data),ProductNewResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.PRODUCT_NEW_OK.getText(), response.getBody().getMessage());
        assertEquals("Apple", response.getBody().getData().getName());
    }

    @Test
    public void productNewRequestFail(){
        ProductNewRequest data = new ProductNewRequest();
        data.setName("Apple");

        ResponseEntity<ProductNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, getAdmin(data),ProductNewResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PARAM_VALID_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void productNewAvailableFail() throws IOException {
        ProductNewRequest data = new ProductNewRequest();
        data.setName("Apple");
        data.setCode("001");
        data.setPrice(1.0);

        Mockito.when(apiProductService.getFruit(Mockito.anyString())).thenReturn(null);

        ResponseEntity<ProductNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, getAdmin(data),ProductNewResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PRODUCT_NEW_NOT_AVAILABLE.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void productNewException() throws JsonProcessingException {
        ProductNewRequest data = new ProductNewRequest();
        data.setName("Apple");
        data.setCode("001");
        data.setPrice(1.0);

        Mockito.when(apiProductService.getFruit(Mockito.anyString())).thenReturn("Apple");
        Mockito.doThrow(new RuntimeException()).when(productDao).save(Mockito.any());
        ResponseEntity<ProductNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, getAdmin(data),ProductNewResponse.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.PRODUCT_NEW_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void productNewControllerException(){
        ProductNewRequest data = new ProductNewRequest();
        data.setName("Apple");
        data.setCode("001");
        data.setPrice(1.0);

        Mockito.doThrow(new RuntimeException()).when(service).newProduct(Mockito.any(), Mockito.any());

        ResponseEntity<ProductNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, getAdmin(data),ProductNewResponse.class);
        Mockito.doCallRealMethod().when(service).newProduct(Mockito.any(), Mockito.any());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
