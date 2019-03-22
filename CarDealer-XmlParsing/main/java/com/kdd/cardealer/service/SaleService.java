package com.kdd.cardealer.service;

import com.kdd.cardealer.domain.dtos.exports.SaleExportRootDTO;

public interface SaleService {
    void importSales();

    SaleExportRootDTO exportSalesDiscounts();
}
