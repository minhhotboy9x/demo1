package sample;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

// location (x, y) == point(y, x);
// loi truc

public class PlayerMap extends LoadMap{

    private PlayerShip[][] playerShips; //which ship occupied coordinate

    public PlayerShip[][] getPlayerShip() {
        return playerShips;
    }

    public void setPlayerShip(int x, int y, PlayerShip playerShip) {
        this.playerShips[x][y] = playerShip;
    }

    public PlayerMap(double beginX, double beginY, double size, int spots, Pane myPane) {
        super(beginX, beginY, size, spots, myPane);
        playerShips = new PlayerShip[spots][spots];
    }

    public boolean shot(int x, int y){
        int val = this.getLocation(x, y);
        double beginX = super.getBeginX();
        double beginY = super.getBeginY();
        double squareSize = super.getSquareSize();
        Pane myPane = super.getPane();
        int[][] location = super.getLocation();

        double posX = beginX + x*squareSize;
        double posY = beginY + y*squareSize;

        Piece boom = new Piece(posX+squareSize/2, posY+squareSize/2, squareSize/3, myPane);

        if(location[x][y]==0) {
            boom.setColor(Color.rgb(102, 163, 255));
            boom.draw();
            location[x][y]=1;
            return false;
        }
        else if(location[x][y]==2) {
            boom.setColor(Color.RED);
            boom.draw();
            playerShips[x][y].hit();
            location[x][y]=3;
            if(!playerShips[x][y].isAlive()){
                EnemyBot.playerLeft--;
                EnemyBot.lengthOfRemainingShip.remove(Integer.valueOf(playerShips[x][y].getLength()));
                //System.out.println(EnemyBot.lengthOfRemainingShip.size());
                int vertical = playerShips[x][y].getVertical();
                int length = playerShips[x][y].getLength();
                int idX = playerShips[x][y].getIdX();
                int idY = playerShips[x][y].getIdY();
                double startX = beginX + idX*squareSize;
                double startY = beginY + idY*squareSize;
                //System.out.println("* "+playerShips[x][y].getIdX()+" "+playerShips[x][y].getIdY()+" "+startX+" "+startY+" "+playerShips[x][y].getLength());
                Line line = new Line(0, 0,
                        squareSize * length * (1 - vertical) - (1 - vertical)*squareSize,
                        squareSize * length * vertical - vertical*squareSize);

                //Line line = new Line(10, 10, 20, 20);
                //System.out.println("* "+startX+" "+startY);
                line.setTranslateX(startX+squareSize/2);
                line.setTranslateY(startY+squareSize/2);
                line.setStroke(Color.BLACK);
                line.setStrokeWidth(5);
                super.getPane().getChildren().add(line);

                for(int i=idX; i<idX + Math.max(1, length*(1-vertical)); i++)
                    for (int j=idY; j<idY + Math.max(1, length*vertical); j++){
                        location[i][j]=4;
                    }
            }
            return true;
        }
        return true;
    }
}
