package com.Gateway.ApiGateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("gateway")
public class Controller {

    @GetMapping("health")
    public String home() {
        return "API Gateway is running";
    }
}
