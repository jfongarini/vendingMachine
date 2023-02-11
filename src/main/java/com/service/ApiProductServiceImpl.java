package com.service;

import com.domain.fruit.Fruit;
import com.domain.fruit.Fruits;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.service.spi.ServiceException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ApiProductServiceImpl implements ApiProductService{

    private static final String URL = "https://www.fruityvice.com/api/fruit/";

    @Autowired
    ConnectionService connectionService;

    @Override
    public String getFruit(String name) throws ServiceException, JsonProcessingException {
        try{
            String newUrl = URL + name;
            String serviceData = connectionService.getData(newUrl);
            JSONObject jsonObject = new JSONObject(serviceData);
            return jsonObject.getString("name");
        } catch (ServiceException | JsonProcessingException se ){
            throw se;
        }
    }

    @Override
    public List<String> getAllFruits() throws ServiceException,JsonProcessingException {
        try{
            String newUrl = URL + "all";
            String serviceData = connectionService.getData(newUrl);
            JSONArray jsonArray = new JSONArray(serviceData);
            List<String> stringList = new ArrayList<>();
            for (Object json : jsonArray) {
                JSONObject jsonObject = (JSONObject) json;
                String name = jsonObject.getString("name");
                stringList.add(name);
            }
            return stringList;
        } catch (ServiceException | JsonProcessingException se ){
            throw se;
        }
    }
}
