package ru.practicum.shareit.item.model;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "Items", indexes = {
        @Index(name = "idx_item_id", columnList = "id")
})
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    @ToString.Exclude
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private Set<Booking> bookings;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private User owner;
    @ToString.Exclude
    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private Set<Comment> comments;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return available == item.available
                && Objects.equals(id, item.id)
                && Objects.equals(name, item.name)
                && Objects.equals(description, item.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, available);
    }
}
