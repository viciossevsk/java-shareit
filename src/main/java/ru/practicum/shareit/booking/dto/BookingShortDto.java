package ru.practicum.shareit.booking.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class BookingShortDto {
    private Long id;
    private Long bookerId;
    private Long itemId;
}

