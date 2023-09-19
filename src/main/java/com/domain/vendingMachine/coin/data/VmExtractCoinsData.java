package com.domain.vendingMachine.coin.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VmExtractCoinsData implements Serializable {

    private String vmName;
    private List<VmExtractCoinData> deletedCoins = new ArrayList<>();

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public List<VmExtractCoinData> getDeletedCoins() {
        return deletedCoins;
    }

    public void setDeletedCoins(List<VmExtractCoinData> deletedCoins) {
        this.deletedCoins = deletedCoins;
    }
}
