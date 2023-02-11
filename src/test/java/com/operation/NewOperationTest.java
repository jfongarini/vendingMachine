package com.operation;

import com.dao.OperationDao;
import com.dao.VendingMachineDao;
import com.domain.operation.response.OperationNewResponse;
import com.model.VendingMachine;
import com.service.OperationService;
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
public class NewOperationTest {

    @SpyBean
    private OperationService service;

    @LocalServerPort
    int localServerPort;

    private String URL = "/api/vendingMachine/{id}/operation/new";

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private VendingMachineDao vendingMachineDao;

    @MockBean
    private OperationDao operationDao;

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
    public void operationNewOk(){

        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setId(5);
        vendingMachine.setName("first");
        Mockito.when(vendingMachineDao.findById(Mockito.any())).thenReturn(Optional.of(vendingMachine));
        Mockito.when(operationDao.save(Mockito.any())).thenReturn(null);
        ResponseEntity<OperationNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<Void>(new HttpHeaders()),OperationNewResponse.class,5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().getMessage());
        assertEquals(MessagesEnum.OPERATION_NEW_OK.getText(), response.getBody().getMessage());
    }

    @Test
    public void operationNewVMNull(){

        ResponseEntity<OperationNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<Void>(new HttpHeaders()),OperationNewResponse.class,5);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MessagesEnum.VM_NOT_EXIST.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void coinNewException(){

        Mockito.doThrow(new RuntimeException()).when(vendingMachineDao).findById(Mockito.any());
        ResponseEntity<OperationNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<Void>(new HttpHeaders()),OperationNewResponse.class,5);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(MessagesEnum.OPERATION_NEW_FAIL.getText(), response.getBody().getError().getMessage());
    }

    @Test
    public void operationNewControllerException(){

        Mockito.doThrow(new RuntimeException()).when(service).newOperation(Mockito.anyInt());

        ResponseEntity<OperationNewResponse> response = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<Void>(new HttpHeaders()),OperationNewResponse.class,5);
        Mockito.doCallRealMethod().when(service).newOperation(Mockito.anyInt());

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
