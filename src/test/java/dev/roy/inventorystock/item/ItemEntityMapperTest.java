package dev.roy.inventorystock.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ItemEntityMapperTest {

    ItemDto itemDto;

    @BeforeEach
    void setUp() {

         itemDto = new ItemDto(
                1L,
                "CODE123456",
                "Jac Daniels",
                "Nose Light with plenty of sweetnesrererr",
                5);

    }

    @Test
//    @DisplayName("Should Map ItemDto to Item Entity")
    void shouldMapItemDtoToEntity() {

        Item item = ItemEntityMapper.mapItemDtoToEntity(itemDto,new Item());

        assertNotNull(item);
        assertEquals(itemDto.getCode(), item.getCode());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
        assertEquals(itemDto.getStockQty(), item.getStockQty());

    }

    @Test
    void shouldMapItemDetailsDtoToEntityById() {

        Item item = ItemEntityMapper.mapItemDetailsDtoToEntityById(itemDto,new Item());

        assertNotNull(item);
        assertEquals(itemDto.getCode(), item.getCode());
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
    }

    @Test
    void shouldMapItemDetailsDtoToEntityByCode() {

        Item item = ItemEntityMapper.mapItemDetailsDtoToEntityByCode(itemDto,new Item());

        assertNotNull(item);
        assertEquals(itemDto.getName(), item.getName());
        assertEquals(itemDto.getDescription(), item.getDescription());
    }
}