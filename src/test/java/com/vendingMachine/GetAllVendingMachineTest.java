package com.vendingMachine;

import com.dao.VendingMachineDao;
import com.domain.vendingMachine.response.VendingMachineGetAllResponse;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class GetAllVendingMachineTest {

    @SpyBean
    private VendingMachineService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingmachine/all";

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
    public void vendingMachineGetAllOk(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setName("ten");
        List<VendingMachine> vendingMachines = new ArrayList<>();
        vendingMachines.add(vendingMachine);
        Mockito.when(vendingMachineDao.save(Mockito.any())).thenReturn(vendingMachine);
        Mockito.when(vendingMachineDao.findAll()).thenReturn(vendingMachines);

        ResponseEntity<VendingMachineGetAllResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()), VendingMachineGetAllResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.VM_GET_ALL_OK.getText(), response.getBody().getMessage());
    }

    @Test
    public void vendingMachineGetAllVendingMachineListNull(){
        Mockito.when(vendingMachineDao.findAll()).thenReturn(new ArrayList<>());

        ResponseEntity<VendingMachineGetAllResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()), VendingMachineGetAllResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.VM_LIST_EMPTY.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineGetAllException(){
        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findAll();

        ResponseEntity<VendingMachineGetAllResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()), VendingMachineGetAllResponse.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_GET_ALL_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineGetAllControllerException(){
        Mockito.doThrow(new RuntimeException()).when(service).getAllVendingMachine();

        ResponseEntity<VendingMachineGetAllResponse> response = restTemplate.exchange(URL, HttpMethod.GET, new HttpEntity<Void>(new HttpHeaders()), VendingMachineGetAllResponse.class);
        Mockito.doCallRealMethod().when(service).getAllVendingMachine();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
