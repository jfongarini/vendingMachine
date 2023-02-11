package com.service;

import com.dao.ProductDao;
import com.util.CommonError;
import com.util.enums.MessagesEnum;
import com.domain.product.data.*;
import com.domain.product.request.ProductNewRequest;
import com.domain.product.request.ProductUpdateRequest;
import com.domain.product.response.*;
import com.model.Product;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    final static Logger LOGGER = LoggerFactory.getLogger(Product.class);

    @Autowired
    private ProductDao productDao;

    @Autowired
    private ApiProductService apiProductService;

    public ProductNewResponse newProduct(ProductNewRequest request, BindingResult result) {
        try {
            if (result.hasErrors()) {
                LOGGER.error("Parameters validation failed. {}.", request);
                return new ProductNewResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.PARAM_VALID_FAIL.getText()).build()).build();
            }
            String productName = StringUtils.capitalize(request.getName().toLowerCase());
            String available = apiProductService.getFruit(productName);
            if (Objects.isNull(available)) {
                LOGGER.error("New Product failed.");
                return new ProductNewResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value())
                        .withMessage(MessagesEnum.PRODUCT_NEW_NOT_AVAILABLE.getText()).build()).build();
            }
            Product product = new Product();
            product.setName(productName);
            product.setCode(request.getCode());
            product.setPrice(request.getPrice());
            productDao.save(product);
            ProductNewData data = new ProductNewData();
            data.setName(productName);
            data.setCode(request.getCode());
            data.setPrice(request.getPrice());
            return new ProductNewResponse.Builder().withData(data).withMessage(MessagesEnum.PRODUCT_NEW_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("New Product failed.", e);
            return new ProductNewResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.PRODUCT_NEW_FAIL.getText()).build()).build();
        }
    }

    public ProductDeleteResponse deleteProduct(int id) {
        try {
            Optional<Product> product = productDao.findById(id);
            if (Objects.isNull(product)){
                return new ProductDeleteResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.PRODUCT_NOT_EXIST.getText()).build()).build();
            }
            ProductDeleteData data = new ProductDeleteData();
            data.setName(product.get().getName());
            data.setPrice(product.get().getPrice());
            data.setCode(product.get().getCode());
            productDao.delete(product.get());
            return new ProductDeleteResponse.Builder().withData(data).withMessage(MessagesEnum.PRODUCT_DELETE_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("Delete Product failed.", e);
            return new ProductDeleteResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.PRODUCT_DELETE_FAIL.getText()).build()).build();
        }
    }

    public ProductGetResponse getProduct(int id) {
        try {
            Optional<Product> product = productDao.findById(id);
            if (Objects.isNull(product)){
                return new ProductGetResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.PRODUCT_NOT_EXIST.getText()).build()).build();
            }
            ProductGetData data = new ProductGetData();
            data.setId(product.get().getProductId());
            data.setName(product.get().getName());
            data.setPrice(product.get().getPrice());
            data.setCode(product.get().getCode());
            return new ProductGetResponse.Builder().withData(data).withMessage(MessagesEnum.PRODUCT_GET_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("Get Product failed.", e);
            return new ProductGetResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.PRODUCT_GET_FAIL.getText()).build()).build();
        }
    }

    public ProductGetAllResponse getAllProduct() {
        try {
            List<Product> products = productDao.findAll();
            if (products.isEmpty()){
                return new ProductGetAllResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.PRODUCT_LIST_EMPTY.getText()).build()).build();
            }
            List<ProductGetData> productGetDataList = products.stream().map(c -> new ProductGetData(c.getProductId(),c.getName(),c.getCode(),c.getPrice()))
                    .collect(Collectors.toList());
            ProductGetAllData data = new ProductGetAllData();
            data.setProductList(productGetDataList);

            return new ProductGetAllResponse.Builder().withData(data).withMessage(MessagesEnum.PRODUCT_GET_ALL_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("Get all Products failed.", e);
            return new ProductGetAllResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.PRODUCT_GET_ALL_FAIL.getText()).build()).build();
        }
    }

    public ProductGetAvailableResponse getAvailableProduct() {
        try {
            List<String> productsApi = apiProductService.getAllFruits();
            if (productsApi.isEmpty()) {
                return new ProductGetAvailableResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.PRODUCT_LIST_EMPTY.getText()).build()).build();
            }
            List<String> availableProducts = new ArrayList<>();
            List<String> usedProducts = new ArrayList<>();
            for (String productApi:productsApi){
                Optional<Product> product = productDao.findByName(productApi);
                if (product.isEmpty()){
                    availableProducts.add(productApi);
                } else {
                    usedProducts.add(productApi);
                }
            }
            ProductGetAvailableData data = new ProductGetAvailableData();
            data.setAvailableProducts(availableProducts);
            data.setUsedProducts(usedProducts);

            return new ProductGetAvailableResponse.Builder().withData(data).withMessage(MessagesEnum.PRODUCT_GET_AVAILABLE_OK.getText()).build();
        } catch (ServiceException se){
            LOGGER.error("Get all Products failed.", se);
            return new ProductGetAvailableResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.PRODUCT_GET_AVAILABLE_FAIL.getText()).build()).build();
        } catch (Exception e) {
            LOGGER.error("Get all Products failed.", e);
            return new ProductGetAvailableResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.PRODUCT_GET_AVAILABLE_FAIL.getText()).build()).build();
        }
    }

    public ProductUpdateResponse updateProduct(ProductUpdateRequest request, int id) {
        try {
            if (Objects.isNull(request.getCode()) && Objects.isNull(request.getPrice())){
                return new ProductUpdateResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.PRODUCT_PARAMETERS_FAIL.getText()).build()).build();
            }
            Optional<Product> product = productDao.findById(id);
            if (Objects.isNull(product)){
                return new ProductUpdateResponse.Builder().withError(new CommonError.Builder(HttpStatus.BAD_REQUEST.value()).
                        withMessage(MessagesEnum.PRODUCT_NOT_EXIST.getText()).build()).build();
            }
            product.get().setCode(Objects.isNull(request.getCode())?product.get().getCode():request.getCode());
            product.get().setPrice(Objects.isNull(request.getPrice())?product.get().getPrice():request.getPrice());
            productDao.save(product.get());

            ProductUpdateData data = new ProductUpdateData();
            data.setId(product.get().getProductId());
            data.setName(product.get().getName());
            data.setCode(product.get().getCode());
            data.setPrice(product.get().getPrice());
            return new ProductUpdateResponse.Builder().withData(data).withMessage(MessagesEnum.PRODUCT_UPDATE_OK.getText()).build();
        } catch (Exception e) {
            LOGGER.error("Update Product failed.", e);
            return new ProductUpdateResponse.Builder().withError(new CommonError.Builder(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .withMessage(MessagesEnum.PRODUCT_UPDATE_FAIL.getText()).build()).build();
        }
    }
}
