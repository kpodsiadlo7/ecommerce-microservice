package com.example.cartservice;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
class EventMapper {

    List<EventProductEntity> toEntityList(List<EventProduct> productsToUnReserve) {
        return productsToUnReserve.stream().map(this::toEntity).toList();
    }

    EventProductEntity toEntity(EventProduct eventProduct) {
        return new EventProductEntity(
                null, // ID encji jest generowane przez bazę danych
                eventProduct.getProductId(),
                eventProduct.getQty()
        );
    }

    List<EventProductRecord> toRecordList(List<EventProduct> eventProducts) {
        return eventProducts.stream().map(this::toRecord).toList();
    }

    EventProductRecord toRecord(EventProduct eventProduct) {
        return new EventProductRecord(
                eventProduct.getProductId(),
                eventProduct.getQty()
        );
    }

    public List<EventProduct> toEventProductList(List<Product> products) {
        return products.stream().map(this::toEventProduct).toList();
    }

    public EventProduct toEventProduct(Product product){
        return new EventProduct(
                product.getProductId(),
                product.getAvailableQty()
        );
    }

    public List<EventProductRecord> toEventProductRecordList(List<EventProduct> products) {
        return products.stream().map(this::toEventProductRecord).toList();
    }

    EventProductRecord toEventProductRecord(EventProduct product){
        return new EventProductRecord(
                product.getProductId(),
                product.getQty()
        );
    }
}
