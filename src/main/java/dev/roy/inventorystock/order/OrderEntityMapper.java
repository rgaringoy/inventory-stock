package dev.roy.inventorystock.order;

public class OrderEntityMapper {

    public static Order mapOrderDtoToEntity(OrderDto orderDto, Order order) {
        order.setReferenceCode(orderDto.getReferenceCode());
        order.setInvoiceQuantity(orderDto.getInvoiceQuantity());
        order.setInvoiceDate(orderDto.getInvoiceDate());
        return order;
    }

}
