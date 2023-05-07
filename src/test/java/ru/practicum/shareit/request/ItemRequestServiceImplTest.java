package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestWithItemsDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(value = "/testSchema.sql")
public class ItemRequestServiceImplTest {
    private final ItemRequestService mockItemRequestService;
    ItemRequestWithItemsDto responseItemRequestDto1;
    ItemRequestDto requestItemRequestDto3;
    ItemRequestDto responseItemRequestDto3;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    long requestorId = 1L;
    LocalDateTime created = LocalDateTime.now();
    int start = 0;
    int size = 10;

    @BeforeEach
    void setUp() {
        requestItemRequestDto3 = ItemRequestDto.builder()
                .description("Хотел бы воспользоваться щёткой для обуви")
                .build();

        responseItemRequestDto3 = ItemRequestDto.builder()
                .id(3L)
                .description("Хотел бы воспользоваться щёткой для обуви")
                .created(created)
                .build();

        responseItemRequestDto1 = ItemRequestWithItemsDto.builder()
                .id(1L)
                .description("description")
                .created(LocalDateTime.parse("2023-06-10T12:00:00", formatter))
                .items(List.of())
                .build();
    }

    @Test
    void createTest_checkData() {

        ItemRequestDto itemRequestDto = mockItemRequestService.createItemRequest(requestorId, requestItemRequestDto3);
        itemRequestDto.setCreated(created);

        assertEquals(responseItemRequestDto3, itemRequestDto);
    }

    @Test
    void getAllItemRequestByOwner_checkData() {

        List<ItemRequestWithItemsDto> itemRequestDto =
                mockItemRequestService.getItemRequestOtherRequestor(requestorId, start, size);

        assertEquals(1, itemRequestDto.size());
    }

    @Test
    void getItemRequestOtherRequestor_checkData() {

        ItemRequestDto itemRequestDto = mockItemRequestService.createItemRequest(requestorId, requestItemRequestDto3);
        itemRequestDto.setCreated(created);
        assertEquals(responseItemRequestDto3, itemRequestDto);
    }

    @Test
    void getItemRequestById_checkData() {
        long itemRequestId = 1L;

        ItemRequestWithItemsDto itemRequestDto = mockItemRequestService.getItemRequestById(itemRequestId, requestorId);
        assertEquals(responseItemRequestDto1, itemRequestDto);
    }
}
