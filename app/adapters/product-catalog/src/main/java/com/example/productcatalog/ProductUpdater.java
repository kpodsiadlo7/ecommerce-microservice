package com.example.productcatalog;

public interface ProductUpdater {
    void updateAvailableQty(Integer availableQty);

    void updateReservedQty(Integer reservedQty);
}
