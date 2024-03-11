package dev.roy.inventorystock.order;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {

    private Long id;
    private String referenceCode;
    private int invoiceQuantity;
    private Date invoiceDate;

}
