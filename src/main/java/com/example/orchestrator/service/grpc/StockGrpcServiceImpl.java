package com.example.orchestrator.service.grpc;

import com.example.grpc.StockServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.example.grpc.*;

@Service
public class StockGrpcServiceImpl implements StockGrpcService {
    private final StockServiceGrpc.StockServiceBlockingStub stockServiceBlockingStub;
    private final ManagedChannel channel;

    public StockGrpcServiceImpl(@Value("${stock.grpc.host}") String grpcHost, @Value("${stock.grpc.port}") int grpcPort) {
        this.channel = ManagedChannelBuilder.forAddress(grpcHost, grpcPort)
                .maxInboundMessageSize(100 * 1024 * 1024)
                .usePlaintext()
                .build();
        this.stockServiceBlockingStub = StockServiceGrpc.newBlockingStub(channel);
    }

    @Override
    public Response getMarkets(Empty empty) {
        return stockServiceBlockingStub.getMarkets(empty);
    }

    @Override
    public Response getStocksByMarket(GetStocksByMarketRequest getStocksByMarketRequest) {
        return stockServiceBlockingStub.getStocksByMarket(getStocksByMarketRequest);
    }

    @Override
    public Response getStockDataByMarketAndCode(GetStockDataByMarketAndCodeRequest getStockDataByMarketAndCodeRequest) {
        return stockServiceBlockingStub.getStockDataByMarketAndCode(getStockDataByMarketAndCodeRequest);
    }

    @Override
    public Response getAllStocksByMarket(GetAllStocksByMarketRequest getAllStocksByMarketRequest) {
        return stockServiceBlockingStub.getAllStocksByMarket(getAllStocksByMarketRequest);
    }

    @Override
    public Response searchStocksByName(SearchStocksByNameRequest searchStocksByNameRequest) {
        return stockServiceBlockingStub.searchStocksByName(searchStocksByNameRequest);
    }

    @Override
    public Response searchStocksByCode(SearchStocksByCodeRequest searchStocksByCodeRequest) {
        return stockServiceBlockingStub.searchStocksByCode(searchStocksByCodeRequest);
    }

    @Override
    public Response getMovingAverages(GetMovingAveragesRequest getMovingAveragesRequest) {
        return stockServiceBlockingStub.getMovingAverages(getMovingAveragesRequest);
    }

    @Override
    public Response getBollingerBands(GetBollingerBandsRequest getBollingerBandsRequest) {
        return stockServiceBlockingStub.getBollingerBands(getBollingerBandsRequest);
    }

    @Override
    public Response getMACD(GetMACDRequest getMACDRequest) {
        return stockServiceBlockingStub.getMACD(getMACDRequest);
    }

    @Override
    public Response getRSI(GetRSIRequest getRSIRequest) {
        return stockServiceBlockingStub.getRSI(getRSIRequest);
    }
}
