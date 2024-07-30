package com.example.cartservice;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventProductMapper {

    List<EventProductEntity> toEntityList(List<EventProduct> productsToUnReserve) {
        return productsToUnReserve.stream().map(this::toEntity).toList();
    }

    EventProductEntity toEntity(EventProduct eventProduct){
        return new EventProductEntity(
                null, // ID encji jest generowane przez bazę danych
                eventProduct.getProductId(),
                eventProduct.getQty()
        );
    }

    List<EventProductRecord> toRecordList(List<EventProduct> eventProducts) {
        return eventProducts.stream().map(this::toRecord).toList();
    }

    EventProductRecord toRecord(EventProduct eventProduct){
        return new EventProductRecord(
                eventProduct.getProductId(),
                eventProduct.getQty()
        );
    }
}
