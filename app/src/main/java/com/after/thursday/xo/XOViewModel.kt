package com.after.thursday.xo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class XOViewModel : ViewModel(), XOListener {

    enum class GameStatus {
        WIN, DRAW, ONGOING
    }

    private lateinit var xoLibrary : XOLibraryWrapper
    var resultString : MutableLiveData<String> = MutableLiveData()
    var gameStatus : MutableLiveData<GameStatus> = MutableLiveData()
    var xoBoard: MutableLiveData<CharArray> = MutableLiveData()

    fun setup() {
        xoLibrary = XOLibraryWrapper(this)
    }

    fun placeXO(row: Int, column: Int) {
        val valid = xoLibrary.placeXO(row, column)
        if (!valid) {
            resultString.value = "Invalid Move"
        }
    }

    fun resetGame() {
        val reset = xoLibrary.resetGame()
        if (reset) {
            gameStatus.value = GameStatus.ONGOING
            resultString.value = "Player X Turn"
        }
    }

    fun getRowColumn(index : Int): Pair<Int, Int> {
        val column = index % 3
        val row = index / 3
        return Pair(row, column)
    }

    override fun onWin(player: String) {
        resultString.value = String.format("Winner Player %s", player)
        gameStatus.value = GameStatus.WIN
    }

    override fun onDraw() {
        resultString.value = "Draw"
        gameStatus.value = GameStatus.DRAW
    }

    override fun onUpdateTurn(player: String) {
        resultString.value = String.format("Player %s Turn", player)
    }

    override fun onUpdateBoard(board: CharArray?) {
        xoBoard.value = board
    }
}