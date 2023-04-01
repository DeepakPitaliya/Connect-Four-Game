package connectfour

import kotlin.math.max
import kotlin.math.min

fun main() {
    ConnectFour()
}

class ConnectFour {
    private var rows: Int = 6
    private var cols: Int = 7
    private var board: Array<Array<String>>
    private val player1: String
    private val player2: String
    private var gameOn = true
    private var totalGamesToPlay = 1
    private var gameNumber = 1          // current game number
    private var player1Score = 0
    private var player2Score = 0

    init {
        println("Connect Four")
        println("First player's name:")
        player1 = readln()
        println("Second player's name:")
        player2 = readln()
        board = getBoard()
        getNumGames()
        rows = board.size
        cols = board[0].size
        printBoard(this.board)
        play(board, player1)
    }

    private fun getBoard(): Array<Array<String>> {
        var inputAsList: List<String>

        while (true) {
            println("Set the board dimensions (Rows x Columns)\nPress Enter for default (6 x 7)")

            val input = readln().replace(" ", "").replace("\t", "").lowercase()

            if (input == "") break
            inputAsList = if (input.contains('x')) {
                input.split("x")
            } else {
                println("Invalid input")
                continue
            }

            try {
                rows = inputAsList[0].toInt()
                cols = inputAsList[1].toInt()
                if (rows !in 5..9) {
                    println("Board rows should be from 5 to 9")
                } else if (cols !in 5..9) {
                    println("Board columns should be from 5 to 9")
                } else {
                    break
                }
            } catch (e: Exception) {
                println("Invalid input")
            }
        }
        return Array(rows) { Array(cols) { " " } }
    }

    private fun printBoard(board: Array<Array<String>>) {
        for (i in 1..board[0].size) {
            print(" $i")
        }
        println()
        for (element in board) {
            for (cell in element) {
                print("║")
                print(cell)
            }
            println("║")
        }
        print("╚")
        repeat(board[0].size - 1) { print("═╩") }
        println("═╝")
    }

    private fun play(board: Array<Array<String>>, player: String) {
        var c: Int
        var r: Int
        while (gameOn) {
            println("$player's turn:")
            val input = readln()
            if (input == "end") {
                println("Game over!")
                break
            } else {
                try {
                    c = input.toInt() - 1
                } catch (e: Exception) {
                    println("Incorrect column number")
                    continue
                }
            }
            if (c !in 0 until cols) {
                println("The column number is out of range (1 - $cols)")
                continue
            }

            r = rows - 1

            while (r >= 0) {
                if (board[r][c] == " ") {
                    board[r][c] = if (player == player1) "o" else "*"
                    printBoard(board)
                    break
                } else {
                    r--
                    if (r < 0) println("Column ${c + 1} is full")
                }
            }
            checkStatus(r, c, board)
            play(board, player = if (player == player1) player2 else player1)
        }
    }

    private fun checkStatus(r: Int, c: Int, board: Array<Array<String>>) {

        fun checkWin(r: Int, c: Int) {
            if (board[r][c] == "o") {
                player1Score += 2
                println("Player $player1 won")
            } else if (board[r][c] == "*") {
                player2Score += 2
                println("Player $player2 won")
            }
            if (totalGamesToPlay == 1) {
                println("Game over!")
                gameOn = false
            } else {
                gameNumber += 1
                println("Score\n$player1: $player1Score $player2: $player2Score")
                if (gameNumber <= totalGamesToPlay) {
                    println("Game #$gameNumber")
                    resetGame(board)
                    printBoard(board)
                    play(board, player = if (gameNumber % 2 == 0) player2 else player1)
                } else {
                    println("Game over!")
                    gameOn = false
                }
            }
        }

        // check along rows
        for (i in r..r) {
            for (j in max(0, c - 3)..min(cols - 4, c)) {
                if (board[i][j] != " "
                    && board[i][j] == board[i][j + 1]
                    && board[i][j + 1] == board[i][j + 2]
                    && board[i][j + 2] == board[i][j + 3]
                ) {
                    checkWin(i, j)
                }
            }
        }

        // check along columns
        for (i in max(0, r - 3)..min(rows - 4, r)) {
            for (j in c..c) {
                if (board[i][j] != " "
                    && board[i][j] == board[i + 1][j]
                    && board[i + 1][j] == board[i + 2][j]
                    && board[i + 2][j] == board[i + 3][j]
                ) {
                    checkWin(i, j)
                }
            }
        }

        // check along diagonal upper left -> lower right
        for (i in 0..rows - 4) {
            for (j in 0..cols - 4) {
                if (board[i][j] != " "
                    && board[i][j] == board[i + 1][j + 1]
                    && board[i + 1][j + 1] == board[i + 2][j + 2]
                    && board[i + 2][j + 2] == board[i + 3][j + 3]
                ) {
                    checkWin(i, j)
                }
            }
        }

        // check along diagonal lower left -> upper right
        for (i in rows - 1 downTo 3) {
            for (j in 0..cols - 4) {
                if (board[i][j] != " "
                    && board[i][j] == board[i - 1][j + 1]
                    && board[i - 1][j + 1] == board[i - 2][j + 2]
                    && board[i - 2][j + 2] == board[i - 3][j + 3]
                ) {
                    checkWin(i, j)
                }
            }
        }

        // check for draw
        for (i in 0 until cols) {
            if (board[0][i] == " ") {
                break
            } else if (totalGamesToPlay == 1) {
                println("It is a draw")
                println("Game over!")
                gameOn = false
            } else {
                gameNumber += 1
                player1Score += 1
                player2Score += 1
                println("It is a draw")
                println("Score\n$player1: $player1Score $player2: $player2Score")
                if (gameNumber <= totalGamesToPlay) {
                    println("Game #$gameNumber")
                    resetGame(board)
                    printBoard(board)
                    play(board, player = if (gameNumber % 2 == 0) player2 else player1)
                } else {
                    println("Game over!")
                    break
                }
            }
        }
    }

    private fun getNumGames(): Int {
        while (true) {
            println(
                "Do you want to play single or multiple games?\n" +
                        "For a single game, input 1 or press Enter\n" +
                        "Input a number of games:"
            )
            val input = readln()
            if (input == "") {
                totalGamesToPlay = 1
                break
            }
            try {
                totalGamesToPlay = input.toInt()
                if (totalGamesToPlay !in 1..9) {
                    println("Invalid input")
                    continue
                } else {
                    break
                }
            } catch (e: Exception) {
                println("Invalid input")
            }
        }
        println("$player1 VS $player2")
        println("$rows X $cols board")
        if (totalGamesToPlay == 1) {
            println("Single Game")
        } else {
            println("Total $totalGamesToPlay games")
            println("Game #$gameNumber")
        }
        return totalGamesToPlay
    }

    private fun resetGame(board: Array<Array<String>>) {
        for (i in board.indices) {
            for (j in board[i].indices) {
                board[i][j] = " "
            }
        }
    }
}
