package com.abnamro.emrahboz.assignment.retail.service;

import com.abnamro.emrahboz.assignment.retail.data.model.UserEntity;
import com.abnamro.emrahboz.assignment.retail.service.model.OrderDto;

public interface NotificationService {

    boolean sendNotification(OrderDto orderDto, UserEntity userEntity);

}
