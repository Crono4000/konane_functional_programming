import scala.collection.parallel.*
import scala.collection.parallel.CollectionConverters.*

type Coord2D = (Int, Int) //(row, column)
type Board = ParMap[Coord2D, Stone]

//Enum in Scala 3
enum Stone:
    case Black, White

def TransformIntCoord2D(number: Int, size: Int): Coord2D = (number % size, number / size)

def getStone(number: Int, size: Int): Stone = {
    val vec: Coord2D = TransformIntCoord2D(number, size)
    val num: Int = number + (vec._2 % 2)
    if (num % 2 == 0) Stone.Black else Stone.White
}

def generateFullBoard(size: Int): Board = (0 to (size*size - 1)).toList.map(x => (TransformIntCoord2D(x, size), getStone(x, size))).toMap.par

def canPlay(board:Board, player: Stone, coordFrom:Coord2D, coordTo:Coord2D, lstOpenCoords:List[Coord2D]): Boolean = {
    if(board(coordFrom) != player) return false
    if(!lstOpenCoords.contains(coordTo)) return false
    if(!board.contains(coordFrom)) return false

    val dx: Int = if(coordFrom._1 - coordTo._1 < 0) (coordFrom._1 - coordTo._1) * -1 else (coordFrom._1 - coordTo._1)
    val dy: Int = if(coordFrom._2 - coordTo._2 < 0) (coordFrom._2 - coordTo._2) * -1 else (coordFrom._2 - coordTo._2)
    if (dx + dy != 2 || dx == 1 || dy == 1) return false

    return true
}

def changeBoardOnPosition(board: Board, pos: Coord2D, value: Stone): Board = board.map { case (k, v) => if(k._1 == pos._1 && k._2 == pos._2) (k, value) else (k, v) }

def getPositionBetween(coordFrom:Coord2D, coordTo:Coord2D): Coord2D = {
    val dx: Int = (coordFrom._1 - coordTo._1)
    val dy: Int = (coordFrom._2 - coordTo._2)

    if (dx == 0) return (coordFrom._1, coordTo._2 + (dy / 2))
    else (coordTo._1 + (dx / 2), coordFrom._2)
}

def play(board:Board, player: Stone, coordFrom:Coord2D, coordTo:Coord2D, lstOpenCoords:List[Coord2D]): (Option[Board], List[Coord2D]) = {
    if (!canPlay(board, player, coordFrom, coordTo, lstOpenCoords))
        return (None, lstOpenCoords)
    (Some(changeBoardOnPosition(board, coordTo, player)), coordFrom :: (getPositionBetween(coordFrom, coordTo) :: lstOpenCoords.filter(coord => coord._1 != coordTo._1 || coord._2 != coordTo._2)))
}

def showBoard(board:Board, number: Int, size: Int, lstOpenCoords:List[Coord2D]): Unit = {
    if (number >= size * size)
        return
    val vec: Coord2D = TransformIntCoord2D(number, size)
    if (!lstOpenCoords.contains(vec))
        print(if(board(vec) == Stone.White) "W" else "B")
    else
        print(" ")
    if (vec._1 + 1 == size)
        println("")
    showBoard(board, number + 1, size, lstOpenCoords)
}
