package com.example.FacturadorFXSmart.controller;

import com.example.FacturadorFXSmart.service.FxService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

@RestController
@RequestMapping("/v1/fx")
public class FxController {
    private final FxService fx;

    public FxController(FxService fx){
        this.fx = fx;
    }

    @GetMapping("/latest")
    public Map<String, BigDecimal> latest(@RequestParam String base, @RequestParam String targets){
        var list = Arrays.stream(targets.split(",")).map(String::toUpperCase).toList();
        return fx.latest(base.toUpperCase(), list);
    }
}
