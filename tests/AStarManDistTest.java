package tests;

import javafx.scene.paint.Color;
import model.AStar;
import model.Cell;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AStarManDistTest {

    @Test
    public void testManDist() {
        Cell[][] cells = new Cell[2][2];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new Cell(10, 10, Color.WHITE, i, j);
            }
        }
        Cell start = cells[0][0];
        Cell finish = cells[1][1];
        AStar astar = new AStar(cells, start, finish);
        int manDistReturns = astar.manDist(start);
        assertEquals(20, manDistReturns);
    }
}