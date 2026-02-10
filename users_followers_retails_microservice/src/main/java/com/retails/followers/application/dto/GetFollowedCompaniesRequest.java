package com.retails.followers.application.dto;

import com.utils.application.RequestContract;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class GetFollowedCompaniesRequest implements RequestContract {
    public Long fk_user_id;
}
