package com.abnamro.emrahboz.assignment.retail.service;

import com.abnamro.emrahboz.assignment.retail.data.repository.OrderProductRepository;
import com.abnamro.emrahboz.assignment.retail.data.repository.OrderRepository;
import com.abnamro.emrahboz.assignment.retail.data.repository.ProductRepository;
import com.abnamro.emrahboz.assignment.retail.service.model.AddToCartDto;
import com.abnamro.emrahboz.assignment.retail.service.model.OrderCompleteDto;
import com.abnamro.emrahboz.assignment.retail.service.model.OrderDto;
import com.abnamro.emrahboz.assignment.retail.service.model.ReportProductDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;


@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReportingServiceTest {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    OrderProductRepository orderProductRepository;
    @Autowired
    ProductRepository productRepository;
    @Spy
    private ReportingService reportingService;
    @Autowired
    private OrderService orderService;


    @BeforeAll
    public void setup() {

        reportingService = new ReportingServiceImpl(orderProductRepository,
                productRepository, orderRepository);

        ReflectionTestUtils.setField(reportingService, "indicationThreshold", 10);


        //Create Orders For Reporting

        OrderDto orderDto = orderService.addToCard(AddToCartDto.builder()
                .productId(1L)
                .quantity(13)
                .userName("Bob")
                .build());
        orderService.completeOrder(OrderCompleteDto.builder()
                .orderId(orderDto.getId())
                .userName("Bob")
                .build());


        orderDto = orderService.addToCard(AddToCartDto.builder()
                .productId(2L)
                .quantity(2)
                .userName("Bob")
                .build());
        orderService.completeOrder(OrderCompleteDto.builder()
                .orderId(orderDto.getId())
                .userName("Bob")
                .build());

        orderDto = orderService.addToCard(AddToCartDto.builder()
                .productId(3L)
                .quantity(2)
                .userName("Bob")
                .build());
        orderService.completeOrder(OrderCompleteDto.builder()
                .orderId(orderDto.getId())
                .userName("Bob")
                .build());

        orderDto = orderService.addToCard(AddToCartDto.builder()
                .productId(5L)
                .quantity(6)
                .userName("Bob")
                .build());
        orderService.completeOrder(OrderCompleteDto.builder()
                .orderId(orderDto.getId())
                .userName("Bob")
                .build());

        orderDto = orderService.addToCard(AddToCartDto.builder()
                .productId(8L)
                .quantity(3)
                .userName("Bob")
                .build());
        orderService.completeOrder(OrderCompleteDto.builder()
                .orderId(orderDto.getId())
                .userName("Bob")
                .build());

        orderDto = orderService.addToCard(AddToCartDto.builder()
                .productId(10L)
                .quantity(6)
                .userName("Bob")
                .build());
        orderService.completeOrder(OrderCompleteDto.builder()
                .orderId(orderDto.getId())
                .userName("Bob")
                .build());


    }

    @Test
    public void whenSelectTopFiveProducts_thenSucceeded() {
        List<ReportProductDto> res = reportingService.topProductsOfToday();
        Assertions.assertEquals(reportingService.topProductsOfToday().size(), 5);
    }

    @Test
    public void whenSelectLeastSellingProduct_thenSucceeded() {
        Assertions.assertNotNull(reportingService.leastProductSellingOfMonth());
    }


    @Test
    public void whenSelectDailySelledProducts_thenSucceeded() {
        List<ReportProductDto> res = reportingService.topProductsOfToday();
        Assertions.assertEquals(reportingService.salesAmountDaily(LocalDate.now().minusDays(3), LocalDate.now().plusDays(3)).size(), 1);
    }

}
