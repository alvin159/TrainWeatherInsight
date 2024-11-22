package compse110.Entity;
/**
 * Represents a demographic response containing details about a specific city.
 *
 * <p>This class encapsulates key demographic information, including:
 * <ul>
 *     <li>Total population</li>
 *     <li>Total land area (in square kilometers)</li>
 *     <li>Population density (people per square kilometer)</li>
 * </ul>
 *
 * <p>It is used to store and transfer demographic data between frontend and backend.
 */
public class DemographicResponse {
    private int population;
    private double landArea;
    private double populationDensity;

    public DemographicResponse(int population, double landArea, double populationDensity) {
        this.population = population;
        this.landArea = landArea;
        this.populationDensity = populationDensity;
    }

    public DemographicResponse() {}

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public double getLandArea() {
        return landArea;
    }

    public void setLandArea(double landArea) {
        this.landArea = landArea;
    }

    public double getPopulationDensity() {
        return populationDensity;
    }

    public void setPopulationDensity(double populationDensity) {
        this.populationDensity = populationDensity;
    }

    @Override
    public String toString() {
        return "DemographicInfoResponse{" +
                "population=" + population +
                ", landArea=" + landArea +
                ", populationDensity=" + populationDensity +
                '}';
    }
}

