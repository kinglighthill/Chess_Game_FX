package sample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;


import javax.swing.event.HyperlinkEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by KCA on 12/11/2017.
 */
public class Board extends GridPane {
    private Main main;

    private static Board context = new Board();
    private Cell[][] cells = new Cell[8][8];
    private ArrayList<Cell> possible_moves = null;
    private Cell selectedCell = null;
    private Cell enPassantCell;

    private Color blue = new Color(0, 0.3058, 1, 1);

    private String path = "/sample/res/images/";

    private String[] white_pieces_path = {"White_Pawn.png", "White_Rook.png", "White_Knight.png",
            "White_Bishop.png", "White_Queen.png", "White_King.png"};
    private String[] black_pieces_path = {"Black_Pawn.png", "Black_Rook.png", "Black_Knight.png",
            "Black_Bishop.png", "Black_Queen.png", "Black_King.png"};

    protected static King whiteKing, blackKing;
    protected static HashMap<String, Piece> whitePieces, blackPieces;

    private int player = 0;
    private int moves = 0;

    private int promotionCountWhite = 0;
    private int promotionCountBlack = 0;

    public boolean isMoved = false;
    public boolean isChanged = false;

    private Board(){
        context = this;
        this.setAlignment(Pos.CENTER);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("board.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try  {
            loader.load();
        }

        catch (IOException e) {
            throw new RuntimeException(e);
        }

        whitePieces = new HashMap<>();
        blackPieces = new HashMap<>();

        setUpBoard();
    }

    protected static Board getInstance() {
        return context;
    }

    protected static Board getInstance(Main main) {
        context.main = main;
        return context;
    }

    protected static Board getNewInstance() {
        context = new Board();
        return context;
    }

    public int getMoves() {
        return moves;
    }

    public int getPlayer() {
        return player;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void setMoves(int moves) {
        this.moves = moves;
    }

    public void setEnPassantCell(Cell enPassantCell) {
        this.enPassantCell = enPassantCell;
    }

    private void setUpBoard() {
        String[] file = {"a", "b", "c", "d", "e", "f", "g", "h"};
        String[] rank = {"8", "7", "6", "5", "4", "3", "2", "1"};
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Cell cell = (Cell) this.getChildren().get(j+i*8);
                cells[i][j] = cell;
                cell.setX_position(i);
                cell.setY_position(j);
                cell.setFile(file[j]);
                cell.setRank(rank[i]);
            }
        }
    }

    private void  addPieces(Cell c, HashMap<String, Piece> pieces) {
        String id;
        switch (c.getPiece().getName()) {
            case "Queen":
                id = "Q" + c.getName();
                pieces.put(id, c.getPiece());
                c.getPiece().setId(id);
                break;
            case "Rook":
                id = "R" + c.getName();
                pieces.put(id, c.getPiece());
                c.getPiece().setId(id);
                break;
            case "Bishop":
                id = "B" + c.getName();
                pieces.put(id, c.getPiece());
                c.getPiece().setId(id);
                break;
            case "Knight":
                id = "N" + c.getName();
                pieces.put(id, c.getPiece());
                c.getPiece().setId(id);
                break;
            case "Pawn":
                id = "P" + c.getName();
                pieces.put(id, c.getPiece());
                c.getPiece().setId(id);
                break;
            default:
                id = "K" + c.getName();
                c.getPiece().setId(id);
                break;
        }
    }

    public void setUpPieces() {
        for (Cell[] cell : cells) {
            for (Cell c : cell) {
                if (c.getPiece() != null) {
                    switch (c.getPiece().getColor()) {
                        case 0:
                            addPieces(c, blackPieces);
                            break;
                        case 1:
                            addPieces(c, whitePieces);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public void refreshBoard(String whitePosition, String blackPosition) {
        for (Cell[] cells : cells) {
            for (Cell cell : cells) {
                if (cell.getPiece() != null) {
                    cell.deletePiece();
                }
            }
        }
        setWhitePieces(whitePosition);
        setBlackPieces(blackPosition);
        if(main.isTimerOn) {
            main.resetTimerLabel();
        }
        isMoved = false;
        player = 0;
    }

    private String getPiecePath(String[] piecePathArray, String piecePath) {
        String selectedPath = "";
        for (String path: piecePathArray){
            if (path.equalsIgnoreCase(piecePath)) {
                selectedPath =  path;
            }
        }
        return selectedPath;
    }

    public Cell[][] getCells() {
        return cells;
    }

    private Piece getPiece(String piece, int k) {
        Piece selectedPiece;
        switch (piece) {
            case "King":
                selectedPiece = new King();
                if (k == 0) {
                    blackKing = (King) selectedPiece;
                }
                else {
                    whiteKing = (King) selectedPiece;
                }
                break;
            case "Queen":
                selectedPiece = new Queen();
                break;
            case "Bishop":
                selectedPiece = new Bishop();
                break;
            case "Knight":
                selectedPiece = new Knight();
                break;
            case "Rook":
                selectedPiece = new Rook();
                break;
            default:
                selectedPiece = new Pawn();
                break;
        }
        return selectedPiece;
    }

    private void setUpCells(String[] piecePathArray, String piece, String piecePath, int i, int j, int k) {
        Cell cell = (Cell) this.getChildren().get(j+i*8);
        cell.setPiece(getPiece(piece, k),
                path + getPiecePath(piecePathArray, piecePath));
        cell.getPiece().setPath(path +
                getPiecePath(piecePathArray, piecePath));
        cell.getPiece().setColor(k);
        cell.getPiece().setX_position(i);
        cell.getPiece().setY_position(j);
    }

    private void setUpCells(int i, int j, String orientation) {
        Cell cell = (Cell) this.getChildren().get(j+i*8);
        if (cell.getPiece() != null) {
            if (cell.getPiece().getName() == "Pawn" || cell.getPiece().getName() == "King") {
                cell.getPiece().setOrientation(orientation);
            }
        }
    }

    public void setBlackPieces(String position) {
        main.setBlackPosition(position);
        switch (position) {
            case "DOWN" :
                for(int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++) {
                        if(i == 6){
                            setUpCells(black_pieces_path, "Pawn", "Black_Pawn.png",
                                    i, j, 0);
                            setUpCells(i, j, "up");
                        }
                        if(i == 7) {
                            if(j == 4) {
                                setUpCells(black_pieces_path, "Queen",
                                        "Black_Queen.png", i, j, 0);
                            }
                            else if (j == 3) {
                                setUpCells(black_pieces_path, "King",
                                        "Black_King.png", i, j, 0);
                                setUpCells(i, j, "up");
                            }
                            else if (j == 0 || j == 7) {
                                setUpCells(black_pieces_path, "Rook",
                                        "Black_Rook.png", i, j, 0);
                            }
                            else if (j == 1 || j == 6) {
                                setUpCells(black_pieces_path, "Knight",
                                        "Black_Knight.png", i, j, 0);
                            }
                            else if (j == 2 || j == 5) {
                                setUpCells(black_pieces_path, "Bishop",
                                        "Black_Bishop.png", i, j, 0);
                            }
                        }
                    }
                }
                break;
            default:
                for(int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++) {
                        if(i == 0) {
                            if(j == 3) {
                                setUpCells(black_pieces_path, "Queen",
                                        "Black_Queen.png", i, j, 0);
                            }
                            else if (j == 4) {
                                setUpCells(black_pieces_path, "King",
                                        "Black_King.png", i, j, 0);
                                setUpCells(i, j, "down");
                            }
                            else if (j == 0 || j == 7) {
                                setUpCells(black_pieces_path, "Rook",
                                        "Black_Rook.png", i, j, 0);
                            }
                            else if (j == 1 || j == 6) {
                                setUpCells(black_pieces_path, "Knight",
                                        "Black_Knight.png", i, j, 0);
                            }
                            else if (j == 2 || j == 5) {
                                setUpCells(black_pieces_path, "Bishop",
                                        "Black_Bishop.png", i, j, 0);
                            }
                        }
                        if(i == 1){
                            setUpCells(black_pieces_path, "Pawn", "Black_Pawn.png",
                                    i, j, 0);
                            setUpCells(i, j, "down");
                        }
                    }
                }
                break;
        }
    }

    public void setWhitePieces(String position) {
        main.setWhitePosition(position);
        switch (position) {
            case "UP" :
                for(int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++) {
                        if(i == 0) {
                            if(j == 4) {
                                setUpCells(white_pieces_path, "Queen",
                                        "White_Queen.png", i, j, 1);
                            }
                            else if (j == 3) {
                                setUpCells(white_pieces_path, "King",
                                        "White_King.png", i, j, 1);
                                setUpCells(i, j, "down");
                            }
                            else if (j == 0 || j == 7) {
                                setUpCells(white_pieces_path, "Rook",
                                        "White_Rook.png", i, j, 1);
                            }
                            else if (j == 1 || j == 6) {
                                setUpCells(white_pieces_path, "Knight",
                                        "White_Knight.png", i, j, 1);
                            }
                            else if (j == 2 || j == 5) {
                                setUpCells(white_pieces_path, "Bishop",
                                        "White_Bishop.png", i, j, 1);
                            }
                        }
                        if(i == 1){
                            setUpCells(white_pieces_path, "Pawn", "White_Pawn.png",
                                    i, j, 1);
                            setUpCells(i, j, "down");
                        }
                    }
                }
                break;
            default:
                for(int i = 0; i < 8; i++){
                    for(int j = 0; j < 8; j++) {
                        if(i == 6){
                            setUpCells(white_pieces_path, "Pawn", "White_Pawn.png",
                                    i, j, 1);
                            setUpCells(i, j, "up");
                        }
                        if(i == 7) {
                            if(j == 3) {
                                setUpCells(white_pieces_path, "Queen",
                                        "White_Queen.png", i, j, 1);
                            }
                            else if (j == 4) {
                                setUpCells(white_pieces_path, "King",
                                        "White_King.png", i, j, 1);
                                setUpCells(i, j, "up");
                            }
                            else if (j == 0 || j == 7) {
                                setUpCells(white_pieces_path, "Rook",
                                        "White_Rook.png", i, j, 1);
                            }
                            else if (j == 1 || j == 6) {
                                setUpCells(white_pieces_path, "Knight",
                                        "White_Knight.png", i, j, 1);
                            }
                            else if (j == 2 || j == 5) {
                                setUpCells(white_pieces_path, "Bishop",
                                        "White_Bishop.png", i, j, 1);
                            }
                        }
                    }
                }
                break;
        }
    }

    public void checkKing() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].getPiece() != null && cells[i][j].getPiece().getName() == "King") {
                    King king = (King) cells[i][j].getPiece();
                    if (king.isOn_check()) {
                        cells[i][j].checkCell();
                    }
                    king.isChecked(king.getX_position(), king.getY_position());
                    if (!king.isChecked()) {
                        king.setOn_check(false);
                    }
                    if (!king.isOn_check()) {
                        cells[i][j].decheckCell();
                    }
                }
            }
        }
    }

    private void move(Cell initial_cell, Cell new_cell) {
        Piece piece = initial_cell.getPiece();
        String path = piece.getPath();
        if (new_cell.getPiece() != null) {
            new_cell.deletePiece();
        }
        new_cell.setPiece(piece, path);
        for (Cell cell : possible_moves) {
            if (cell.isCastle()) {
                cell.setCastle(false);
            }
            cell.dehighlightCell();
        }
        if (initial_cell.getPiece() != null && initial_cell.getPiece().getName() == "King") {
            if (initial_cell.isChecked()) {
                initial_cell.decheckCell();
            }
        }
        initial_cell.deletePiece();
        new_cell.getPiece().setX_position(new_cell.getX_position());
        new_cell.getPiece().setY_position(new_cell.getY_position());
        new_cell.getPiece().setMoved(true);

        moves++;

        piecesCheckKing();
        checkKing();
        checkMate();

        selectedCell = null;

        changeChance();
        setMoved();
        setChanged();

    }

    public void changeChance() {
        player ^= 1;
        if (main.isTimerOn) {
            main.changeChance(player);
        }
        setChanged();
    }

    private void setMoved() {
        if(!isMoved) {
            isMoved = true;
        }
    }

    private void setChanged() {
        if(!isChanged) {
            isChanged = true;
        }
    }

    public void movePiece(MouseEvent event, Cell initial_cell, Cell new_cell) {
        if (new_cell.isCastle()) {
            if (new_cell.getCastleCell() != null) {
                move(new_cell.getCastleCell()[0], new_cell.getCastleCell()[1]);
            }
            new_cell.setCastle(false);
            move(initial_cell, new_cell);
            changeChance();
        }
        else if (initial_cell.getPiece().getName() == "Pawn" &&
                (initial_cell.getPiece()).getOrientation() == "down" &&
                new_cell.getX_position() == 7) {
            promotePawn(event, initial_cell, new_cell);
        }
        else if (initial_cell.getPiece().getName() == "Pawn" &&
                (initial_cell.getPiece()).getOrientation() == "up" &&
                new_cell.getX_position() == 0) {
            promotePawn(event, initial_cell, new_cell);
        }
        else if (initial_cell.getPiece() != null && initial_cell.getPiece().getName() == "Pawn" &&
                !initial_cell.getPiece().isMoved()) {
            checkEnPassant(initial_cell, new_cell);
            move(initial_cell, new_cell);
        }
        else if (new_cell.isEnPassant()) {
            move(initial_cell, new_cell);
            if (new_cell.getEnPassantCell() != null) {
                new_cell.getEnPassantCell().deletePiece();
            }
            new_cell.setEnPassant(false);
        }
        else {
            move(initial_cell, new_cell);
        }
        if (enPassantCell != null && enPassantCell.getPiece() != null && enPassantCell.getPiece().getName() == "Pawn" &&
                new_cell.getPiece() != null && enPassantCell.getPiece().getOrientation()
                != new_cell.getPiece().getOrientation()) {
            ((Pawn)enPassantCell.getPiece()).setEn_passant(false);
            enPassantCell.setEnPassant(false);
            enPassantCell = null;
        }
    }

    private void checkEnPassant(Cell initial_cell, Cell new_cell) {
        if (initial_cell.getPiece().getOrientation() == "down") {
            if (new_cell.getX_position() - initial_cell.getX_position() == 2) {
                if (new_cell.getY_position() < 7 && cells[new_cell.getX_position()][new_cell.getY_position()+1].getPiece()
                        != null && cells[new_cell.getX_position()][new_cell.getY_position()+1].getPiece().getName() ==
                        "Pawn" && initial_cell.getPiece().getOrientation() !=
                        cells[new_cell.getX_position()][new_cell.getY_position()+1].getPiece().getOrientation()) {
                    ((Pawn)initial_cell.getPiece()).setEn_passant(true);
                }
                if (new_cell.getY_position() > 0 && cells[new_cell.getX_position()][new_cell.getY_position()-1].getPiece()
                        != null && cells[new_cell.getX_position()][new_cell.getY_position()-1].getPiece().getName() ==
                        "Pawn" && initial_cell.getPiece().getOrientation() !=
                        cells[new_cell.getX_position()][new_cell.getY_position()-1].getPiece().getOrientation()) {
                    ((Pawn)initial_cell.getPiece()).setEn_passant(true);
                }
            }
        }
        else {
            if (initial_cell.getX_position() - new_cell.getX_position() == 2) {
                if (new_cell.getY_position() < 7 && cells[new_cell.getX_position()][new_cell.getY_position()+1].getPiece()
                        != null && cells[new_cell.getX_position()][new_cell.getY_position()+1].getPiece().getName() ==
                        "Pawn" && initial_cell.getPiece().getOrientation() !=
                        cells[new_cell.getX_position()][new_cell.getY_position()+1].getPiece().getOrientation()) {
                    ((Pawn)initial_cell.getPiece()).setEn_passant(true);
                }
                if (new_cell.getY_position() > 0 && cells[new_cell.getX_position()][new_cell.getY_position()-1].getPiece()
                        != null && cells[new_cell.getX_position()][new_cell.getY_position()-1].getPiece().getName() ==
                        "Pawn" && initial_cell.getPiece().getOrientation() !=
                        cells[new_cell.getX_position()][new_cell.getY_position()-1].getPiece().getOrientation()) {
                    ((Pawn)initial_cell.getPiece()).setEn_passant(true);
            }
            }
        }
    }

    private void promotePawn(MouseEvent event, Cell initial_cell, Cell new_cell) {
        MenuItem[] menuItems = {new MenuItem("Queen"), new MenuItem("Rook"),
                new MenuItem("Bishop"), new MenuItem("Knight")};
        ContextMenu contextMenu = new ContextMenu(menuItems);
        Listener listener = new Listener(contextMenu, initial_cell, new_cell);
        for (MenuItem menuItem : contextMenu.getItems()) {
            menuItem.setOnAction(listener);
        }
        contextMenu.show(this, (int) event.getScreenX(), (int) event.getScreenY());
    }

    private class Listener implements EventHandler<javafx.event.ActionEvent> {

        ContextMenu contextMenu;
        Cell initial_cell, new_cell;
        String path;

        public Listener (ContextMenu contextMenu, Cell initial_cell, Cell new_cell) {
            this.contextMenu = contextMenu;
            this.initial_cell = initial_cell;
            this.new_cell = new_cell;
        }

        @Override
        public void handle(javafx.event.ActionEvent event) {
            MenuItem menuItem = (MenuItem) event.getSource();
            String promotedPieceName = menuItem.getText();

            switch (promotedPieceName) {
                case "Rook":
                    setPath(new Rook(), "Rook", "R");
                    break;
                case "Bishop":
                    setPath(new Bishop(), "Bishop", "B");
                    break;
                case "Knight":
                    setPath(new Knight(), "Knight", "N");
                    break;
                default:
                    setPath(new Queen(), "Queen", "Q");
                    break;
            }

            move(initial_cell, new_cell);
            contextMenu.hide();
        }

        private void setPath(Piece piece, String name, String id) {
            if (initial_cell.getPiece().getColor() == 0) {
                piece.setColor(0);
                piece.setId(id + "i"+ String.valueOf(++promotionCountBlack));
                blackPieces.put(piece.getId(), piece);
                path = Board.context.path + "Black_" + name + ".png";
            }
            else {
                piece.setColor(1);
                piece.setId(id + "j"+ String.valueOf(++promotionCountWhite));
                whitePieces.put(piece.getId(), piece);
                path = Board.context.path + "White_" + name + ".png";
            }
            initial_cell.setPiece(piece, path);
            initial_cell.getPiece().setPath(path);
        }

    }

    private void piecesCheckKing() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                Piece piece = cells[i][j].getPiece();
                if (piece != null) {
                    if (piece.getName() == "Queen" || piece.getName() == "Rook" ||
                            piece.getName() == "Bishop" || piece.getName() == "Knight" || piece.getName() == "Pawn") {
                        piece.checkKing();
                    }
                }
            }
        }
    }

    public void checkMate() {
        if (blackKing.isOn_check() && isCheckMate(1)) {
            Platform.runLater( () ->
                createGameOverDialog("White wins")
            );
            blackKing.setCheck_mate(true);
        }
        else {
            blackKing.setCheck_mate(false);
        }

        if (whiteKing.isOn_check() && isCheckMate(0)) {
            whiteKing.setCheck_mate(true);
            Platform.runLater( () ->
                    createGameOverDialog("Black wins")
            );
        }
        else {
            whiteKing.setCheck_mate(false);
        }
        if(main.isTimerOn && ((blackKing.isOn_check() && isCheckMate(1)) || (whiteKing.isOn_check() && isCheckMate(0)) )) {
            main.stopTimer();
            main.hasEnded = true;
        }
    }

    private boolean isCheckMate(int k) {
        boolean isCheckMate = false;
        ArrayList<Cell> cell = new ArrayList<>();
        for (Cell[] cells : cells) {
            for (Cell c : cells) {
                Piece piece = c.getPiece();
                if (piece != null) {
                    if (piece.getColor() != k ) {
                        piece.move();
                        cell.addAll(piece.getPossible_moves());
                    }
                }
            }
        }
        if (cell.size() == 0) {
            isCheckMate = true;
        }
        return isCheckMate;
    }

    public void createGameOverDialog(String message) {
        Dialog dialog = new Dialog();
        dialog.setTitle("Game Over");
        dialog.setContentText(message);
        dialog.initOwner(Main.ROOT.getScene().getWindow());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK);
        dialog.show();
        dialog.getDialogPane().getScene().getWindow().setOnCloseRequest(
                (event) ->
                        main.quit()
        );
        Button okButton = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okButton.addEventFilter(ActionEvent.ACTION, event -> {
            main.quit();
        } );
    }

    public static Piece addDummyPiece(Piece piece, int x, int y) {
        Piece initialPiece = null;
        Cell cell = getInstance().cells[x][y];
        if (cell.getPiece() != null) {
            initialPiece = cell.getPiece();
        }
        cell.setPiece(piece);
        return initialPiece;
    }

    public static void removeDummyPiece(Piece piece, int x, int y) {
        Cell cell = getInstance().cells[x][y];
        cell.deletePiece();
        if (piece != null) {
            cell.setPiece(piece, piece.getPath());
        }
    }

    private void click(Cell cell) {
        if (cell.getPiece() != null) {
            Piece piece = cell.getPiece();
            piece.move();
            possible_moves = piece.getPossible_moves();
            for (Cell c : possible_moves) {
                c.highlightCell();
            }
        }
        selectedCell = cell;
    }

    private void listener(MouseEvent event, Cell cell) {
        boolean check = false;
        if (selectedCell != null) {
            // deselects a cell when another cell is been clicked
            if (cell != selectedCell && !cell.isHighlighted()) {
                selectedCell.deselectCell();
                for (Cell c : possible_moves) {
                    c.dehighlightCell();
                }
            }

            // deselects a cell when clicked the second time
            if (cell == selectedCell && cell.isSelected()) {
                selectedCell.deselectCell();
                for (Cell c : possible_moves) {
                    c.dehighlightCell();
                }
                // prevents click() from executing
                check = true;
            }

            // moves a piece from one cell to another
            if (cell.isHighlighted()) {
                movePiece(event, selectedCell, cell);
                // prevents click() from executing
                check = true;
            }
        }

        //Highlights possible cells and selects a cell
        if (!check && cell.getPiece() != null && !cell.isHighlighted()) {
            cell.selectCell();
            click(cell);
        }

        //Always checks the King if its on check or check mates
        if (blackKing != null && blackKing.isOn_check()) {
            blackKing.getCell().checkCell();
        }
        if (whiteKing != null && whiteKing.isOn_check()) {
            whiteKing.getCell().checkCell();
        }

        // check mates the King's cell
        if (blackKing != null && blackKing.isCheck_mate()) {
            blackKing.getCell().checkMateCell();
        }
        if (whiteKing != null && whiteKing.isCheck_mate()) {
            whiteKing.getCell().checkMateCell();
        }
    }

    @FXML public void listener1 (MouseEvent event) {
        Cell cell = (Cell) event.getSource();
        try {
            if (cell.isHighlighted() || cell.getPiece().getColor() != player) {
                listener(event, cell);
            }
        } catch (Exception ex){
            listener(event, cell);
        }
    }
}
