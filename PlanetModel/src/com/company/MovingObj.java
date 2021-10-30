package com.company;

import java.awt.*;
import java.util.ArrayList;

public class MovingObj {

    public String name;

    public boolean canMove;

    public double mass;
    public double xPos;
    public double yPos;
    public double xPosDraw;
    public double yPosDraw;
    public double xAccel = 0;
    public double yAccel = 0;
    public double force;
    public double xVel;
    public double yVel;
    public double rad;

    public int sizeX;
    public int sizeY;
    public int sizeXDraw;
    public int sizeYDraw;

    public Color color;

    public ArrayList<Vector> vectors = new ArrayList<Vector>();

    public Image pic;

    public MovingObj(String name, double mass, int sizeX, int sizeY, double xPos, double yPos, Color color, MovingObj orbiting, Image pic, boolean canMove){
        System.out.println(orbiting.xPos);
        this.name = name;
        this.mass = mass;
        this.xPos = xPos;
        this.yPos = yPos;
        xPosDraw = this.xPos;
        yPosDraw = this.yPos;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        sizeXDraw = this.sizeX;
        sizeYDraw = this.sizeY;
        this.color = color;
        this.pic = pic;
        this.canMove = canMove;
        setOrbitVel(orbiting);
    }

    public MovingObj (String name, double mass, int sizeX, int sizeY, double xPos, double yPos, Color color, double yVel, double xVel, Image pic, boolean canMove) {
        this.name = name;
        this.mass = mass;
        this.xPos = xPos;
        this.yPos = yPos;
        xPosDraw = this.xPos;
        yPosDraw = this.yPos;
        this.xVel = xVel;
        this.yVel = yVel;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        sizeXDraw = this.sizeX;
        sizeYDraw = this.sizeY;
        this.color = color;
        this.pic = pic;
        this.canMove = canMove;
    }

    public void setOrbitVel(MovingObj orbiting) {
        double G = 0.0005;

        double dx = (xPos + sizeX / 2) - (orbiting.xPos + orbiting.sizeX / 2);
        double dy = (yPos + sizeY / 2) - (orbiting.yPos + orbiting.sizeY / 2);
        double netd = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));

        double netVel = Math.sqrt(G * orbiting.mass / netd);
        double rad1;

        rad1 = Math.atan(dy / dx);
        if (dx > 0) {
            rad1 += Math.PI;
        }

        System.out.println(rad1);

        xVel = (Math.sin(rad1) * netVel) * -1 + orbiting.xVel;
        System.out.println(xVel);
        yVel = (Math.cos(rad1) * netVel) + orbiting.yVel;
        System.out.println(yVel);
    }

    public void setVectors(){
        xAccel = 0;
        yAccel = 0;
        rad = 0;
        force = 0;

        for(int i=0; i<vectors.size(); i++){
            xAccel += vectors.get(i).xForce()/mass;
            yAccel += vectors.get(i).yForce()/mass;
        }
        rad = Math.atan(yAccel/xAccel);
        if(xAccel<0){
            rad += Math.PI;
        }
        vectors.clear();
    }

    public void scale(double scale, MovingObj focusedObj){
        if(this != focusedObj) {
            double dx = (xPos + sizeX / 2) - (focusedObj.xPos + focusedObj.sizeX / 2);
            double dy = (yPos + sizeY / 2) - (focusedObj.yPos + focusedObj.sizeY / 2);
            double netd = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
            double netdScaled = netd * scale;
            double rad1 = Math.atan(dy / dx);
            if (dx > 0) {
                rad1 += Math.PI;
            }

            double dxScaled = Math.cos(rad1) * netdScaled;
            double dyScaled = Math.sin(rad1) * netdScaled;
            xPosDraw = focusedObj.xPos + dxScaled;
            yPosDraw = focusedObj.yPos + dyScaled;
        } else {
            xPosDraw = xPos;
            yPosDraw = yPos;
        }
        sizeXDraw = (int) (sizeX * scale);
        sizeYDraw = (int) (sizeY * scale);
    }

    public void move(){
        setVectors();
        xVel += xAccel;
        yVel += yAccel;
        xPos += xVel;
        yPos += yVel;
    }
}
