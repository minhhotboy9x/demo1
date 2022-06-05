package sample;

import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class PlayerShip extends Ship{
    private double beginX;
    private double beginY;
    private int idX; //id of cell which ship is in
    private int idY;
    private double mouseAnchorX;
    private double mouseAnchorY;
    private boolean locked;
    private Rectangle r;
    private PlayerMap myMap;

    public PlayerShip(double beginX, double beginY, int length, double squareSize, int vertical, Pane myPane, PlayerMap myMap){
        super(length, squareSize, vertical, myPane);
        this.beginX = beginX;
        this.beginY = beginY;
        this.locked = false;
        this.r = new Rectangle();
        this.myMap = myMap;
        myPane.getChildren().add(r);
        this.draw(beginX, beginY);
        //set behaviour for player
        this.r.setOnMouseReleased(event -> releasedMouse(event));
        this.r.setOnMouseClicked(event -> clickedMouse(event));
        this.r.setOnMouseDragged(event -> draggedMouse(event));
        this.r.setOnMousePressed(event -> pressedMouse(event));
        this.r.setArcWidth(30.0);
        this.r.setArcHeight(20.0);
        //this.r.setStyle( "-fx-border-radius: 10 10 10 10;");
    }

    public int getIdX() {
        return idX;
    }

    public void setIdX(int idX) {
        this.idX = idX;
    }

    public int getIdY() {
        return idY;
    }

    public void setIdY(int idY) {
        this.idY = idY;
    }

    public double getBeginX(){ return beginX; }

    public double getBeginY(){ return beginY; }

    public boolean getLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public Rectangle getR() { return r; }

    public void setColor(Color color){
        r.setFill(color);
    }

    public Boolean inMap() {
        double posX = (double) this.r.getTranslateX();
        double posY = (double) this.r.getTranslateY();

        double size = this.myMap.getSize();
        int vertical = super.getVertical();
        double shipSquareSize = super.getSquareSize();
        double endX, endY;
        if(vertical==1){
            endX = posX+shipSquareSize;
            endY = posY+shipSquareSize*super.getLength();
        }
        else {
            endX = posX+shipSquareSize*super.getLength();
            endY = posY+shipSquareSize;
        }
        return (posX>=myMap.getBeginX() && posY >=myMap.getBeginY()) && (endX<=myMap.getBeginX()+size && endY<=myMap.getBeginY()+size);
    }

    @Override
    public void changeShape() {
        super.changeShape();
        draw(beginX, beginY);
    }

    private void pressedMouse(MouseEvent event) {
        mouseAnchorX = event.getX();
        mouseAnchorY = event.getY();
        double squareSize = super.getSquareSize();
        int vertical = super.getVertical();
        int length = super.getLength();

        //System.out.println(this.inMap() +" "+ this.locked);
        if(this.inMap() && this.locked==false){
            int idX = (int) ( (this.r.getTranslateX() - myMap.getBeginX()) / squareSize); //idX of map
            int idY = (int) ( (this.r.getTranslateY() - myMap.getBeginY()) / squareSize); //idY of map
            //System.out.println(idX+" "+idY);
            for (int i = idX; i < idX + Math.max(1, length * (1 - vertical)); i++)
                for (int j = idY; j < idY + Math.max(1, length * vertical); j++) {
                    myMap.setLocation(i, j, 0);
                    myMap.getPlayerShip()[i][j] = null;
                }
        }
    }

    private void draggedMouse(MouseEvent event) {
        if(this.locked==false){
            Rectangle r = this.r;
            r.setTranslateX(event.getSceneX()-mouseAnchorX);
            r.setTranslateY(event.getSceneY()-mouseAnchorY);
        }
    }

    private void clickedMouse(MouseEvent event) {
        double squareSize = super.getSquareSize();
        int vertical = super.getVertical();
        Pane myPane = super.getMyPane();
        //System.out.println(mouseAnchorX+ " "+mouseAnchorY);
        if(!this.inMap()) {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2
                    && !this.inMap()) {
                this.changeShape();
            }
        }
       /* else if(this.locked == true){
            double posX = r.getTranslateX();
            double posY = r.getTranslateY();
            int idX = (int) (event.getX()/squareSize); //id of cell ship
            int idY = (int) (event.getY()/squareSize);

            int x = (int) ((event.getSceneX()-myMap.getBeginX())/squareSize); //id of map
            int y = (int) ((event.getSceneY()-myMap.getBeginY())/squareSize);

            if(myMap.isFree(x, y)) {
                myMap.setLocation(x, y, 1);
                this.hit();
                //System.out.println(x+" "+y+" "+myMap.getLocation(x, y)+" "+myMap.isFree(x, y));
                Piece piece = new Piece(posX+idX*(1-vertical)*squareSize +squareSize/2, posY+idY*vertical*squareSize+squareSize/2,
                        squareSize/3, myPane);
                piece.setColor(Color.RED);
                piece.draw();
            }
        }
        */
    }

    private void releasedMouse(MouseEvent event) {
        if(this.locked==true)
            return;

        double squareSize = super.getSquareSize();
        int vertical = super.getVertical();
        int length = super.getLength();
        int idX = (int) ( (r.getTranslateX() - myMap.getBeginX()) / squareSize); //idX of map
        int idY = (int) ( (r.getTranslateY() - myMap.getBeginY()) / squareSize); //idY of map
       // System.out.println(idX+" "+idY);
        double boundX = (myMap.getBeginX() + idX * squareSize + squareSize);
        double boundY = (myMap.getBeginY() + idY * squareSize + squareSize);

        if(r.getTranslateX()+this.getWidth() <= myMap.getBeginX()+myMap.getSize()){
            double d1 = Math.abs(boundX-r.getTranslateX()); //kc tu bound den x ben trai
            double d2 = Math.abs(boundX-r.getTranslateX()-squareSize); //kc tu bound den x ben phai
            if(d1<d2)
                idX++;
        }

        if(r.getTranslateY()+this.getHeight() <= myMap.getBeginY()+myMap.getSize()){
            double d1 = Math.abs(boundY-r.getTranslateY()); //kc tu bound den y ben tren
            double d2 = Math.abs(boundY-r.getTranslateY()-squareSize); //kc tu bound den y ben duoi
            if(d1<d2)
                idY++;
        }

        this.draw(myMap.getBeginX()+idX*myMap.getSquareSize(), myMap.getBeginY()+idY*myMap.getSquareSize()); //draw ship in map

        if( !inMap() || myMap.isCollision(idX, idY, (Ship) this))  { //if ship isn't in map draw back to the beginning
            this.draw(this.beginX, this.beginY);
        }
        if(inMap() && !myMap.isCollision(idX, idY, (Ship) this)) {
            this.idX = idX;
            this.idY = idY;
            for (int i = idX; i < idX + Math.max(1, length * (1 - vertical)); i++)
                for (int j = idY; j < idY + Math.max(1, length * vertical); j++) {
                    myMap.setLocation(i, j, 2);
                    myMap.setPlayerShip(i, j, this);
                }
        }
    }

    public void draw(double x, double y){
        int length = super.getLength();
        double squareSize = super.getSquareSize();
        int vertical = super.getVertical();
        r.setTranslateX(x);
        r.setTranslateY(y);
        //System.out.println(r.getX()+" "+ r.getY());
        //r.setFill(Color.rgb(153, 0, 204));
        if(super.getVertical() == 1) {
            r.setFill(new ImagePattern(new Image("media/tau2.png")));
            //System.out.println(1);
        }
        else {
            r.setFill(new ImagePattern(new Image("media/tau.png")));
            //System.out.println(0);
        }
        r.setHeight(Math.max(length * vertical * squareSize, squareSize));
        r.setWidth(Math.max(length * (1-vertical) * squareSize, squareSize));
    }
}
