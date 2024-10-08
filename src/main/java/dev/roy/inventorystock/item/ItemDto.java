package dev.roy.inventorystock.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class            ItemDto {

    private Long id;
    private String code;
    private String name;
    private String description;
    private int stockQty;

}
