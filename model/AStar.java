package model;

import java.util.LinkedList;

/**
 * Created by Toisen on 13.01.2016.
 */
public class AStar {
    private boolean isFound = false;
    private boolean isRouteUnreachable = false;
    private Cell[][] cells;
    private Cell start;
    private Cell finish;
    private LinkedList<Cell> openList;
    private LinkedList<Cell> closedList;
    private LinkedList<Cell> neighbours;

    public AStar(Cell[][] cells, Cell start, Cell finish) {
        this.cells = cells;
        this.start = start;
        this.finish = finish;
        this.openList = new LinkedList<Cell>();
        this.closedList = new LinkedList<Cell>();
        this.neighbours = new LinkedList<Cell>();
    }

    public void findWay() {
        openList.push(start);
        while (!isFound && !isRouteUnreachable) {
            Cell min = openList.getFirst();
            for (Cell cell : openList) {
                if (cell.F < min.F) {
                    min = cell;
                }
            }
            closedList.push(min);
            openList.remove(min);
            makeNeighboursList(min);
            for (Cell neighbour : neighbours) {
                //Если клетка непроходимая или она находится в закрытом списке, игнорируем ее
                if (neighbour.isBlocked || closedList.contains(neighbour)) continue;
                //Если клетка еще не в открытом списке, то добавляем ее туда. Делаем текущую клетку родительской для это клетки. Расчитываем стоимости F, G и H клетки.
                if (!openList.contains(neighbour)) {
                    openList.add(neighbour);
                    neighbour.parent = min;
                    neighbour.H = mandist(neighbour, finish);
                    neighbour.G = price(min,neighbour);
                    neighbour.F = neighbour.H + neighbour.G;
                    continue;
                }
            }
        }
    }

    private int mandist(Cell currentCell, Cell nextCell) {
        return 10 * (Math.abs(currentCell.x - finish.x) + Math.abs(currentCell.y - finish.y));
    }

    private int price(Cell currentCell, Cell nextCell) {
        if (currentCell.x == nextCell.x || currentCell.y == nextCell.y) {
            return 10;
        } else {
            return 14;
        }
    }

    private void makeNeighboursList(Cell currentCell) {
        neighbours.clear();
/*        if (currentCell.x > 0) {
            neighbours.add(cells[currentCell.x - 1][currentCell.y]);
            if (currentCell.y > 0) {
                neighbours.add(cells[currentCell.x][currentCell.y - 1]);
                neighbours.add(cells[currentCell.x - 1][currentCell.y - 1]);
            }
            if (currentCell.y < cells[0].length - 1) {
                neighbours.add(cells[currentCell.x][currentCell.y + 1]);
                neighbours.add(cells[currentCell.x - 1][currentCell.y + 1]);
            }
            if (currentCell.x < cells.length - 1) {

            }
        }*/
        neighbours.add(cells[currentCell.x - 1][currentCell.y - 1]);
        neighbours.add(cells[currentCell.x][currentCell.y - 1]);
        neighbours.add(cells[currentCell.x + 1][currentCell.y - 1]);
        neighbours.add(cells[currentCell.x + 1][currentCell.y]);
        neighbours.add(cells[currentCell.x + 1][currentCell.y + 1]);
        neighbours.add(cells[currentCell.x][currentCell.y + 1]);
        neighbours.add(cells[currentCell.x - 1][currentCell.y + 1]);
        neighbours.add(cells[currentCell.x - 1][currentCell.y]);
    }
}
