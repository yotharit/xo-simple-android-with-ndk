package com.after.thursday.xo;

import android.util.Log;

import androidx.annotation.NonNull;

public class XOLibraryWrapper implements XOListener{

    static {
        System.loadLibrary("xo");
    }

    private final XOListener listener;

    public XOLibraryWrapper(XOListener listener) {
        this.listener = listener;
    }

    public native boolean placeXO(int row, int column);

    public native boolean resetGame();

    @Override
    public void onWin(@NonNull String player) {
        listener.onWin(player);
    }

    @Override
    public void onDraw() {
        listener.onDraw();
    }

    @Override
    public void onUpdateTurn(@NonNull String player) {
        listener.onUpdateTurn(player);
    }

    @Override
    public void onUpdateBoard(char[] board) {
        String result = new String(board);
        Log.d("LogXO", "Board : " + result);
        listener.onUpdateBoard(board);
    }
}