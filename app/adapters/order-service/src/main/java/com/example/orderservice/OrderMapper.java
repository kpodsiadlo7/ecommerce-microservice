package com.example.orderservice;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class OrderMapper {

    private final ProductMapper productMapper;

    OrderRecord toRecord(Order order) {
        return new OrderRecord(
                order.getOrderId(),
                order.getCustomerId(),
                order.getCartId(),
                order.getOrderDate(),
                productMapper.toRecordList(order.getProducts()),
                order.getTotalPrice(),
                order.getStatus()
        );
    }

    OrderEntity toEntity(Order order) {
        return new OrderEntity(
                null, // ID encji jest generowane przez bazę danych
                order.getOrderId(),
                order.getCustomerId(),
                order.getCartId(),
                order.getOrderDate(),
                productMapper.toEntityList(order.getProducts()),
                order.getTotalPrice(),
                order.getStatus()
        );
    }

    public Order toDomain(OrderEntity orderEntity) {
        return new Order(
                orderEntity.getOrderId(),
                orderEntity.getUserId(),
                orderEntity.getCartId(),
                orderEntity.getOrderDate(),
                productMapper.fromEntityList(orderEntity.getProducts()),
                orderEntity.getTotalPrice(),
                orderEntity.getStatus()
        );
    }

    public OrderInfoRecord toOrderInfoRecord(OrderInfo orderInfo) {
        return new OrderInfoRecord(
                orderInfo.getCartId(),
                orderInfo.getOrderId(),
                orderInfo.getTotalPrice()
        );
    }
}
