package dev.roy.inventorystock.item;

import dev.roy.inventorystock.exception.ItemAlreadyExistsException;
import dev.roy.inventorystock.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemServiceTest {

    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemDtoMapper itemDtoMapper;
    @InjectMocks
    private ItemService underTest;

    private Item item;
    private ItemDto itemDto;

            @BeforeEach
            void setUp() {

                item = new Item(
                        1L,
                        "CODE123456",
                        "Jac Daniels",
                        "Nose Light with plenty of sweetnesrererr",
                        5,
                        null);

                itemDto = new ItemDto(
                        1L,
                        "CODE123456",
                        "Jac Daniels",
                        "Nose Light with plenty of sweetnesrererr",
                        5);
            }

    @Test
    void getAllItems_ShouldReturnListOfItemDto() {
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        when(itemDtoMapper.apply(item)).thenReturn(itemDto);

        List<ItemDto> items = underTest.getAllItems();

        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals(itemDto, items.get(0));

        verify(itemRepository, times(1)).findAll();
        verify(itemDtoMapper, times(1)).apply(item);
    }

    @Test
    void getItemById_ShouldReturnItemDto_WhenItemExists() {
        long id = 1L;
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(itemDtoMapper.apply(item)).thenReturn(itemDto);

        Optional<ItemDto> result = underTest.getItemById(id);

        assertNotNull(result);
        assertEquals(itemDto, result.get());
        verify(itemRepository, times(1)).findById(id);
        verify(itemDtoMapper, times(1)).apply(item);
    }

    @Test
    void getItemById_ShouldThrowException_WhenItemDoesNotExist() {
        long id = 1L;
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getItemById(id)).isInstanceOf(ResourceNotFoundException.class);
        verify(itemRepository, times(1)).findById(id);
        verifyNoInteractions(itemDtoMapper);
    }

    @Test
    void getItemByCode_ShouldReturnItemDto_WhenItemExists() {
        String code = "CODE123456";
        when(itemRepository.findByCode(code)).thenReturn(Optional.of(item));
        when(itemDtoMapper.apply(item)).thenReturn(itemDto);

        Optional<ItemDto> itemByCode = underTest.getItemByCode(code);

        assertNotNull(itemByCode);
        assertEquals(itemDto, itemByCode.get());
        verify(itemRepository, times(1)).findByCode(code);
        verify(itemDtoMapper, times(1)).apply(item);
    }

    @Test
    void getItemByCode_ShouldReturnItemDto_WhenItemNotExists() {
        String code = "CODE123456";
        when(itemRepository.findByCode(code)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.getItemByCode(code)).isInstanceOf(ResourceNotFoundException.class);
        verify(itemRepository, times(1)).findByCode(code);
        verifyNoInteractions(itemDtoMapper);
    }

    @Test
    void createItem_shouldThrowException_WhenItemAlreadyExists() {
        String code = "CODE123456";
        when(itemRepository.findByCode(code)).thenReturn(Optional.of(item));

//        assertThrows(ItemAlreadyExistsException.class, () -> itemService.createItem(itemDto));
        assertThatThrownBy(() -> underTest.createItem(itemDto))
                        .isInstanceOf(ItemAlreadyExistsException.class)
                                .hasMessage("Item already registered with given code " + itemDto.getCode());
        verify(itemRepository, times(1)).findByCode(code);
        verify(itemRepository, never()).save(any());
    }

    @Test
    void createItem_ShouldSaveItem_WhenItemDoesNotExist() {

        String code = "CODE123456";
        when(itemRepository.findByCode(code)).thenReturn(Optional.empty());
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        Item result = underTest.createItem(itemDto);

//        ArgumentCaptor<Item> argumentCaptor = ArgumentCaptor.forClass(Item.class);
//
//        verify(itemRepository).save(argumentCaptor.capture());
//
//        Item capturedItem = argumentCaptor.getValue();
//        assertThat(capturedItem).isEqualTo(item);
        assertNotNull(result);
        assertEquals(item.getCode(), result.getCode());
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void updateItem_ShouldUpdateItem_WhenItemIdExist() {
        long id = 1L;
        when(itemRepository.findById(id)).thenReturn(Optional.ofNullable(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        boolean isUpdated = underTest.updateItemById(id, itemDto);

        assertThat(isUpdated).isTrue();
        verify(itemRepository, times(1)).findById(id);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void updateItemById_shouldThrowException_WhenItemIdNotExists() {
        long id = 1L;
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.updateItemById(id, itemDto))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(itemRepository, times(1)).findById(id);
        verify(itemRepository, never()).save(any());
    }

    @Test
    void updateItemByCode_ShouldUpdateItem_WhenItemCodeExist() {
        String code = "CODE123456";
        when(itemRepository.findByCode(code)).thenReturn(Optional.ofNullable(item));
        when(itemRepository.save(any(Item.class))).thenReturn(item);

        boolean isUpdated = underTest.updateItemByCode(code, itemDto);

        assertThat(isUpdated).isTrue();
        verify(itemRepository, times(1)).findByCode(code);
        verify(itemRepository, times(1)).save(item);
    }

    @Test
    void updateItemByCode_shouldThrowException_WhenItemCodeNotExists() {
        String code = "CODE123456";
        when(itemRepository.findByCode(code)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.updateItemByCode(code, itemDto))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(itemRepository, times(1)).findByCode(code);
        verify(itemRepository, never()).save(any());
    }

    @Test
    void addItemStockByQtyById_shouldAddItemQty_WhenItemIdExist() {
        long id = 1L;
        when(itemRepository.findById(id)).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(item);

        boolean isUpdated = underTest.addItemStockQtyById(id, itemDto);

        assertThat(isUpdated).isTrue();
        assertThat(10).isEqualTo(item.getStockQty());
        verify(itemRepository, times(1)).findById(id);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void addItemStockByQtyById_shouldNThrowException_WhenItemIdNotExist() {
        long id = 1L;
        when(itemRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.addItemStockQtyById(id, itemDto))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(itemRepository, times(1)).findById(id);
        verify(itemRepository, never()).save(any(Item.class));
    }

    @Test
    void addItemStockQtyByCode_shouldAddItemQty_WhenItemCodeExist() {
        String code = "CODE123456";
        when(itemRepository.findByCode(code)).thenReturn(Optional.of(item));
        when(itemRepository.save(any())).thenReturn(item);

        boolean isUpdated = underTest.addItemStockQtyByCode(code, itemDto);

        assertThat(isUpdated).isTrue();
        assertThat(10).isEqualTo(item.getStockQty());
        verify(itemRepository, times(1)).findByCode(code);
        verify(itemRepository, times(1)).save(any(Item.class));
    }

    @Test
    void addItemStockQtyByCode_shouldNThrowException_WhenItemCodeNotExist() {
        String code = "CODE123456";
        when(itemRepository.findByCode(code)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.addItemStockQtyByCode(code, itemDto))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(itemRepository, times(1)).findByCode(code);
        verify(itemRepository, never()).save(any(Item.class));
    }

}