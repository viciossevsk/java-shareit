package ru.practicum.shareit.request.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.Collection;
import java.util.List;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    List<ItemRequest> findByRequestorId(Long RequestorId);

    @Query(
            "select ir" +
                    " from ItemRequest ir" +
                    " where ir.requestor.id not in :requestorId"
    )
    Collection<ItemRequest> findAllExceptUserId(@Param("requestorId") Long requestorId, PageRequest page);


}
