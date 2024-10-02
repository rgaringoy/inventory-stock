package dev.roy.inventorystock.item;

import dev.roy.inventorystock.exception.ItemAlreadyExistsException;
import dev.roy.inventorystock.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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



}