package compse110.Utils;

import compse110.Entity.Station;
import compse110.Entity.WeatherRequest;
import compse110.Entity.WeatherResponse;
import compse110.frontend.Entity.TrainInformation;
import compse110.Entity.DemographicRequest;
import compse110.Entity.DemographicResponse;
import java.util.List;
import java.util.Date;


public class Events {
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
        SEARCH_STATION_RESPONSE
    }

    public static class WeatherRequestEvent {
        public static final EventType TOPIC = EventType.WEATHER_REQUEST;

        public static class Payload implements EventPayload {
            private final WeatherRequest weatherRequest;
            private String errorMessage;

            public Payload(WeatherRequest weatherRequest) {
                this.weatherRequest = weatherRequest;
            }

            public WeatherRequest getWeatherRequest() {
                return weatherRequest;
            }

            @Override
            public String getErrorMessage() {
                return errorMessage;
            }

            @Override
            public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
            }
        }
    }

    public static class WeatherResponseEvent {
        public static final EventType TOPIC = EventType.WEATHER_RESPONSE;

        public static class Payload implements EventPayload {
            private final WeatherResponse weatherResponse;
            private String errorMessage;

            public Payload(WeatherResponse weatherResponse) {
                this.weatherResponse = weatherResponse;
            }

            public WeatherResponse getWeatherResponse() {
                return weatherResponse;
            }

            @Override
            public String getErrorMessage() {
                return errorMessage;
            }

            @Override
            public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
            }
        }
    }

    public static class TrainRequestEvent {
        public static final EventType TOPIC = EventType.TRAIN_REQUEST;

        public static class Payload implements EventPayload {
            private final Date departingDate;
            private final String departureStationShortCode;
            private final String arrivalStationShortCode;
            private String errorMessage;

            public Payload(Date departingDate, String departureStationShortCode, String arrivalStationShortCode) {
                this.departingDate = departingDate;
                this.departureStationShortCode = departureStationShortCode;
                this.arrivalStationShortCode = arrivalStationShortCode;
            }

            public Date getDepartingDate() {
                return departingDate;
            }

            public String getDepartureStationShortCode() {
                return departureStationShortCode;
            }

            public String getArrivalStationShortCode() {
                return arrivalStationShortCode;
            }

            @Override
            public String getErrorMessage() {
                return errorMessage;
            }

            @Override
            public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
            }
        }
    }

    public static class TrainResponseEvent {
        public static final EventType TOPIC = EventType.TRAIN_RESPONSE;

        public static class Payload implements EventPayload {
            private final List<TrainInformation> trainInformationList;
            private String errorMessage;

            public Payload(List<TrainInformation> trainInformationList) {
                this.trainInformationList = trainInformationList;
            }

            public List<TrainInformation> getTrainInformationList() {
                return trainInformationList;
            }

            @Override
            public String getErrorMessage() {
                return errorMessage;
            }

            @Override
            public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
            }
        }
    }

    public static class DemographicRequestEvent {
        public static final EventType TOPIC = EventType.DEMOGRAPHIC_REQUEST;

        public static class Payload implements EventPayload {
            private final DemographicRequest demographicRequest;
            private String errorMessage;

            public Payload(DemographicRequest demographicRequest) {
                this.demographicRequest = demographicRequest;
            }

            public DemographicRequest getDemographicRequest() {
                return demographicRequest;
            }

            @Override
            public String getErrorMessage() {
                return errorMessage;
            }

            @Override
            public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
            }
        }
    }

    public static class DemographicResponseEvent {
        public static final EventType TOPIC = EventType.DEMOGRAPHIC_RESPONSE;

        public static class Payload implements EventPayload {
            private final DemographicResponse demographicResponse;
            private String errorMessage;

            public Payload(DemographicResponse demographicResponse) {
                this.demographicResponse = demographicResponse;
            }

            public DemographicResponse getDemographicResponse() {
                return demographicResponse;
            }

            @Override
            public String getErrorMessage() {
                return errorMessage;
            }

            @Override
            public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
            }
        }
    }

    public static class AbbreviationRequest {
        public static final EventType TOPIC = EventType.ABBREVIATION_REQUEST;

        public static class Payload implements EventPayload {
            private final String stationShortCode;
            private String errorMessage;

            public Payload(String stationShortCode) {
                this.stationShortCode = stationShortCode;
            }

            public String getStationShortCode() {
                return stationShortCode;
            }

            @Override
            public String getErrorMessage() {
                return errorMessage;
            }

            @Override
            public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
            }
        }
    }

    public static class AbbreviationResponse {
        public static final EventType TOPIC = EventType.ABBREVIATION_RESPONSE;

        public static class Payload implements EventPayload {
            private final Station station;
            private String errorMessage;

            public Payload(Station station) {
                this.station = station;
            }

            public Station getStation() {
                return station;
            }

            @Override
            public String getErrorMessage() {
                return errorMessage;
            }

            @Override
            public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
            }
        }
    }

    public static class SearchStationRequest {
        public static final EventType TOPIC = EventType.SEARCH_STATION_REQUEST;

        public static class Payload implements EventPayload {
            private final String currentSearchInput;
            private final String textFieldId;
            private String errorMessage;

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

            @Override
            public String getErrorMessage() {
                return errorMessage;
            }

            @Override
            public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
            }
        }
    }

    public static class SearchStationResponse {
        public static final EventType TOPIC = EventType.SEARCH_STATION_RESPONSE;

        public static class Payload implements EventPayload {
            private final List<Station> stationList;
            private final String textFieldId;
            private String errorMessage;

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

            @Override
            public String getErrorMessage() {
                return errorMessage;
            }

            @Override
            public void setErrorMessage(String errorMessage) {
                this.errorMessage = errorMessage;
            }
        }
    }
}
