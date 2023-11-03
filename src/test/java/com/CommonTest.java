package com;

import com.dao.UserDao;
import com.dao.VendingMachineDao;
import com.model.User;
import com.model.VendingMachine;
import com.security.JwtService;
import com.util.enums.UserEnum;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

public class CommonTest {

    @Autowired
    private VendingMachineDao vendingMachineDao;

    @Autowired
    private UserDao userDao;

    @MockBean
    private JwtService jwtService;

    public HttpEntity getAdmin(){
        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        VendingMachine vm = new VendingMachine();
        vm.setExist(true);
        vm.setName("vm");
        vendingMachineDao.save(vm);

        User user = new User();
        user.setRole(UserEnum.ADMIN.name());
        user.setVendingMachine(vm);
        userDao.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");

        return new HttpEntity(headers);
    }

    public HttpEntity getAdmin(Object request){
        Mockito.when(jwtService.getUserNameFromToken(Mockito.anyString())).thenReturn("1");
        Mockito.when(jwtService.isTokenValid(Mockito.anyString(),Mockito.any())).thenReturn(true);
        VendingMachine vm = new VendingMachine();
        vm.setExist(true);
        vm.setName("vm");
        vendingMachineDao.save(vm);

        User user = new User();
        user.setRole(UserEnum.ADMIN.name());
        user.setVendingMachine(vm);
        userDao.save(user);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer 123");
        return new HttpEntity<>(request,headers);
    }

}
