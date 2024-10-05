package compse110.Utils;

import compse110.Entity.Station;
import compse110.Entity.WeatherRequest;
import compse110.Entity.WeatherResponse;

// This class defines the events that can be sent and received by the components
public class Events {
    // Define the types of events that can be sent and received
    public enum EventType {
        ABBREVIATION_REQUEST,
        ABBREVIATION_RESPONSE,
        ERROR_RESPONSE,
        WEATHER_REQUEST,
        WEATHER_RESPONSE
    }

    public static class WeatherRequestEvent {
        public static final EventType TOPIC = EventType.WEATHER_REQUEST;

        public static class Payload implements EventPayload {
            private final WeatherRequest weatherRequest;

            public Payload(WeatherRequest weatherRequest) {
                this.weatherRequest = weatherRequest;
            }

            public compse110.Entity.WeatherRequest getWeatherRequest() {
                return weatherRequest;
            }
        }
    }

    public static class WeatherResponseEvent {
        public static final EventType TOPIC = EventType.WEATHER_RESPONSE;

        public static class Payload implements EventPayload {
            private final WeatherResponse weatherResponse;

            public Payload(WeatherResponse weatherResponse) {
                this.weatherResponse = weatherResponse;
            }

            public compse110.Entity.WeatherResponse getWeatherResponse() {
                return weatherResponse;
            }
        }
    }

    public static class AbbreviationRequest {
        public static final EventType TOPIC = EventType.ABBREVIATION_REQUEST;

        public static class Payload implements EventPayload {
            private final String stationShortCode;

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

        public static class Payload  implements EventPayload {
            private final Station station;

            public Payload(Station station) {
                this.station = station;
            }

            public Station getStation() {
                return station;
            }
        }
    }
}
