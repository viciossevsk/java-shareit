package ru.practicum.shareit.booking.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Bookings")

public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
    @DateTimeFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    @Column(name = "start_time")
    @FutureOrPresent(message = "Start time can't be the past")
    private LocalDateTime start;
    @DateTimeFormat(pattern = "YYYY-MM-DDTHH:mm:ss")
    @Column(name = "end_time")
    private LocalDateTime end;
    @ToString.Exclude
    @JsonBackReference(value = "item")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;
    @ToString.Exclude
    @JsonBackReference(value = "booker")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "booker_id", referencedColumnName = "id")
    private User booker;
}
