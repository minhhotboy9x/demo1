package sample;

import javafx.scene.control.Alert;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class EnemyMap extends LoadMap{
    private EnemyBot enemyBot;

    public EnemyBot getEnemyBot() {
        return enemyBot;
    }

    public void setEnemyBot(EnemyBot enemyBot) {
        this.enemyBot = enemyBot;
    }

    private EnemyShip[][] enemyShip;

    public EnemyShip[][] getEnemyShip() {
        return enemyShip;
    }

    public void setEnemyShip(EnemyShip[][] enemyShip) {
        this.enemyShip = enemyShip;
    }

    public EnemyMap(double beginX, double beginY, double size, int spots, Pane myPane, EnemyBot enemyBot) {
        super(beginX, beginY, size, spots, myPane);
        this.enemyBot = enemyBot;
        for(int i=0;i<spots;i++)
            for(int j=0;j<spots;j++){
                this.getGrid()[i][j].setOnMouseClicked(event -> clicked(event));
            }
        enemyShip = new EnemyShip[spots][spots];
    }

    private void clicked(MouseEvent event) {
        if(this.getLocked()==false)
            return;

        double beginX = super.getBeginX();
        double beginY = super.getBeginY();
        double squareSize = super.getSquareSize();
        Pane myPane = super.getPane();
        int[][] location = super.getLocation();

        int x = (int) ((event.getSceneX()-beginX)/squareSize);
        int y = (int) ((event.getSceneY()-beginY)/squareSize);

        double posX = beginX + x*squareSize;
        double posY = beginY + y*squareSize;

        if(isFree(x, y)) {
            Piece boom = new Piece(posX+squareSize/2, posY+squareSize/2, squareSize/3, myPane);
            if(location[x][y]==0){
                boom.setColor(Color.rgb(102, 163, 255));
                boom.draw();
            }
            else {
                boom.setColor(Color.RED);
                boom.draw();
                enemyShip[x][y].hit();
                if(!enemyShip[x][y].isAlive()){
                    int vertical = enemyShip[x][y].getVertical();
                    int length = enemyShip[x][y].getLength();

                    double startX = enemyShip[x][y].getIdX()*squareSize + beginX;
                    double startY = enemyShip[x][y].getIdY()*squareSize + beginY;
                    //System.out.println("* "+enemyShip[x][y].getIdX()+" "+enemyShip[x][y].getIdY()+" "+startX+" "+startY);
                    Line line = new Line(0, 0,
                            squareSize * length * (1 - vertical) - (1 - vertical)*squareSize,
                            squareSize * length * vertical - vertical*squareSize);
                    line.setTranslateX(startX+squareSize/2);
                    line.setTranslateY(startY+squareSize/2);

                    line.setStroke(Color.BLACK);
                    line.setStrokeWidth(5);

                    super.getPane().getChildren().add(line);

                    enemyBot.enemyLeft--;
                    if(enemyBot.enemyLeft==0) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, "You won!");
                        alert.showAndWait();
                        enemyBot.getEnemyMap().setLocked(false);
                    }
                }
            }
            //System.out.println(super.getLocation(x, y));
            location[x][y]=1;

            // System.out.println(super.getLocation(x, y));
            if(enemyBot.enemyLeft!=0)
                enemyBot.play();
        }
    }
}
