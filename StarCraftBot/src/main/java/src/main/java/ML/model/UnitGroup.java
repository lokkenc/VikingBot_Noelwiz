package src.main.java.ML.model;

import bwapi.Unit;

import java.util.ArrayList;

/*
 * Class: Units
 * Description: Represents a collection of units
 */
public class UnitGroup {
    private ArrayList<Unit> units;

    public UnitGroup(ArrayList<Unit> units) {
        this.units = units;
    }

    public ArrayList<Unit> getUnits() {return this.units;}

    public int getNumberOfUnits() {
        return units.size();
    }

    /**
     * This function returns a Unit that is closest the center of all units
     * @return Unit that is located closest to the center
     */
    public Unit getCentralUnit() {

        // Basically this just gets the mins and maxes for x and y of all units
        int[] minMaxX = new int[2];
        int[] minMaxY = new int[2];
        minMaxX[0] = minMaxY[0] = Integer.MAX_VALUE;
        minMaxX[1] = minMaxY[1] = Integer.MIN_VALUE;
        for(Unit unit: this.units) {
            if(unit.getX() < minMaxX[0]) {
                minMaxX[0] = unit.getX();
            } else if(unit.getX() > minMaxX[1]) {
                minMaxX[1] = unit.getX();
            }

            if(unit.getY() < minMaxY[0]) {
                minMaxY[0] = unit.getY();
            } else if(unit.getY() > minMaxY[1]) {
                minMaxY[1] = unit.getY();
            }
        }

        // This now uses those mins and maxes to find the center point and then finds the unit closest to that point
        Unit centralUnit = null;
        int minDifference = Integer.MAX_VALUE;
        int[] midpoint = new int[2];
        midpoint[0] = minMaxX[1] - ((minMaxX[1] - minMaxX[0]) / 2);
        midpoint[1] = minMaxY[1] - ((minMaxY[1] - minMaxY[0]) / 2);
        for(Unit unit: this.units) {
            if(Math.abs((midpoint[0] - unit.getX()) + (midpoint[1] - unit.getY())) < minDifference) {
                centralUnit = unit;
                minDifference = Math.abs((midpoint[0] - unit.getX()) + (midpoint[1] - unit.getY()));
            }
        }

        return centralUnit;
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