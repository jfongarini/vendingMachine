package com.domain.coin.response;

import com.domain.coin.data.CoinNewData;
import com.util.CommonResponse;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = CoinNewResponse.Builder.class)
public class CoinNewResponse extends CommonResponse<CoinNewData> {

    protected CoinNewResponse(CoinNewResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<CoinNewData, CoinNewResponse.Builder> {

        @Override
        public CoinNewResponse build() {
            return new CoinNewResponse(this);
        }

        @Override
        protected CoinNewResponse.Builder self() {
            return this;
        }
    }
}
