package dev.roy.inventorystock.item;


public class ItemEntityMapper {

    public static Item mapItemDtoToEntity(ItemDto itemDto, Item item) {
        item.setCode(itemDto.getCode());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setStockQty(itemDto.getStockQty());
        return item;
    }

    public static Item mapItemDetailsDtoToEntityById(ItemDto itemDto, Item item) {
        item.setCode(itemDto.getCode());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        return item;
    }

    public static Item mapItemDetailsDtoToEntityByCode(ItemDto itemDto, Item item) {
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        return item;
    }

}
