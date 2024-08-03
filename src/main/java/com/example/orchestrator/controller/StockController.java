package com.example.orchestrator.controller;

import com.example.orchestrator.constants.HttpConstants;
import com.example.orchestrator.dto.MovingAveragePeriodsDto;
import com.example.orchestrator.dto.PaginationRequestDto;
import com.example.orchestrator.dto.SortParamsDto;
import com.example.orchestrator.dto.TimeframeRequestDto;
import com.example.orchestrator.service.StockService;
import com.example.orchestrator.util.GrpcResponseHelper;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.grpc.*;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/stock")
public class StockController {
    private static final Logger logger = LoggerFactory.getLogger(StockController.class);
    private final StockService stockService;
    private final GrpcResponseHelper grpcResponseHelper;

    @Autowired
    public StockController(StockService stockService, GrpcResponseHelper grpcResponseHelper) {
        this.stockService = stockService;
        this.grpcResponseHelper = grpcResponseHelper;
    }

    @GetMapping("/markets")
    public ResponseEntity<String> getMarkets() {
        Response response = stockService.getMarkets();

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(HttpConstants.CACHE_MAX_AGE_SECONDS, TimeUnit.SECONDS))
                .body(grpcResponseHelper.createJsonResponse(response).getBody());
    }

    @GetMapping("/markets/{marketName}")
    public ResponseEntity<String> getMarketByName(@PathVariable String marketName) {
        Response response = stockService.getStocksByMarket(marketName);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(HttpConstants.CACHE_MAX_AGE_SECONDS, TimeUnit.SECONDS))
                .body(grpcResponseHelper.createJsonResponse(response).getBody());
    }

    @GetMapping("/markets/{marketName}/securities")
    public ResponseEntity<String> getAllStocks(
            @PathVariable String marketName,
            @Valid @ModelAttribute PaginationRequestDto paginationRequestDto,
            @Valid @ModelAttribute SortParamsDto sortParamsDto) {
        Response response = stockService.getAllStocksByMarket(marketName, paginationRequestDto, sortParamsDto);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(HttpConstants.CACHE_MAX_AGE_SECONDS, TimeUnit.SECONDS))
                .body(grpcResponseHelper.createJsonResponse(response).getBody());
    }

    @GetMapping("/markets/{marketName}/securities/search/name")
    public ResponseEntity<String> searchStocksByName(
            @PathVariable String marketName,
            @RequestParam String name,
            @Valid @ModelAttribute PaginationRequestDto paginationRequestDto,
            @Valid @ModelAttribute SortParamsDto sortParamsDto
    ) {
        Response response = stockService.searchStocksByName(name, paginationRequestDto, sortParamsDto);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(HttpConstants.CACHE_MAX_AGE_SECONDS, TimeUnit.SECONDS))
                .body(grpcResponseHelper.createJsonResponse(response).getBody());
    }

    @GetMapping("/markets/{marketName}/securities/search/code")
    public ResponseEntity<String> searchStocksByCode(
            @PathVariable String marketName,
            @RequestParam String code,
            @Valid @ModelAttribute PaginationRequestDto paginationRequestDto,
            @Valid @ModelAttribute SortParamsDto sortParamsDto
    ) {
        Response response = stockService.searchStocksByCode(code, paginationRequestDto, sortParamsDto);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(HttpConstants.CACHE_MAX_AGE_SECONDS, TimeUnit.SECONDS))
                .body(grpcResponseHelper.createJsonResponse(response).getBody());
    }

    @GetMapping("/markets/{marketName}/securities/code/{code}/data")
    public ResponseEntity<String> getStockByCode(
            @PathVariable String marketName,
            @PathVariable String code,
            @Valid @ModelAttribute TimeframeRequestDto timeframeRequestDto
    ) {
        Response response = stockService.getStockByCode(marketName, code, timeframeRequestDto);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(HttpConstants.CACHE_MAX_AGE_SECONDS, TimeUnit.SECONDS))
                .body(grpcResponseHelper.createJsonResponse(response).getBody());
    }

    @GetMapping("/securities/{code}/moving-averages")
    public ResponseEntity<String> getMovingAverages(
            @PathVariable String code,
            @Valid @ModelAttribute MovingAveragePeriodsDto movingAveragePeriodsDto,
            @Valid @ModelAttribute TimeframeRequestDto timeframeRequestDto
    ) {
        Response response = stockService.getMovingAverages(code, movingAveragePeriodsDto, timeframeRequestDto);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(HttpConstants.CACHE_MAX_AGE_SECONDS, TimeUnit.SECONDS))
                .body(grpcResponseHelper.createJsonResponse(response).getBody());
    }

    @GetMapping("/securities/{code}/bollinger-bands")
    public ResponseEntity<String> getBollingerBands(
            @PathVariable String code,
            @Valid @ModelAttribute TimeframeRequestDto timeframeRequestDto
    ) {
        Response response = stockService.getBollingerBands(code, timeframeRequestDto);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(HttpConstants.CACHE_MAX_AGE_SECONDS, TimeUnit.SECONDS))
                .body(grpcResponseHelper.createJsonResponse(response).getBody());
    }

    @GetMapping("/securities/{code}/macd")
    public ResponseEntity<String> getMACD(
            @PathVariable String code,
            @Valid @ModelAttribute TimeframeRequestDto timeframeRequestDto
    ) {
        Response response = stockService.getMACD(code, timeframeRequestDto);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(HttpConstants.CACHE_MAX_AGE_SECONDS, TimeUnit.SECONDS))
                .body(grpcResponseHelper.createJsonResponse(response).getBody());
    }

    @GetMapping("/securities/{code}/rsi")
    public ResponseEntity<String> getRSI(
            @PathVariable String code,
            @Valid @ModelAttribute TimeframeRequestDto timeframeRequestDto
    ) {
        Response response = stockService.getRSI(code, timeframeRequestDto);

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(HttpConstants.CACHE_MAX_AGE_SECONDS, TimeUnit.SECONDS))
                .body(grpcResponseHelper.createJsonResponse(response).getBody());
    }
}
