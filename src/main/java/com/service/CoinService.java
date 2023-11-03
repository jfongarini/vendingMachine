package com.service;

import com.dao.CoinDao;
import com.domain.coin.data.*;
import com.domain.coin.request.CoinNewRequest;
import com.domain.coin.request.CoinUpdateRequest;
import com.domain.coin.response.*;
import com.security.JwtService;
import com.util.CommonError;
import com.util.enums.MessagesEnum;
import com.model.Coin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CoinService {

    final static Logger LOGGER = LoggerFactory.getLogger(Coin.class);

    @Autowired
    private CoinDao coinDao;

    @Autowired
    private JwtService jwtService;

    public CoinNewResponse newCoin(CoinNewRequest request, BindingResult result) {
        try {
            if (result.hasErrors()) {
                LOGGER.error("Parameters validation failed. {}.", request);
                return new CoinNewResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.PARAM_VALID_FAIL.getText()).build()).build();
            }
            Coin coin = new Coin();
            coin.setName(request.getName());
            coin.setValue(request.getValue());
            coin.setExist(true);
            coinDao.save(coin);
            CoinNewData data = new CoinNewData();
            data.setName(request.getName());
            data.setValue(request.getValue());
            return new CoinNewResponse.Builder().withData(data).withMessage(MessagesEnum.COIN_NEW_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("New Coin failed.", e);
            return new CoinNewResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.COIN_NEW_FAIL.getText()).build()).build();
        }
    }

    public CoinDeleteResponse deleteCoin(int id) {
        try {
            Optional<Coin> coin = coinDao.findById(id);
            if (coin.isEmpty() || coin.get().getExist().equals("false")){
                return new CoinDeleteResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.COIN_NOT_EXIST.getText()).build()).build();
            }
            coin.get().setExist(false);
            coinDao.save(coin.get());

            CoinDeleteData data = new CoinDeleteData();
            data.setName(coin.get().getName());
            data.setValue(coin.get().getValue());
            return new CoinDeleteResponse.Builder().withData(data).withMessage(MessagesEnum.COIN_DELETE_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("Delete Coin failed.", e);
            return new CoinDeleteResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.COIN_DELETE_FAIL.getText()).build()).build();
        }
    }

    public CoinGetResponse getCoin(int id) {
        try {
            Optional<Coin> coin = coinDao.findById(id);
            if (Objects.isNull(coin)){
                return new CoinGetResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.COIN_NOT_EXIST.getText()).build()).build();
            }
            CoinGetData data = new CoinGetData();
            data.setId(coin.get().getCoinId());
            data.setName(coin.get().getName());
            data.setValue(coin.get().getValue());
            return new CoinGetResponse.Builder().withData(data).withMessage(MessagesEnum.COIN_GET_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("Delete Coin failed.", e);
            return new CoinGetResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.COIN_GET_FAIL.getText()).build()).build();
        }
    }

    public CoinGetAllResponse getAllCoin() {
        try {
            List<Coin> coins = coinDao.findAll();
            if (coins.isEmpty()){
                return new CoinGetAllResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.COIN_LIST_EMPTY.getText()).build()).build();
            }
            List<CoinGetData> coinGetDataList = coins.stream().map(c -> new CoinGetData(c.getCoinId(),c.getName(),c.getValue()))
                    .collect(Collectors.toList());
            CoinGetAllData data = new CoinGetAllData();
            data.setCoinList(coinGetDataList);

            return new CoinGetAllResponse.Builder().withData(data).withMessage(MessagesEnum.COIN_GET_ALL_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("Delete Coin failed.", e);
            return new CoinGetAllResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.COIN_GET_ALL_FAIL.getText()).build()).build();
        }
    }

    public CoinUpdateResponse updateCoin(CoinUpdateRequest request, int id) {
        try {
            if (Objects.isNull(request.getName()) && (Objects.isNull(request.getValue()))){
                return new CoinUpdateResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.COIN_PARAMETERS_FAIL.getText()).build()).build();
            }
            Optional<Coin> coin = coinDao.findById(id);
            if (Objects.isNull(coin)){
                return new CoinUpdateResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.COIN_NOT_EXIST.getText()).build()).build();
            }
            coin.get().setName(Objects.isNull(request.getName())?coin.get().getName():request.getName());
            coin.get().setValue(Objects.isNull(request.getValue())?coin.get().getValue():request.getValue());
            coinDao.save(coin.get());

            CoinUpdateData data = new CoinUpdateData();
            data.setId(coin.get().getCoinId());
            data.setName(coin.get().getName());
            data.setValue(coin.get().getValue());
            return new CoinUpdateResponse.Builder().withData(data).withMessage(MessagesEnum.COIN_UPDATE_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("Delete Coin failed.", e);
            return new CoinUpdateResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.COIN_UPDATE_FAIL.getText()).build()).build();
        }
    }
}
