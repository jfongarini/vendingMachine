package com.vendingMachine;

import com.dao.VendingMachineDao;
import com.domain.vendingMachine.request.VendingMachineUpdateRequest;
import com.domain.vendingMachine.response.VendingMachineUpdateResponse;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class UpdateVendingMachineTest {

    @SpyBean
    private VendingMachineService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingmachine/update/{id}";

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
    public void vendingMachineUpdateOk(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setName("ten");
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        VendingMachineUpdateRequest data = new VendingMachineUpdateRequest();
        data.setName("ten10");
        HttpEntity<VendingMachineUpdateRequest> request = new HttpEntity<>(data);

        vendingMachine.setName(data.getName());
        Mockito.when(vendingMachineDao.save(Mockito.any())).thenReturn(vendingMachine);

        ResponseEntity<VendingMachineUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, request,VendingMachineUpdateResponse.class,10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.VM_UPDATE_OK.getText(), response.getBody().getMessage());
    }

    @Test
    public void vendingMachineUpdateRequestFail(){
        VendingMachineUpdateRequest data = new VendingMachineUpdateRequest();
        HttpEntity<VendingMachineUpdateRequest> request = new HttpEntity<>(data);

        ResponseEntity<VendingMachineUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, request,VendingMachineUpdateResponse.class,10);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.VM_PARAMETERS_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineUpdateVendingMachineNull(){
        VendingMachineUpdateRequest data = new VendingMachineUpdateRequest();
        data.setName("ten10");
        HttpEntity<VendingMachineUpdateRequest> request = new HttpEntity<>(data);

        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(null);

        ResponseEntity<VendingMachineUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, request,VendingMachineUpdateResponse.class,10);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineUpdateException(){
        VendingMachineUpdateRequest data = new VendingMachineUpdateRequest();
        data.setName("ten10");
        HttpEntity<VendingMachineUpdateRequest> request = new HttpEntity<>(data);

        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.anyInt());
        ResponseEntity<VendingMachineUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, request,VendingMachineUpdateResponse.class,10);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_UPDATE_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineUpdateControllerException(){
        VendingMachineUpdateRequest data = new VendingMachineUpdateRequest();
        data.setName("ten10");
        HttpEntity<VendingMachineUpdateRequest> request = new HttpEntity<>(data);

        Mockito.doThrow(new RuntimeException()).when(service).updateVendingMachine(Mockito.any(), Mockito.anyString());

        ResponseEntity<VendingMachineUpdateResponse> response = restTemplate.exchange(URL, HttpMethod.PUT, request,VendingMachineUpdateResponse.class,10);
        Mockito.doCallRealMethod().when(service).updateVendingMachine(Mockito.any(), Mockito.anyString());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
