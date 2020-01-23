package com.test.analysis.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Item extends BaseModel {
    private String id;
    private Long itemQuantity;
    private Double price;

    public Item(String id, Long itemQuantity, Double price) {
        this.id = id;
        this.itemQuantity = itemQuantity;
        this.price = price;
    }

    public Double getTotalPrice() {
        Long qt = itemQuantity != null ? itemQuantity : 0;
        Double pri = price != null ? price : 0;
        return pri * qt;
    }
}
