package com.abnamro.emrahboz.assignment.retail.service;

import com.abnamro.emrahboz.assignment.retail.service.model.ReportProductDto;
import com.abnamro.emrahboz.assignment.retail.service.model.SaleAmountsDto;


import java.time.LocalDate;
import java.util.List;

public interface ReportingService {

    List<ReportProductDto> topProductsOfToday();

    ReportProductDto leastProductSellingOfMonth();

    List<SaleAmountsDto> salesAmountDaily(LocalDate startDate, LocalDate endDate);

}
