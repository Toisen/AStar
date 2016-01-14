package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import model.Cell;

/**
 * Created by Toisen on 16.12.2015.
 */
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
    private Button generateButton;

    @FXML
    Button SubmitButton;

    private Cell cells[][]; // поле с клетками. Основное поле считается с 1 (не 0) элемента массива и заканчивается n-1
    private Cell start;
    private Cell finish;

    @FXML
    private void buttonGenerateClicked() {
//        stageGridPane.setGridLinesVisible(true);    // TODO: remove if necessary

        ObservableList<String> options = FXCollections.observableArrayList("Wall", "Start", "End");
        CellType.setItems(options);
        CellType.setValue("Wall");
        CellType.setVisible(true);
        // В будущем добавим непроходимые клетки по краям чтобы упростить логику вдальнейшем
        int xCellsAmount = Integer.parseInt(XCellsAmountField.getText()) + 2;
        int yCellsAmount = Integer.parseInt(YCellsAmountField.getText()) + 2;
        cells = new Cell[xCellsAmount][yCellsAmount];
        if (stageGridPane != null) {
            // Устанавливаем размер клетки
            stageGridPane.getColumnConstraints().add(new ColumnConstraints(10));
            stageGridPane.getRowConstraints().add(new RowConstraints(10));
            // Заполняем рамку основного поля непроходимыми клетками
            fillCircuit();
            // Заполняем массив клеток основного поля и выводим его на экран
            for (int i = 1; i < xCellsAmount - 1; i++) {
                stageGridPane.addRow(i);
                for (int j = 1; j < yCellsAmount - 1; j++) {
                    Cell tempRect = new Cell(10, 10, Color.WHITE, i, j);
                    stageGridPane.addColumn(j);
                    stageGridPane.add(cells[i][j] = tempRect , i, j);
                    cells[i][j].setStroke(Color.BLACK);
                    cells[i][j].setOnMouseClicked(new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent t) {
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
//                            printRectangleArrayWeights(cells);
                        }
                    });
                }
            }
        } else {
            System.out.print("stageGridPane is null");
        }
        System.out.println("GENERATION COMPLETE. X cells amount is " + XCellsAmountField.getText()
                + " and Y cells amount is " + YCellsAmountField.getText()
                + "\n Row count: " + getRowCount(stageGridPane));

    }

    @FXML
    private void buttonSubmitClicked() {
    }

    /**
     * Заполняет рамку вокруг основного поля непроходимыми клетками
     */
    private void fillCircuit() {
        for (int i = 0; i < cells.length; i++) {
            cells[0][i] = new Cell(10, 10, Color.WHITE, true, 0, i);
            cells[cells.length][i] = new Cell(10, 10, Color.WHITE, true, 0, i);
        }
        for (int i = 0; i < cells[0].length; i++) {
            cells[i][0] = new Cell(10, 10, Color.WHITE, true, 0, i);
            cells[cells[0].length][i] = new Cell(10, 10, Color.WHITE, true, 0, i);
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
                System.out.println(eRect.isBlocked);
            }
        }
    }
}
