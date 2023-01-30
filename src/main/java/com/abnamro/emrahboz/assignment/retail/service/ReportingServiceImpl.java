package com.abnamro.emrahboz.assignment.retail.service;

import com.abnamro.emrahboz.assignment.retail.data.model.ProductEntity;
import com.abnamro.emrahboz.assignment.retail.data.repository.OrderRepository;
import com.abnamro.emrahboz.assignment.retail.data.repository.OrderProductRepository;
import com.abnamro.emrahboz.assignment.retail.data.repository.ProductRepository;
import com.abnamro.emrahboz.assignment.retail.service.model.ReportProductDto;
import com.abnamro.emrahboz.assignment.retail.service.model.SaleAmountsDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.Tuple;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReportingServiceImpl implements ReportingService {

    @Value("${retail.service.indicationThreshold}")
    private Integer indicationThreshold;

    private final OrderProductRepository orderProductRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;


    @Override
    public List<ReportProductDto> topProductsOfToday() {

        return orderProductRepository.findTopFiveDailyProduct().stream()
                .map(x -> ReportProductDto
                        .builder()
                        .price((BigDecimal) x.get("price"))
                        .availableStock((Integer) x.get("available_stock"))
                        .name(x.get("name").toString())
                        .lowQuantity((Integer) x.get("available_stock") < indicationThreshold)
                        .build()).toList();

    }

    @Override
    public ReportProductDto leastProductSellingOfMonth() {

        Optional<ProductEntity> productEntity = productRepository.findTopByLastUpdateLessThan(LocalDateTime.now().withDayOfMonth(1));
        if (productEntity.isPresent()) {
            return ReportProductDto
                    .builder()
                    .price(productEntity.get().getPrice())
                    .availableStock(productEntity.get().getAvailableStock())
                    .name(productEntity.get().getName())
                    .lowQuantity(productEntity.get().getAvailableStock() < indicationThreshold)
                    .build();
        }

        Tuple leastProduct = orderProductRepository.findLeastProductSellingOfMonth();
        return ReportProductDto
                .builder()
                .price((BigDecimal) leastProduct.get("price"))
                .availableStock((Integer) leastProduct.get("available_stock"))
                .name(leastProduct.get("name").toString())
                .lowQuantity((Integer) leastProduct.get("available_stock") < indicationThreshold)
                .build();
    }

    @Override
    public List<SaleAmountsDto> salesAmountDaily(LocalDate startDate, LocalDate endDate) {

        return orderRepository.findSalesAmount(startDate, endDate).stream()
                .map(x -> SaleAmountsDto
                        .builder()
                        .saleAmount((BigDecimal) x.get("order_total_amount"))
                        .day(x.get("day").toString())
                        .build()).toList();
    }
}
