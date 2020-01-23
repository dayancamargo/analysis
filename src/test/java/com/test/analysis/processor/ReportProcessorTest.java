package com.test.analysis.processor;

import com.test.analysis.model.Customer;
import com.test.analysis.model.DataFile;
import com.test.analysis.model.Item;
import com.test.analysis.model.Sale;
import com.test.analysis.model.SaleReport;
import com.test.analysis.model.Salesman;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

@RunWith(MockitoJUnitRunner.class)
public class ReportProcessorTest {

    ReportProcessor reportProcessor;
    String suffix = "_testSuf";

    @Before
    public void setUp() throws Exception {
        reportProcessor = new ReportProcessor();
        ReflectionTestUtils.setField(reportProcessor, "suffix", suffix);
    }

    @Test
    public void testProcessNullFile() {
        Exchange exchange = createExchange(null);
        exchange.getMessage().setHeader("CamelFileName", "fileTest.txt");

        reportProcessor.process(exchange);

        SaleReport result = exchange.getIn().getBody(SaleReport.class);

        Assert.assertNotNull("SaleReport should not be null", result);
        Assert.assertEquals("Customer quantity should be 0",0L, result.getCustomerQuantity().longValue());
        Assert.assertEquals("Salesman quantity should be 0",0L, result.getSalesmanQuantity().longValue());
        Assert.assertTrue("Better Sale has some message", StringUtils.isNotEmpty(result.getBetterSale()));
        Assert.assertTrue("Worst Salesman has some message", StringUtils.isNotEmpty(result.getWorstSalesman()));
    }

    @Test
    public void testProcessEmptyFile() {
        Exchange exchange = createExchange(new DataFile());
        exchange.getMessage().setHeader("CamelFileName", "fileTest.txt");

        reportProcessor.process(exchange);

        SaleReport result = exchange.getIn().getBody(SaleReport.class);

        Assert.assertNotNull("SaleReport should not be null", result);
        Assert.assertEquals("Customer quantity should be 0",0L, result.getCustomerQuantity().longValue());
        Assert.assertEquals("Salesman quantity should be 0",0L, result.getSalesmanQuantity().longValue());
        Assert.assertTrue("Better Sale has some message", StringUtils.isNotEmpty(result.getBetterSale()));
        Assert.assertTrue("Worst Salesman has some message", StringUtils.isNotEmpty(result.getWorstSalesman()));
    }

    @Test
    public void testProcessOk() {
        Exchange exchange = createExchange(createMockOk());
        exchange.getMessage().setHeader("CamelFileName", "fileTest.txt");

        reportProcessor.process(exchange);

        SaleReport result = exchange.getIn().getBody(SaleReport.class);

        Assert.assertNotNull("SaleReport should not be null", result);
        Assert.assertEquals("Customer quantity should be 2",2L, result.getCustomerQuantity().longValue());
        Assert.assertEquals("Salesman quantity should be 2",2L, result.getSalesmanQuantity().longValue());
        Assert.assertEquals("Better Sale is 10","10", result.getBetterSale());
        Assert.assertEquals("Worst Salesman is Paulo ", "Paulo", result.getWorstSalesman());
    }

    @Test
    public void testProcessFull() {
        Exchange exchange = createExchange(createMockFull());
        exchange.getMessage().setHeader("CamelFileName", "fileTest.txt");

        reportProcessor.process(exchange);

        SaleReport result = exchange.getIn().getBody(SaleReport.class);

        Assert.assertEquals("Customer quantity should be 5",5L, result.getCustomerQuantity().longValue());
        Assert.assertEquals("Salesman quantity should be 4",4L, result.getSalesmanQuantity().longValue());
        Assert.assertEquals("Better Sale is 05","05", result.getBetterSale());
        Assert.assertEquals("Worst Salesman is Maria ", "Maria", result.getWorstSalesman());
    }

    @Test
    public void testFileName() {

        Exchange exchange = createExchange(null);
        exchange.getMessage().setHeader("CamelFileName", "fileTest.txt");

        reportProcessor.process(exchange);

        Assert.assertNotNull("CamelFileName should not be null", exchange.getMessage().getHeader("CamelFileName"));
        Assert.assertTrue("CamelFileName should not be null",
                          StringUtils.contains(exchange.getMessage().getHeader("CamelFileName").toString(),suffix));
        Assert.assertEquals("CamelFileName should be fileTest_testSuf.txt",
                            "fileTest_testSuf.txt",
                            exchange.getMessage().getHeader("CamelFileName", String::new));
    }

    private Exchange createExchange(Object body){
        CamelContext ctx = new DefaultCamelContext();
        Exchange exchange = new DefaultExchange(ctx);
        exchange.getIn().setBody(body);

        return  exchange;
    }

    private DataFile createMockOk() {
        DataFile result = new DataFile();
        result.addSalesman(new Salesman("1234567891234","Pedro",50000D));
        result.addSalesman(new Salesman("3245678865434","Paulo",40000.99D));

        result.addCustomer(new Customer("2345675434544345", "Jose da Silva", "Rural"));
        result.addCustomer(new Customer("2345675433444345", "Eduardo Pereira", "Rural"));

        result.addSale(new Sale("10",
                 Arrays.asList(new Item("1", 10L, 100D),new Item("2", 30L, 2.5D),new Item("3", 40L, 3.1D)),
                "Pedro"));

        result.addSale(new Sale("08",
                       Arrays.asList(new Item("1", 34L, 10D),new Item("2", 33L, 1.5D),new Item("3", 40L, 0.1D)),
                "Paulo"));

        return result;
    }

    private DataFile createMockFull(){
        DataFile result = createMockOk();

        result.addSalesman(new Salesman("3245678865434","Maria",30000D));
        result.addSalesman(new Salesman("3245678865434","Marcio",20000D));

        result.addCustomer(new Customer("2345675434544345", "Joao Silva", "Comercio"));
        result.addCustomer(new Customer("2345675434544345", "Ted Smith", "Comercio"));
        result.addCustomer(new Customer("2345675434544345", "Blah Blah", "Comercio"));

        result.addSale(new Sale("09",
                Arrays.asList(new Item("1", 34L, 2D),
                              new Item("2", 33L, 1.5D),
                              new Item("3", 40L, 0.1D)),
                "Maria"));

        result.addSale(new Sale("07",
                Arrays.asList(new Item("1", 1L, 10D),
                              new Item("2", 5L, 2.5D)),
                "Maria"));

        result.addSale(new Sale("06",
                Arrays.asList(new Item("1", 50L, 1D),
                              new Item("2", 40L, 1D),
                              new Item("3", 30L, 2D),
                              new Item("4", 20L, 2D)),
                "Maria"));

        result.addSale(new Sale("05",
                Arrays.asList(new Item("1", 10L, 500D)),
                "Marcio"));

        return  result;
    }

}
