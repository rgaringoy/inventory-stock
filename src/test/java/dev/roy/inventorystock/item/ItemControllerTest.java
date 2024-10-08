package dev.roy.inventorystock.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.roy.inventorystock.order.OrderService;
import dev.roy.inventorystock.response.ResponseDto;
import dev.roy.inventorystock.response.ResponseStatusConstants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    private Item item;
    private ItemDto itemDto;
    private ItemDto itemDto2;

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

        itemDto2 = new ItemDto(
                2L,
                "CODE123459",
                "Jac Danielsss",
                "Nose Light with plenty of sweetnesrererr",
                5);
    }

    @Test
    void shouldFetchAllItems() throws Exception {

        List<ItemDto> itemDtos = new ArrayList<>();
        itemDtos.add(itemDto);
        itemDtos.add(itemDto2);

        when(itemService.getAllItems()).thenReturn(itemDtos);

        mockMvc.perform(get(ItemConstants.ITEM_API_URI)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$.size()", is(itemDtos.size())));
    }

    @Test
    void shouldFetchItem_whenItemIdExist() throws Exception {
        long id = 1L;
        when(itemService.getItemById(id)).thenReturn(Optional.of(itemDto));

        mockMvc.perform(get(ItemConstants.ITEM_API_URI + "/" + id)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldFetchItem_whenItemCodeExist() throws Exception {
        String code = "CODE123456";
        when(itemService.getItemByCode(code)).thenReturn(Optional.of(itemDto));

        mockMvc.perform(get(ItemConstants.ITEM_API_URI + "/code/" + code)
                        .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").exists());
    }

    @Test
    void shouldCreateItem() throws Exception {
        ResponseDto responseDto = new ResponseDto(
                ResponseStatusConstants.STATUS_201,
                ResponseStatusConstants.MESSAGE_201);

        when(itemService.createItem(any(ItemDto.class))).thenReturn(new Item());

        String content = "{\"code\": \"ABC123\", \"name\": \"Test Item\"}";
        mockMvc.perform(post(ItemConstants.ITEM_API_URI)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(responseDto.getStatusCode()))
                .andExpect(jsonPath("$.statusMsg").value(responseDto.getStatusMsg()));
    }

    @Test
    void shouldUpdateItem_whenItemIdExist() throws Exception {
        ResponseDto responseDto = new ResponseDto(
                ResponseStatusConstants.STATUS_200,
                ResponseStatusConstants.MESSAGE_200);

        long id = 1L;
        when(itemService.updateItemById(eq(id),any(ItemDto.class))).thenReturn(true);

        String updatedItem = "{\"code\": \"ABC123\", \"name\": \"Updated Item\"}";
        mockMvc.perform(put(ItemConstants.ITEM_API_URI  + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(updatedItem))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(responseDto.getStatusCode()))
                .andExpect(jsonPath("$.statusMsg").value(responseDto.getStatusMsg()));
    }

    @Test
    void shouldUpdateItem_whenItemIdNotExist() throws Exception {
        ResponseDto responseDto = new ResponseDto(
                ResponseStatusConstants.STATUS_417,
                ResponseStatusConstants.MESSAGE_417_UPDATE);

        long id = 1L;
        when(itemService.updateItemById(eq(id),any(ItemDto.class))).thenReturn(false);

        String updatedItem = "{\"code\": \"ABC123\", \"name\": \"Updated Item\"}";
        mockMvc.perform(put(ItemConstants.ITEM_API_URI  + "/" + id)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(updatedItem))
                .andExpect(status().isExpectationFailed())
                .andExpect(jsonPath("$.statusCode").value(responseDto.getStatusCode()))
                .andExpect(jsonPath("$.statusMsg").value(responseDto.getStatusMsg()));
    }
    @Test
    void shouldUpdateItem_whenItemCodeExist() throws Exception {
        ResponseDto responseDto = new ResponseDto(
                ResponseStatusConstants.STATUS_200,
                ResponseStatusConstants.MESSAGE_200);

        String code = "CODE123456";
        when(itemService.updateItemByCode(eq(code),any(ItemDto.class))).thenReturn(true);

        String updatedItem = "{\"code\": \"ABC123\", \"name\": \"Updated Item\"}";
        mockMvc.perform(put(ItemConstants.ITEM_API_URI  + "/code/" + code)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(updatedItem))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(responseDto.getStatusCode()))
                .andExpect(jsonPath("$.statusMsg").value(responseDto.getStatusMsg()));
    }

    @Test
    void shouldUpdateItem_whenItemCodeNotExist() throws Exception {
        ResponseDto responseDto = new ResponseDto(
                ResponseStatusConstants.STATUS_417,
                ResponseStatusConstants.MESSAGE_417_UPDATE);

        String code = "CODE123456";
        when(itemService.updateItemByCode(eq(code),any(ItemDto.class))).thenReturn(false);

        String updatedItem = "{\"code\": \"ABC123\", \"name\": \"Updated Item\"}";
        mockMvc.perform(put(ItemConstants.ITEM_API_URI  + "/code/" + code)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(updatedItem))
                .andExpect(status().isExpectationFailed())
                .andExpect(jsonPath("$.statusCode").value(responseDto.getStatusCode()))
                .andExpect(jsonPath("$.statusMsg").value(responseDto.getStatusMsg()));
    }

    @Test
    void shouldAddStockQty_whenItemIdExist() throws Exception {
        Long id = 1L;
        ResponseDto responseDto = new ResponseDto(ResponseStatusConstants.STATUS_200, ResponseStatusConstants.MESSAGE_200);

        when(itemService.addItemStockQtyById(Mockito.eq(id), Mockito.any(ItemDto.class))).thenReturn(true);

        mockMvc.perform(patch(ItemConstants.ITEM_API_URI + "/stock/" + id + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stockQty\": 10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(responseDto.getStatusCode()))
                .andExpect(jsonPath("$.statusMsg").value(responseDto.getStatusMsg()));
    }

    @Test
    void shouldAddStockQty_whenItemIdNotExist() throws Exception {
        Long id = 1L;
        ResponseDto responseDto = new ResponseDto(ResponseStatusConstants.STATUS_417, ResponseStatusConstants.MESSAGE_417_UPDATE);

        when(itemService.addItemStockQtyById(Mockito.eq(id), Mockito.any(ItemDto.class))).thenReturn(false);

        mockMvc.perform(patch(ItemConstants.ITEM_API_URI + "/stock/" + id + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stockQty\": 10}"))
                .andExpect(status().isExpectationFailed())
                .andExpect(jsonPath("$.statusCode").value(responseDto.getStatusCode()))
                .andExpect(jsonPath("$.statusMsg").value(responseDto.getStatusMsg()));
    }

    @Test
    void shouldAddStockQty_whenItemCodeExist() throws Exception {
        String code = "CODE123456";
        ResponseDto responseDto = new ResponseDto(ResponseStatusConstants.STATUS_200, ResponseStatusConstants.MESSAGE_200);

        when(itemService.addItemStockQtyByCode(Mockito.eq(code), Mockito.any(ItemDto.class))).thenReturn(true);

        mockMvc.perform(patch(ItemConstants.ITEM_API_URI + "/stock/code/" + code + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stockQty\": 10}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode").value(responseDto.getStatusCode()))
                .andExpect(jsonPath("$.statusMsg").value(responseDto.getStatusMsg()));

    }

    @Test
    void shouldAddStockQty_whenItemCodeNotExist() throws Exception {
        String code = "CODE123456";
        ResponseDto responseDto = new ResponseDto(ResponseStatusConstants.STATUS_417, ResponseStatusConstants.MESSAGE_417_UPDATE);

        when(itemService.addItemStockQtyByCode(Mockito.eq(code), Mockito.any(ItemDto.class))).thenReturn(false);

        mockMvc.perform(patch(ItemConstants.ITEM_API_URI + "/stock/code/" + code + "/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"stockQty\": 10}"))
                .andExpect(status().isExpectationFailed())
                .andExpect(jsonPath("$.statusCode").value(responseDto.getStatusCode()))
                .andExpect(jsonPath("$.statusMsg").value(responseDto.getStatusMsg()));

    }
}