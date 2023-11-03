package com.product;

import com.CommonTest;
import com.dao.ProductDao;
import com.domain.product.response.ProductGetAvailableResponse;
import com.model.Product;
import com.service.ApiProductService;
import com.service.ProductService;
import com.util.enums.MessagesEnum;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import org.springframework.http.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class GetAvailableProductTest extends CommonTest {

    @SpyBean
    private ProductService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/products/available";

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
    public void availableProductOk() throws IOException {

        List<String> stringList = new ArrayList<>();
        stringList.add("Apple");
        stringList.add("Banana");
        Mockito.when(apiProductService.getAllFruits()).thenReturn(stringList);

        Product product = new Product();
        product.setProductId(10);
        product.setName("Apple");
        product.setCode("001");
        product.setPrice(1.0);
        product.setExist(true);
        productDao.save(product);

        ResponseEntity<ProductGetAvailableResponse> response = restTemplate.exchange(URL, HttpMethod.GET, getAdmin(),ProductGetAvailableResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.PRODUCT_GET_AVAILABLE_OK.getText(), response.getBody().getMessage());

    }

    @Test
    public void availableProductEmpty() throws IOException {

        List<String> stringList = new ArrayList<>();
        Mockito.when(apiProductService.getAllFruits()).thenReturn(stringList);

        ResponseEntity<ProductGetAvailableResponse> response = restTemplate.exchange(URL, HttpMethod.GET, getAdmin(),ProductGetAvailableResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PRODUCT_LIST_EMPTY.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void availableProductServiceException() throws IOException {
        Mockito.when(apiProductService.getAllFruits()).thenThrow(ServiceException.class);

        ResponseEntity<ProductGetAvailableResponse> response = restTemplate.exchange(URL, HttpMethod.GET, getAdmin(),ProductGetAvailableResponse.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.PRODUCT_GET_AVAILABLE_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void availableProductException() throws IOException {
        Mockito.when(apiProductService.getAllFruits()).thenThrow(RuntimeException.class);;

        ResponseEntity<ProductGetAvailableResponse> response = restTemplate.exchange(URL, HttpMethod.GET, getAdmin(),ProductGetAvailableResponse.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.PRODUCT_GET_AVAILABLE_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void availableProductControllerException() throws IOException {

        Mockito.doThrow(new RuntimeException()).when(service).getAvailableProduct();
        ResponseEntity<ProductGetAvailableResponse> response = restTemplate.exchange(URL, HttpMethod.GET, getAdmin(), ProductGetAvailableResponse.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
