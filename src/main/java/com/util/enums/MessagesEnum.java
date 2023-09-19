package com.util.enums;

public enum MessagesEnum {
    //Common
    PARAM_VALID_FAIL("Parameters validation failed."),
    USER_NOT_EXIST("User doesn't exist."),

    // Vending Machine
    VM_NOT_EXIST("Vending Machine doesn't exist."),
    VM_LIST_EMPTY("There are no Vending Machines"),
    VM_PARAMETERS_FAIL("Vending Machine name must be reported."),
    VM_NEW_OK("New Vending Machine created successfully."),
    VM_NEW_FAIL("New Vending Machine failed."),
    VM_DELETE_OK("Delete Vending Machine successful."),
    VM_DELETE_FAIL("Delete Vending Machine failed."),
    VM_GET_OK("Get Vending Machine successful."),
    VM_GET_FAIL("Get Vending Machine failed."),
    VM_GET_ALL_OK("Get All Vending Machine successful."),
    VM_GET_ALL_FAIL("Get All Vending Machine failed."),
    VM_UPDATE_OK("Update Vending Machine successful."),
    VM_UPDATE_FAIL("Update Vending Machine failed."),
    VM_LOGIN_OK("Login Vending Machine successful."),
    VM_LOGIN_FAIL("Login Vending Machine failed."),


    // Vending Machine - Coin
    VMC_POST_OK("Insert Coin in Vending Machine successful."),
    VMC_POST_FAIL("Insert Coin in Vending Machine failed."),
    VMC_GET_OK("Get Coins from Vending Machine successful."),
    VMC_GET_FAIL("Get Coins from Vending Machine failed."),
    VMC_DELETE_OK("Extract all Coins from Vending Machine successful."),
    VMC_DELETE_FAIL("Extract all Coins from Vending Machine failed."),

    // Vending Machine - Product
    VMP_POST_OK("Insert Product in Vending Machine successful."),
    VMP_POST_FAIL("Insert Product in Vending Machine failed."),
    VMP_GET_OK("Get Products from Vending Machine successful."),
    VMP_GET_FAIL("Get Products from Vending Machine failed."),
    VMP_DELETE_OK("Extract all Products from Vending Machine successful."),
    VMP_DELETE_FAIL("Extract all Products from Vending Machine failed."),

    // Vending Machine - Operation
    VMO_OPERATION_NOT_EXIST("Operation doesn't exist."),
    VMO_GET_OK("Get Operation from Vending Machine successful."),
    VMO_GET_FAIL("Get Operation from Vending Machine failed."),
    VMO_GET_ALL_OK("Get Operations from Vending Machine successful."),
    VMO_GET_ALL_FAIL("Get Operations from Vending Machine failed."),

    //Coin
    COIN_NOT_EXIST("Coin doesn't exist."),
    COIN_LIST_EMPTY("There are no Coins"),
    COIN_PARAMETERS_FAIL("Coin name or value must be reported."),
    COIN_NEW_OK("New Coin created successfully."),
    COIN_NEW_FAIL("New Coin failed."),
    COIN_DELETE_OK("Delete Coin successful."),
    COIN_DELETE_FAIL("Delete Coin failed."),
    COIN_GET_OK("Get Coin successful."),
    COIN_GET_FAIL("Get Coin failed."),
    COIN_GET_ALL_OK("Get All Coin successful."),
    COIN_GET_ALL_FAIL("Get All Coin failed."),
    COIN_UPDATE_OK("Update Coin successful."),
    COIN_UPDATE_FAIL("Update Coin failed."),

    //Product
    PRODUCT_NOT_EXIST("Product doesn't exist."),
    PRODUCT_LIST_EMPTY("There are no Product"),
    PRODUCT_PARAMETERS_FAIL("Product name, code or price must be reported."),
    PRODUCT_NEW_OK("New Product created successfully."),
    PRODUCT_NEW_FAIL("New Product failed."),
    PRODUCT_NEW_NOT_AVAILABLE("New Product failed. Product not available."),
    PRODUCT_DELETE_OK("Delete Product successful."),
    PRODUCT_DELETE_FAIL("Delete Product failed."),
    PRODUCT_GET_OK("Get Product successful."),
    PRODUCT_GET_FAIL("Get Product failed."),
    PRODUCT_GET_ALL_OK("Get All Product successful."),
    PRODUCT_GET_ALL_FAIL("Get All Product failed."),
    PRODUCT_GET_AVAILABLE_OK("Get Available Product successful."),
    PRODUCT_GET_AVAILABLE_FAIL("Get Available Product failed."),
    PRODUCT_UPDATE_OK("Update Product successful."),
    PRODUCT_UPDATE_FAIL("Update Product failed."),

    //Operation
    OPERATION_NOT_EXIST("Operation doesn't exist."),
    OPERATION_CLOSED("Operation is closed."),
    OPERATION_INCOMPLETE_COIN("Operation incomplete. Has no coins."),
    OPERATION_INCOMPLETE_PRODUCT("Operation incomplete. Has no products."),
    OPERATION_NEW_OK("New Operation created successfully."),
    OPERATION_NEW_FAIL("New Operation failed."),
    OPERATION_INS_COIN_OK("Insert Coin in Operation successful."),
    OPERATION_INS_COIN_FAIL("Insert Coin in Operation failed."),
    OPERATION_GET_COINS_OK("Get Coins from Operation successful."),
    OPERATION_GET_COINS_FAIL("Get Coins from Operation failed."),
    OPERATION_INS_PRODUCT_OK("Insert Product in Operation successful."),
    OPERATION_INS_PRODUCT_FAIL("Insert Product in Operation failed."),
    OPERATION_GET_PRODUCT_OK("Get Product from Operation successful."),
    OPERATION_GET_PRODUCT_FAIL("Get Product from Operation failed."),
    OPERATION_ACCEPT_NOT_ENOUGH("Accept Operation failed because the money is not enough."),
    OPERATION_ACCEPT_OK("Accept Operation successful."),
    OPERATION_CANCEL_OK("Cancel Operation successful."),
    OPERATION_CANCEL_FAIL("Cancel Operation failed.")

    ;
    private final String text;
    MessagesEnum(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

}
