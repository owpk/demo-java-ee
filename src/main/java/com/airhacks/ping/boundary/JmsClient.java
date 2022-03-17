package com.airhacks.ping.boundary;

import javax.jms.*;

/**
 * @author Vorobyev Vyacheslav
 */
public class JmsClient {

    private static class MyMessageListener implements MessageListener {

        @Override
        public void onMessage(Message message) {
            System.out.println("MSG RECEIVED: " + message);
            try {
                if (message instanceof TextMessage) {
                    String msg = ((TextMessage) message).getText();
                    System.out.println("String message recieved >> " + msg);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}