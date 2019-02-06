package com.testprojects.camelactivemq.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;

public class SimpleRouteBuilder extends RouteBuilder {
		//Configure route for jms component
	@Override
	public void configure() throws Exception {
		
		
		// EIP Splitter
		//Move file content to queue using EIPsplitter to split contents based on new line
		from("file:C:/InputFolder").split().tokenize("\n").to("jms:queue:camel.test.out");	
		
		//EIP Content based Routing
		from("jms:queue:camel.test.out").
		choice().
		when(body().contains("first")).
		to("jms:queue:camel.test.first").
		when(body().contains("second")).
		to("jms:queue:camel.test.second").
		otherwise().
		to("jms:queue:camel.test.otherwise");
		
		/*
		//EIP Message Filter - if condition not met, discard queues
		from("jms:queue:camel.test.otherwise").
		filter(body().contains("activemq")).
		to("jms:queue:camel.test.first");		
		*/
		/*  EIP Recipient		
		 *  Dynamically set the queue name using message content

		from("file:C:/InputFolder").split().tokenize("\n").to("direct:test");	
		from("direct:test")
		.process(new Processor(){
			public void process(Exchange exchange) throws Exception{
				String recipient = exchange.getIn().getBody().toString();
				String recipientQueue = "jms:queue:"+recipient;
				exchange.getIn().setHeader("queue", recipientQueue);
			}
		}).recipientList(header("queue"));
		*/
		/*
		//EIP WireTap
		from("jms:queue:camel.test.first").split().tokenize("\n").to("direct:test1");
		//Send message to 2 destinations
		from("direct:test1").
		wireTap("jms:queue:camel.test.deadletterqueue").
		to("direct:test2");
		
		from("direct:test2").
		process(new Processor() {
			public void process(Exchange exchange) throws Exception{				
				System.out.println("Processing based on content");
			}
		}).choice().
		when(body().contains("activemq")).
		to("jms:queue:dynamic.queue.1").
		when(body().contains("second")).
		to("jms:queue:dynamic.queue.2").
		otherwise().
		to("jms:queue:camel.test.otherwise");*/
		
	}
}
