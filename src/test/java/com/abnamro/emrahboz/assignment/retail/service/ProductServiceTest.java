package com.abnamro.emrahboz.assignment.retail.service;

import com.abnamro.emrahboz.assignment.retail.data.model.ProductEntity;
import com.abnamro.emrahboz.assignment.retail.data.repository.ProductRepository;
import com.abnamro.emrahboz.assignment.retail.exceptions.ResourceNotFoundException;
import com.abnamro.emrahboz.assignment.retail.exceptions.ResourceViolationException;
import com.abnamro.emrahboz.assignment.retail.mapper.MapperUtil;
import com.abnamro.emrahboz.assignment.retail.mock.MockData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProductServiceTest {

    private MockData mockData;

    ProductRepository mockProductRepository;

    MapperUtil mapperUtil;
    @Spy
    private ProductService productService;


    @BeforeAll
    public void setup() {

        mockData = new MockData();
        mockProductRepository = Mockito.mock(ProductRepository.class);

        mapperUtil = new MapperUtil(mockProductRepository);


        productService = new ProductServiceImpl(mockProductRepository, mapperUtil);

    }

    @Test
    public void givenExistingProduct_whenSelectThisProduct_thenSucceeded() {

        Optional<ProductEntity> productEntity = Optional.of(mockData.getProductEntity());
        Mockito.doReturn(productEntity)
                .when(mockProductRepository).findById(any(Long.class));

        Assertions.assertNotNull(productService.get(1L));
    }

    @Test
    public void givenNotExistingProduct_whenSelectThisProduct_thenThrowError() {

        Mockito.doReturn(Optional.empty())
                .when(mockProductRepository).findById(any(Long.class));

        ResourceNotFoundException thrown = Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.get(1L);
        });
        Assertions.assertEquals("Product Id : 1 not found", thrown.getMessage());
    }

    @Test
    public void givenSearchProduct_whenSendingKeyword_thenSucceeded() {

        List<ProductEntity> productEntities = new ArrayList<>();
        productEntities.add(mockData.getProductEntity());

        Mockito.doReturn(productEntities)
                .when(mockProductRepository).findByNameContaining(any(String.class));

        Assertions.assertNotNull(productService.search("Apple"));
    }


    @Test
    public void givenAProduct_whenUpdateThisProduct_thenSucceeded() {

        Optional<ProductEntity> productEntity = Optional.of(mockData.getProductEntity());
        Mockito.doReturn(productEntity)
                .when(mockProductRepository).findById(any(Long.class));

        Mockito.doReturn(productEntity.get())
                .when(mockProductRepository).saveAndFlush(any(ProductEntity.class));

        Assertions.assertEquals(productService.update(1L, 1).getAvailableStock(), 1);
    }

    @Test
    public void givenAProduct_whenUpdateThisProductAboveAvailableStock_thenThrowError() {

        Optional<ProductEntity> productEntity = Optional.of(mockData.getProductEntity());
        Mockito.doReturn(productEntity)
                .when(mockProductRepository).findById(any(Long.class));

        ResourceViolationException thrown = Assertions.assertThrows(ResourceViolationException.class, () -> {
            productService.update(1L, 10);
        });
        Assertions.assertEquals("There is not enough stock for this product id : 1 ", thrown.getMessage());
    }

}
