package com.vendingMachine;

import com.dao.VendingMachineDao;
import com.domain.vendingMachine.response.VendingMachineGetResponse;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class GetVendingMachineTest {

    @SpyBean
    private VendingMachineService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingmachine/{id}";

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
    public void vendingMachineGetOk(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setName("ten");
        Mockito.when(vendingMachineDao.save(Mockito.any())).thenReturn(vendingMachine);
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        ResponseEntity<VendingMachineGetResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),VendingMachineGetResponse.class,10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.VM_GET_OK.getText(), response.getBody().getMessage());
        assertEquals("ten", response.getBody().getData().getName());
    }

    @Test
    public void vendingMachineGetVendingMachineNull(){
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(null);

        ResponseEntity<VendingMachineGetResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),VendingMachineGetResponse.class,10);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineGetException(){
        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.any());

        ResponseEntity<VendingMachineGetResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),VendingMachineGetResponse.class,10);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_GET_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineGetControllerException(){
        Mockito.doThrow(new RuntimeException()).when(service).getVendingMachine(Mockito.anyInt());

        ResponseEntity<VendingMachineGetResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()),VendingMachineGetResponse.class,10);
        Mockito.doCallRealMethod().when(service).getVendingMachine(Mockito.anyInt());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
