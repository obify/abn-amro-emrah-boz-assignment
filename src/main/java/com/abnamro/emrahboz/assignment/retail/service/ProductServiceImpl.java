package com.abnamro.emrahboz.assignment.retail.service;

import com.abnamro.emrahboz.assignment.retail.data.model.ProductEntity;
import com.abnamro.emrahboz.assignment.retail.data.repository.ProductRepository;
import com.abnamro.emrahboz.assignment.retail.exceptions.ResourceNotFoundException;
import com.abnamro.emrahboz.assignment.retail.exceptions.ResourceViolationException;
import com.abnamro.emrahboz.assignment.retail.mapper.MapperUtil;
import com.abnamro.emrahboz.assignment.retail.service.model.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {


    private final ProductRepository productRepository;
    private final MapperUtil mapperUtil;

    @Override
    public ProductDto get(Long productId) {

        return productRepository.findById(productId)
                .map(mapperUtil::mapProductToDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Product Id : %d not found", productId)));

    }

    @Override
    public List<ProductDto> search(String productName) {

        return productRepository.findByNameContaining(productName)
                .stream()
                .map(mapperUtil::mapProductToDto)
                .toList();
    }

    @Override
    public ProductEntity update(Long id, Integer requestedQuantity) {


        Optional<ProductEntity> entity = productRepository.findById(id);

        if (entity.isPresent()) {

            if (requestedQuantity.compareTo(entity.get().getAvailableStock()) > 0) {
                throw new ResourceViolationException(
                        String.format("There is not enough stock for this product id : %d "
                                , id));
            }

            entity.get().setAvailableStock(entity.get().getAvailableStock() - requestedQuantity);
            return productRepository.saveAndFlush(entity.get());


        } else {
            throw new ResourceNotFoundException(
                    String.format("Product Id : %d not found", id));
        }
    }

}
