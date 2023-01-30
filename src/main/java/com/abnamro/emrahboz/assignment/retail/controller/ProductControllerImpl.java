package com.abnamro.emrahboz.assignment.retail.controller;

import com.abnamro.emrahboz.assignment.retail.service.ProductService;
import com.abnamro.emrahboz.assignment.retail.service.model.ProductDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"Product API"}, description = "Product Operations API")
public class ProductControllerImpl implements ProductController {

    private final ProductService service;

    @Override
    @PreAuthorize("hasRole('ROLE_CUSTOMER')")
    public List<ProductDto> search(Optional<String> productName) {
        return service.search(productName.orElseGet(String::new));
    }

}
