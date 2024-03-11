package dev.roy.inventorystock.order;

import dev.roy.inventorystock.exception.OrderQuantityInsufficientException;
import dev.roy.inventorystock.exception.ResourceNotFoundException;
import dev.roy.inventorystock.item.Item;
import dev.roy.inventorystock.item.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final OrderDtoMapper orderDtoMapper;

    public List<OrderDto> getAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(orderDtoMapper)
                .collect(Collectors.toList());
    }

    public Optional<OrderDto> getOrderById(Long id) {
        return Optional.ofNullable((orderRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Order", "orderId", String.valueOf(id))
        ))).map(orderDtoMapper);
    }

    public Optional<OrderDto> getOrderByRefCode(String refCode) {
        return Optional.ofNullable((orderRepository.findByReferenceCode(refCode).orElseThrow(
                () -> new ResourceNotFoundException("Order", "orderRefCode", refCode)
        ))).map(orderDtoMapper);
    }

    public void createOrderById(Long id, OrderDto orderDto) {
        Order order = OrderEntityMapper.mapOrderDtoToEntity(orderDto, new Order());
        Item itemById = itemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Item", "ItemId", String.valueOf(id))
        );
        int currentQty = itemById.getStockQty() - order.getInvoiceQuantity();
        if (currentQty < 0) {
            throw new OrderQuantityInsufficientException("Order cannot be processed due to insufficient quantity with refCode " + order.getReferenceCode());
        }
        itemById.setStockQty(currentQty);
        itemRepository.save(itemById);
        order.setItem(itemById);
        Order save = orderRepository.save(order);
    }

    public void createOrderByCode(String code, OrderDto orderDto) {
        Order order = OrderEntityMapper.mapOrderDtoToEntity(orderDto, new Order());
        Item itemByCode = itemRepository.findByCode(code).orElseThrow(
                () -> new ResourceNotFoundException("Item", "ItemId", code)
        );
        int currentQty = itemByCode.getStockQty() - order.getInvoiceQuantity();
        if (currentQty < 0) {
            throw new OrderQuantityInsufficientException("Order cannot be processed due to insufficient quantity with refCode " + order.getReferenceCode());
        }
        itemByCode.setStockQty(currentQty);
        itemRepository.save(itemByCode);
        order.setItem(itemByCode);
        Order save = orderRepository.save(order);
    }

}
