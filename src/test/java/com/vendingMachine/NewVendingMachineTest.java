package com.vendingMachine;

import com.dao.VendingMachineDao;
import com.domain.vendingMachine.request.VendingMachineNewRequest;
import com.domain.vendingMachine.response.VendingMachineNewResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class NewVendingMachineTest {

    @SpyBean
    private VendingMachineService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingmachine/new";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private VendingMachineDao vendingMachineDao;

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
    public void vendingMachineNewOk(){
        VendingMachineNewRequest data = new VendingMachineNewRequest();
        data.setName("first");
        HttpEntity<VendingMachineNewRequest> request = new HttpEntity<>(data);

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setName(data.getName());
        Mockito.when(vendingMachineDao.save(Mockito.any())).thenReturn(vendingMachine);

        ResponseEntity<VendingMachineNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VendingMachineNewResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.VM_NEW_OK.getText(), response.getBody().getMessage());
        assertEquals("first", response.getBody().getData().getName());
    }

    @Test
    public void vendingMachineNewRequestFail(){
        VendingMachineNewRequest data = new VendingMachineNewRequest();

        HttpEntity<VendingMachineNewRequest> request = new HttpEntity<>(data);

        ResponseEntity<VendingMachineNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VendingMachineNewResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.PARAM_VALID_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineNewException(){
        VendingMachineNewRequest data = new VendingMachineNewRequest();
        data.setName("first");

        HttpEntity<VendingMachineNewRequest> request = new HttpEntity<>(data);

        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).save(Mockito.any());
        ResponseEntity<VendingMachineNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VendingMachineNewResponse.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NEW_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineNewControllerException(){
        VendingMachineNewRequest data = new VendingMachineNewRequest();
        data.setName("first");

        HttpEntity<VendingMachineNewRequest> request = new HttpEntity<>(data);

        Mockito.doThrow(new RuntimeException()).when(service).newVendingMachine(Mockito.any(), Mockito.any());

        ResponseEntity<VendingMachineNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, request,VendingMachineNewResponse.class);
        Mockito.doCallRealMethod().when(service).newVendingMachine(Mockito.any(), Mockito.any());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
