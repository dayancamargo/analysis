package com.test.analysis.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class DataFile extends BaseModel {

    List<Salesman> salesmanList;
    List<Customer> customerList;
    List<Sale> saleList;

    public DataFile() {
        salesmanList = new ArrayList<>();
        customerList = new ArrayList<>();
        saleList = new ArrayList<>();
    }

    public void addSale(Sale sale){
        if(CollectionUtils.isEmpty(saleList)) {
            saleList = new ArrayList<>();
        }
        saleList.add(sale);
    }

    public void addCustomer(Customer customer){
        if(CollectionUtils.isEmpty(customerList)) {
            customerList = new ArrayList<>();
        }
        customerList.add(customer);
    }

    public void addSalesman(Salesman salesmen){
        if(CollectionUtils.isEmpty(salesmanList)) {
            salesmanList = new ArrayList<>();
        }
        salesmanList.add(salesmen);
    }
}
