package sample;

import javafx.scene.Node;

public class DraggableMaker {
    private static double mouseAnchorX;
    private static double mouseAnchorY;

    public static void makeDraggable(Node node){
        node.setOnMousePressed(mouseEvent -> {
            mouseAnchorX = mouseEvent.getX();
            mouseAnchorY = mouseEvent.getY();
            //System.out.println(mouseAnchorX +" "+mouseAnchorY+" "+mouseEvent.getSceneX()+" "+mouseEvent.getSceneY());
        });
        node.setOnMouseDragged(mouseEvent -> {
            node.setTranslateX(mouseEvent.getSceneX()-mouseAnchorX);
            node.setTranslateY(mouseEvent.getSceneY()-mouseAnchorY);
            //System.out.println(mouseEvent.getSceneX() +" "+mouseEvent.getSceneY());
        });
    }
}
