package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemRequestService mockItemRequestService;
    @Autowired
    private MockMvc mvc;
    private final LocalDateTime localDateTime = LocalDateTime.now();
    private long requestorId = 1L;
    private long itemRequestId = 1L;
    private int from = 0;
    private int size = 5;
    private ItemRequestDto itemDtoRequest = ItemRequestDto.builder()
            .description("description")
            .created(localDateTime)
            .build();
    private ItemRequestDto itemDtoResponse = ItemRequestDto.builder()
            .id(1L)
            .description("description")
            .created(localDateTime)
            .build();

    private ItemRequestWithItemsDto itemResponseWithItemsDto = ItemRequestWithItemsDto.builder()
            .id(1L)
            .description("description")
            .created(localDateTime)
            .build();

    @Test
    void createItemRequest_thenRequest_whenResponseOk() throws Exception {

        when(mockItemRequestService.createItemRequest(anyLong(), any(ItemRequestDto.class)))
                .thenReturn(itemDtoResponse);

        mvc.perform(post("/requests")
                            .content(mapper.writeValueAsString(itemDtoRequest))
                            .header("X-Sharer-User-Id", requestorId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.description", is(itemDtoResponse.getDescription().toString())))
                .andExpect(jsonPath("$.created",
                                    is(itemDtoResponse.getCreated().format(DateTimeFormatter.ISO_DATE_TIME))));

        verify(mockItemRequestService).createItemRequest(anyLong(), any(ItemRequestDto.class));
    }


    @Test
    void getAllItemRequestByRequestor_thenRequest_whenResponseOk() throws Exception {

        when(mockItemRequestService.getAllItemRequestByOwner(anyLong()))
                .thenReturn(List.of(itemResponseWithItemsDto));

        mvc.perform(get("/requests")
                            .header("X-Sharer-User-Id", requestorId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockItemRequestService).getAllItemRequestByOwner(anyLong());
    }


    @Test
    void getItemRequestOtherRequestor_thenThrow_whenIsNotFound() throws Exception {

        when(mockItemRequestService.getItemRequestOtherRequestor(anyLong(), eq(from), eq(size)))
                .thenThrow(new EntityNotFoundException("Mistaken user id"));

        mvc.perform(get("/requests/all")
                            .header("X-Sharer-User-Id", requestorId)
                            .param("from", String.valueOf(from))
                            .param("size", String.valueOf(size))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void getItemRequestById_thenRequest_whenStartOneTimes() throws Exception {

        when(mockItemRequestService.getItemRequestById(anyLong(), anyLong()))
                .thenReturn(itemResponseWithItemsDto);

        mvc.perform(get("/requests/{requestId}", itemRequestId)
                            .header("X-Sharer-User-Id", requestorId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockItemRequestService, Mockito.times(1)).getItemRequestById(anyLong(), anyLong());
    }


}
