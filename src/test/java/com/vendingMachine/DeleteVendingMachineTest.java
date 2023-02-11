package com.vendingMachine;

import com.dao.VendingMachineDao;
import com.domain.vendingMachine.response.VendingMachineDeleteResponse;
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
public class DeleteVendingMachineTest {

    @SpyBean
    private VendingMachineService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingmachine/delete/{id}";

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
    public void vendingMachineDeleteOk(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setName("first");
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        ResponseEntity<VendingMachineDeleteResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),VendingMachineDeleteResponse.class,10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.VM_DELETE_OK.getText(), response.getBody().getMessage());
    }

    @Test
    public void vendingMachineDeleteVendingMachineNull(){

        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(null);
        ResponseEntity<VendingMachineDeleteResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),VendingMachineDeleteResponse.class,10);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody().getError().getMessage());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineDeleteException(){
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setName("first");
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));

        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).delete(Mockito.any());
        ResponseEntity<VendingMachineDeleteResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),VendingMachineDeleteResponse.class,11);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.VM_DELETE_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void vendingMachineDeleteControllerException(){

        Mockito.doThrow(new RuntimeException()).when(service).deleteVendingMachine(Mockito.anyInt());

        ResponseEntity<VendingMachineDeleteResponse> response = restTemplate.exchange(URL, HttpMethod.DELETE, new HttpEntity<Void>(new HttpHeaders()),VendingMachineDeleteResponse.class,10);
        Mockito.doCallRealMethod().when(service).deleteVendingMachine(Mockito.anyInt());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
