package ru.practicum.shareit.item.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.EntityNotFoundException;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    private final LocalDateTime created = LocalDateTime.now();
    @Autowired
    ObjectMapper mapper;
    @MockBean
    ItemService mockItemService;
    CommentDto commentDto = CommentDto.builder()
            .id(1L)
            .text("Good comment")
            .authorName("authorName")
            .created(created)
            .itemId(1L)
            .build();
    @Autowired
    private MockMvc mvc;
    private long ownerId = 1L;
    private long itemId = 1L;
    private int from = 0;
    private int size = 5;
    private ItemDto itemDtoRequest = ItemDto.builder()
            .name("name")
            .description("description")
            .available(true)
            .requestId(1L)
            .build();
    private ItemDto itemDtoResponse = ItemDto.builder()
            .id(1L)
            .name("name")
            .description("description")
            .available(true)
            .requestId(1L)
            .build();

    @Test
    void getItemById_thenItemIdFail_whenEntityNotFoundException() throws Exception {
        when(mockItemService.getItemById(anyLong(), anyLong()))
                .thenThrow(new EntityNotFoundException("Mistaken Item id"));

        mvc.perform(get("/items/{id}", itemId)
                            .header("X-Sharer-User-Id", ownerId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());

        verify(mockItemService).getItemById(anyLong(), anyLong());
    }

    @Test
    void createItem_thenRequest_whenResponseOk() throws Exception {
        when(mockItemService.createItem(any(ItemDto.class), anyLong()))
                .thenReturn(itemDtoResponse);

        mvc.perform(post("/items")
                            .content(mapper.writeValueAsString(itemDtoRequest))
                            .header("X-Sharer-User-Id", ownerId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDtoResponse.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDtoResponse.getName().toString())))
                .andExpect(jsonPath("$.description", is(itemDtoResponse.getDescription().toString())))
                .andExpect(jsonPath("$.available", is(itemDtoResponse.getAvailable()), Boolean.class))
                .andExpect(jsonPath("$.requestId", is(itemDtoResponse.getRequestId().intValue())));

        verify(mockItemService).createItem(any(ItemDto.class), anyLong());
    }

    @Test
    void updateItem_thenNewName_whenCheckName() throws Exception {
        itemDtoResponse.setName("New name");
        when(mockItemService.updateItem(any(ItemDto.class), anyLong(), anyLong()))
                .thenReturn(itemDtoResponse);

        mvc.perform(patch("/items/{id}", itemId)
                            .content(mapper.writeValueAsString(itemDtoRequest))
                            .header("X-Sharer-User-Id", ownerId)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("New name")));

        verify(mockItemService).updateItem(any(ItemDto.class), anyLong(), anyLong());
    }

    @Test
    void getAllItemsByOwner_checkParam() throws Exception {
        when(mockItemService.getAllItemsByOwner(eq(ownerId), eq(from), eq(size)))
                .thenReturn(List.of(itemDtoResponse));

        mvc.perform(get("/items/{id}", itemId)
                            .header("X-Sharer-User-Id", ownerId)
                            .param("from", String.valueOf(from))
                            .param("size", String.valueOf(size))
                            .characterEncoding(StandardCharsets.UTF_8)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockItemService, never()).getAllItemsByOwner(eq(ownerId), eq(from), eq(size));
    }

    @Test
    void deleteItem_thenRequest_whenResponseOk() throws Exception {
        mvc.perform(delete("/items/{id}", itemId)
                            .header("X-Sharer-User-Id", ownerId))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockItemService).deleteItem(eq(itemId), anyLong());
    }

    @Test
    void searchItemByText_thenRequest_whenResponseOk() throws Exception {
        String searchRequest = "searchRequest";
        when(mockItemService.searchItemByText(eq(searchRequest), eq(from), eq(size))).thenReturn(List.of(itemDtoResponse));

        mvc.perform(get("/items/search")
                            .param("text", searchRequest)
                            .param("from", String.valueOf(from))
                            .param("size", String.valueOf(size)))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockItemService).searchItemByText(eq(searchRequest), eq(from), eq(size));
    }

    @Test
    void createComment_thenRequest_whenResponseOk() throws Exception {
        mvc.perform(post("/items/{id}/comment", itemId)
                            .content(mapper.writeValueAsString(commentDto))
                            .header("X-Sharer-User-Id", ownerId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .characterEncoding(StandardCharsets.UTF_8)
                            .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

        verify(mockItemService, never()).createComment(anyLong(), anyLong(), eq(commentDto));
    }


}
