package dev.roy.inventorystock.item;

import dev.roy.inventorystock.audit.AuditEntity;
import dev.roy.inventorystock.order.Order;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "items")
public class Item extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Name can not be a null or empty")
    @Pattern(regexp = "CODE(^$|[0-9]{6})", message = "Code must start with CODE and 6 digits")
    private String code;

    @NotEmpty(message = "Name can not be a null or empty")
    @Size(min = 5, max = 30, message = "The length of the name should be between 5 and 30")
    private String name;

    @NotEmpty(message = "Description can not be a null or empty")
    @Size(min = 5, max = 50, message = "The length of the name should be between 5 and 50")
    private String description;

    private int stockQty;

    @OneToMany(mappedBy = "item")
    private List<Order> orders;

}
