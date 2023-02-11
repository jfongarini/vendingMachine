package com.product;

import com.dao.ProductDao;
import com.domain.product.request.ProductUpdateRequest;
import com.domain.product.response.ProductUpdateResponse;
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
public class UpdateProductTest {

    @SpyBean
    private ProductService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/product/update/{id}";

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
    public void productUpdateOk(){
        Product product = new Product();
        product.setProductId(10);
        product.setName("Apple");
        product.setCode("001");
        product.setPrice(1.0);
        Mockito.when(productDao.findById(Mockito.any())).thenReturn(Optional.of(product));

        ProductUpdateRequest data = new ProductUpdateRequest();
        data.setCode("099");
        HttpEntity<ProductUpdateRequest> request = new HttpEntity<>(data);

        product.setCode(data.getCode());
        Mockito.when(productDao.save(Mockito.any())).thenReturn(product);

        ResponseEntity<ProductUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, request,ProductUpdateResponse.class,10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.PRODUCT_UPDATE_OK.getText(), response.getBody().getMessage());
    }

    @Test
    public void productUpdateRequestFail(){
        ProductUpdateRequest data = new ProductUpdateRequest();
        HttpEntity<ProductUpdateRequest> request = new HttpEntity<>(data);

        ResponseEntity<ProductUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, request,ProductUpdateResponse.class,10);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PRODUCT_PARAMETERS_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void productUpdateProductNull(){
        ProductUpdateRequest data = new ProductUpdateRequest();
        data.setCode("099");
        HttpEntity<ProductUpdateRequest> request = new HttpEntity<>(data);

        Mockito.when(productDao.findById(Mockito.any())).thenReturn(null);

        ResponseEntity<ProductUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, request,ProductUpdateResponse.class,10);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PRODUCT_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void productUpdateException(){
        ProductUpdateRequest data = new ProductUpdateRequest();
        data.setCode("099");
        HttpEntity<ProductUpdateRequest> request = new HttpEntity<>(data);

        Mockito.doThrow(new RuntimeException()).when(productDao).findById(Mockito.anyInt());
        ResponseEntity<ProductUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, request,ProductUpdateResponse.class,10);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.PRODUCT_UPDATE_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void productUpdateControllerException(){
        ProductUpdateRequest data = new ProductUpdateRequest();
        data.setCode("099");
        HttpEntity<ProductUpdateRequest> request = new HttpEntity<>(data);

        Mockito.doThrow(new RuntimeException()).when(service).updateProduct(Mockito.any(), Mockito.anyInt());

        ResponseEntity<ProductUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, request,ProductUpdateResponse.class,10);
        Mockito.doCallRealMethod().when(service).updateProduct(Mockito.any(), Mockito.anyInt());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
