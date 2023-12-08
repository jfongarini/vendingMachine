package com.service;

import com.dao.*;
import com.domain.vendingMachine.coin.data.*;
import com.domain.vendingMachine.coin.request.VmCoinRequest;
import com.domain.vendingMachine.coin.request.VmInsertCoinsRequest;
import com.domain.vendingMachine.coin.response.VmExtractCoinsResponse;
import com.domain.vendingMachine.coin.response.VmGetCoinsResponse;
import com.domain.vendingMachine.coin.response.VmInsertCoinsResponse;
import com.domain.vendingMachine.operation.data.VmGetOperationCoinData;
import com.domain.vendingMachine.operation.data.VmGetOperationData;
import com.domain.vendingMachine.operation.data.VmGetOperationProductData;
import com.domain.vendingMachine.operation.data.VmGetOperationsData;
import com.domain.vendingMachine.operation.response.VmGetOperationResponse;
import com.domain.vendingMachine.operation.response.VmGetOperationsResponse;
import com.domain.vendingMachine.product.data.*;
import com.domain.vendingMachine.product.request.VmInsertProductsRequest;
import com.domain.vendingMachine.product.request.VmProductRequest;
import com.domain.vendingMachine.product.response.VmExtractProductsResponse;
import com.domain.vendingMachine.product.response.VmGetProductsResponse;
import com.domain.vendingMachine.product.response.VmInsertProductsResponse;
import com.model.*;
import com.security.JwtService;
import com.util.CommonError;
import com.domain.vendingMachine.data.*;
import com.util.enums.MessagesEnum;
import com.domain.vendingMachine.request.*;
import com.domain.vendingMachine.response.*;
import com.util.enums.StatusEnum;
import com.util.enums.UserEnum;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VendingMachineService {

    final static Logger LOGGER = LoggerFactory.getLogger(VendingMachine.class);
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private VendingMachineDao vendingMachineDao;

    @Autowired
    private CoinDao coinDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private OperationDao operationDao;

    @Autowired
    private JwtService jwtService;

    public VendingMachineLoginResponse loginVendingMachine(int id){
        try{
            VendingMachine vendingMachine = vendingMachineDao.findById(id).orElse(null);
            if (Objects.isNull(vendingMachine)) {
                return new VendingMachineLoginResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            User user = new User();
            user.setRole(UserEnum.ADMIN.name());
            user.setExpirationDate(DateUtils.addMinutes(Calendar.getInstance().getTime(),30));
            user.setVendingMachine(vendingMachine);
            userDao.save(user);

            VendingMachineLoginData data = new VendingMachineLoginData();
            data.setName(vendingMachine.getName());
            data.setToken(jwtService.getToken(user));
            return new VendingMachineLoginResponse.Builder().withData(data).withMessage(MessagesEnum.VM_LOGIN_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Login Vending Machine failed.",e);
            return new VendingMachineLoginResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value()).withMessage(MessagesEnum.VM_LOGIN_FAIL.getText()).build()).build();
        }
    }

    public VendingMachineNewResponse newVendingMachine(VendingMachineNewRequest request, BindingResult result){
        try{
            if (result.hasErrors()){
                LOGGER.error("Parameters validation failed. {}.",request);
                return new VendingMachineNewResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).withMessage(MessagesEnum.PARAM_VALID_FAIL.getText()).build()).build();
            }

            VendingMachine vendingMachine = new VendingMachine();
            vendingMachine.setName(request.getName());
            vendingMachine.setExist(true);
            vendingMachineDao.save(vendingMachine);

            User user = new User();
            user.setRole(UserEnum.ADMIN.name());
            user.setExpirationDate(DateUtils.addMinutes(Calendar.getInstance().getTime(),30));
            user.setVendingMachine(vendingMachine);
            userDao.save(user);

            VendingMachineNewData data = new VendingMachineNewData();
            data.setName(request.getName());
            data.setToken(jwtService.getToken(user));
            return new VendingMachineNewResponse.Builder().withData(data).withMessage(MessagesEnum.VM_NEW_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("New Vending Machine failed.",e);
            return new VendingMachineNewResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value()).withMessage(MessagesEnum.VM_NEW_FAIL.getText()).build()).build();
        }
    }

    public VendingMachineDeleteResponse deleteVendingMachine(int id) {
        try {
            Optional<VendingMachine> vendingMachine = vendingMachineDao.findById(id);
            if (vendingMachine.isEmpty()){
                return new VendingMachineDeleteResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }
            vendingMachine.get().setExist(false);
            vendingMachineDao.save(vendingMachine.get());
            VendingMachineDeleteData data = new VendingMachineDeleteData();
            data.setName(vendingMachine.get().getName());
            return new VendingMachineDeleteResponse.Builder().withData(data).withMessage(MessagesEnum.VM_DELETE_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("Delete Vending Machine failed.", e);
            return new VendingMachineDeleteResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VM_DELETE_FAIL.getText()).build()).build();
        }
    }

    public VendingMachineGetResponse getVendingMachine(int id) {
        try {
            Optional<VendingMachine> vendingMachine = vendingMachineDao.findById(id);
            if (vendingMachine.isEmpty()){
                return new VendingMachineGetResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }
            VendingMachineGetData data = new VendingMachineGetData();
            data.setId(vendingMachine.get().getId());
            data.setName(vendingMachine.get().getName());
            return new VendingMachineGetResponse.Builder().withData(data).withMessage(MessagesEnum.VM_GET_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("Delete Vending Machine failed.", e);
            return new VendingMachineGetResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VM_GET_FAIL.getText()).build()).build();
        }
    }

    public VendingMachineGetAllResponse getAllVendingMachine() {
        try {
            List<VendingMachine> vendingMachines = vendingMachineDao.findAll();
            if (vendingMachines.isEmpty()){
                return new VendingMachineGetAllResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_LIST_EMPTY.getText()).build()).build();
            }
            List<VendingMachineGetData> vendingMachineGetDataList = vendingMachines.stream().map(c -> new VendingMachineGetData(c.getId(),c.getName()))
                    .collect(Collectors.toList());
            VendingMachineGetAllData data = new VendingMachineGetAllData();
            data.setVendingMachineList(vendingMachineGetDataList);

            return new VendingMachineGetAllResponse.Builder().withData(data).withMessage(MessagesEnum.VM_GET_ALL_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("Delete Vending Machine failed.", e);
            return new VendingMachineGetAllResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VM_GET_ALL_FAIL.getText()).build()).build();
        }
    }

    public VendingMachineUpdateResponse updateVendingMachine(VendingMachineUpdateRequest request, int id) {
        try {
            if (Objects.isNull(request.getName())){
                return new VendingMachineUpdateResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_PARAMETERS_FAIL.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(id).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VendingMachineUpdateResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }
            vendingMachine.setName(request.getName());
            vendingMachineDao.save(vendingMachine);

            VendingMachineUpdateData data = new VendingMachineUpdateData();
            data.setId(vendingMachine.getId());
            data.setName(vendingMachine.getName());
            return new VendingMachineUpdateResponse.Builder().withData(data).withMessage(MessagesEnum.VM_UPDATE_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("Delete VendingMachine failed.", e);
            return new VendingMachineUpdateResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VM_UPDATE_FAIL.getText()).build()).build();
        }
    }

    //************************
    //vending-machine Coins
    //************************

    public VmInsertCoinsResponse insertCoinsVendingMachine(String token, VmInsertCoinsRequest request, BindingResult result){
        try{
            if (result.hasErrors()){
                LOGGER.error("Parameters validation failed. {}.",request);
                return new VmInsertCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).withMessage(MessagesEnum.PARAM_VALID_FAIL.getText()).build()).build();
            }

            int idUser = Integer.parseInt(jwtService.getUserNameFromToken(token));
            User user = userDao.findById(idUser).orElse(null);
            if (Objects.isNull(user)) {
                return new VmInsertCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.USER_NOT_EXIST.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(user.getVendingMachine().getId()).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmInsertCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<VmInsertCoinData> vmInsertCoinDataList = new ArrayList<>();
            List<Coin> coinList = new ArrayList<>();
            BigDecimal coinValue = BigDecimal.ZERO;
            for (VmCoinRequest coinRequest:request.getCoins()) {
                Optional<Coin> coin = coinDao.findByName(coinRequest.getName());
                if (coin.isEmpty()){
                    return new VmInsertCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .withMessage(MessagesEnum.COIN_NOT_EXIST.getText()).build()).build();
                }
                for (int i = 0; i < coinRequest.getQuantity(); i++) {
                    vendingMachine.getCoins().add(coin.get());
                    coinList.add(coin.get());
                }
                VmInsertCoinData vmInsertCoinData = new VmInsertCoinData();
                vmInsertCoinData.setName(coin.get().getName());
                vmInsertCoinData.setValue(coin.get().getValue());
                vmInsertCoinData.setQuantity(coinRequest.getQuantity());
                vmInsertCoinDataList.add(vmInsertCoinData);
                coinValue = coinValue.add(coin.get().getValue().multiply(BigDecimal.valueOf(coinRequest.getQuantity())));
            }

            vendingMachineDao.save(vendingMachine);

            Operation operation = new Operation();
            operation.setValue(coinValue);
            operation.setStatus(StatusEnum.INSERT_COINS.name());
            operation.setDate(new Date());
            operation.setUser(user);
            operation.setCoins(coinList);
            operationDao.save(operation);

            VmInsertCoinsData data = new VmInsertCoinsData();
            data.setVmName(vendingMachine.getName());
            data.setInsertedCoins(vmInsertCoinDataList);
            return new VmInsertCoinsResponse.Builder().withData(data).withMessage(MessagesEnum.VMC_POST_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Insert Coin in Vending Machine failed.",e);
            return new VmInsertCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VMC_POST_FAIL.getText()).build()).build();
        }
    }

    public VmGetCoinsResponse getCoinsVendingMachine(String token){
        try{

            int idUser = Integer.parseInt(jwtService.getUserNameFromToken(token));
            User user = userDao.findById(idUser).orElse(null);
            if (Objects.isNull(user)) {
                return new VmGetCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.USER_NOT_EXIST.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(user.getVendingMachine().getId()).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmGetCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<Coin> coinList = coinDao.findAll();
            List<VmGetCoinData> coins = new ArrayList<>();
            long totalCoins = 0L;
            BigDecimal totalValue = BigDecimal.ZERO;
            for (Coin coin: coinList) {

                long countCoins = vendingMachine.getCoins().stream().filter(vmc -> vmc.getName().equals(coin.getName())).count();

                if (countCoins != 0){
                    VmGetCoinData coinData = new VmGetCoinData();
                    coinData.setName(coin.getName());
                    coinData.setValue(coin.getValue());
                    coinData.setQuantity(countCoins);
                    coins.add(coinData);
                    totalCoins = totalCoins + countCoins;
                    totalValue = totalValue.add(coinData.getValue().multiply(BigDecimal.valueOf(countCoins)));
                }
            }

            VmGetCoinsData data = new VmGetCoinsData();
            data.setVmName(vendingMachine.getName());
            data.setTotalCoins(totalCoins);
            data.setTotalValue(df.format(totalValue));
            data.setCoins(coins);
            return new VmGetCoinsResponse.Builder().withData(data).withMessage(MessagesEnum.VMC_GET_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Get Coin from Vending Machine failed.",e);
            return new VmGetCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VMC_GET_FAIL.getText()).build()).build();
        }
    }

    public VmExtractCoinsResponse extractCoinsVendingMachine(String token){
        try{
            int idUser = Integer.parseInt(jwtService.getUserNameFromToken(token));
            User user = userDao.findById(idUser).orElse(null);
            if (Objects.isNull(user)) {
                return new VmExtractCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.USER_NOT_EXIST.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(user.getVendingMachine().getId()).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmExtractCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<Coin> coinList = coinDao.findAll();
            List<VmExtractCoinData> coins = new ArrayList<>();
            BigDecimal coinValue = BigDecimal.ZERO;
            List<Coin> operationCoins = vendingMachine.getCoins();
            for (Coin coin: coinList) {

                long countCoins = vendingMachine.getCoins().stream().filter(vmc -> vmc.getName().equals(coin.getName())).count();

                if (countCoins != 0){
                    VmExtractCoinData coinData = new VmExtractCoinData();
                    coinData.setName(coin.getName());
                    coinData.setValue(coin.getValue());
                    coinData.setQuantityToDelete(countCoins);
                    coinData.setOldQuantity(countCoins);
                    coinData.setNewQuantity(0L);
                    coins.add(coinData);
                    coinValue = coinValue.add(coin.getValue().multiply(BigDecimal.valueOf(countCoins)));
                }
            }

            vendingMachine.setCoins(new ArrayList<>());
            vendingMachineDao.save(vendingMachine);

            Operation operation = new Operation();
            operation.setValue(coinValue);
            operation.setStatus(StatusEnum.EXTRACT_COINS.name());
            operation.setDate(new Date());
            operation.setUser(user);
            operation.setCoins(operationCoins);
            operationDao.save(operation);

            VmExtractCoinsData data = new VmExtractCoinsData();
            data.setVmName(vendingMachine.getName());
            data.setDeletedCoins(coins);
            return new VmExtractCoinsResponse.Builder().withData(data).withMessage(MessagesEnum.VMC_DELETE_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Extract all Coins from Vending Machine failed.",e);
            return new VmExtractCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VMC_DELETE_FAIL.getText()).build()).build();
        }
    }

    //************************
    //vending-machine Products
    //************************

    public VmInsertProductsResponse insertProductsVendingMachine(String token, VmInsertProductsRequest request, BindingResult result){
        try{
            if (result.hasErrors()){
                LOGGER.error("Parameters validation failed. {}.",request);
                return new VmInsertProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).withMessage(MessagesEnum.PARAM_VALID_FAIL.getText()).build()).build();
            }

            int idUser = Integer.parseInt(jwtService.getUserNameFromToken(token));
            User user = userDao.findById(idUser).orElse(null);
            if (Objects.isNull(user)) {
                return new VmInsertProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.USER_NOT_EXIST.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(user.getVendingMachine().getId()).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmInsertProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<VmInsertProductData> vmInsertProductDataList = new ArrayList<>();
            List<Product> productList = new ArrayList<>();
            BigDecimal productValue = BigDecimal.ZERO;
            for (VmProductRequest productRequest:request.getProducts()) {
                Optional<Product> product = productDao.findByName(productRequest.getName());
                if (product.isEmpty()){
                    return new VmInsertProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .withMessage(MessagesEnum.PRODUCT_NOT_EXIST.getText()).build()).build();
                }
                for (int i = 0; i < productRequest.getQuantity(); i++) {
                    vendingMachine.getProducts().add(product.get());
                    productList.add(product.get());
                }
                VmInsertProductData vmInsertProductData = new VmInsertProductData();
                vmInsertProductData.setName(product.get().getName());
                vmInsertProductData.setPrice(product.get().getPrice());
                vmInsertProductData.setCode(product.get().getCode());
                vmInsertProductData.setQuantity(productRequest.getQuantity());
                vmInsertProductDataList.add(vmInsertProductData);
                productValue = productValue.add(product.get().getPrice());
            }

            vendingMachineDao.save(vendingMachine);

            Operation operation = new Operation();
            operation.setValue(productValue);
            operation.setStatus(StatusEnum.INSERT_PRODUCTS.name());
            operation.setDate(new Date());
            operation.setUser(user);
            operation.setProducts(productList);
            operationDao.save(operation);

            VmInsertProductsData data = new VmInsertProductsData();
            data.setVmName(vendingMachine.getName());
            data.setInsertedProducts(vmInsertProductDataList);
            return new VmInsertProductsResponse.Builder().withData(data).withMessage(MessagesEnum.VMP_POST_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Insert Product in Vending Machine failed.",e);
            return new VmInsertProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VMP_POST_FAIL.getText()).build()).build();
        }
    }

    public VmGetProductsResponse getProductsVendingMachine(String token){
        try{

            int idUser = Integer.parseInt(jwtService.getUserNameFromToken(token));
            User user = userDao.findById(idUser).orElse(null);
            if (Objects.isNull(user)) {
                return new VmGetProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.USER_NOT_EXIST.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(user.getVendingMachine().getId()).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmGetProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<Product> productList = productDao.findAll();
            List<VmGetProductData> products = new ArrayList<>();
            long quantity = 0L;
            BigDecimal totalValue = BigDecimal.ZERO;
            for (Product product: productList) {

                long countProducts = vendingMachine.getProducts().stream().filter(vmp -> vmp.getName().equals(product.getName())).count();

                if (countProducts != 0){
                    VmGetProductData productData = new VmGetProductData();
                    productData.setName(product.getName());
                    productData.setPrice(product.getPrice());
                    productData.setCode(product.getCode());
                    productData.setQuantity(countProducts);
                    products.add(productData);
                    quantity = quantity + countProducts;
                    totalValue = totalValue.add(productData.getPrice().multiply(BigDecimal.valueOf(countProducts)));
                }
            }

            VmGetProductsData data = new VmGetProductsData();
            data.setVmName(vendingMachine.getName());
            data.setProducts(products);
            data.setQuantity(quantity);
            data.setTotalValue(df.format(totalValue));
            return new VmGetProductsResponse.Builder().withData(data).withMessage(MessagesEnum.VMP_GET_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Get Product from Vending Machine failed.",e);
            return new VmGetProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VMP_GET_FAIL.getText()).build()).build();
        }
    }

    public VmExtractProductsResponse extractProductsVendingMachine(String token){
        try{

            int idUser = Integer.parseInt(jwtService.getUserNameFromToken(token));
            User user = userDao.findById(idUser).orElse(null);
            if (Objects.isNull(user)) {
                return new VmExtractProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.USER_NOT_EXIST.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(user.getVendingMachine().getId()).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmExtractProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<Product> productList = productDao.findAll();
            List<VmExtractProductData> products = new ArrayList<>();
            BigDecimal productValue = BigDecimal.ZERO;
            List<Product> operationProducts = vendingMachine.getProducts();
            for (Product product: productList) {

                long countProducts = vendingMachine.getProducts().stream().filter(vmp -> vmp.getName().equals(product.getName())).count();

                if (countProducts != 0){
                    VmExtractProductData productData = new VmExtractProductData();
                    productData.setName(product.getName());
                    productData.setCode(product.getCode());
                    productData.setPrice(product.getPrice());
                    productData.setQuantityToDelete(countProducts);
                    productData.setOldQuantity(countProducts);
                    productData.setNewQuantity(0L);
                    products.add(productData);
                    productValue = productValue.add(product.getPrice().multiply(BigDecimal.valueOf(countProducts)));
                }
            }

            List<Product> newProductList = new ArrayList<>();
            vendingMachine.setProducts(newProductList);
            vendingMachineDao.save(vendingMachine);

            Operation operation = new Operation();
            operation.setValue(productValue);
            operation.setStatus(StatusEnum.EXTRACT_PRODUCTS.name());
            operation.setDate(new Date());
            operation.setUser(user);
            operation.setProducts(operationProducts);
            operationDao.save(operation);

            VmExtractProductsData data = new VmExtractProductsData();
            data.setVmName(vendingMachine.getName());
            data.setDeletedProducts(products);
            return new VmExtractProductsResponse.Builder().withData(data).withMessage(MessagesEnum.VMP_DELETE_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Extract all Coins from Vending Machine failed.",e);
            return new VmExtractProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VMP_DELETE_FAIL.getText()).build()).build();
        }
    }

    //************************
    //vending-machine Operations
    //************************

    public VmGetOperationsResponse getOperationsVendingMachine(String token, String from, String to){
        try{

            int idUser = Integer.parseInt(jwtService.getUserNameFromToken(token));
            User user = userDao.findById(idUser).orElse(null);
            if (Objects.isNull(user)) {
                return new VmGetOperationsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.USER_NOT_EXIST.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(user.getVendingMachine().getId()).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmGetOperationsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<Operation> operations;
            if (Objects.isNull(to) && Objects.isNull(from)){
                operations = operationDao.findAll();
            } else {
                Date dateFrom= null;
                Date dateTo= null;
                if (Objects.nonNull(from)){
                    dateFrom=new SimpleDateFormat("dd/MM/yyyy").parse(from);
                } else {
                    dateTo=new SimpleDateFormat("dd/MM/yyyy").parse(to);
                }
                operations = operationDao.getAllBetweenDates(dateFrom,dateTo);
            }

            BigDecimal totalValue = BigDecimal.ZERO;
            List<VmGetOperationData> operationDataList = new ArrayList<>();
            for (Operation operation: operations) {
                VmGetOperationData vmGetOperationData = new VmGetOperationData();
                vmGetOperationData.setVendingMachine(user.getVendingMachine().getId());
                vmGetOperationData.setOperationId(operation.getOperationId());

                vmGetOperationData.setDate(sdf.format(operation.getDate()));
                vmGetOperationData.setValue(operation.getValue());
                vmGetOperationData.setStatus(operation.getStatus());
                List<VmGetOperationCoinData> vmGetOperationCoinDataList = new ArrayList<>();
                for (Coin coin: operation.getCoins()){
                    VmGetOperationCoinData vmGetOperationCoinData = new VmGetOperationCoinData();
                    vmGetOperationCoinData.setName(coin.getName());
                    vmGetOperationCoinData.setValue(coin.getValue());
                    vmGetOperationCoinDataList.add(vmGetOperationCoinData);
                }
                vmGetOperationData.setCoins(vmGetOperationCoinDataList);
                List<VmGetOperationProductData> vmGetOperationProductDataList = new ArrayList<>();
                for (Product product: operation.getProducts()){
                    VmGetOperationProductData vmGetOperationProductData = new VmGetOperationProductData();
                    vmGetOperationProductData.setName(product.getName());
                    vmGetOperationProductData.setPrice(product.getPrice());
                    vmGetOperationProductData.setCode(product.getCode());
                    vmGetOperationProductDataList.add(vmGetOperationProductData);
                }
                vmGetOperationData.setProducts(vmGetOperationProductDataList);
                operationDataList.add(vmGetOperationData);
                totalValue = totalValue.add(operation.getValue());
            }

            VmGetOperationsData data = new VmGetOperationsData();
            data.setVendingMachine(vendingMachine.getId());
            data.setTotalValue(df.format(totalValue));
            data.setOperations(operationDataList);
            return new VmGetOperationsResponse.Builder().withData(data).withMessage(MessagesEnum.VMO_GET_ALL_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Get Operations from Vending Machine failed.",e);
            return new VmGetOperationsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VMO_GET_ALL_FAIL.getText()).build()).build();
        }
    }

    public VmGetOperationResponse getOperationVendingMachine(String token, int operationId){
        try{

            int idUser = Integer.parseInt(jwtService.getUserNameFromToken(token));
            User user = userDao.findById(idUser).orElse(null);
            if (Objects.isNull(user)) {
                return new VmGetOperationResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.USER_NOT_EXIST.getText()).build()).build();
            }

            Optional<Operation> operation = operationDao.findById(operationId);
            if (operation.isEmpty()){
                return new VmGetOperationResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VMO_OPERATION_NOT_EXIST.getText()).build()).build();
            }

            List<VmGetOperationCoinData> vmGetOperationCoinDataList = new ArrayList<>();
            for (Coin coin: operation.get().getCoins()){
                VmGetOperationCoinData vmGetOperationCoinData = new VmGetOperationCoinData();
                vmGetOperationCoinData.setName(coin.getName());
                vmGetOperationCoinData.setValue(coin.getValue());
                vmGetOperationCoinDataList.add(vmGetOperationCoinData);
            }

            List<VmGetOperationProductData> vmGetOperationProductDataList = new ArrayList<>();
            for (Product product: operation.get().getProducts()){
                VmGetOperationProductData vmGetOperationProductData = new VmGetOperationProductData();
                vmGetOperationProductData.setName(product.getName());
                vmGetOperationProductData.setPrice(product.getPrice());
                vmGetOperationProductData.setCode(product.getCode());
                vmGetOperationProductDataList.add(vmGetOperationProductData);
            }

            VmGetOperationData data = new VmGetOperationData();
            data.setDate(sdf.format(operation.get().getDate()));
            data.setVendingMachine(user.getVendingMachine().getId());
            data.setStatus(operation.get().getStatus());
            data.setOperationId(operation.get().getOperationId());
            data.setCoins(vmGetOperationCoinDataList);
            data.setProducts(vmGetOperationProductDataList);
            data.setValue(operation.get().getValue());

            return new VmGetOperationResponse.Builder().withData(data).withMessage(MessagesEnum.VMO_GET_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Get Operation from Vending Machine failed.",e);
            return new VmGetOperationResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VMO_GET_FAIL.getText()).build()).build();
        }
    }
}
