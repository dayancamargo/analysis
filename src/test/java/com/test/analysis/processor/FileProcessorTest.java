package com.test.analysis.processor;

import com.test.analysis.model.DataFile;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

@RunWith(MockitoJUnitRunner.class)
public class FileProcessorTest {
    FileProcessor fileProcessor;

    @Before
    public void setUp() {
        fileProcessor = new FileProcessor();
    }

    @Test
    public void testProcessNullFile() {
        Exchange exchange = createExchange(createFileNull());

        fileProcessor.process(exchange);

        Assert.assertEquals("Sales should have 0 elements", 0, exchange.getIn().getBody(DataFile.class).getSaleList().size());
        Assert.assertEquals("Salesman should have 0 elements", 0, exchange.getIn().getBody(DataFile.class).getSalesmanList().size());
        Assert.assertEquals("Customer should have 0 elements", 0, exchange.getIn().getBody(DataFile.class).getCustomerList().size());
    }

    @Test
    public void testProcessEmptyFile() {
        Exchange exchange = createExchange(createFileEmpty());

        fileProcessor.process(exchange);

        Assert.assertEquals("Sales should have 0 elements", 0, exchange.getIn().getBody(DataFile.class).getSaleList().size());
        Assert.assertEquals("Salesman should have 0 elements", 0, exchange.getIn().getBody(DataFile.class).getSalesmanList().size());
        Assert.assertEquals("Customer should have 0 elements", 0, exchange.getIn().getBody(DataFile.class).getCustomerList().size());
    }

    @Test
    public void testProcessFileSimple() {
        Exchange exchange = createExchange(createFileSimple());

        fileProcessor.process(exchange);

        Assert.assertEquals("Salesman should have 1 elements", 1, exchange.getIn().getBody(DataFile.class).getSalesmanList().size());
        Assert.assertEquals("First salesman should be SalTeste", "SalTeste", exchange.getIn().getBody(DataFile.class).getSalesmanList().get(0).getName());

        Assert.assertEquals("Customer should have 01 elements", 1, exchange.getIn().getBody(DataFile.class).getCustomerList().size());
        Assert.assertEquals("Second customer should be CustTeste", "CustTeste", exchange.getIn().getBody(DataFile.class).getCustomerList().get(0).getName());

        Assert.assertEquals("Sales should have 1 elements", 1, exchange.getIn().getBody(DataFile.class).getSaleList().size());
        Assert.assertEquals("First sales should have 3 itens", 3, exchange.getIn().getBody(DataFile.class).getSaleList().get(0).getItems().size());
    }

    @Test
    public void testProcessWithoutSales() {
        Exchange exchange = createExchange(createFileWithoutSales());

        fileProcessor.process(exchange);

        Assert.assertEquals("Salesman should have 1 elements", 1, exchange.getIn().getBody(DataFile.class).getSalesmanList().size());
        Assert.assertEquals("First salesman should be SalTeste", "SalTeste", exchange.getIn().getBody(DataFile.class).getSalesmanList().get(0).getName());

        Assert.assertEquals("Customer should have 01 elements", 1, exchange.getIn().getBody(DataFile.class).getCustomerList().size());
        Assert.assertEquals("Second customer should be CustTeste", "CustTeste", exchange.getIn().getBody(DataFile.class).getCustomerList().get(0).getName());

        Assert.assertEquals("Sales should have 0 elements", 0, exchange.getIn().getBody(DataFile.class).getSaleList().size());
    }

    @Test
    public void testProcessFileOk() {
        Exchange exchange = createExchange(createFileOk());

        fileProcessor.process(exchange);

        Assert.assertEquals("Sales should have 2 elements", 2, exchange.getIn().getBody(DataFile.class).getSaleList().size());
        Assert.assertEquals("First sales should have 3 itens", 3, exchange.getIn().getBody(DataFile.class).getSaleList().get(0).getItems().size());

        Assert.assertEquals("Salesman should have 2 elements", 2, exchange.getIn().getBody(DataFile.class).getSalesmanList().size());
        Assert.assertEquals("First salesman should be Pedro", "Pedro", exchange.getIn().getBody(DataFile.class).getSalesmanList().get(0).getName());

        Assert.assertEquals("Customer should have 0 elements", 2, exchange.getIn().getBody(DataFile.class).getCustomerList().size());
        Assert.assertEquals("Second customer should be Eduardo", "Eduardo Pereira", exchange.getIn().getBody(DataFile.class).getCustomerList().get(1).getName());
    }

    private File createFileOk(){
        return new File("src/test/resources/testeProva.txt");
    }

    private File createFileSimple(){
        return new File("src/test/resources/testeProvaSimple.txt");
    }

    private File createFileWithoutSales(){
        return new File("src/test/resources/testeProvaWithoutSales.txt");
    }

    private File createFileEmpty(){
        return new File("src/test/resources/testeProvaEmpty.txt");
    }

    private File createFileNull(){
        return null;
    }

    private Exchange createExchange(Object body){
        CamelContext ctx = new DefaultCamelContext();
        Exchange exchange = new DefaultExchange(ctx);
        exchange.getIn().setBody(body);
        return  exchange;
    }
}
