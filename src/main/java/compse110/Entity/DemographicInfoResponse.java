package compse110.Entity;

public class DemographicInfoResponse {
    private int population;
    private double landArea;
    private double populationDensity;

    public DemographicInfoResponse(int population, double landArea, double populationDensity) {
        this.population = population;
        this.landArea = landArea;
        this.populationDensity = populationDensity;
    }

    public DemographicInfoResponse() {}

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

