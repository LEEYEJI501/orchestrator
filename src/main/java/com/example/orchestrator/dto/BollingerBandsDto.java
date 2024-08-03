package com.example.orchestrator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BollingerBandsDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Double> upperBand;
    private List<Double> middleBand;
    private List<Double> lowerBand;
}
