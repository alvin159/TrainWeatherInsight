package compse110.frontend.Entity;

public class CityDetails {

    private int population;

    private double area;

    private double populationDensity;

    public CityDetails(int population, double area, double populationDensity) {
        this.population = population;
        this.area = area;
        this.populationDensity = populationDensity;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getPopulationDensity() {
        return populationDensity;
    }

    public void setPopulationDensity(double populationDensity) {
        this.populationDensity = populationDensity;
    }
}
