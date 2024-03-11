package dev.roy.inventorystock.order;

import dev.roy.inventorystock.response.ResponseDto;
import dev.roy.inventorystock.response.ResponseStatusConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private OrderDto testOrderDto;

    @BeforeEach
    void setUp() {
        testOrderDto = new OrderDto();
        testOrderDto.setId(1L);
        testOrderDto.setReferenceCode("ABC1234");
        testOrderDto.setInvoiceQuantity(5);
        testOrderDto.setInvoiceDate(Date.from(Instant.now()));
    }

    @Test
    void shouldGetAllOrders() {
        List<OrderDto> orderList = new ArrayList<>();
        orderList.add(testOrderDto);

        when(orderService.getAllOrders()).thenReturn(orderList);

        ResponseEntity<List<OrderDto>> responseEntity = orderController.getAllOrders();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldGetOrderById() {
        when(orderService.getOrderById(1L)).thenReturn(Optional.of(testOrderDto));

        ResponseEntity<Optional<OrderDto>> responseEntity = orderController.getOrderById(1L);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldGetOrderByRefCode() {
        when(orderService.getOrderByRefCode("ABC1234")).thenReturn(Optional.of(testOrderDto));

        ResponseEntity<Optional<OrderDto>> responseEntity = orderController.getOrderByRefCode("ABC1234");

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void shouldOrderItemById() {
        ResponseEntity<ResponseDto> responseEntity = orderController.orderItemById(1L, testOrderDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void shouldOrderItemByCode() {
        ResponseEntity<ResponseDto> responseEntity = orderController.orderItemByCode("ABC1234", testOrderDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }
}
