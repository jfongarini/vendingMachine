package com.domain.vendingMachine.coin.data;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VmGetCoinsData implements Serializable {

    private String vmName;
    private Long totalCoins;
    private String totalValue;
    private List<VmGetCoinData> coins = new ArrayList<>();

    public String getVmName() {
        return vmName;
    }

    public void setVmName(String vmName) {
        this.vmName = vmName;
    }

    public List<VmGetCoinData> getCoins() {
        return coins;
    }

    public void setCoins(List<VmGetCoinData> coins) {
        this.coins = coins;
    }

    public Long getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(Long totalCoins) {
        this.totalCoins = totalCoins;
    }

    public String getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(String totalValue) {
        this.totalValue = totalValue;
    }
}
