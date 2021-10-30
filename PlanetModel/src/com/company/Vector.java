package com.company;

import java.util.ArrayList;

public class Vector {
    public double rad;
    public double force;

    public Vector(double direction, double magnitude){
        this.rad = direction;
        this.force = magnitude;
    }

    public double xForce(){
        return Math.cos(rad)*force;
    }

    public double yForce(){
        return Math.sin(rad)*force;
    }
}
