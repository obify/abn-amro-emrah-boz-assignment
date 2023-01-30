package com.abnamro.emrahboz.assignment.retail.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Api(tags = "Client Controller", description = "Controller created for try Retail Rest API")
public class ClientControllerImpl {

    @GetMapping("/client")
    @ApiOperation(value = "Return demo page as HTML")
    public String getDemo( Model model) {
        return "client";
    }
}
