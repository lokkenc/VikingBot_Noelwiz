package model;

import bwapi.Unit;

import java.util.ArrayList;

/*
 * Class: Units
 * Description: Represents a collection of units
 */
public class Units {
    private ArrayList<Unit> units;

    public Units(ArrayList<Unit> units) {
        this.units = units;
    }

    public ArrayList<Unit> getUnits() {return this.units;}

    public int getNumberOfUnits() {
        return units.size();
    }

    public double getTotalHp() {
        double totalHp = 0.0;

        for(Unit unit : units) {
            totalHp += unit.getHitPoints();
        }

        return totalHp;
    }

    public double getAverageHp() {
        double totalHp = 0.0;
        int numberOfUnits = units.size();

        for(Unit unit : units) {
            totalHp += unit.getHitPoints();
        }

        return totalHp / numberOfUnits;
    }

    // Get the sum of all unit distances
    public double getTotalDistanceFromUnit(Unit source) {
        double totalDistance = 0.0;

        for(Unit unit : units) {
            totalDistance += unit.getDistance(source);
        }

        return totalDistance;
    }

    // Get the average distance of all units
    public double getAverageDistanceFromUnit(Unit source) {
        double totalDistance = 0.0;
        int numberOfUnits = units.size();

        for(Unit unit : units) {
            totalDistance += unit.getDistance(source);
        }

        return totalDistance / numberOfUnits;
    }

    // Get the closest unit
    public Unit getClosestUnitToUnit(Unit source) {
        Unit closest;

        if(!units.isEmpty()) {
            closest = units.get(0);
        }
        else {
            closest = null;
        }

        return closest;
    }

    // Get the farthest unit
    public Unit getFarthestUnitFromUnit(Unit source) {
        Unit farthest;

        if(!units.isEmpty()) {
            farthest = units.get(units.size() - 1);
        }
        else {
            farthest = null;
        }

        return farthest;
    }
}