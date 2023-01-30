package com.abnamro.emrahboz.assignment.retail.mapper;

import com.abnamro.emrahboz.assignment.retail.data.model.OrderProductEntity;
import com.abnamro.emrahboz.assignment.retail.data.model.ProductEntity;
import com.abnamro.emrahboz.assignment.retail.data.repository.ProductRepository;
import com.abnamro.emrahboz.assignment.retail.exceptions.ResourceNotFoundException;
import com.abnamro.emrahboz.assignment.retail.service.model.OrderProductDto;
import com.abnamro.emrahboz.assignment.retail.service.model.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MapperUtil {

    private final ProductRepository productRepository;

    public OrderProductDto mapOrderProductToDto(OrderProductEntity entity) {

        String productName = productRepository.findById(entity.getProductId())
                .map(ProductEntity::getName)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Product Id : %d not found", entity.getProductId())));


        return OrderProductDto.builder()
                .productAmount(entity.getProductAmount())
                .productId(entity.getProductId())
                .productQuantity(entity.getProductQuantity())
                .productName(productName)
                .build();
    }

    public ProductDto mapProductToDto(ProductEntity entity) {

        return ProductDto.builder()
                .availableStock(entity.getAvailableStock())
                .price(entity.getPrice())
                .lastUpdate(entity.getLastUpdate())
                .name(entity.getName())
                .id(entity.getId())
                .build();
    }

}
