package com.domain.coin.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CoinGetAllData implements Serializable {

    private List<CoinGetData> coinList = new ArrayList<>();

    public List<CoinGetData> getCoinList() {
        return coinList;
    }

    public void setCoinList(List<CoinGetData> coinList) {
        this.coinList = coinList;
    }
}
