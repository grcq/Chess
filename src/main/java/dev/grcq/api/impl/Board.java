package dev.grcq.api.impl;

import dev.grcq.api.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Board implements IBoard {

    private Piece[][] board = new Piece[8][8];
    private List<Move> moveHistory = new ArrayList<>();

    public Board() {
        this.resetBoard();
    }

    public Board(String fen) {
        this();
    }

    public void resetBoard() {

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
        return getLastMove().isCheck();
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
        return moveHistory.get(moveHistory.size() - 1);
    }
}
