package com.airhacks.ping.boundary;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.jms.*;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("jms")
@Produces(MediaType.APPLICATION_JSON)
public class DemoServlet {

    private static final int MSG_COUNT = 5;

    @Inject
    @JMSConnectionFactory("java:/JmsXA")
    private JMSContext context;

    @Resource(lookup = "java:/queues/OrderQueue")
    private Queue queue;

    @Resource(name = "ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @GET
    @Path("/send")
    public String send() throws JMSException {
        System.out.println("SEND: OK");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        MessageProducer messageProducer = session.createProducer(queue);

        for (int i = 0; i < 3; i++) {
            TextMessage textMessage = session.createTextMessage("MESSAGE: " + i);
            messageProducer.send(textMessage);
        }

        session.close();
        connection.close();
        return "OK";
    }

    @GET
    @Path("/receive")
    public String receive() throws JMSException {
        System.out.println("RECEIVE: OK");
        Connection con = connectionFactory.createConnection();
        con.start();

        Session ses = con.createSession();
        MessageConsumer consumer = ses.createConsumer(queue);
        Message message = consumer.receiveNoWait();
        System.out.println("MESSAGE: " + message);

        con.close();
        ses.close();
        con.close();
        if (message instanceof TextMessage)
            return ((TextMessage) message).getText();
        else return "ERROR";
    }
}