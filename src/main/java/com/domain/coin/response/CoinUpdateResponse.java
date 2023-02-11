package com.domain.coin.response;

import com.domain.coin.data.CoinUpdateData;
import com.util.CommonResponse;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = CoinUpdateResponse.Builder.class)
public class CoinUpdateResponse extends CommonResponse<CoinUpdateData> {

    protected CoinUpdateResponse(CoinUpdateResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<CoinUpdateData, CoinUpdateResponse.Builder> {

        @Override
        public CoinUpdateResponse build() {
            return new CoinUpdateResponse(this);
        }

        @Override
        protected CoinUpdateResponse.Builder self() {
            return this;
        }
    }
}