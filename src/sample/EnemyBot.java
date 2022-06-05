package sample;

import javafx.scene.control.Alert;

import java.util.*;

public class EnemyBot {
    private PlayerMap playerMap;
    private EnemyMap enemyMap;
    private boolean targetMode;
    private int[] dx = {1, 0, -1, 0};
    private int[] dy = {0, 1, 0, -1};
    private int dIter = 0;
    public static int enemyLeft, playerLeft;



    public EnemyMap getEnemyMap() {
        return enemyMap;
    }

    public void setEnemyMap(EnemyMap enemyMap) {
        this.enemyMap = enemyMap;
    }

    ArrayList<Coordinate> coordinates1;
    ArrayList<PlayerShip> playerShips;
    static ArrayList<Integer> lengthOfRemainingShip;
    ArrayList<Coordinate> hittingCoordinates;

    public EnemyBot(PlayerMap playerMap, EnemyMap enemyMap) {
        this.playerMap = playerMap;
        this.enemyMap = enemyMap;

        this.targetMode = false;
        this.enemyLeft = this.playerLeft = 5;//number of ship remaining

        int spots = playerMap.getSpots();
        coordinates1 = new ArrayList<Coordinate>();
        playerShips = new ArrayList<PlayerShip>();
        hittingCoordinates = new ArrayList<Coordinate>();

        lengthOfRemainingShip = new ArrayList<Integer>();

        for (int i = 0; i < spots; i++)
            for (int j = 0; j < spots; j++) {
                if((i+j)%2==1)
                    coordinates1.add(new Coordinate(i, j));
            }
        //coordinates1.forEach(n->System.out.println(n.getX()+" "+n.getY()));
        Collections.shuffle(coordinates1);
    }

    public void createEnemyShip(int length){
        EnemyShip enemyShip;
        int spots = enemyMap.getSpots();
        Random rand = new Random();
        double squareSize = enemyMap.getSquareSize();
        int vertical = rand.nextInt(2);
        while(true)
        {
            int x = rand.nextInt(spots);
            int y = rand.nextInt(spots);
            enemyShip = new EnemyShip(length, squareSize, vertical, enemyMap.getPane(), enemyMap);
            if(enemyShip.inMap(x, y) && !enemyMap.isCollision(x, y, enemyShip)) {
                    enemyShip.setInMap(x, y);
                    break;
            }
        }

        lengthOfRemainingShip.add(length);
    }

    public void play() {
        if(coordinates1.isEmpty())
            return;
        //Random rand = new Random();

        if(!targetMode)
            inHuntMode();
        else
            inTargetMode();
        if(!lengthOfRemainingShip.isEmpty())
            removeNoShipCoordinates();

        if(this.playerLeft==0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "You are a lessor!");
            alert.showAndWait();
            this.getEnemyMap().setLocked(false);
        }
    }

    public void removeNoShipCoordinates(){
        int[][] location = playerMap.getLocation();
        int spots = playerMap.getSpots();
        int[][] cal1 = new int[spots][spots];
        int[][] cal2 = new int[spots][spots];

        for(int i=0; i<spots; i++){
            for(int j=0; j<spots; j++) {
                if(location[i][j] == 0 || location[i][j] == 2)
                    cal1[i][j] = 1 + cal1[i][Math.max(0,j-1)];
            }

            for(int j=spots-2; j>=0; j--) {
                if(location[i][j] == 0 || location[i][j] == 2)
                    cal1[i][j] = Math.max(cal1[i][j], cal1[i][j+1]);
            }
        }

        for(int j=0; j<spots; j++){
            for(int i=0; i<spots; i++) {
                if(location[i][j] == 0 || location[i][j] == 2)
                    cal2[i][j] = 1 + cal2[Math.max(0, i-1)][j];
            }

            for(int i=spots-2; i>=0; i--) {
                if(location[i][j] == 0 || location[i][j] == 2)
                    cal2[i][j] = Math.max(cal2[i][j], cal2[i+1][j]);
            }
        }

        for(int i=0; i<spots; i++)
            for (int j=0; j<spots; j++)
                if(cal1[i][j] < Collections.min(lengthOfRemainingShip) && cal2[i][j] < Collections.min(lengthOfRemainingShip)){
                    coordinates1.remove(new Coordinate(i, j));
                }
    }

    /*public void calculateNextMove(int x, int y) {

        if(this.hitting==true) {
            if(lastHit.getY()!=-1 && Math.abs(y - lastHit.getY())<=1){
                if (predictionLine == -1) {
                    predictionLine = Math.abs(y - lastHit.getY());
                    changePriority(x + (1 - predictionLine), y + predictionLine, 0);
                    changePriority(x - (1 - predictionLine), y - predictionLine, 0);
                    changePriority(lastHit.getX() + (1 - predictionLine), lastHit.getY() + predictionLine, 0);
                    changePriority(lastHit.getX() - (1 - predictionLine), lastHit.getY() - predictionLine, 0);
                }
                else {
                    changePriority(x + (1 - predictionLine), y + predictionLine, 0);
                    changePriority(x - (1 - predictionLine), y - predictionLine, 0);
                }
            }
            else {
                changePriority(x, y + 1, 1);
                changePriority(x, y - 1, 1);
                changePriority(x + 1, y, 1);
                changePriority(x - 1, y, 1);
            }
            lastHit.setX(x);
            lastHit.setY(y);
        }
       else{
           lastHit.setX(-1);
           lastHit.setY(-1);
       }

        if(this.hitting == true && !playerMap.getPlayerShip()[x][y].isAlive()) {//ban ha 1 tau
            int idX = playerMap.getPlayerShip()[x][y].getIdX();
            int idY = playerMap.getPlayerShip()[x][y].getIdY();

            int vertical = playerMap.getPlayerShip()[x][y].getVertical();
            int length = playerMap.getPlayerShip()[x][y].getLength();

            Iterator itr = hittingCoordinates.iterator();
            while (itr.hasNext()) {
                Coordinate c = (Coordinate) itr.next();
                if(idX<=c.getX() && c.getX()<idX + Math.max(1, length * (1 - vertical)) &&
                idY<=c.getY() && c.getY()<idY + Math.max(1, length * vertical)) {
                    itr.remove();
                }
            }
            if(hittingCoordinates.isEmpty()) {
                coordinates.forEach(n -> n.setPriority(2));
                Collections.shuffle(coordinates);
            }
            Collections.sort(coordinates);
            lastHit.setX(-1);
            lastHit.setY(-1);
            this.predictionLine = -1;
            this.playerLeft--;
            Integer val = Integer.valueOf(playerMap.getPlayerShip()[x][y].getLength());
            this.lengthOfRemainingShip.remove(val);
        }

        hittingCoordinates.forEach(n -> {
            int x1 = hittingCoordinates.get(0).getX();
            int y1 = hittingCoordinates.get(0).getY();

            changePriority(x1, y1 + 1, 1);
            changePriority(x1, y1 - 1, 1);
            changePriority(x1 + 1, y1, 1);
            changePriority(x1 - 1, y1, 1);
           // System.out.println(predictionLine+" "+n.getX()+" "+n.getY());
        });
       // System.out.println("------------------------");
        Collections.sort(coordinates);
    }
     */
    public void inHuntMode() {
        int x = coordinates1.get(0).getX();
        int y = coordinates1.get(0).getY();

        //System.out.println(lastHit.getX() + " " + lastHit.getY() + " "+x+" "+y+" "+":" +val+" "+predictionLine);

        coordinates1.remove(0);
        this.targetMode=this.playerMap.shot(x, y);
        if(this.targetMode)
            hittingCoordinates.add(new Coordinate(x, y));

        //System.out.println("mode: "+targetMode+" "+hittingCoordinates.size());
    }

    private void inTargetMode() {
        int[][] location = playerMap.getLocation();
        int spots = enemyMap.getSpots();
        int x, y;

        if(hittingCoordinates.size()==1){
            while(true){
                x = dx[dIter]+hittingCoordinates.get(0).getX();
                y = dy[dIter]+hittingCoordinates.get(0).getY();
                dIter=(dIter+1)%4;
                //System.out.println("iter: "+dIter+" diem "+x+" "+y);
                if(playerMap.isFree(x, y))
                    break;
            }
            boolean hit = this.playerMap.shot(x, y);
            if(hit)
                hittingCoordinates.add(new Coordinate(x, y));
        }
        else {
            Collections.sort(hittingCoordinates);
            int ok = 0;
            Coordinate a = hittingCoordinates.get(hittingCoordinates.size()-1);
            Coordinate b = hittingCoordinates.get(hittingCoordinates.size()-2);
            if(playerMap.isFree(a.add(a.subtract(b)))) {
                ok = 1;
                Coordinate c = a.add(a.subtract(b));
                boolean hit = this.playerMap.shot(c.getX(), c.getY());

                if(hit)
                    hittingCoordinates.add(c);
                //System.out.println("ok="+ok);
            }
            else {
                a = hittingCoordinates.get(0);
                b = hittingCoordinates.get(1);
                if(playerMap.isFree(a.add(a.subtract(b)))) {
                    ok = 1;
                    Coordinate c = a.add(a.subtract(b));
                    boolean hit = this.playerMap.shot(c.getX(), c.getY());
                    if(hit)
                        hittingCoordinates.add(c);

                    //System.out.println("ok="+ok);
                }
            }
            if(ok==0){
                Coordinate c = hittingCoordinates.get(0);
                hittingCoordinates.clear();
                hittingCoordinates.add(c);
               // System.out.println("break "+hittingCoordinates.size());
                while(true){
                    x = dx[dIter]+hittingCoordinates.get(0).getX();
                    y = dy[dIter]+hittingCoordinates.get(0).getY();
                    dIter=(dIter+1)%4;
                    //System.out.println("iter: "+dIter+" diem "+x+" "+y);
                    if(playerMap.isFree(x, y))
                        break;
                }
                boolean hit = this.playerMap.shot(x, y);
                if(hit)
                    hittingCoordinates.add(new Coordinate(x, y));
            }
        }
        Iterator itr = hittingCoordinates.iterator();
        while (itr.hasNext()) {
            Coordinate c = (Coordinate) itr.next();
            if (location[c.getX()][c.getY()] == 4)
                itr.remove();
        }
        itr = coordinates1.iterator();
        while (itr.hasNext()) {
            Coordinate c = (Coordinate) itr.next();
            if (!(location[c.getX()][c.getY()] == 0 || location[c.getX()][c.getY()] ==2))
                itr.remove();
        }
        //System.out.println("is empty"+hittingCoordinates.isEmpty());

        if(hittingCoordinates.isEmpty()) {
            //System.out.println("removed");
            for(int i = 0; i<spots; i++)
                for(int j = 0; j<spots; j++) {
                    if(location[i][j]==3) {
                        hittingCoordinates.add(new Coordinate(i, j));
                    }
                }

            if(hittingCoordinates.isEmpty())
                this.targetMode = false;
        }

        //System.out.println("mode: "+targetMode+" "+hittingCoordinates.size());
    }
}
