package model;

import java.util.LinkedList;

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
        this.openList = new LinkedList<>();
        this.closedList = new LinkedList<>();
        this.neighbours = new LinkedList<>();
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
                // Если клетка непроходимая или она находится в закрытом списке, игнорируем ее
                if (neighbour.isBlocked || closedList.contains(neighbour)) continue;
                // Если клетка еще не в открытом списке, то добавляем ее туда.
                // Делаем текущую клетку родительской для этой клетки.
                // Расчитываем стоимости F, G и H клетки.
                if (!openList.contains(neighbour)) {
                    openList.add(neighbour);
                    neighbour.parent = min;
                    neighbour.H = manDist(neighbour, finish);
                    neighbour.G = price(min, neighbour); // TODO check this
                    neighbour.F = neighbour.H + neighbour.G;
                    continue;
                }
                // Если клетка уже в открытом списке, то проверяем, не дешевле ли будет путь через эту клетку.
                // Для сравнения используем стоимость G.
                if (neighbour.G + price(min, neighbour) < min.G) {
                    /* Более низкая стоимость G указывает на то, что путь будет дешевле.
                    * Эсли это так, то меняем родителя клетки на текущую клетку
                    * и пересчитываем для нее стоимости G и F.
                    */
                    neighbour.parent = min;
                    neighbour.H = manDist(neighbour, finish);
                    neighbour.G = price(min, start);    // TODO check this
                    neighbour.F = neighbour.H + neighbour.G;
                }
                // Если добавили в открытый список целевую клетку, то путь найден
                if (openList.contains(finish)) {
                    isFound = true;
                }
                // Если проверили все клетки и не нашли путь, то его не существует
                if (openList.isEmpty()) {
                    isRouteUnreachable = true;
                }
            }
        }
        // Сохраняем путь. Двигаясь назад от целевой точки, проходя от каждой точки к ее родителю до тех пор,
        // пока не дойдем до стартовой точки. Это и будет наш путь.
        if (!isRouteUnreachable) {
            Cell rd = finish.parent;
            while (!rd.equals(start)) {
                rd.isRoad = true;
                rd = rd.parent;
                if (rd == null) break;
            }
//            cells.printPath();
        } else {
            System.out.println("NO ROUTE");
        }
    }

    private int manDist(Cell currentCell, Cell nextCell) {
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
