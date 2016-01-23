package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import model.AStar;
import model.Cell;

public class SceneController {
    @FXML
    TextField XCellsAmountField;

    @FXML
    TextField YCellsAmountField;

    @FXML
    GridPane stageGridPane;

    @FXML
    ComboBox<String> CellType;
    @FXML
    Button SubmitButton;
    @FXML
    private Button generateButton;
    private Cell cells[][]; // поле с клетками. Основное поле считается с 1 (не 0) элемента массива и заканчивается n-1
    private Cell start;
    private Cell finish;

    @FXML
    private void buttonGenerateClicked() {
        start = null;
        finish = null;
        ObservableList<String> options = FXCollections.observableArrayList("Wall", "Start", "End");
        CellType.setItems(options);
        CellType.setValue("Wall");
        CellType.setVisible(true);
        // В будущем добавим непроходимые клетки по краям чтобы упростить логику вдальнейшем
        int xCellsAmount = Integer.parseInt(XCellsAmountField.getText()) + 2;
        int yCellsAmount = Integer.parseInt(YCellsAmountField.getText()) + 2;
        cells = new Cell[xCellsAmount][yCellsAmount];
        makeBoard(cells);
        System.out.println("GENERATION COMPLETE. X cells amount is " + XCellsAmountField.getText()
                + " and Y cells amount is " + YCellsAmountField.getText()
                + "\n Row count: " + getRowCount(stageGridPane));
    }

    @FXML
    private void buttonSubmitClicked() {
        AStar astar = new AStar(cells, start, finish);
        astar.findWay();
        makeBoard(cells);
    }

    /**
     * Заполняет рамку вокруг основного поля непроходимыми клетками
     */
    private void fillCircuit() {
        for (int i = 0; i < cells.length; i++) {
            cells[0][i] = new Cell(10, 10, Color.WHITE, true, 0, i);
            cells[cells.length - 1][i] = new Cell(10, 10, Color.WHITE, true, cells.length - 1, i);
        }
        for (int i = 0; i < cells[0].length; i++) {
            cells[i][0] = new Cell(10, 10, Color.WHITE, true, i, 0);
            cells[i][cells[0].length - 1] = new Cell(10, 10, Color.WHITE, true, i, cells[0].length - 1);
        }
    }

    public void makeBoard(Cell[][] cells) {
        if (stageGridPane != null) {
            // Устанавливаем размер клетки
            stageGridPane.getColumnConstraints().add(new ColumnConstraints(10));
            stageGridPane.getRowConstraints().add(new RowConstraints(10));
            if (cells[0][0] == null) {
                fillCircuit();
            }
            // Заполняем массив клеток основного поля и выводим его на экран
            for (int i = 1; i < cells.length - 1; i++) {
                stageGridPane.addRow(i);
                for (int j = 1; j < cells[0].length - 1; j++) {
                    stageGridPane.addColumn(j);
                    Cell tempRect = new Cell(10, 10, Color.WHITE, i, j);
                    if (cells[i][j] == null) {
                        stageGridPane.add(cells[i][j] = tempRect, i, j);
                    } else {
                        if (cells[i][j].isRoad) {
                            cells[i][j].setFill(Color.BLUE);
                        }
//                        stageGridPane.add(cells[i][j], i, j);
                    }
                    cells[i][j].setStroke(Color.BLACK);
                    cells[i][j].setOnMouseClicked(t -> {
                        switch (CellType.getValue()) {
                            case "Wall":
                                if (tempRect.getFill() == Color.BLACK) {
                                    tempRect.setFill(Color.WHITE);
                                    tempRect.isBlocked = false;
                                } else {
                                    tempRect.setFill(Color.BLACK);
                                    tempRect.isBlocked = true;
                                }
                                break;
                            case "Start":
                                if (tempRect.getFill() == Color.GREEN) {
                                    tempRect.setFill(Color.WHITE);
                                    start = null;
                                } else if (start == null) {
                                    tempRect.setFill(Color.GREEN);
                                    start = tempRect;
                                }
                                break;
                            case "End":
                                if (tempRect.getFill() == Color.RED) {
                                    tempRect.setFill(Color.WHITE);
                                    finish = null;
                                } else if (finish == null) {
                                    tempRect.setFill(Color.RED);
                                    finish = tempRect;
                                }
                                break;
                        }
                        printRectangleArrayWeights(cells);
                    });
                }
            }
        } else {
            System.out.print("stageGridPane is null");
        }
    }

    // debug only
    private int getRowCount(GridPane pane) {
        int numRows = pane.getRowConstraints().size();
        for (int i = 0; i < pane.getChildren().size(); i++) {
            Node child = pane.getChildren().get(i);
            if (child.isManaged()) {
                Integer rowIndex = GridPane.getRowIndex(child);
                if(rowIndex != null){
                    numRows = Math.max(numRows,rowIndex+1);
                }
            }
        }
        return numRows;
    }

    // debug only
    private void printRectangleArrayWeights(Cell[][] rectangleArray) {
        System.out.println("Nodes weight: \n");
        for (Cell[] eRectArr : rectangleArray) {
            for (Cell eRect : eRectArr) {
                System.out.println("X: " + eRect.x + " Y: " + eRect.y + " BLOCKED: " + eRect.isBlocked);
            }
        }
    }
}
