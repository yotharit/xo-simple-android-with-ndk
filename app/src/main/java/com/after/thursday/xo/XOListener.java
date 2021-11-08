package com.after.thursday.xo;

interface XOListener {
    void onWin(String player);
    void onDraw();
    void onUpdateTurn(String player);
    void onUpdateBoard(char[] board);
}
