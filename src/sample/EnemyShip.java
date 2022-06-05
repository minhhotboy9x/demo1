package sample;

import javafx.scene.layout.Pane;

public class EnemyShip extends Ship{
    private int idX; //id of cell which ship is in
    private int idY;
    private EnemyMap myMap;
    public EnemyShip(int length, double squareSize, int vertical, Pane myPane, EnemyMap myMap) {
        super(length, squareSize, vertical, myPane);
        this.myMap = myMap;
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

    public boolean setInMap(int x, int y) {
        idX = x;
        idY = y;
        int vertical = super.getVertical();
        int length = super.getLength();
        int[][] location = myMap.getLocation();
        int spots = this.myMap.getSpots();

        if(inMap(x, y) && !myMap.isCollision(idX, idY, (Ship) this)) {
            for (int i = idX; i < idX + Math.max(1, length * (1 - vertical)); i++)
                for (int j = idY; j < idY + Math.max(1, length * vertical); j++) {
                    location[i][j]=2;
                    myMap.getEnemyShip()[i][j] = this;
                }
            return true;
        }
        return false;
    }

    public Boolean inMap(int idX, int idY) {
        int vertical = super.getVertical();
        int length = super.getLength();
        int spots = this.myMap.getSpots();

        for (int i = idX; i < idX + Math.max(1, length * (1 - vertical)); i++)
            for (int j = idY; j < idY + Math.max(1, length * vertical); j++) {
                if(i>=spots || j>=spots)
                    return false;
            }
        return true;
    }
}
