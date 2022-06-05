package sample;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Ship {
    private int length;
    private int vertical;
    private double squareSize;

    private int health;
    private Pane myPane;

    public Ship(int length, double squareSize, int vertical, Pane myPane) {
        this.health = this.length = length;
        this.squareSize = squareSize;
        this.myPane = myPane;
        this.vertical = vertical;
    }

    public int getVertical(){ return this.vertical; }

    public int getLength(){ return this.length; }

    public void hit(){ this.health--; }

    public boolean isAlive(){
        return health>0;
    }

    public double getSquareSize(){ return this.squareSize; }

    public int getHealth() {
        return health;
    }

    public Pane getMyPane() {
        return myPane;
    }

    public void changeShape(){
        vertical = 1-vertical;
    }

    public double getHeight(){ return Math.max(length * vertical * squareSize, squareSize); }
    public double getWidth(){ return Math.max(length * (1-vertical) * squareSize, squareSize); }


}
