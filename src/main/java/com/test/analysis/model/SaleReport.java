package com.test.analysis.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class SaleReport extends BaseModel {

    private Integer customerQuantity;
    private Integer salesmanQuantity;
    private String betterSale;
    private String worstSalesman;

    public SaleReport() {
        customerQuantity = 0;
        salesmanQuantity = 0;
        betterSale = "Cannot found best sale";
        worstSalesman = "Cannot found worst salesman";
    }

    @Override
    public String toString() {

        return  String.format("Customers: %s Salesmen: %s Better Sale Id: %s Worst Salesman: %s",
                customerQuantity,
                salesmanQuantity,
                betterSale,
                worstSalesman);
    }
}
