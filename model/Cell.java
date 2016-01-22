package model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends Rectangle {
    public boolean isBlocked;
//    public boolean isStart = false;
//    public boolean isEnd = false;
    public boolean isRoad = false;
    public int x = -1;
    public int y = -1;
    public int F = 0;
    public int G = 0;
    public int H = 0;
    public Cell parent = this;

    public Cell(int width, int height, Color color, int x, int y) {
        super(width, height, color);
        this.x = x;
        this.y = y;
        isBlocked = false;
    }

    public Cell(int width, int height, Color color, boolean isBlocked, int x, int y) {
        super(width, height, color);
        this.x = x;
        this.y = y;
        this.isBlocked = isBlocked;
    }
}
