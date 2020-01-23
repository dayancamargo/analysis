package com.test.analysis.processor;

import com.test.analysis.model.DataFile;
import com.test.analysis.model.Sale;
import com.test.analysis.model.SaleReport;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;

@Slf4j
@Component
public class ReportProcessor implements Processor {

    @Value("${file.success.suffix}")
    private String suffix;

    @Override
    public void process(Exchange exchange) {

        SaleReport message;
        DataFile dataFile = exchange.getMessage().getBody(DataFile.class);

        if(Objects.isNull(dataFile)){
            message = new SaleReport();

        } else {

            message = new SaleReport(getQuantity(dataFile.getCustomerList()),
                                     getQuantity(dataFile.getSalesmanList()),
                                     betterSale(dataFile.getSaleList()),
                                     worstSalesman(dataFile.getSaleList()));
        }

        exchange.getMessage().setBody(message);
        exchange.getMessage().setHeader("CamelFileName", generateFileName(exchange));
    }

    private String generateFileName(Exchange exchange) {

        String fullPath = exchange.getMessage().getHeader("CamelFileName", String.class);

        int i = StringUtils.lastIndexOf(fullPath, '.');
        if (i > 0) {
            return StringUtils.overlay(fullPath, suffix, i, i);
        }

        return fullPath;
    }

    private Integer getQuantity(List list) {
        if(CollectionUtils.isEmpty(list)) {
            return 0;
        } else {
            return list.size();
        }
    }

    private String betterSale(List<Sale> saleList) {

        if(CollectionUtils.isEmpty(saleList)){
            return "File without sales";
        }

        Sale sale = saleList
                .stream()
                .max(Comparator.comparing(Sale::getTotalSaleValue))
                .orElse(null);

        if(sale != null) {
            log.debug("Best sale: {}", sale);
            return sale.getId();
        } else {
            return "Cannot found best sale";
        }
    }

    private String worstSalesman(List<Sale> saleList){

        if(CollectionUtils.isEmpty(saleList)){
            return "File without salesman";
        }

        Map<String, Double> salesmanValues = saleList.stream().collect(groupingBy(Sale::getSalesman, summingDouble(Sale::getTotalSaleValue)));

        if (!salesmanValues.isEmpty()) {
            log.debug("Salesman sales sum: {}", salesmanValues);
            return Collections.min(salesmanValues.entrySet(), Map.Entry.comparingByValue()).getKey();
        } else {
            return "Cannot found worst salesman";
        }
    }
}
