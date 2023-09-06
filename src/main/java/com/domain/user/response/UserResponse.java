package com.domain.user.response;

import com.domain.user.data.UserData;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.util.CommonResponse;

@JsonDeserialize(builder = UserResponse.Builder.class)
public class UserResponse extends CommonResponse<UserData> {

    protected UserResponse(UserResponse.Builder builder) {
        super(builder);
    }

    public static class Builder extends CommonResponse.Builder<UserData, UserResponse.Builder>{

        @Override
        public UserResponse build() {
            return new UserResponse(this);
        }

        @Override
        protected UserResponse.Builder self() {
            return this;
        }
    }
}
