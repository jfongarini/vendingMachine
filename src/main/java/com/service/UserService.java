package com.service;

import com.dao.UserDao;
import com.domain.user.data.UserData;
import com.domain.user.response.UserResponse;
import com.model.User;
import com.security.JwtService;
import com.util.enums.UserEnum;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserService {

    final static Logger LOGGER = LoggerFactory.getLogger(User.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtService jwtService;

    public UserResponse loginUser(){
         User user = new User();
         user.setRole(UserEnum.USER.name());
         user.setExpirationDate(DateUtils.addMinutes(Calendar.getInstance().getTime(),30));
         userDao.save(user);

        UserData data = new UserData();
        //data.setToken(jwtService.getToken(user));

        return new UserResponse.Builder().withData(data).withMessage("Token ok").build();
    }
}
