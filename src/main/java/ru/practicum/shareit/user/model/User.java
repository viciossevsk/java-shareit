package ru.practicum.shareit.user.model;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    @Column(name = "email", unique = true)
    @Email
    private String email;
    @ToString.Exclude
    @OneToMany(mappedBy = "booker", fetch = FetchType.LAZY)
    private Set<Booking> bookings;

    @ToString.Exclude
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<Item> items;

    @ToString.Exclude
    @OneToMany(mappedBy = "booker", fetch = FetchType.LAZY)
    private Set<Comment> comments;
}
