package com.testprojects.camelactivemq.main;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;

import com.testprojects.camelactivemq.route.SimpleRouteBuilder;

public class Main {
	
	public static void main(String[] args) {
		
		SimpleRouteBuilder route = new SimpleRouteBuilder();
		CamelContext ctx = new DefaultCamelContext();
		
		// configure jms component
		//Active MQ default port : tcp://0.0.0.0:61616
		ConnectionFactory connFactory = new ActiveMQConnectionFactory("tcp://0.0.0.0:61616");
		
		//Add jms component to CamelContext
		ctx.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connFactory));
				
		try {
			//Add Route to CamelContext
			ctx.addRoutes(route);
			ctx.start();
			Thread.sleep(5*6*1000);
			ctx.stop();
		} catch (Exception e) {			
			e.printStackTrace();
		}

	}

}
