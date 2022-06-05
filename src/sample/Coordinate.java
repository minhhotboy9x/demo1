package sample;

public class Coordinate implements Comparable<Coordinate>{
    private int x, y;

    //priority cang nho xac xuat trung cang lon 3, 2, 1
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinate subtract(Coordinate y){
        return new Coordinate(this.x - y.x, this.y - y.y);
    }

    public Coordinate add(Coordinate y){
        return new Coordinate(this.x + y.x, this.y + y.y);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }


    @Override
    public int compareTo(Coordinate y) {
        if(this.x != y.x)
            return this.x-y.x;
        else
            return this.y-y.y;
    }
}
