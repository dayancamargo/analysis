package com.test.analysis.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Sale extends BaseModel {
    private String id;
    private List<Item> items;
    private String salesman;

    public Sale(String id, List<Item> items, String salesman) {
        this.id = id;
        this.items = items;
        this.salesman = salesman;
    }

    public void addItem(Item item){
        if(CollectionUtils.isEmpty(items)) {
            items = new ArrayList<>();
        }

        if(item != null) {
            items.add(item);
        }
    }

    public Double getTotalSaleValue(){
        if(CollectionUtils.isEmpty(items)){
            return 0D;
        } else {
            return  items.stream().mapToDouble(i -> i.getTotalPrice()).sum();
        }
    }
}
