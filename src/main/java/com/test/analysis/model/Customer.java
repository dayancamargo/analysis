package com.test.analysis.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer extends BaseModel {

    private String cnpj;
    private String name;
    private String businessArea;

    public Customer(String cnpj, String name, String businessArea) {
        this.cnpj = cnpj;
        this.name = name;
        this.businessArea = businessArea;
    }

}
