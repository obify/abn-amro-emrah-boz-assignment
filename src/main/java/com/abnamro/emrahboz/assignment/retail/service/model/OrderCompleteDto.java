package com.abnamro.emrahboz.assignment.retail.service.model;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class OrderCompleteDto {


    private String orderId;
    private String userName;

}
