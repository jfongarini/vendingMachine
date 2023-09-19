package com.domain.vendingMachine.coin.request;

import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VmInsertCoinsRequest implements Serializable {

    @NotEmpty
    private List<VmCoinRequest> coins = new ArrayList<>();

    public List<VmCoinRequest> getCoins() {
        return coins;
    }

    public void setCoins(List<VmCoinRequest> coins) {
        this.coins = coins;
    }
}
