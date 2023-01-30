package com.abnamro.emrahboz.assignment.retail.controller;

import com.abnamro.emrahboz.assignment.retail.service.ReportingService;
import com.abnamro.emrahboz.assignment.retail.service.model.ReportProductDto;
import com.abnamro.emrahboz.assignment.retail.service.model.SaleAmountsDto;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@RestController
@Slf4j
@RequiredArgsConstructor
@Api(tags = {"Reporting API"}, description = "Reporting Operations API")
public class ReportingControllerImpl implements ReportingController {

    private final ReportingService service;


    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public List<ReportProductDto> dailyTopProducts() {
        return service.topProductsOfToday();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ReportProductDto leastSellingProductMonthly() {
        return service.leastProductSellingOfMonth();
    }

    @Override
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public List<SaleAmountsDto> salesAmountDaily(String startDate, String endDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatter = formatter.withLocale(Locale.US);

        return service.salesAmountDaily(LocalDate.parse(startDate, formatter), LocalDate.parse(endDate, formatter));
    }
}
