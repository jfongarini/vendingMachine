package com.service;

import com.dao.CoinDao;
import com.dao.OperationDao;
import com.dao.ProductDao;
import com.dao.VendingMachineDao;
import com.util.CommonError;
import com.domain.vendingMachine.data.*;
import com.util.enums.MessagesEnum;
import com.domain.vendingMachine.request.*;
import com.domain.vendingMachine.response.*;
import com.model.Coin;
import com.model.Operation;
import com.model.Product;
import com.model.VendingMachine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class VendingMachineService {

    final static Logger LOGGER = LoggerFactory.getLogger(VendingMachine.class);
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Autowired
    private VendingMachineDao vendingMachineDao;

    @Autowired
    private CoinDao coinDao;

    @Autowired
    private ProductDao productDao;

    @Autowired
    private OperationDao operationDao;

    public VendingMachineNewResponse newVendingMachine(VendingMachineNewRequest request, BindingResult result){
        try{
            if (result.hasErrors()){
                LOGGER.error("Parameters validation failed. {}.",request);
                return new VendingMachineNewResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).withMessage(MessagesEnum.PARAM_VALID_FAIL.getText()).build()).build();
            }
            VendingMachine vendingMachine = new VendingMachine();
            vendingMachine.setName(request.getName());
            vendingMachineDao.save(vendingMachine);
            VendingMachineNewData data = new VendingMachineNewData();
            data.setName(request.getName());
            return new VendingMachineNewResponse.Builder().withData(data).withMessage(MessagesEnum.VM_NEW_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("New Vending Machine failed.",e);
            return new VendingMachineNewResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value()).withMessage(MessagesEnum.VM_NEW_FAIL.getText()).build()).build();
        }
    }

    public VendingMachineDeleteResponse deleteVendingMachine(int id) {
        try {
            Optional<VendingMachine> vendingMachine = vendingMachineDao.findById(id);
            if (Objects.isNull(vendingMachine)){
                return new VendingMachineDeleteResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }
            VendingMachineDeleteData data = new VendingMachineDeleteData();
            data.setName(vendingMachine.get().getName());
            vendingMachineDao.delete(vendingMachine.get());
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
            if (Objects.isNull(vendingMachine)){
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
            Optional<VendingMachine> vendingMachine = vendingMachineDao.findById(id);
            if (Objects.isNull(vendingMachine)){
                return new VendingMachineUpdateResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }
            vendingMachine.get().setName(request.getName());
            vendingMachineDao.save(vendingMachine.get());

            VendingMachineUpdateData data = new VendingMachineUpdateData();
            data.setId(vendingMachine.get().getId());
            data.setName(vendingMachine.get().getName());
            return new VendingMachineUpdateResponse.Builder().withData(data).withMessage(MessagesEnum.VM_UPDATE_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("Delete VendingMachine failed.", e);
            return new VendingMachineUpdateResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VM_UPDATE_FAIL.getText()).build()).build();
        }
    }

    //************************
    //vendingmachine Coins
    //************************

    public VmInsertCoinsResponse insertCoinsVendingMachine(int id, VmInsertCoinsRequest request, BindingResult result){
        try{
            if (result.hasErrors()){
                LOGGER.error("Parameters validation failed. {}.",request);
                return new VmInsertCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).withMessage(MessagesEnum.PARAM_VALID_FAIL.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(id).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmInsertCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<VmInsertCoinData> vmInsertCoinDataList = new ArrayList<>();
            for (VmCoinRequest coinRequest:request.getCoins()) {
                Optional<Coin> coin = coinDao.findByName(coinRequest.getName());
                if (coin.isEmpty()){
                    return new VmInsertCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .withMessage(MessagesEnum.COIN_NOT_EXIST.getText()).build()).build();
                }
                for (int i = 0; i < coinRequest.getQuantity(); i++) {
                    vendingMachine.getCoins().add(coin.get());
                }
                VmInsertCoinData vmInsertCoinData = new VmInsertCoinData();
                vmInsertCoinData.setName(coin.get().getName());
                vmInsertCoinData.setValue(coin.get().getValue());
                vmInsertCoinData.setQuantity(coinRequest.getQuantity());
                vmInsertCoinDataList.add(vmInsertCoinData);
            }

            vendingMachineDao.save(vendingMachine);

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

    public VmGetCoinsResponse getCoinsVendingMachine(int id){
        try{

            VendingMachine vendingMachine = vendingMachineDao.findById(id).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmGetCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<Coin> coinList = coinDao.findAll();
            List<VmGetCoinData> coins = new ArrayList<>();
            Long totalCoins = 0L;
            Double totalValue = 0.0;
            for (Coin coin: coinList) {

                Long countCoins = vendingMachine.getCoins().stream().filter(vmc -> vmc.getName().equals(coin.getName())).count();

                if (countCoins != 0){
                    VmGetCoinData coinData = new VmGetCoinData();
                    coinData.setName(coin.getName());
                    coinData.setValue(coin.getValue());
                    coinData.setQuantity(countCoins);
                    coins.add(coinData);
                    totalCoins = totalCoins + countCoins;
                    totalValue = totalValue + (coinData.getValue() * countCoins);
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

    public VmExtractCoinsResponse extractCoinsVendingMachine(int id){
        try{

            VendingMachine vendingMachine = vendingMachineDao.findById(id).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmExtractCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<Coin> coinList = coinDao.findAll();
            List<VmExtractCoinData> coins = new ArrayList<>();
            for (Coin coin: coinList) {

                Long countCoins = vendingMachine.getCoins().stream().filter(vmc -> vmc.getName().equals(coin.getName())).count();

                if (countCoins != 0){
                    VmExtractCoinData coinData = new VmExtractCoinData();
                    coinData.setName(coin.getName());
                    coinData.setValue(coin.getValue());
                    coinData.setQuantityToDelete(countCoins);
                    coinData.setOldQuantity(countCoins);
                    coinData.setNewQuantity(0L);
                    coins.add(coinData);
                }
            }

            List<Coin> newCoinList = new ArrayList<>();
            vendingMachine.setCoins(newCoinList);
            vendingMachineDao.save(vendingMachine);

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

    public VmExtractCoinsResponse extractSomeCoinsVendingMachine(int id, VmExtractSomeCoinsRequest request, BindingResult result){
        try{
            if (result.hasErrors()){
                LOGGER.error("Parameters validation failed. {}.",request);
                return new VmExtractCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).withMessage(MessagesEnum.PARAM_VALID_FAIL.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(id).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmExtractCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<Coin> newCoinList = new ArrayList<>();
            List<VmExtractCoinData> coins = new ArrayList<>();
            for (VmCoinRequest coinRequest: request.getCoins()) {

                Long countCoins = vendingMachine.getCoins().stream().filter(vmc -> vmc.getName().equals(coinRequest.getName())).count();

                if (countCoins >= coinRequest.getQuantity()){
                    VmExtractCoinData coinData = new VmExtractCoinData();
                    Optional<Coin> coin = coinDao.findByName(coinRequest.getName());
                    if (Objects.isNull(coin)){
                        return new VmExtractCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .withMessage(MessagesEnum.COIN_NOT_EXIST.getText()).build()).build();
                    }

                    Long newQuantity = countCoins - coinRequest.getQuantity();
                    for (int i = 0; i < newQuantity; i++) {
                        newCoinList.add(coin.get());
                    }
                    coinData.setName(coin.get().getName());
                    coinData.setValue(coin.get().getValue());
                    coinData.setQuantityToDelete(coinRequest.getQuantity());
                    coinData.setOldQuantity(countCoins);
                    coinData.setNewQuantity(newQuantity);
                    coins.add(coinData);
                } else {
                    return new VmExtractCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value())
                            .withMessage(MessagesEnum.VMC_DELETE_SOME_FAIL.getText() + ": Quantity of "+coinRequest.getName()+" is not correct.").build()).build();
                }
            }


            vendingMachine.setCoins(newCoinList);
            vendingMachineDao.save(vendingMachine);

            VmExtractCoinsData data = new VmExtractCoinsData();
            data.setVmName(vendingMachine.getName());
            data.setDeletedCoins(coins);
            return new VmExtractCoinsResponse.Builder().withData(data).withMessage(MessagesEnum.VMC_DELETE_SOME_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Extract some Coins from Vending Machine failed.",e);
            return new VmExtractCoinsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VMC_DELETE_SOME_FAIL.getText()).build()).build();
        }
    }

    //************************
    //vendingmachine Products
    //************************

    public VmInsertProductsResponse insertProductsVendingMachine(int id, VmInsertProductsRequest request, BindingResult result){
        try{
            if (result.hasErrors()){
                LOGGER.error("Parameters validation failed. {}.",request);
                return new VmInsertProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).withMessage(MessagesEnum.PARAM_VALID_FAIL.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(id).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmInsertProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<VmInsertProductData> vmInsertProductDataList = new ArrayList<>();
            for (VmProductRequest productRequest:request.getProducts()) {
                Optional<Product> product = productDao.findByName(productRequest.getName());
                if (product.isEmpty()){
                    return new VmInsertProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                            .withMessage(MessagesEnum.PRODUCT_NOT_EXIST.getText()).build()).build();
                }
                for (int i = 0; i < productRequest.getQuantity(); i++) {
                    vendingMachine.getProducts().add(product.get());
                }
                VmInsertProductData vmInsertProductData = new VmInsertProductData();
                vmInsertProductData.setName(product.get().getName());
                vmInsertProductData.setPrice(product.get().getPrice());
                vmInsertProductData.setCode(product.get().getCode());
                vmInsertProductData.setQuantity(productRequest.getQuantity());
                vmInsertProductDataList.add(vmInsertProductData);
            }

            vendingMachineDao.save(vendingMachine);

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

    public VmGetProductsResponse getProductsVendingMachine(int id){
        try{

            VendingMachine vendingMachine = vendingMachineDao.findById(id).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmGetProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<Product> productList = productDao.findAll();
            List<VmGetProductData> products = new ArrayList<>();
            Long quantity = 0L;
            Double totalValue = 0.0;
            for (Product product: productList) {

                Long countProducts = vendingMachine.getProducts().stream().filter(vmp -> vmp.getName().equals(product.getName())).count();

                if (countProducts != 0){
                    VmGetProductData productData = new VmGetProductData();
                    productData.setName(product.getName());
                    productData.setPrice(product.getPrice());
                    productData.setCode(product.getCode());
                    productData.setQuantity(countProducts);
                    products.add(productData);
                    quantity = quantity + countProducts;
                    totalValue = totalValue + (productData.getPrice() * countProducts);
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

    public VmExtractProductsResponse extractProductsVendingMachine(int id){
        try{

            VendingMachine vendingMachine = vendingMachineDao.findById(id).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmExtractProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<Product> productList = productDao.findAll();
            List<VmExtractProductData> products = new ArrayList<>();
            for (Product product: productList) {

                Long countProducts = vendingMachine.getProducts().stream().filter(vmp -> vmp.getName().equals(product.getName())).count();

                if (countProducts != 0){
                    VmExtractProductData productData = new VmExtractProductData();
                    productData.setName(product.getName());
                    productData.setCode(product.getCode());
                    productData.setPrice(product.getPrice());
                    productData.setQuantityToDelete(countProducts);
                    productData.setOldQuantity(countProducts);
                    productData.setNewQuantity(0L);
                    products.add(productData);
                }
            }

            List<Product> newProductList = new ArrayList<>();
            vendingMachine.setProducts(newProductList);
            vendingMachineDao.save(vendingMachine);

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

    public VmExtractProductsResponse extractSomeProductsVendingMachine(int id, VmExtractSomeProductsRequest request, BindingResult result){
        try{
            if (result.hasErrors()){
                LOGGER.error("Parameters validation failed. {}.",request);
                return new VmExtractProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).withMessage(MessagesEnum.PARAM_VALID_FAIL.getText()).build()).build();
            }

            VendingMachine vendingMachine = vendingMachineDao.findById(id).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmExtractProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<Product> newProductList = new ArrayList<>();
            List<VmExtractProductData> products = new ArrayList<>();
            for (VmProductRequest productRequest: request.getProducts()) {

                Long countProducts = vendingMachine.getProducts().stream().filter(vmc -> vmc.getName().equals(productRequest.getName())).count();

                if (countProducts >= productRequest.getQuantity()){
                    VmExtractProductData productData = new VmExtractProductData();
                    Optional<Product> product = productDao.findByName(productRequest.getName());
                    if (Objects.isNull(product)){
                        return new VmExtractProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .withMessage(MessagesEnum.PRODUCT_NOT_EXIST.getText()).build()).build();
                    }

                    Long newQuantity = countProducts - productRequest.getQuantity();
                    for (int i = 0; i < newQuantity; i++) {
                        newProductList.add(product.get());
                    }
                    productData.setName(product.get().getName());
                    productData.setCode(product.get().getCode());
                    productData.setPrice(product.get().getPrice());
                    productData.setQuantityToDelete(productRequest.getQuantity());
                    productData.setOldQuantity(countProducts);
                    productData.setNewQuantity(newQuantity);
                    products.add(productData);
                } else {
                    return new VmExtractProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value())
                            .withMessage(MessagesEnum.VMP_DELETE_SOME_FAIL.getText() + ": Quantity of "+productRequest.getName()+" is not correct.").build()).build();
                }
            }


            vendingMachine.setProducts(newProductList);
            vendingMachineDao.save(vendingMachine);

            VmExtractProductsData data = new VmExtractProductsData();
            data.setVmName(vendingMachine.getName());
            data.setDeletedProducts(products);
            return new VmExtractProductsResponse.Builder().withData(data).withMessage(MessagesEnum.VMP_DELETE_SOME_OK.getText()).build();
        } catch (Exception e){
            LOGGER.error("Extract some Coins from Vending Machine failed.",e);
            return new VmExtractProductsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.VMP_DELETE_SOME_FAIL.getText()).build()).build();
        }
    }

    //************************
    //vendingmachine Operations
    //************************

    public VmGetOperationsResponse getOperationsVendingMachine(int id, String from, String to){
        try{

            VendingMachine vendingMachine = vendingMachineDao.findById(id).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmGetOperationsResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            List<Operation> operations = new ArrayList<>();
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

            Double totalValue = 0.0;
            List<VmGetOperationData> operationDataList = new ArrayList<>();
            for (Operation operation: operations) {
                VmGetOperationData vmGetOperationData = new VmGetOperationData();
                vmGetOperationData.setVendingMachine(operation.getVendingMachine().getId());
                vmGetOperationData.setOperationId(operation.getOperationId());
                vmGetOperationData.setDate(operation.getDate());
                vmGetOperationData.setValue(operation.getValue());
                vmGetOperationData.setStatus(operation.getStatus());
                List<VmGetOperationCoinData> vmGetOperationCoinDataList = new ArrayList<>();
                for (Coin coin: operation.getCoins()){
                    VmGetOperationCoinData vmGetOperationCoinData = new VmGetOperationCoinData();
                    vmGetOperationCoinData.setName(coin.getName());
                    vmGetOperationCoinData.setValue(coin.getValue());
                }
                vmGetOperationData.setCoins(vmGetOperationCoinDataList);
                List<VmGetOperationProductData> vmGetOperationProductDataList = new ArrayList<>();
                for (Product product: operation.getProducts()){
                    VmGetOperationProductData vmGetOperationProductData = new VmGetOperationProductData();
                    vmGetOperationProductData.setName(product.getName());
                    vmGetOperationProductData.setPrice(product.getPrice());
                    vmGetOperationProductData.setCode(product.getCode());
                }
                vmGetOperationData.setProducts(vmGetOperationProductDataList);
                totalValue = totalValue + operation.getValue();
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

    public VmGetOperationResponse getOperationVendingMachine(int id,  int operationId){
        try{

            VendingMachine vendingMachine = vendingMachineDao.findById(id).orElse(null);
            if (Objects.isNull(vendingMachine)){
                return new VmGetOperationResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VM_NOT_EXIST.getText()).build()).build();
            }

            Optional<Operation> operation = null;
            if (Objects.nonNull(vendingMachine.getOperations())){
                operation = vendingMachine.getOperations().stream().filter(o -> o.getOperationId() == operationId).findFirst();
            }

            if (Objects.isNull(operation)){
                return new VmGetOperationResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .withMessage(MessagesEnum.VMO_OPERATION_NOT_EXIST.getText()).build()).build();
            }

            List<VmGetOperationCoinData> vmGetOperationCoinDataList = new ArrayList<>();
            for (Coin coin: operation.get().getCoins()){
                VmGetOperationCoinData vmGetOperationCoinData = new VmGetOperationCoinData();
                vmGetOperationCoinData.setName(coin.getName());
                vmGetOperationCoinData.setValue(coin.getValue());
            }

            List<VmGetOperationProductData> vmGetOperationProductDataList = new ArrayList<>();
            for (Product product: operation.get().getProducts()){
                VmGetOperationProductData vmGetOperationProductData = new VmGetOperationProductData();
                vmGetOperationProductData.setName(product.getName());
                vmGetOperationProductData.setPrice(product.getPrice());
                vmGetOperationProductData.setCode(product.getCode());
            }

            VmGetOperationData data = new VmGetOperationData();
            data.setDate(operation.get().getDate());
            data.setVendingMachine(operation.get().getVendingMachine().getId());
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
