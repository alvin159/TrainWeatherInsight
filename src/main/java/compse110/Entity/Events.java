package compse110.Entity;

// This class defines the events that can be sent and received by the components
public class Events {
    // Define the types of events that can be sent and received
    public enum EventType {
        ABBREVIATION_REQUEST,
        ABBREVIATION_RESPONSE,
        ERROR_RESPONSE
    }

    public static class AbbreviationRequest {
        public static final EventType TOPIC = EventType.ABBREVIATION_REQUEST;

        public static class Payload {
            private String stationShortCode;

            public Payload(String stationShortCode) {
                this.stationShortCode = stationShortCode;
            }

            public String getStationShortCode() {
                return stationShortCode;
            }
        }
    }

    public static class AbbreviationResponse {
        public static final EventType TOPIC = EventType.ABBREVIATION_RESPONSE;

        public static class Payload {
            private Station station;

            public Payload(Station station) {
                this.station = station;
            }

            public Station getStation() {
                return station;
            }
        }
    }
}
