package com.abnamro.emrahboz.assignment.retail.service;

import com.abnamro.emrahboz.assignment.retail.data.model.ProductEntity;
import com.abnamro.emrahboz.assignment.retail.service.model.ProductDto;


import java.util.List;

public interface ProductService {

    ProductDto get(Long productId);

    List<ProductDto> search(String productName);

    ProductEntity update(Long id, Integer requestedQuantity);


}
