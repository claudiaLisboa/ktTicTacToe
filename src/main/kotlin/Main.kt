import react.*
import react.FC
import react.Props
import react.useState
import csstype.ClassName
import emotion.react.css
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.li
import react.dom.html.ReactHTML.ol
import react.dom.html.ReactHTML.button
import react.dom.client.createRoot




/**https://kotlinlang.org/docs/lambdas.html#function-types*/
external interface SquareProps : Props {
    var squareValue: String?
    var onClick: () ->Unit
}
/**The FC function creates a function component react */
/**Component (renders a single <button>) */
val Square = FC<SquareProps> { props ->
    button {
        className = ClassName("square")
        onClick = { props.onClick() }

        val displayText = props.squareValue ?: ""
        +displayText
    }
}

external interface BoardProps : Props{
    var squares: Array<String?>
    var onClick: (Int) ->Unit
}
/**The FC function creates a function component react */
/**Component (renders 9 squares) */
val Board = FC<BoardProps>{ props ->
    div{
        div{
            className = ClassName("board-row")

            Square{
                squareValue = props.squares[0]
                onClick = { props.onClick(0) }
            }
            Square {
                squareValue = props.squares[1]
                onClick = { props.onClick(1) }
            }
            Square {
                squareValue = props.squares[2]
                onClick = { props.onClick(2) }
            }
        }
        div{
            className = ClassName("board-row")

            Square{
                squareValue = props.squares[3]
                onClick = { props.onClick(3) }
            }
            Square {
                squareValue = props.squares[4]
                onClick = { props.onClick(4) }
            }
            Square {
                squareValue = props.squares[5]
                onClick = { props.onClick(5) }
            }
        }
        div{
            className = ClassName("board-row")

            Square{
                squareValue = props.squares[6]
                onClick = { props.onClick(6) }
            }
            Square {
                squareValue = props.squares[7]
                onClick = { props.onClick(7) }
            }
            Square {
                squareValue = props.squares[8]
                onClick = { props.onClick(8) }
            }
        }

    }

}
external interface GameProps : Props

fun calculateWinner(squares: Array<String?>): String?{
    val lines = arrayOf(
        intArrayOf(0,1,2),
        intArrayOf(3,4,5),
        intArrayOf(6,7,8),
        intArrayOf(0,3,6),
        intArrayOf(1,4,7),
        intArrayOf(2,5,8),
        intArrayOf(0,4,8),
        intArrayOf(2,4,6)
    )
    for (row in lines) {
        val row0 = row[0]
        val row1 = row[1]
        val row2 = row[2]
        if (!squares[row0].isNullOrEmpty()) {
            if (squares[row0].equals(squares[row1])) {
                if (squares[row0].equals(squares[row2])) {
                    return squares[row0]
                }
            }
        }
    }
    return null
}
/**The FC function creates a function component react */
/**Component (renders a board - placeholder values) */
val GameApp = FC<GameProps>{
    /* var (history, setHistory)  = useState(arrayOf(arrayOfNulls<String?>(9)))
     var (nextPlayer, setNextPlayer) = useState(true)
     var (stepNumber, setNumber) = useState(0)*/
    /**The by keyword indicates that useState() acts as a delegated property. read and write values -
     * by keyword is a delegate, because the get() (and set()) that correspond
     * to the property will be delegated to its getValue() and setValue() methods.
     */
    var history by useState(arrayOf(arrayOfNulls<String?>(9)))
    var nextPlayer by useState(true)
    var stepNumber by useState(0)

    fun handleClick(index: Int) {
        val squares = history[stepNumber]

        if (squares[index].isNullOrEmpty() and calculateWinner(squares).isNullOrEmpty()) {
            val newSquare = (
                    squares.slice(0 until index) +
                            (if (nextPlayer) "X" else "O") +
                            squares.slice(index + 1..8)
                    )
                .toTypedArray()

            val historyCopy = history.slice(0..stepNumber).toMutableList()

            historyCopy.add(newSquare)

            history = historyCopy.toTypedArray()
            nextPlayer = !nextPlayer
            stepNumber = historyCopy.size - 1
        }
    }

    fun jumpTo(step: Int) {
        stepNumber = step
        nextPlayer = step % 2 == 0
    }

    val historyCopy = history
    val current = historyCopy[stepNumber]

    val winner = calculateWinner(current)
    val totalStep = historyCopy.size - 1

    val status = if (winner.isNullOrEmpty()) {
        "Next player: ${if (nextPlayer) "X" else "O"}"
    } else {
        "WINNER: $winner"
    }
    div {
        className = ClassName("game")

        div {
            className = ClassName("game-board")

            Board {
                squares = current
                onClick = { i: Int -> handleClick(i) }
            }
        }
        div {
            className = ClassName("game-info")

            div {
                +status
            }
            ol {
                for (step in 0..totalStep) {
                    val desc = if (step == 0) "Go to game start" else "Go to step #$step"
                    li {
                        key = step.toString()
                        button {
                            onClick = { jumpTo(step) }

                            +desc
                        }
                    }
                }
            }
        }
    }

}