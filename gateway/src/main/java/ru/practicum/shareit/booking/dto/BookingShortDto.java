package ru.practicum.shareit.booking2.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class BookingShortDto {
    private Long id;
    private Long bookerId;
    private Long itemId;
}

