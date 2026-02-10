package com.retails.followers.application.dto;

import com.utils.application.RequestContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class UserFollowersRetailRequest implements RequestContract {

    private Long fk_user_id,fk_retail_company_id;

}
