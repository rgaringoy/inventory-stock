package dev.roy.inventorystock.item;

import dev.roy.inventorystock.response.ResponseDto;
import dev.roy.inventorystock.response.ResponseStatusConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static dev.roy.inventorystock.item.ItemConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = ITEM_API_URI, produces = (MediaType.APPLICATION_JSON_VALUE))
public class ItemController {

    private final ItemService itemService;

    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems() {
        List<ItemDto> allItems = itemService.getAllItems();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allItems);
    }

    @GetMapping(path = ITEM_ID)
    public ResponseEntity<Optional<ItemDto>> getItemById(@PathVariable Long id) {
        Optional<ItemDto> itemById = itemService.getItemById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemById);
    }

    @GetMapping(path = ITEM_CODE)
    public ResponseEntity<Optional<ItemDto>> getItemByCode(@PathVariable String code) {
        Optional<ItemDto> itemByCode = itemService.getItemByCode(code);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(itemByCode);
    }

    @PostMapping
    public ResponseEntity<ResponseDto> createNewItem(@Valid @RequestBody ItemDto itemDto) {
        log.info("ItemController createItem(): {}", itemDto);
        itemService.createItem(itemDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ResponseStatusConstants.STATUS_201, ResponseStatusConstants.MESSAGE_201));
    }

    @PutMapping(path = ITEM_ID)
    public ResponseEntity<ResponseDto> updateItemById(@Valid @PathVariable Long id,
                                                        @RequestBody ItemDto itemDto) {
        log.info("ItemController updateItemDetailsById(): {}, itemDto: {}", id, itemDto);
        boolean isUpdated = itemService.updateItemById(id, itemDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ResponseStatusConstants.STATUS_200, ResponseStatusConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ResponseStatusConstants.STATUS_417, ResponseStatusConstants.MESSAGE_417_UPDATE));
        }
    }

    @PutMapping(path = ITEM_CODE)
    public ResponseEntity<ResponseDto> updateItemByCode(@Valid @PathVariable String code,
                                                      @RequestBody ItemDto itemDto) {
        log.info("ItemController updateItemByCode(): {}, itemDto: {}", code, itemDto);
        boolean isUpdated = itemService.updateItemByCode(code, itemDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ResponseStatusConstants.STATUS_200, ResponseStatusConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ResponseStatusConstants.STATUS_417, ResponseStatusConstants.MESSAGE_417_UPDATE));
        }
    }

    @PutMapping(path = ADD_STOCK_BY_ITEM_ID)
    public ResponseEntity<ResponseDto> addStockQtyById(@Valid @PathVariable Long id,
                                                      @RequestBody ItemDto itemDto) {
        log.info("ItemController addStockQtyById(): {}, itemDto: {}", id, itemDto);
        boolean isUpdated = itemService.addItemStockQtyById(id, itemDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ResponseStatusConstants.STATUS_200, ResponseStatusConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ResponseStatusConstants.STATUS_417, ResponseStatusConstants.MESSAGE_417_UPDATE));
        }
    }

    @PutMapping(path = ADD_STOCK_BY_ITEM_CODE)
    public ResponseEntity<ResponseDto> addStockQtyByCode(@Valid @PathVariable String code,
                                                        @RequestBody ItemDto itemDto) {
        log.info("ItemController addStockQtyByCode(): {}, itemDto: {}", code, itemDto);
        boolean isUpdated = itemService.addItemStockQtyByCode(code, itemDto);
        if (isUpdated) {
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new ResponseDto(ResponseStatusConstants.STATUS_200, ResponseStatusConstants.MESSAGE_200));
        } else {
            return ResponseEntity
                    .status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDto(ResponseStatusConstants.STATUS_417, ResponseStatusConstants.MESSAGE_417_UPDATE));
        }
    }

}
