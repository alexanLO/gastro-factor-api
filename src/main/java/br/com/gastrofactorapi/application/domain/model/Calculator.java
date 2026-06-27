package br.com.gastrofactorapi.application.domain.model;

import java.math.BigDecimal;

public record Calculator(String foodName, BigDecimal grossWeight, BigDecimal netWeight, BigDecimal cookedWeight) {}