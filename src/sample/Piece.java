package sample;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Piece {
    private double x;
    private double y;
    private double radius;
    private Circle c;
    private Pane myPane;

    public Piece(double x, double y, double radius, Pane myPane){
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.myPane = myPane;
        this.c = new Circle();
    }
    public Piece(double x, double y, double radius, Pane myPane, Circle c) {
        this(x, y, radius, myPane);
        this.c = c;
    }

    public void setX(double x){
        this.x = x;
    }

    public void setY(double y){
        this.y = y;
    }

    public double getY(){
        return this.y;
    }

    public double getX(){
        return this.x;
    }

    public void setColor(Color color){
        c.setFill(color);
    }

    public void draw(){
        c.setRadius(radius);
        c.setTranslateX(x);
        c.setTranslateY(y);
        c.setStroke(Color.BLACK);
        //c.setViewOrder(-2);
        myPane.getChildren().add(c);
    }
}
