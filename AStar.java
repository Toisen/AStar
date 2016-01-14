import java.util.LinkedList;

class Cell {
    public int x = -1;
    public int y = -1;
    public Cell parent = this;
    public boolean isBlocked = false;
    public boolean isStart = false;
    public boolean isFinish = false;
    public boolean isRoad = false;
    public int F = 0;
    public int G = 0;
    public int H = 0;
    /**
     * Создает клетку с координатами x, y.
     * @param isBlocked является ли клетка непроходимой
     */
    public Cell(int x, int y, boolean isBlocked) {
        this.x = x;
        this.y = y;
        this.isBlocked = isBlocked;
    }

    /**
     * Функция вычисления манхеттенского расстояния от текущей
     * клетки до isFinish
     * @param finish конечная клетка
     * @return расстояние
     */
    public int mandist(Cell finish) {
        return 10 * (Math.abs(this.x - finish.x) + Math.abs(this.y - finish.y));
    }

    /**
     * Вычисление стоимости пути до соседней клетки isFinish
     * @param finish соседняя клетка
     * @return 10, если клетка по горизонтали или вертикали от текущей, 14, если по диагонали
     * (это типа 1 и sqrt(2) ~ 1.44)
     */
    public int price(Cell finish) {
        if (this.x == finish.x || this.y == finish.y) {
            return 10;
        } else {
            return 14;
        }
    }

    /**
     * Устанавливает текущую клетку как стартовую
     */
    public void setAsStart() {
        this.isStart = true;
    }

    /**
     * Устанавливает текущую клетку как конечную
     */
    public void setAsFinish() {
        this.isFinish = true;
    }

    /**
     * Сравнение клеток
     * @param second вторая клетка
     * @return true, если координаты клеток равны, иначе - false
     */
    public boolean equals(Cell second) {
        return (this.x == second.x) && (this.y == second.y);
    }

    /**
     * Красиво печатаем
     * * - путь (это в конце)
     * + - стартовая или конечная
     * # - непроходимая
     * . - обычная
     * @return строковое представление клетки
     */
    public String toString() {
        if (this.isRoad) {
            return " * ";
        }
        if (this.isStart || this.isFinish) {
            return " + ";
        }
        if (this.isBlocked) {
            return " # ";
        }
        return " . ";
    }
}

class Table<T extends Cell> {
    public int width;
    public int height;
    private Cell[][] table;

    /**
     * Создаем карту игры с размерами width и height
     */
    public Table(int width, int height) {
        this.width = width;
        this.height = height;
        this.table = new Cell[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                table[i][j] = new Cell(0, 0, false);
            }
        }
    }

    /**
     * Добавить клетку на карту
     */
    public void add(Cell cell) {
        table[cell.x][cell.y] = cell;
    }

    /**
     * Получить клетку по координатам x, y
     * @return клетка, либо фейковая клетка, которая всегда блокирована (чтобы избежать выхода за границы)
     */
    @SuppressWarnings("unchecked")
    public T get(int x, int y) {
        if (x < width && x >= 0 && y < height && y >= 0) {
            return (T)table[x][y];
        }
        // а разве так можно делать в Java? оО но работает оО
        return (T)(new Cell(0, 0, true));
    }

    /**
     * Печать всех клеток поля. Красиво.
     */
    public void printPath() {
        for (int i = 0; i < AStar.WIDTH; i++) {
            for (int j = 0; j < AStar.HEIGHT; j++) {
                System.out.print(this.get(j, i));
            }
            System.out.println();
            System.out.println();
        }
        System.out.println();
        System.out.println();
    }
}

public class AStar {
    public static int WIDTH = 10;
    public static int HEIGHT = 10;

    /**
     * Пример хуевой реализации алгоритма поиска пути А*
     * @param args нихуя
     */
    public static void main(String[] args) {
        // Создадим все нужные списки
        Table<Cell> cells = new Table<>(AStar.WIDTH, AStar.HEIGHT);
        Table blockList = new Table(AStar.WIDTH, AStar.HEIGHT);
        LinkedList<Cell> openList = new LinkedList<>();
        LinkedList<Cell> closedList = new LinkedList<>();
        LinkedList<Cell> neighbours = new LinkedList<>();

        // Создадим преграду
        blockList.add(new Cell(4, 2, true));
        blockList.add(new Cell(4, 3, true));
        blockList.add(new Cell(4, 4, true));
        blockList.add(new Cell(4, 5, true));
        blockList.add(new Cell(4, 6, true));
        blockList.add(new Cell(4, 7, true));

        // Заполним карту как-то клетками, учитывая преграду
        for (int i = 0; i < AStar.WIDTH; i++) {
            for (int j = 0; j < AStar.HEIGHT; j++) {
                cells.add(new Cell(j, i, blockList.get(j, i).isBlocked));
            }
        }

        // Стартовая и конечная
        cells.get(1, 4).setAsStart();
        cells.get(6, 5).setAsFinish();
        Cell start = cells.get(1, 4);
        Cell finish = cells.get(6, 5);

        cells.printPath();

        // Фух, начинаем
        boolean isFound = false;
        boolean isRouteUnreachable = false;

        //1) Добавляем стартовую клетку в открытый список.
        openList.push(start);

        //2) Повторяем следующее:
        while (!isFound && !isRouteUnreachable) {
            //a) Ищем в открытом списке клетку с наименьшей стоимостью F. Делаем ее текущей клеткой.
            Cell min = openList.getFirst();
            for (Cell cell : openList) {
                // тут я специально тестировал, при < или <= выбираются разные пути,
                // но суммарная стоимость G у них совершенно одинакова. Забавно, но так и должно быть.
                if (cell.F < min.F) min = cell;
            }

            //b) Помещаем ее в закрытый список. (И удаляем с открытого)
            closedList.push(min);
            openList.remove(min);
            //System.out.println(openList);

            //c) Для каждой из соседних 8-ми клеток ...
            neighbours.clear();
            neighbours.add(cells.get(min.x - 1, min.y - 1));
            neighbours.add(cells.get(min.x,     min.y - 1));
            neighbours.add(cells.get(min.x + 1, min.y - 1));
            neighbours.add(cells.get(min.x + 1, min.y));
            neighbours.add(cells.get(min.x + 1, min.y + 1));
            neighbours.add(cells.get(min.x,     min.y + 1));
            neighbours.add(cells.get(min.x - 1, min.y + 1));
            neighbours.add(cells.get(min.x - 1, min.y));

            for (Cell neighbour : neighbours) {
                //Если клетка непроходимая или она находится в закрытом списке, игнорируем ее. В противном случае делаем следующее.
                if (neighbour.isBlocked || closedList.contains(neighbour)) continue;

                //Если клетка еще не в открытом списке, то добавляем ее туда. Делаем текущую клетку родительской для это клетки. Расчитываем стоимости F, G и H клетки.
                if (!openList.contains(neighbour)) {
                    openList.add(neighbour);
                    neighbour.parent = min;
                    neighbour.H = neighbour.mandist(finish);
                    neighbour.G = start.price(min);
                    neighbour.F = neighbour.H + neighbour.G;
                    continue;
                }

                // Если клетка уже в открытом списке, то проверяем, не дешевле ли будет путь через эту клетку. Для сравнения используем стоимость G.
                if (neighbour.G + neighbour.price(min) < min.G) {
                    // Более низкая стоимость G указывает на то, что путь будет дешевле. Эсли это так, то меняем родителя клетки на текущую клетку и пересчитываем для нее стоимости G и F.
                    neighbour.parent = min; // вот тут я честно хз, надо ли min.parent или нет
                    neighbour.H = neighbour.mandist(finish);
                    neighbour.G = start.price(min);
                    neighbour.F = neighbour.H + neighbour.G;
                }

                // Если вы сортируете открытый список по стоимости F, то вам надо отсортировать свесь список в соответствии с изменениями.
            }

            //d) Останавливаемся если:
            //Добавили целевую клетку в открытый список, в этом случае путь найден.
            //Или открытый список пуст и мы не дошли до целевой клетки. В этом случае путь отсутствует.

            if (openList.contains(finish)) {
                isFound = true;
            }

            if (openList.isEmpty()) {
                isRouteUnreachable = true;
            }
        }

        //3) Сохраняем путь. Двигаясь назад от целевой точки, проходя от каждой точки к ее родителю до тех пор, пока не дойдем до стартовой точки. Это и будет наш путь.
        if (!isRouteUnreachable) {
            Cell rd = finish.parent;
            while (!rd.equals(start)) {
                rd.isRoad = true;
                rd = rd.parent;
                if (rd == null) break;
            }
            cells.printPath();
        } else {
            System.out.println("NO ROUTE");
        }

    }
}