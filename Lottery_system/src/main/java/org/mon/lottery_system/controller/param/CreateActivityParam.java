package org.mon.lottery_system.controller.param;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import java.io.Serializable;
import java.util.List;

@Data
public class CreateActivityParam implements Serializable {

    /**
     * 活动名称
     */
    @NotBlank(message = "活动名称不能为空！")
    private String activityName;

    /**
     * 描述
     */
    @NotBlank(message = "活动描述不能为空！")
    private String description;

    /**
     * 活动关联奖品列表
     */
    @NotEmpty(message = "活动关联奖品列表不能为空！")
    @Valid
    private List<CreatePrizeByActivityParam> activityPrizeList;

    /**
     * 活动关联人员列表
     */
    @NotEmpty(message = "活动关联人员列表不能为空！")
    @Valid
    private List<CreateUserByActivityParam> activityUserList;



}
