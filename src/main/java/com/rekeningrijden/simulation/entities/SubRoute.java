package com.rekeningrijden.simulation.entities;

import java.util.ArrayList;
import java.util.List;

public class SubRoute {
    private String countryCode; //Country the car is driving in
    private List<Coordinate> coordinates;
    private boolean subRouteDriven;

    public SubRoute(String countryCode) {
        coordinates = new ArrayList<>();
        this.countryCode = countryCode;
        this.subRouteDriven = false;
    }

    public List<Coordinate> getCoordinates() {
        return coordinates;
    }

    public boolean isSubRouteDriven() {
        return subRouteDriven;
    }

    public void setSubRouteDriven(boolean subRouteDriven) {
        this.subRouteDriven = subRouteDriven;
    }

    public void addCoordinate(Coordinate coor) {
        this.coordinates.add(coor);
    }

    public String getCountryCode() {
        return countryCode;
    }

    public Coordinate getNextCoordinateAtIndex(int index){
        if (index >= coordinates.size()){
            this.subRouteDriven = true;
            return null;
        }
        return coordinates.get(index);
    }
}
