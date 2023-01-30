package com.abnamro.emrahboz.assignment.retail.controller;

import com.abnamro.emrahboz.assignment.retail.service.model.ProductDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

@Validated
@RequestMapping(value = "/api/v1/product")
public interface ProductController {


    @ApiOperation(value = "Search Product(s) With Search Keyword", authorizations = {@Authorization("ROLE_CUSTOMER")})
    @RequestMapping(value = {"/", "/{productName}"}, method = RequestMethod.GET)
    List<ProductDto> search(@PathVariable Optional<String> productName);


}
