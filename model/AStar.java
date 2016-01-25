package model;

import java.util.LinkedList;
import java.util.NoSuchElementException;

public class AStar {
    private boolean isPathFound;
    private boolean isRouteUnreachable;
    private Cell[][] cells;
    private Cell start;
    private Cell finish;
    private LinkedList<Cell> openList;
    private LinkedList<Cell> closedList;

    public AStar(Cell[][] cells, Cell start, Cell finish) {
        this.isPathFound = false;
        this.isRouteUnreachable = false;
        this.cells = cells;
        this.start = start;
        this.finish = finish;
        this.openList = new LinkedList<>();
        this.closedList = new LinkedList<>();
    }

    public int findWay() {
        openList.push(start);
        while (!isPathFound && !isRouteUnreachable) {
            Cell currentCell;
            try {
                currentCell = openList.getFirst();
            } catch (NoSuchElementException ex) {
                ex.printStackTrace();
                return 2;
            }
            closedList.push(currentCell);
            openList.remove(currentCell);
            LinkedList<Cell> neighbours = makeNeighboursList(currentCell);
            for (Cell neighbour : neighbours) {
                // Если клетка непроходимая или она находится в закрытом списке, игнорируем ее
                if (neighbour.isBlocked || closedList.contains(neighbour)) {
                    continue;
                }
                // Если клетка еще не в открытом списке, то добавляем ее туда.
                // Делаем текущую клетку родительской для этой клетки.
                // Расчитываем стоимости F, G и H клетки.
                if (!openList.contains(neighbour)) {
                    openList.add(neighbour);
                    neighbour.parent = currentCell;
                    neighbour.H = manDist(neighbour);
                    neighbour.G = price(currentCell);
                    neighbour.F = neighbour.H + neighbour.G;
                    // Если клетка уже в открытом списке, то проверяем, не дешевле ли будет путь через эту клетку.
                    // Для сравнения используем стоимость G.
                    if (neighbour.G < currentCell.G) {
                    /* Более низкая стоимость G указывает на то, что путь будет дешевле.
                    * Эсли это так, то меняем родителя клетки на текущую клетку
                    * и пересчитываем для нее стоимости G и F.
                    */
                        neighbour.parent = currentCell;
                        neighbour.H = manDist(neighbour);
                        neighbour.G = price(currentCell);
                        neighbour.F = neighbour.H + neighbour.G;
                    }
                }
            }
            // Если добавили в открытый список целевую клетку, то путь найден
            if (openList.contains(finish)) {
                isPathFound = true;
            }
            // Если проверили все клетки и не нашли путь, то его не существует
            if (openList.isEmpty()) {
                isRouteUnreachable = true;
            }
        }
        // Сохраняем путь, двигаясь назад от целевой точки, проходя от каждой точки к ее родителю до тех пор,
        // пока не дойдем до стартовой точки. Это и будет наш путь.
        if (!isRouteUnreachable) {
            Cell rd = finish.parent;
            while (!rd.equals(start)) {
                rd.isRoad = true;
                rd = rd.parent;
                if (rd == null) break;
            }
        } else {
            return 1;
        }
        return 0;
    }

    public int manDist(Cell currentCell) {
        return 10 * (Math.abs(currentCell.x - finish.x) + Math.abs(currentCell.y - finish.y));
    }

    public int price(Cell currentCell) {
        if (currentCell.x == start.x || currentCell.y == start.y) {
            return currentCell.G + 10;
        } else {
            return currentCell.G + 14;
        }
    }

    // TODO: write test
    public LinkedList<Cell> makeNeighboursList(Cell currentCell) {
        LinkedList<Cell> neighbours = new LinkedList<>();
        if (!(cells[currentCell.x - 1][currentCell.y].isBlocked || cells[currentCell.x][currentCell.y - 1].isBlocked)) {
            neighbours.add(cells[currentCell.x - 1][currentCell.y - 1]);
        }
        neighbours.add(cells[currentCell.x - 1][currentCell.y]);
        if (!(cells[currentCell.x - 1][currentCell.y].isBlocked || cells[currentCell.x][currentCell.y + 1].isBlocked)) {
            neighbours.add(cells[currentCell.x - 1][currentCell.y + 1]);
        }
        neighbours.add(cells[currentCell.x][currentCell.y - 1]);
        neighbours.add(cells[currentCell.x][currentCell.y + 1]);
        if (!(cells[currentCell.x + 1][currentCell.y].isBlocked || cells[currentCell.x][currentCell.y - 1].isBlocked)) {
            neighbours.add(cells[currentCell.x + 1][currentCell.y - 1]);
        }
        neighbours.add(cells[currentCell.x + 1][currentCell.y]);
        if (!(cells[currentCell.x + 1][currentCell.y].isBlocked || cells[currentCell.x][currentCell.y + 1].isBlocked)) {
            neighbours.add(cells[currentCell.x + 1][currentCell.y + 1]);
        }
        return neighbours;
    }
}
