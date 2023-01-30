package com.abnamro.emrahboz.assignment.retail.controller;

import com.abnamro.emrahboz.assignment.retail.service.model.ReportProductDto;
import com.abnamro.emrahboz.assignment.retail.service.model.SaleAmountsDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@RequestMapping(value = "/api/v1/reporting")
public interface ReportingController {


    @ApiOperation(value = "Return today's most selling five products", authorizations = {@Authorization("ROLE_EMPLOYEE")})
    @RequestMapping(value = "/dailyTopProducts", method = RequestMethod.GET)
    List<ReportProductDto> dailyTopProducts();

    @ApiOperation(value = "Returns least selling product for this month", authorizations = {@Authorization("ROLE_EMPLOYEE")})
    @RequestMapping(value = "/leastSellingProductMonthly", method = RequestMethod.GET)
    ReportProductDto leastSellingProductMonthly();

    @ApiOperation(value = "Returns daily gained amount within defined date interval", authorizations = {@Authorization("ROLE_EMPLOYEE")})
    @RequestMapping(value = "/dailyGainedAmount", method = RequestMethod.GET)
    List<SaleAmountsDto> salesAmountDaily(@RequestParam(name = "startDate") String startDate, @RequestParam(name = "endDate") String endDate);

}
