package tests;

import javafx.scene.paint.Color;
import model.AStar;
import model.Cell;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AStarFindWayTest {

    @Test
    public void testFindUnreachableWay() {
        Cell[][] cells = new Cell[5][5];
        for (int i = 0; i < cells.length; i++) {
            cells[0][i] = new Cell(10, 10, Color.WHITE, true, 0, i);
            cells[cells.length - 1][i] = new Cell(10, 10, Color.WHITE, true, cells.length - 1, i);
        }
        for (int i = 0; i < cells[0].length; i++) {
            cells[i][0] = new Cell(10, 10, Color.WHITE, true, i, 0);
            cells[i][cells[0].length - 1] = new Cell(10, 10, Color.WHITE, true, i, cells[0].length - 1);
        }
        for (int i = 1; i < cells.length - 1; i++) {
            for (int j = 1; j < cells[0].length - 1; j++) {
                cells[i][j] = new Cell(10, 10, Color.WHITE, i, j);
            }
        }
        Cell start = cells[1][1];
        Cell finish = cells[3][3];
        cells[2][1].isBlocked = true;
        cells[2][2].isBlocked = true;
        cells[2][3].isBlocked = true;
        AStar astar = new AStar(cells, start, finish);
        assertEquals(1, astar.findWay());
    }

    @Test
    public void testFindReachableWay() {
        Cell[][] cells = new Cell[5][5];
        for (int i = 0; i < cells.length; i++) {
            cells[0][i] = new Cell(10, 10, Color.WHITE, true, 0, i);
            cells[cells.length - 1][i] = new Cell(10, 10, Color.WHITE, true, cells.length - 1, i);
        }
        for (int i = 0; i < cells[0].length; i++) {
            cells[i][0] = new Cell(10, 10, Color.WHITE, true, i, 0);
            cells[i][cells[0].length - 1] = new Cell(10, 10, Color.WHITE, true, i, cells[0].length - 1);
        }
        for (int i = 1; i < cells.length - 1; i++) {
            for (int j = 1; j < cells[0].length - 1; j++) {
                cells[i][j] = new Cell(10, 10, Color.WHITE, i, j);
            }
        }
        Cell start = cells[1][1];
        Cell finish = cells[3][3];
        cells[2][1].isBlocked = true;
        cells[2][3].isBlocked = true;
        AStar astar = new AStar(cells, start, finish);
        assertEquals(0, astar.findWay());
        for (Cell[] cell1 : cells) {
            for (Cell cell : cell1) {
                System.out.println("x:" + cell.x + " y:" + cell.y + " isRoad: " + cell.isRoad);
            }
        }
        assertEquals(true, cells[1][2].isRoad);
        assertEquals(true, cells[2][2].isRoad);
        assertEquals(true, cells[3][2].isRoad);
    }
}