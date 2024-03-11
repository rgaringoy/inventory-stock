package dev.roy.inventorystock.item;

import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class ItemDtoMapper implements Function<Item, ItemDto> {

    @Override
    public ItemDto apply(Item item) {
        return new ItemDto(
                item.getId(),
                item.getCode(),
                item.getName(),
                item.getDescription(),
                item.getStockQty()
        );
    }

}