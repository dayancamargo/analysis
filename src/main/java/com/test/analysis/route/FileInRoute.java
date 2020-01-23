package com.test.analysis.route;

import com.test.analysis.processor.FileProcessor;
import com.test.analysis.processor.ReportProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class FileInRoute extends RouteBuilder {

    @Autowired
    private FileProcessor fileProcessor;
    @Autowired
    private ReportProcessor reportProcessor;

    @Override
    public void configure() throws Exception {

        onException(Exception.class)
            .log(LoggingLevel.ERROR, "${exception.message}")
        .end();

        from("file:{{file.path.in}}?move={{file.success.dir}}&moveFailed={{file.fail.dir}}&delay={{file.delay.pool}}&charset=ISO-8859-1")
            .routeId("fileIn")
            .process(exchange -> MDC.put("LogId", UUID.randomUUID().toString()))
            .log(LoggingLevel.INFO, "Processing: ${header.CamelFileName}")
            .process(fileProcessor)
            .process(reportProcessor)
            .process(exchange -> {
                exchange.getIn().setBody(exchange.getIn().getBody(String.class));
            })
        .to("file:{{file.path.out}}");
    }
}
