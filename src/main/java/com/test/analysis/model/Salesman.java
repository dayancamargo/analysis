package com.test.analysis.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Salesman extends BaseModel {
    private String cpf;
    private String name;
    private Double salary;

    public Salesman(String cpf, String name, Double salary) {
        this.cpf = cpf;
        this.name = name;
        this.salary = salary;
    }
}
