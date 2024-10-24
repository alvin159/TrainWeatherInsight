package compse110.Utils;

import compse110.Entity.Station;
import compse110.Entity.WeatherRequest;
import compse110.Entity.WeatherResponse;
import compse110.Entity.TrainRequest;
import compse110.Entity.TrainResponse;
import compse110.Entity.DemographicRequest;
import compse110.Entity.DemographicResponse;
import java.util.List;


// This class defines the events that can be sent and received by the components
public class Events {
    // Define the types of events that can be sent and received
    public enum EventType {
        ABBREVIATION_REQUEST,
        ABBREVIATION_RESPONSE,
        WEATHER_REQUEST,
        WEATHER_RESPONSE,
        DEMOGRAPHIC_REQUEST,
        DEMOGRAPHIC_RESPONSE,
        TRAIN_REQUEST,
        TRAIN_RESPONSE,
        SEARCH_STATION_REQUEST,
        SEARCH_STATION_RESPONSE,
        ERROR_RESPONSE
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

    public static class TrainRequestEvent {
        public static final EventType TOPIC = EventType.TRAIN_REQUEST;

        public static class Payload implements EventPayload {
            private final TrainRequest trainRequest;

            public Payload(TrainRequest trainRequest) {
                this.trainRequest = trainRequest;
            }

            public TrainRequest getTrainRequest() {
                return trainRequest;
            }
        }
    }

    public static class TrainResponseEvent {
        public static final EventType TOPIC = EventType.TRAIN_RESPONSE;

        public static class Payload implements EventPayload {
            private final TrainResponse trainResponse;

            public Payload(TrainResponse trainResponse) {
                this.trainResponse = trainResponse;
            }

            public TrainResponse getTrainResponse() {
                return trainResponse;
            }
        }
    }

    public static class DemographicRequestEvent {
        public static final EventType TOPIC = EventType.DEMOGRAPHIC_REQUEST;

        public static class Payload implements EventPayload {
            private final DemographicRequest demographicRequest;

            public Payload(DemographicRequest demographicRequest) {
                this.demographicRequest = demographicRequest;
            }

            public DemographicRequest getDemographicRequest() {
                return demographicRequest;
            }
        }
    }

    public static class DemographicResponseEvent {
        public static final EventType TOPIC = EventType.DEMOGRAPHIC_RESPONSE;

        public static class Payload implements EventPayload {
            private final DemographicResponse demographicResponse;

            public Payload(DemographicResponse demographicResponse) {
                this.demographicResponse = demographicResponse;
            }

            public DemographicResponse getDemographicResponse() {
                return demographicResponse;
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

    public static class SearchStationRequest {
        public static final EventType TOPIC = EventType.SEARCH_STATION_REQUEST;

        public static class Payload implements EventPayload {
            private final String currentSearchInput;
            private final String textFieldId;

            public Payload(String currentSearchInput, String textFieldId) {
                this.currentSearchInput = currentSearchInput;
                this.textFieldId = textFieldId;
            }

            public String getCurrentSearchInput() {
                return currentSearchInput;
            }

            public String getTextFieldId() {
                return textFieldId;
            }
        }
    }

    public static class SearchStationResponse {
        public static final EventType TOPIC = EventType.SEARCH_STATION_RESPONSE;

        public static class Payload implements EventPayload {
            private final List<Station> stationList;
            private final String textFieldId;

            public Payload(List<Station> stationList, String textFieldId) {
                this.stationList = stationList;
                this.textFieldId = textFieldId;
            }

            public List<Station> getStationList() {
                return stationList;
            }

            public String getTextFieldId() {
                return textFieldId;
            }
        }
    }

}
