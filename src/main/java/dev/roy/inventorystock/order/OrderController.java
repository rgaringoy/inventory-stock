package dev.roy.inventorystock.order;

import dev.roy.inventorystock.response.ResponseDto;
import dev.roy.inventorystock.response.ResponseStatusConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

import static dev.roy.inventorystock.order.OrderConstants.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = ORDER_API_URI, produces = (MediaType.APPLICATION_JSON_VALUE))
@Validated
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<OrderDto>> getAllOrders() {
        List<OrderDto> allOrders = orderService.getAllOrders();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(allOrders);
    }

    @GetMapping(path = ORDER_ID)
    public ResponseEntity<Optional<OrderDto>> getOrderById(@PathVariable Long id) {
        Optional<OrderDto> orderById = orderService.getOrderById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderById);
    }

    @GetMapping(path = ORDER_REF_CODE)
    public ResponseEntity<Optional<OrderDto>> getOrderByRefCode(@PathVariable String refCode) {
        Optional<OrderDto> orderByRefCode = orderService.getOrderByRefCode(refCode);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(orderByRefCode);
    }

    @PostMapping(path = ORDER_ID)
    public ResponseEntity<ResponseDto> orderItemById(@Valid @PathVariable Long id,
                                                     @RequestBody OrderDto orderDto) {
        log.info("OrderController createOrderById(): {}", orderDto);
        orderService.createOrderById(id, orderDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ResponseStatusConstants.STATUS_201, ResponseStatusConstants.MESSAGE_201));
    }

    @PostMapping(path = ORDER_CODE)
    public ResponseEntity<ResponseDto> orderItemByCode(@Valid @PathVariable String code,
                                                     @RequestBody OrderDto orderDto) {
        log.info("OrderController createOrderByCode(): {}", orderDto);
        orderService.createOrderByCode(code, orderDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(ResponseStatusConstants.STATUS_201, ResponseStatusConstants.MESSAGE_201));
    }

}
