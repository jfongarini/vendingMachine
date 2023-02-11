package com.domain.vendingMachine.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VendingMachineGetAllData implements Serializable {

    private List<VendingMachineGetData> vendingMachineList = new ArrayList<>();

    public List<VendingMachineGetData> getVendingMachineList() {
        return vendingMachineList;
    }

    public void setVendingMachineList(List<VendingMachineGetData> vendingMachineList) {
        this.vendingMachineList = vendingMachineList;
    }
}
