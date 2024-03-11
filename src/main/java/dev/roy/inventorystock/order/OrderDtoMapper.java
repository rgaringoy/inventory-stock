package dev.roy.inventorystock.order;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class OrderDtoMapper implements Function<Order, OrderDto> {

    @Override
    public OrderDto apply(Order order) {
        return new OrderDto(
                order.getId(),
                order.getReferenceCode(),
                order.getInvoiceQuantity(),
                order.getInvoiceDate()
        );
    }

}
