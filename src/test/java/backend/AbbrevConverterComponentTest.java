package backend;

import compse110.backend.AbbrevConverterComponent;
import compse110.backend.AbbreviationObject;
import compse110.messagebroker.MessageBroker;
import compse110.messagebroker.MessageCallback;

public class AbbrevConverterComponentTest {

    private static final MessageBroker broker = MessageBroker.getInstance();

    public static void main(String[] args) {

        broker.subscribe("abbrevConverterResponse", new MessageCallback() {
            @Override
            public void onMessageReceived(String topic, Object payload) {
                if (topic.equals("abbrevConverterResponse")) {
                    System.out.println("Response message received topic in backend: " + topic);
                    System.out.println("Response message received payload in backend: " + payload);
                }

            }
        });

        broker.publish("abbrevConverterRequest", new AbbreviationObject("HKI"));
        broker.publish("abbrevConverterRequest", new AbbreviationObject("TPE"));
        broker.publish("abbrevConverterRequest", new AbbreviationObject("OUL"));

    }
}
