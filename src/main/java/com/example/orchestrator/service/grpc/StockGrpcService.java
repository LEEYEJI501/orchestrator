package com.example.orchestrator.service.grpc;

import com.example.grpc.*;

public interface StockGrpcService {
    Response getMarkets(Empty empty);
    Response getStocksByMarket(GetStocksByMarketRequest getStocksByMarketRequest);
    Response getStockDataByMarketAndCode(GetStockDataByMarketAndCodeRequest getStockDataByMarketAndCodeRequest);
    Response getAllStocksByMarket(GetAllStocksByMarketRequest getAllStocksByMarketRequest);
    Response searchStocksByName(SearchStocksByNameRequest searchStocksByNameRequest);
    Response searchStocksByCode(SearchStocksByCodeRequest searchStocksByCodeRequest);
    Response getMovingAverages(GetMovingAveragesRequest getMovingAveragesRequest);
    Response getBollingerBands(GetBollingerBandsRequest getBollingerBandsRequest);
    Response getMACD(GetMACDRequest getMACDRequest);
    Response getRSI(GetRSIRequest getRSIRequest);
}
