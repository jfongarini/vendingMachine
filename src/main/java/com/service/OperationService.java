package com.service;

import com.dao.*;
import com.model.*;
import com.security.JwtService;
import com.util.CommonError;
import com.util.enums.MessagesEnum;
import com.util.enums.StatusEnum;
import com.domain.operation.data.*;
import com.domain.operation.request.OperationAddCoinRequest;
import com.domain.operation.request.OperationAddCoinsRequest;
import com.domain.operation.request.OperationSelectProductRequest;
import com.domain.operation.response.*;
import com.util.enums.UserEnum;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Service
public class OperationService {

    final static Logger LOGGER = LoggerFactory.getLogger(OperationService.class);
    private static final Locale locale = new Locale("en", "US");
    private static final DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
    private static final DecimalFormat df = new DecimalFormat("0.00",symbols);

    @Autowired
    private VendingMachineDao vendingMachineDao;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private CoinDao coinDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private JwtService jwtService;

    public OperationService() {
    }

    public OperationNewResponse newOperation(int id){
        try {

            VendingMachine vendingMachine = vendingMachineDao.findById(id).orElse(null);
            if (Objects.isNull(vendingMachine)) {
                return new OperationNewResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            Operation operation = new Operation();
            operation.setValue(0.0);
            operation.setStatus(StatusEnum.OPEN.name());
            operation.setDate(new Date());

            User user = new User();
            user.setRole(UserEnum.USER.name());
            user.setExpirationDate(DateUtils.addMinutes(Calendar.getInstance().getTime(),30));
            user.setVendingMachine(vendingMachine);
            operation.setUser(user);

            operationDao.save(operation);
            OperationNewData data = new OperationNewData();
            data.setOperation(operation.getOperationId());
            data.setStatus(operation.getStatus());
            data.setToken(jwtService.getToken(user));

            return new OperationNewResponse.Builder().withData(data).withMessage(MessagesEnum.OPERATION_NEW_OK.getText()).build();

        } catch (Exception e) {
            LOGGER.error("New Operation failed.", e);
            return new OperationNewResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.OPERATION_NEW_FAIL.getText()).build()).build();
        }
    }

    public OperationAddCoinsResponse addCoinsOperation(String token, OperationAddCoinsRequest request, BindingResult result){
        try {
            if (result.hasErrors()){
                LOGGER.error("Parameters validation failed. {}.",request);
                return new OperationAddCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).withMessage(MessagesEnum.PARAM_VALID_FAIL.getText()).build()).build();
            }

            int idUser = Integer.parseInt(jwtService.getUserNameFromToken(token));
            User user = userDao.findById(idUser).orElse(null);
            Operation operation = operationDao.findByUser(user).orElse(null);
            if (Objects.isNull(operation)) {
                return new OperationAddCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.OPERATION_NOT_EXIST.getText()).build()).build();
            }

            if (!operation.getStatus().equals(StatusEnum.OPEN.name())) {
                return new OperationAddCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.OPERATION_CLOSED.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(operation.getUser().getVendingMachine().getId()).orElse(null);
            if (Objects.isNull(vendingMachine)) {
                return new OperationAddCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<OperationAddCoinData> operationAddCoinDataList = new ArrayList<>();
            for (OperationAddCoinRequest coinRequest:request.getCoins()) {
                Optional<Coin> coin = coinDao.findByName(coinRequest.getName());
                if (coin.isEmpty()){
                    return new OperationAddCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .withMessage(MessagesEnum.COIN_NOT_EXIST.getText()).build()).build();
                }
                for (int i = 0; i < coinRequest.getQuantity(); i++) {
                    operation.getCoins().add(coin.get());
                }
                OperationAddCoinData operationAddCoinData = new OperationAddCoinData();
                operationAddCoinData.setName(coin.get().getName());
                operationAddCoinData.setValue(coin.get().getValue());
                operationAddCoinData.setQuantity(coinRequest.getQuantity());
                operationAddCoinDataList.add(operationAddCoinData);
            }
            operationDao.save(operation);
            OperationAddCoinsData data = new OperationAddCoinsData();
            data.setOperation(operation.getOperationId());
            data.setCoins(operationAddCoinDataList);
            return new OperationAddCoinsResponse.Builder().withData(data).withMessage(MessagesEnum.OPERATION_INS_COIN_OK.getText()).build();

        } catch (Exception e) {
            LOGGER.error("Insert Coin in Operation failed.", e);
            return new OperationAddCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.OPERATION_INS_COIN_FAIL.getText()).build()).build();
        }
    }

    public OperationGetCoinsResponse getCoinsOperation(String token){
        try{

            int idUser = Integer.parseInt(jwtService.getUserNameFromToken(token));
            User user = userDao.findById(idUser).orElse(null);
            Operation operation = operationDao.findByUser(user).orElse(null);
            if (Objects.isNull(operation)) {
                return new OperationGetCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.OPERATION_NOT_EXIST.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(operation.getUser().getVendingMachine().getId()).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new OperationGetCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<Coin> coinList = coinDao.findAll();
            List<OperationGetCoinData> coins = new ArrayList<>();
            Double totalValue = 0.0;
            for (Coin coin: coinList) {

                long countCoins = operation.getCoins().stream().filter(vmc -> vmc.getName().equals(coin.getName())).count();

                if (countCoins != 0){
                    OperationGetCoinData coinData = new OperationGetCoinData();
                    coinData.setName(coin.getName());
                    coinData.setValue(coin.getValue());
                    coinData.setQuantity(countCoins);
                    coins.add(coinData);
                    totalValue = totalValue + (coinData.getValue() * coinData.getQuantity());
                }
            }

            OperationGetCoinsData data = new OperationGetCoinsData();
            data.setOperation(operation.getOperationId());
            data.setTotalValue(df.format(totalValue));
            data.setCoins(coins);
            return new OperationGetCoinsResponse.Builder().withData(data).withMessage(MessagesEnum.OPERATION_GET_COINS_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Get Coins from Operation failed.",e);
            return new OperationGetCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.OPERATION_GET_COINS_FAIL.getText()).build()).build();
        }
    }

    public OperationSelectProductResponse addProductOperation(String token, OperationSelectProductRequest request, BindingResult result){
        try {
            if (result.hasErrors()){
                LOGGER.error("Parameters validation failed. {}.",request);
                return new OperationSelectProductResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).withMessage(MessagesEnum.PARAM_VALID_FAIL.getText()).build()).build();
            }

            int idUser = Integer.parseInt(jwtService.getUserNameFromToken(token));
            User user = userDao.findById(idUser).orElse(null);
            Operation operation = operationDao.findByUser(user).orElse(null);
            if (Objects.isNull(operation)) {
                return new OperationSelectProductResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.OPERATION_NOT_EXIST.getText()).build()).build();
            }

            if (!operation.getStatus().equals(StatusEnum.OPEN.name())) {
                return new OperationSelectProductResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.OPERATION_CLOSED.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(operation.getUser().getVendingMachine().getId()).orElse(null);
            if (Objects.isNull(vendingMachine)) {
                return new OperationSelectProductResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            Product product = vendingMachine.getProducts().stream()
                    .filter(vm -> vm.getCode().equals(request.getCode())).findFirst().orElse(null);
            if (Objects.isNull(product)){
                return new OperationSelectProductResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.PRODUCT_NOT_EXIST.getText()).build()).build();
            }

            List<Product> products = new ArrayList<>();
            products.add(product);
            operation.setProducts(products);
            operationDao.save(operation);

            OperationSelectProductData data = new OperationSelectProductData();
            data.setOperation(operation.getOperationId());
            data.setCode(product.getCode());
            data.setPrice(product.getPrice());
            data.setName(product.getName());
            return new OperationSelectProductResponse.Builder().withData(data).withMessage(MessagesEnum.OPERATION_INS_PRODUCT_OK.getText()).build();

        } catch (Exception e) {
            LOGGER.error("Insert Product in Operation failed.", e);
            return new OperationSelectProductResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.OPERATION_INS_PRODUCT_FAIL.getText()).build()).build();
        }
    }

    public OperationGetSelectedProductsResponse getProductOperation(String token){
        try{

            int idUser = Integer.parseInt(jwtService.getUserNameFromToken(token));
            User user = userDao.findById(idUser).orElse(null);
            Operation operation = operationDao.findByUser(user).orElse(null);
            if (Objects.isNull(operation)) {
                return new OperationGetSelectedProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.OPERATION_NOT_EXIST.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(operation.getUser().getVendingMachine().getId()).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new OperationGetSelectedProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<Product> productList = productDao.findAll();
            List<OperationGetSelectedProductData> products = new ArrayList<>();
            double totalValue = 0.0;
            for (Product product: productList) {

                long countProducts = operation.getProducts().stream().filter(o -> o.getName().equals(product.getName())).count();

                if (countProducts != 0){
                    OperationGetSelectedProductData productData = new OperationGetSelectedProductData();
                    productData.setName(product.getName());
                    productData.setPrice(product.getPrice());
                    productData.setQuantity(countProducts);
                    productData.setCode(product.getCode());
                    products.add(productData);
                    totalValue = totalValue + (productData.getPrice() * productData.getQuantity());
                }
            }

            OperationGetSelectedProductsData data = new OperationGetSelectedProductsData();
            data.setOperation(operation.getOperationId());
            data.setTotalValue(totalValue);
            data.setProducts(products);
            return new OperationGetSelectedProductsResponse.Builder().withData(data).withMessage(MessagesEnum.OPERATION_GET_PRODUCT_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Get Products from Operation failed.",e);
            return new OperationGetSelectedProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.OPERATION_GET_PRODUCT_FAIL.getText()).build()).build();
        }
    }

    public OperationAcceptResponse acceptOperation(String token){
        try{

            int idUser = Integer.parseInt(jwtService.getUserNameFromToken(token));
            User user = userDao.findById(idUser).orElse(null);
            Operation operation = operationDao.findByUser(user).orElse(null);

            if (Objects.isNull(operation)) {
                return new OperationAcceptResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.OPERATION_NOT_EXIST.getText()).build()).build();
            }

            if (!operation.getStatus().equals(StatusEnum.OPEN.name())) {
                return new OperationAcceptResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.OPERATION_CLOSED.getText()).build()).build();
            }

            if(Objects.isNull(operation.getCoins())){
                return new OperationAcceptResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.OPERATION_INCOMPLETE_COIN.getText()).build()).build();
            }

            if(Objects.isNull(operation.getProducts())){
                return new OperationAcceptResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.OPERATION_INCOMPLETE_PRODUCT.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(operation.getUser().getVendingMachine().getId()).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new OperationAcceptResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            Double totalValueCoin = operation.getCoins().stream().mapToDouble(Coin::getValue).sum();
            Double totalValueProduct = operation.getProducts().stream().mapToDouble(Product::getPrice).sum();
            if (totalValueCoin < totalValueProduct){
                operation.setStatus(StatusEnum.CLOSE_FAIL.name() + ": money not enough");
                operation.setValue(totalValueProduct);
                operationDao.save(operation);
                vendingMachineDao.save(vendingMachine);
                return new OperationAcceptResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.OPERATION_ACCEPT_NOT_ENOUGH.getText()).build()).build();
            }

            Double discountValueProduct = Double.valueOf(df.format(totalValueCoin - totalValueProduct));

            List<Coin> coinsVMSorted = vendingMachine.getCoins().stream()
                    .sorted(Comparator.comparing(Coin::getValue).reversed())
                    .collect(toList());
            List<OperationAcceptCoinData> coinReturnList = new ArrayList<>();
            double moneyReturned = 0.0;
            for (Coin coin: coinsVMSorted){
                if (coin.getValue() <= discountValueProduct){
                    discountValueProduct = discountValueProduct - coin.getValue();
                    long countCoin = coinReturnList.stream().filter(c -> c.getName().equals(coin.getName())).count();
                    OperationAcceptCoinData coinReturn = new OperationAcceptCoinData();
                    coinReturn.setName(coin.getName());
                    coinReturn.setValue(coin.getValue());
                    coinReturn.setQuantity(countCoin + 1);
                    coinReturnList.add(coinReturn);
                    moneyReturned = moneyReturned + coin.getValue();
                }
            }
            List<OperationAcceptProductData> productReturnList = new ArrayList<>();
            for (Product product: operation.getProducts()){
                OperationAcceptProductData productData = productReturnList.stream().filter(p -> p.getCode().equals(product.getCode())).findFirst().orElse(null);

                if (Objects.isNull(productData)){
                    OperationAcceptProductData productReturn = new OperationAcceptProductData();
                    productReturn.setCode(product.getCode());
                    productReturn.setName(product.getName());
                    productReturn.setPrice(product.getPrice());
                    productReturn.setQuantity(1);
                    productReturnList.add(productReturn);
                }  else {
                    productData.setQuantity(productData.getQuantity() + 1);
                    productReturnList.set(productReturnList.indexOf(productData), productData);
                }
                vendingMachine.getProducts().remove(product);
            }

            for (Coin coin:operation.getCoins()){
                vendingMachine.getCoins().add(coin);
            }

            operation.setStatus(StatusEnum.CLOSE_OK.name());
            operation.setValue(totalValueProduct);
            operationDao.save(operation);
            vendingMachineDao.save(vendingMachine);

            OperationAcceptData data = new OperationAcceptData();
            data.setOperation(operation.getOperationId());
            data.setStatus(operation.getStatus());
            data.setValue(operation.getValue());
            data.setMoneyAdded(df.format(totalValueCoin));
            data.setNumberOfProducts(operation.getProducts().size());
            data.setProducts(productReturnList);
            data.setMoneyReturned(moneyReturned);
            data.setCoinsReturned(coinReturnList);
            return new OperationAcceptResponse.Builder().withData(data).withMessage(MessagesEnum.OPERATION_ACCEPT_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Accept Operation failed.",e);
            return new OperationAcceptResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.OPERATION_GET_PRODUCT_FAIL.getText()).build()).build();
        }
    }

    public OperationCancelResponse cancelOperation(String token){
        try{

            int idUser = Integer.parseInt(jwtService.getUserNameFromToken(token));
            User user = userDao.findById(idUser).orElse(null);
            Operation operation = operationDao.findByUser(user).orElse(null);
            if (Objects.isNull(operation)) {
                return new OperationCancelResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.OPERATION_NOT_EXIST.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(operation.getUser().getVendingMachine().getId()).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new OperationCancelResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            Double totalValueProduct = operation.getProducts().stream().mapToDouble(Product::getPrice).sum();
            operation.setStatus(StatusEnum.CANCELED.name());
            operation.setValue(totalValueProduct);
            operationDao.save(operation);
            vendingMachineDao.save(vendingMachine);

            OperationCancelData data = new OperationCancelData();
            data.setOperation(operation.getOperationId());
            data.setStatus(operation.getStatus());
            return new OperationCancelResponse.Builder().withData(data).withMessage(MessagesEnum.OPERATION_CANCEL_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Cancel Operation failed.",e);
            return new OperationCancelResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.OPERATION_CANCEL_FAIL.getText()).build()).build();
        }
    }

}
