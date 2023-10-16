package dev.grcq.api.impl;

import dev.grcq.api.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static dev.grcq.api.PieceType.KNIGHT;

public class Board implements IBoard {

    private Piece[][] board = new Piece[8][8];
    private List<Move> moveHistory = new ArrayList<>();

    private Colour turn;

    public Board() {
        this.resetBoard();
    }

    public Board(String fen) {
        this();
    }

    public void resetBoard() {
        this.board = new Piece[8][8];
        this.turn = Colour.WHITE;

        this.board[0][0] = new Piece(PieceType.ROOK, Colour.WHITE);
        this.board[0][1] = new Piece(PieceType.KNIGHT, Colour.WHITE);
        this.board[0][2] = new Piece(PieceType.BISHOP, Colour.WHITE);
        this.board[0][3] = new Piece(PieceType.QUEEN, Colour.WHITE);
        this.board[0][4] = new Piece(PieceType.KING, Colour.WHITE);
        this.board[0][5] = new Piece(PieceType.BISHOP, Colour.WHITE);
        this.board[0][6] = new Piece(PieceType.KNIGHT, Colour.WHITE);
        this.board[0][7] = new Piece(PieceType.ROOK, Colour.WHITE);
        for (int i = 0; i < 8; i++) {
            this.board[1][i] = new Piece(PieceType.PAWN, Colour.WHITE);
        }

        this.board[7][0] = new Piece(PieceType.ROOK, Colour.BLACK);
        this.board[7][1] = new Piece(PieceType.KNIGHT, Colour.BLACK);
        this.board[7][2] = new Piece(PieceType.BISHOP, Colour.BLACK);
        this.board[7][3] = new Piece(PieceType.QUEEN, Colour.BLACK);
        this.board[7][4] = new Piece(PieceType.KING, Colour.BLACK);
        this.board[7][5] = new Piece(PieceType.BISHOP, Colour.BLACK);
        this.board[7][6] = new Piece(PieceType.KNIGHT, Colour.BLACK);
        this.board[7][7] = new Piece(PieceType.ROOK, Colour.BLACK);
        for (int i = 0; i < 8; i++) {
            this.board[6][i] = new Piece(PieceType.PAWN, Colour.BLACK);
        }
    }

    @Override
    public boolean validateMove(Move move) {
        Square fromSquare = Square.fromNotation(move.getFrom());
        Square toSquare = Square.fromNotation(move.getTo());

        Piece piece = getPiece(fromSquare);
        if (piece == null) return false;

        Piece toPiece = getPiece(toSquare);
        if (toPiece != null) {
            move.setCapture(true);
            move.setCapturedType(toPiece.getType());

            if (piece.getColour() == toPiece.getColour()) return false;
        }

        if (isCheck()) {
            if (!move.isCheck()) {
                move(move);
                if (isCheck()) {
                    undo();
                    return false;
                }

                undo();
            }
        }

        return validatePieceMovement(fromSquare, toSquare, piece.getType());
    }

    private boolean validatePieceMovement(Square from, Square to, PieceType type) {
        switch (type) {
            case PAWN:
                if (this.turn == Colour.WHITE) {
                    if (from.getFile() != to.getFile()) {
                        // todo: en passant
                        return false;
                    }

                    if (from.getRow() == 1 && from.getRow() + 2 == to.getRow()) {
                        if (getPiece(to) != null && getPiece(Square.fromRowAndFile(from.getRow() + 1, from.getFile())) != null) return false;
                        return true;
                    }

                    if (from.getRow() + 1 == to.getRow()) {
                        if (getPiece(to) != null) return false;
                        return true;
                    }
                } else {
                    if (from.getFile() != to.getFile()) {
                        // todo: en passant
                        return false;
                    }

                    if (from.getRow() == 6 && from.getRow() - 2 == to.getRow()) {
                        if (getPiece(to) != null && getPiece(Square.fromRowAndFile(from.getRow() - 1, from.getFile())) != null) return false;
                        return true;
                    }

                    if (from.getRow() - 1 == to.getRow()) {
                        if (getPiece(to) != null) return false;
                        return true;
                    }
                }
            case KNIGHT:
                if (Math.abs(from.getRow() - to.getRow()) == 2 && Math.abs(from.getFile() - to.getFile()) == 1) return true;
                if (Math.abs(from.getRow() - to.getRow()) == 1 && Math.abs(from.getFile() - to.getFile()) == 2) return true;
                break;
            case BISHOP:
                if (Math.abs(from.getRow() - to.getRow()) == Math.abs(from.getFile() - to.getFile())) {
                    int row = from.getRow();
                    int file = from.getFile();
                    int rowDiff = (from.getRow() < to.getRow() ? 1 : -1);
                    int fileDiff = (from.getFile() < to.getFile() ? 1 : -1);

                    while (row != to.getRow() && file != to.getFile()) {
                        row += rowDiff;
                        file += fileDiff;

                        if (getPiece(Square.fromRowAndFile(row, file)) != null) return false;
                    }

                    return true;
                }
                break;
            case ROOK:
                if (from.getRow() == to.getRow()) {
                    int file = from.getFile();
                    int fileDiff = (from.getFile() < to.getFile() ? 1 : -1);

                    while (file != to.getFile()) {
                        file += fileDiff;

                        if (getPiece(Square.fromRowAndFile(from.getRow(), file)) != null) return false;
                    }

                    return true;
                }

                if (from.getFile() == to.getFile()) {
                    int row = from.getRow();
                    int rowDiff = (from.getRow() < to.getRow() ? 1 : -1);

                    while (row != to.getRow()) {
                        row += rowDiff;

                        if (getPiece(Square.fromRowAndFile(row, from.getFile())) != null) return false;
                    }

                    return true;
                }
                break;
            case QUEEN:
                if (Math.abs(from.getRow() - to.getRow()) == Math.abs(from.getFile() - to.getFile())) {
                    int row = from.getRow();
                    int file = from.getFile();
                    int rowDiff = (from.getRow() < to.getRow() ? 1 : -1);
                    int fileDiff = (from.getFile() < to.getFile() ? 1 : -1);

                    while (row != to.getRow() && file != to.getFile()) {
                        row += rowDiff;
                        file += fileDiff;

                        if (getPiece(Square.fromRowAndFile(row, file)) != null) return false;
                    }

                    return true;
                }

                if (from.getRow() == to.getRow()) {
                    int file = from.getFile();
                    int fileDiff = (from.getFile() < to.getFile() ? 1 : -1);

                    while (file != to.getFile()) {
                        file += fileDiff;

                        if (getPiece(Square.fromRowAndFile(from.getRow(), file)) != null) return false;
                    }

                    return true;
                }

                if (from.getFile() == to.getFile()) {
                    int row = from.getRow();
                    int rowDiff = (from.getRow() < to.getRow() ? 1 : -1);

                    while (row != to.getRow()) {
                        row += rowDiff;

                        if (getPiece(Square.fromRowAndFile(row, from.getFile())) != null) return false;
                    }

                    return true;
                }
                break;
            case KING:
                if (Math.abs(from.getRow() - to.getRow()) <= 1 && Math.abs(from.getFile() - to.getFile()) <= 1) return true;
                break;
        }

        return false;
    }

    @Override
    public Move[] getMoves() {
        return getMoves(false);
    }

    public Move[] getMoves(PieceType... moveForTypes) {
        Move[] moves = getMoves(false);
        return Arrays.stream(moves).filter(move -> {
            for (PieceType type : moveForTypes) {
                if (type == Objects.requireNonNull(getPiece(Square.fromNotation(move.getFrom()))).getType())
                    return true;
            }

            return false;
        }).toArray(Move[]::new);
    }

    public Move[] getMoves(boolean checkOnly) {
        List<Move> moves = new ArrayList<>();
        for (int i = 0; i < Square.values().length; i++) {
            Square from = Square.values()[i];
            for (int j = 0; j < Square.values().length; j++) {
                Square to = Square.values()[j];
                Piece piece = getPiece(from);
                if (piece == null) continue;
                if (piece.getColour() != turn) continue;

                Move move = new Move(from.getNotation(), to.getNotation());

                if (validateMove(move) && (
                        !checkOnly || (getPiece(to) != null && getPiece(to).getType() == PieceType.KING)
                )) moves.add(move);
            }
        }

        return moves.toArray(new Move[0]);
    }

    @Nullable
    @Override
    public Piece getPiece(Square square) {
        if (square == null) return null;

        int row = square.getRow();
        int file = square.getFile();
        return this.board[row][file];
    }

    @Override
    public boolean isDraw() {
        return getGameOverReason() != null && (
                getGameOverReason() == GameFinishedReason.STALEMATE ||
                getGameOverReason() == GameFinishedReason.FIFTY_MOVE_RULE ||
                getGameOverReason() == GameFinishedReason.INSUFFICIENT_MATERIAL ||
                getGameOverReason() == GameFinishedReason.TIMEOUT ||
                getGameOverReason() == GameFinishedReason.THREE_FOLD_REPETITION ||
                getGameOverReason() == GameFinishedReason.TIMEOUT_AND_INSUFFICIENT_MATERIAL
        );
    }

    @Override
    public boolean isCheckmate() {
        return isCheck() && getMoves().length == 0;
    }

    public boolean isCheck() {
        return (getLastMove() != null && getLastMove().isCheck());
    }

    @Nullable
    @Override
    public GameFinishedReason getGameOverReason() {
        if (!isCheck() && getMoves().length == 0) return GameFinishedReason.STALEMATE;
        return null;
    }

    @Override
    public String toFEN() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < this.board.length; i++) {
            boolean a = false;
            int k = 0;
            for (int j = 0; j < this.board[i].length; j++) {
                Piece piece = this.board[i][j];
                if (piece == null) {
                    k++;
                    a = true;
                    continue;
                }

                if (k != 0) str.append(k);
                k = 0;
                a = false;

                PieceType type = piece.getType();
                str.append((piece.getColour() == Colour.WHITE ? (type.getId() + "").toUpperCase() : type.getId()));
            }

            if (a) str.append(k);
            str.append("/");
        }

        str.deleteCharAt(str.length() - 1);
        return str.toString();
    }

    @Override
    public void move(Move move) {

    }

    @Override
    public Move undo() {
        return moveHistory.remove(moveHistory.size() - 1);
    }

    @Override
    public Move undo(Move move) {
        return null;
    }

    private Move getLastMove() {
        return getLastMove(turn);
    }

    private Move getLastMove(Colour colour) {
        if ((colour == turn && moveHistory.size() < 2) || moveHistory.isEmpty()) return null;
        return (colour == turn ? moveHistory.get(moveHistory.size() - 2) : moveHistory.get(moveHistory.size() - 1));
    }
}
