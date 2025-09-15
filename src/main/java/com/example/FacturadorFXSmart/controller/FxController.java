package com.example.FacturadorFXSmart.controller;

import com.example.FacturadorFXSmart.service.FxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

@RestController
@RequestMapping("/v1/fx")
@Validated
@Tag(name = "FX", description = "Tasas de cambio y conversion")
public class FxController {

    private final FxService fx;

    public FxController(FxService fx){
        this.fx = fx;
    }

    @Operation(summary = "Obtener tasas de cambio del dia", description = "Devuelve un mapa {monedaObjetivo: tasa} cacheado por dia.")
    @ApiResponse(responseCode = "200", description = "Tasas devueltas correctamente",
            content = @Content(mediaType = "application/json",
            schema = @Schema(implementation =  Map.class)))
    @ApiResponse(responseCode = "400", description = "Parametros invalidos",
            content = @Content(mediaType = "aplication/json"))
    @GetMapping("/latest")
    public Map<String, BigDecimal> latest(@RequestParam String base, @RequestParam String targets){
        //var list = Arrays.stream(targets.split(",")).map(String::toUpperCase).toList();

        List<String> list = Stream.of(targets.split(",")).map(String::trim).filter(s -> !s.isEmpty()).map(String::toUpperCase).toList();

        if (list.isEmpty()){
            throw new IllegalArgumentException("El parametro 'target' no puede estar vacio (ej: EUR,CRC,MXN).");
        }
        return fx.latest(base.toUpperCase(), list);
    }
}
