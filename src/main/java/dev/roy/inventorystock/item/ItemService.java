package dev.roy.inventorystock.item;

import dev.roy.inventorystock.exception.ItemAlreadyExistsException;
import dev.roy.inventorystock.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemDtoMapper itemDtoMapper;


    public List<ItemDto> getAllItems() {
        return itemRepository.findAll()
                .stream()
                .map(itemDtoMapper)
                .collect(Collectors.toList());
    }

    public Optional<ItemDto> getItemById(Long id) {
        return Optional.ofNullable((itemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Item", "itemId", String.valueOf(id))
        ))).map(itemDtoMapper);
    }

    public Optional<ItemDto> getItemByCode(String code) {
        return Optional.ofNullable((itemRepository.findByCode(code).orElseThrow(
                () -> new ResourceNotFoundException("Item", "itemCode", code)
        ))).map(itemDtoMapper);
    }

    public Item createItem(ItemDto itemDto) {
        Item item = ItemEntityMapper.mapItemDtoToEntity(itemDto, new Item());
        Optional<Item> optionalCode = itemRepository.findByCode(item.getCode());
        if (optionalCode.isPresent()) {
            throw new ItemAlreadyExistsException("Item already registered with given code " + itemDto.getCode());
        }
        return itemRepository.save(item);
    }

    public boolean updateItemById(Long id, ItemDto itemDto) {
        boolean isUpdated = false;
        Item itemById = itemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Item", "ItemId", String.valueOf(id))
        );
        Item item = ItemEntityMapper.mapItemDetailsDtoToEntityById(itemDto, itemById);
        itemRepository.save(item);
        isUpdated = true;
        return isUpdated;
    }

    public boolean updateItemByCode(String code, ItemDto itemDto) {
        boolean isUpdated = false;
        Item itemById = itemRepository.findByCode(code).orElseThrow(
                () -> new ResourceNotFoundException("Item", "ItemCode", code)
        );
        Item item = ItemEntityMapper.mapItemDetailsDtoToEntityByCode(itemDto, itemById);
        itemRepository.save(item);
        isUpdated = true;
        return isUpdated;
    }

    public boolean addItemStockQtyById(Long id, ItemDto itemDto) {
        boolean isUpdated = false;
        Item itemById = itemRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Item", "ItemId", String.valueOf(id))
        );
        int stockQty = itemById.getStockQty() + itemDto.getStockQty();
        itemById.setStockQty(stockQty);
        itemRepository.save(itemById);
        isUpdated = true;
        return isUpdated;
    }

    public boolean addItemStockQtyByCode(String code, ItemDto itemDto) {
        boolean isUpdated = false;
        Item itemByCode = itemRepository.findByCode(code).orElseThrow(
                () -> new ResourceNotFoundException("Item", "ItemCode", code)
        );
        int stockQty = itemByCode.getStockQty() + itemDto.getStockQty();
        itemByCode.setStockQty(stockQty);
        itemRepository.save(itemByCode);
        isUpdated = true;
        return isUpdated;
    }

}
