package dev.roy.inventorystock.order.item;

import dev.roy.inventorystock.item.ItemController;
import dev.roy.inventorystock.item.ItemDto;
import dev.roy.inventorystock.item.ItemService;
import dev.roy.inventorystock.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @InjectMocks
    private ItemController itemController;

    private ItemDto testItemDto;

    @BeforeEach
    void setUp() {
        testItemDto = new ItemDto();
        testItemDto.setId(1L);
        testItemDto.setName("Jack Daniels");
        testItemDto.setCode("CODE123456");
        testItemDto.setDescription("Nose Light with plenty of sweetness");
        testItemDto.setStockQty(20);

    }

    @Test
    void shouldGetAllItems() {
        List<ItemDto> itemList = new ArrayList<>();
        itemList.add(testItemDto);

        when(itemService.getAllItems()).thenReturn(itemList);

        ResponseEntity<List<ItemDto>> responseEntity = itemController.getAllItems();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(itemList, responseEntity.getBody());
    }

    @Test
    void shouldGetItemById() {
        when(itemService.getItemById(1L)).thenReturn(Optional.of(testItemDto));

        ResponseEntity<Optional<ItemDto>> responseEntity = itemController.getItemById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(testItemDto, Objects.requireNonNull(responseEntity.getBody()).orElse(null));
    }

    @Test
    void shouldGetItemByCode() {
        when(itemService.getItemByCode("CODE123456")).thenReturn(Optional.of(testItemDto));

        ResponseEntity<Optional<ItemDto>> responseEntity = itemController.getItemByCode("CODE123456");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(testItemDto, responseEntity.getBody().orElse(null));
    }

    @Test
    void shouldCreateNewItem() {
        ResponseEntity<ResponseDto> responseEntity = itemController.createNewItem(testItemDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void shouldUpdateItemById() {
        when(itemService.updateItemById(1L, testItemDto)).thenReturn(true);

        ResponseEntity<ResponseDto> responseEntity = itemController.updateItemById(1L, testItemDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldUpdateItemByCode() {
        when(itemService.updateItemByCode("CODE123456", testItemDto)).thenReturn(true);

        ResponseEntity<ResponseDto> responseEntity = itemController.updateItemByCode("CODE123456", testItemDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldAddStockQtyById() {
        when(itemService.addItemStockQtyById(1L, testItemDto)).thenReturn(true);

        ResponseEntity<ResponseDto> responseEntity = itemController.addStockQtyById(1L, testItemDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldAddStockQtyByCode() {
        when(itemService.addItemStockQtyByCode("CODE123456", testItemDto)).thenReturn(true);

        ResponseEntity<ResponseDto> responseEntity = itemController.addStockQtyByCode("CODE123456", testItemDto);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

}
