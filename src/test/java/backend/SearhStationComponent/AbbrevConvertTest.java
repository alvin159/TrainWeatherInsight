package backend.SearhStationComponent;

import compse110.backend.SearhStationComponent.AbbrevConvert;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbbrevConvertTest {

    @Test
    public void testGetStationFullName() {
        AbbrevConvert abbrevConvert = new AbbrevConvert();
        try {
            // Positive testing
            // Getting same station twice should use the cache on the second time
            assertEquals("Helsinki asema", abbrevConvert.getStationFullName("HKI"));
            assertEquals("Tampere asema", abbrevConvert.getStationFullName("TPE"));
            assertEquals("Tampere asema", abbrevConvert.getStationFullName("TPE"));
            assertEquals("Oulu asema", abbrevConvert.getStationFullName("OL"));
            assertEquals("Oulu asema", abbrevConvert.getStationFullName("OL"));

            // Negative testing
            // Getting a station that does not exist should return null
            assertEquals(null, abbrevConvert.getStationFullName("New York"));
            assertEquals(null, abbrevConvert.getStationFullName(""));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}