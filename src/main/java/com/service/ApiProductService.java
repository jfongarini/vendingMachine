package com.service;

import com.domain.fruit.Fruit;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.hibernate.service.spi.ServiceException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ApiProductService {
    public String getFruit(String name) throws ServiceException, JsonProcessingException;
    public List<String> getAllFruits() throws ServiceException, JsonProcessingException;
}
