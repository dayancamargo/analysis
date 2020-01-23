package com.test.analysis.processor;

import com.test.analysis.model.Customer;
import com.test.analysis.model.DataFile;
import com.test.analysis.model.Item;
import com.test.analysis.model.Sale;
import com.test.analysis.model.Salesman;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
public class FileProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {

        DataFile dataFile = new DataFile();

        List<String> lines = getLines(exchange.getIn().getBody(File.class));

        for(String line : lines) {

            log.debug("--------> {}", line);
            StringTokenizer tokens = new StringTokenizer(line, "ç");

            while (tokens.hasMoreElements()) {
                String type = tokens.nextToken();

                if(type.equalsIgnoreCase("001")) {
                    populateSalesman(dataFile, tokens);

                } else if (type.equalsIgnoreCase("002")) {
                    populateCustomer(dataFile, tokens);

                } else if (type.equalsIgnoreCase("003")) {
                    populateSale(dataFile, tokens);

                } else {
                    log.warn("Unknown type: {}", type);
                }
            }
        }

        log.debug("dataFile: {} ", dataFile);

        exchange.getMessage().setBody(dataFile, DataFile.class);
    }

    private void populateSale(DataFile dataFile, StringTokenizer tokens) {

        try {

            String id = tokens.hasMoreElements() ? tokens.nextToken() : null;
            String items = tokens.hasMoreElements() ? tokens.nextToken() : null;

            String salesman = tokens.hasMoreElements() ? tokens.nextToken() : null;

            if(id != null && items != null && salesman != null) {
                Sale sale = new Sale(id, null, salesman);

                String itemList = items.substring(1, items.length() - 1);

                StringTokenizer itemTokens = new StringTokenizer(itemList, ",");

                while (itemTokens.hasMoreElements()) {
                    StringTokenizer token = new StringTokenizer(itemTokens.nextToken(), "-");

                    String itemId = token.hasMoreElements() ? token.nextToken() : null;
                    Long quantity = token.hasMoreElements() ? Long.valueOf(token.nextToken()) : null;
                    Double price  = token.hasMoreElements() ? Double.valueOf(token.nextToken()) : null;

                    if (itemId != null && quantity != null && price != null) {
                        sale.addItem(new Item(itemId, quantity, price));
                    }
                }
                dataFile.addSale(sale);
            }

        } catch (Exception ex) {
            log.error("Cannot create Sale", ex);
        }
    }

    private void populateCustomer(DataFile dataFile, StringTokenizer tokens) {
        try {
            String cnpj = tokens.hasMoreElements() ? tokens.nextToken() : null;
            String name = tokens.hasMoreElements() ? tokens.nextToken() : null;
            String businessArea = tokens.hasMoreElements() ? tokens.nextToken() : null;

            if (cnpj != null && name != null && businessArea != null)
                dataFile.addCustomer(new Customer(cnpj, name, businessArea));

        } catch (Exception ex) {
            log.error("Cannot create Customer", ex);
        }

    }

    private void populateSalesman(DataFile dataFile, StringTokenizer tokens) {
        try {
            String cpf = tokens.hasMoreElements() ? tokens.nextToken() : null;
            String name = tokens.hasMoreElements() ? tokens.nextToken() : null;
            Double salary  = tokens.hasMoreElements() ? Double.valueOf(tokens.nextToken()) : null;


            if(cpf != null && name != null && salary != null)
                dataFile.addSalesman(new Salesman(cpf, name, salary));

        } catch (Exception ex) {
            log.error("Cannot create Salesman", ex);
        }
    }

    private List<String> getLines(File file) {

        if(Objects.isNull(file)){
            return Collections.emptyList();
        }

        try (Stream<String> stream = Files.lines(file.toPath(), StandardCharsets.ISO_8859_1)) {

            return stream.filter(line -> line != null && !line.trim().isEmpty())
                    .collect(Collectors.toList());

        } catch (IOException ex) {
            log.error("Cannot get lines", ex);
            return Collections.emptyList();
        }
    }
}
