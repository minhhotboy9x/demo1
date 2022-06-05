package sample;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
// location (x, y) == point(y, x);
// loi truc
public class LoadMap {
    private double beginX ;
    private double beginY ;
    private double size ;
    private int spots ; //so o vuong moi hang va cot
    private double squareSize;
    private Rectangle[][] grid;

    public Rectangle[][] getGrid() {
        return grid;
    }

    public void setGrid(Rectangle[][] grid) {
        this.grid = grid;
    }

    private int[][] location; //1: da ban, 2: co thuyen, 3 co thuyen dang bi ban, 4 thuyen bi ha
    private Pane myPane;
    private boolean locked;

    public LoadMap(double beginX, double beginY, double size, int spots, Pane myPane) {
        this.beginX = beginX;
        this.beginY = beginY;
        this.size = size;
        this.spots = spots;
        this.squareSize = size/spots;
        grid = new Rectangle[spots][spots];
        this.myPane = myPane;
        location = new int[spots][spots];
        this.locked = false;
        this.draw();
    }

    public boolean getLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public double getBeginX(){
        return this.beginX;
    }

    public double getBeginY(){
        return this.beginY;
    }

    public double getSquareSize(){
        return this.squareSize;
    }

    public int getSpots(){return this.spots;}

    public double getSize(){return this.size;}

    public int getLocation(int i, int j){ return location[i][j]; }

    public int[][] getLocation() { return location; }

    public void setLocation(int i, int j, int val){ location[i][j] = val; }

    public void draw() {
        for(int i=0;i<spots;i++)
            for(int j=0;j<spots;j++) {
                Rectangle r = new Rectangle();
                r.setTranslateX(beginX + squareSize*i);
                r.setTranslateY(beginY + squareSize*j);
                r.setHeight(squareSize);
                r.setWidth(squareSize);
                r.setStroke(Color.BLACK);
                r.setFill(Color.rgb(192,192,192));
                grid[i][j] = r;
                //grid[i][j].setOnMouseClicked(event -> clicked(event, r));
                myPane.getChildren().add(r);
            }
    }

    public Boolean isFree(int x, int y){ //ô trống
        return 0<=x && x<spots && 0<=y && y<spots && (location[x][y]==0 || location[x][y]==2);
    }

    public Boolean isFree(Coordinate a){ //ô trống
        int x = a.getX();
        int y = a.getY();
        return 0<=x && x<spots && 0<=y && y<spots && (location[x][y]==0 || location[x][y]==2);
    }

    public Boolean isCollision(int idX, int idY, Ship myShip) {
        int lengthShip = myShip.getLength();
        int verticalShip = myShip.getVertical();
        for(int i=idX; i<idX + Math.max(1, lengthShip*(1-verticalShip)); i++)
            for (int j=idY; j<idY + Math.max(1, lengthShip*verticalShip); j++){
                if(this.location[i][j] == 2) {
                    return true;
                }
            }
        return false;
    };

    protected Pane getPane() { return this.myPane; }

    /*private void clicked(MouseEvent event, Rectangle r) {
        if(this.locked==false)
            return;
        int x = (int) ((event.getSceneX()-beginX)/squareSize);
        int y = (int) ((event.getSceneY()-beginY)/squareSize);

        double posX = beginX + x*squareSize;
        double posY = beginY + y*squareSize;

        if(isFree(x, y)) {
            Piece boom = new Piece(posX+squareSize/2, posY+squareSize/2, squareSize/3, myPane);
            boom.setColor(Color.rgb(102, 163, 255));
            location[x][y]=1;
            boom.draw();
        }
    }
    */
}
