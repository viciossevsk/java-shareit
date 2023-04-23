package ru.practicum.shareit.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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
    @JsonManagedReference(value = "booker")
    @OneToMany(mappedBy = "booker", fetch = FetchType.LAZY)
    private Set<Booking> bookings;

    @ToString.Exclude
    @JsonManagedReference(value = "owner")
    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY)
    private Set<Item> items;

    @ToString.Exclude
    @JsonManagedReference(value = "booker_comment")
    @OneToMany(mappedBy = "booker", fetch = FetchType.LAZY)
    private Set<Comment> comments;
}
