package com.retails.followers.application.dto;

import com.utils.application.ResponseContract;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class UserFollowersRetailResponse implements ResponseContract {
    private Long followId;
    private Long userId;
    private Long companyId;
    private String companyName;
    private Byte status;
    private String followedAt;
}
