package com.domain.vendingMachine.coin.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VmInsertCoinsData implements Serializable {

    private String vmName;
    private List<VmInsertCoinData> insertedCoins = new ArrayList<>();

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public List<VmInsertCoinData> getInsertedCoins() {
        return insertedCoins;
    }

    public void setInsertedCoins(List<VmInsertCoinData> insertedCoins) {
        this.insertedCoins = insertedCoins;
    }
}
