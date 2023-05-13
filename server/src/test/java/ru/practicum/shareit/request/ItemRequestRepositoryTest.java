package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Transactional
@SpringBootTest
@AutoConfigureTestDatabase
@Sql(value = "/testSchema.sql")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestRepositoryTest {
    private final ItemRequestRepository mockItemRequestRepository;

    long requestorId = 1L;


    @Test
    void findByRequestorId_Test() {

        List<ItemRequest> itemRequests = mockItemRequestRepository.findByRequestorId(requestorId);

        assertEquals(1, itemRequests.size());
    }
}
