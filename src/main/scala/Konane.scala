
package Konane

import MyRandom.MyRandom

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

def initBoard(size: Int): Board = (0 to (size*size - 1)).toList.map(x => (TransformIntCoord2D(x, size), getStone(x, size))).toMap.par

def canPlay(board:Board, player: Stone, coordFrom:Coord2D, coordTo:Coord2D, pb: Coord2D, lstOpenCoords:List[Coord2D]): Boolean = {
    val dx: Int = (coordFrom._1 - coordTo._1).abs
    val dy: Int = (coordFrom._2 - coordTo._2).abs

    //Verifies if coordFrom is valid within the context of the game (in the board and a valid piece)
    if(!board.contains(coordFrom) || lstOpenCoords.contains(coordFrom)) return false
    if(board(coordFrom) != player) return false

    //Verifies if the destination is open
    if(!lstOpenCoords.contains(coordTo)) return false

    //Verifies if the movement is valid within the game context (maximum distance that I can move is 2)
    if(dx + dy > 2)
        return false
    else if (dx + dy == 2) {

        //verifies if I am trying to do a diagonal move
        if (dx == dy) return false

        //verifies if the position that I will try to eat has a valid piece
        if (lstOpenCoords.contains(pb) || board(pb) == player) return false
    }
    return true
}

def changeBoardOnPosition(board: Board, pos: Coord2D, value: Stone): Board = board.map { case (k, v) => if(k._1 == pos._1 && k._2 == pos._2) (k, value) else (k, v) }

/***
 * @return The position in between 2 positions, or None if they are adjacent
 */
def getPositionBetween(coordFrom:Coord2D, coordTo:Coord2D): Coord2D = {
    val dx: Int = (coordFrom._1 - coordTo._1)
    val dy: Int = (coordFrom._2 - coordTo._2)

    if (dx == 0) return (coordFrom._1, coordTo._2 + (dy / 2))
    else (coordTo._1 + (dx / 2), coordFrom._2)
}

def play(board:Board, player: Stone, coordFrom:Coord2D, coordTo:Coord2D, lstOpenCoords:List[Coord2D]): (Option[Board], List[Coord2D]) = {
    val pb: Coord2D = getPositionBetween(coordFrom, coordTo)
    if (!canPlay(board, player, coordFrom, coordTo, pb, lstOpenCoords))
        return (None, lstOpenCoords)
    (Some(changeBoardOnPosition(board, coordTo, player)), coordFrom :: (pb :: lstOpenCoords.filter(coord => coord._1 != coordTo._1 || coord._2 != coordTo._2)))
}

def randomMove(lstOpenCoords: List[Coord2D], rand: MyRandom): (Coord2D, MyRandom) = {
    val random: (Int, MyRandom) = rand.nextInt(lstOpenCoords.size)
    return (lstOpenCoords(random._1), random._2)
}

def getStones(entries: List[(Coord2D, Stone)], player: Stone): List[Coord2D] = entries match {
    case Nil => Nil
    case (pos, stone) :: rest =>
        if (stone == player) pos :: getStones(rest, player)
        else getStones(rest, player)
}

def playRandomly(board: Board, rand: MyRandom, player: Stone, openCoords: List[Coord2D], f: (List[Coord2D], MyRandom) => (Coord2D, MyRandom)): (Option[Board], MyRandom, List[Coord2D], Option[Coord2D]) = {
    val playerStones = getStones(board.toList, player)
    if (playerStones.isEmpty || openCoords.isEmpty) {
        (None, rand, openCoords, None)
    } else {
        val (from, r1) = f(playerStones, rand)
        val (to, r2) = f(openCoords, r1)
        play(board, player, from, to, openCoords) match {
            case (Some(newBoard), newOpen) => (Some(newBoard), r2, newOpen, Some(to))
            case (None, _) => playRandomly(board, r2, player, openCoords, f)
        }
    }
}

implicit def boardToList(board: Board): List[(Coord2D, Stone)] = {
  return board.toList.sortBy { case ((x, y), _) => (x, y) }
}

implicit def stoneToString(stone: Stone): String = stone match {
    case Stone.Black => "B"
    case Stone.White => "W"
}

def getAvailableMoves(board: Board, player: Stone, openCoords: List[Coord2D]): List[(coordFrom: Coord2D, coordTo: Coord2D)] = {
    val playerStones = getStones(board.toList, player)
    val moves = playerStones.map(x => openCoords.map(y => (x, y))).foldLeft(List.empty[(Coord2D, Coord2D)]) { (acc, elem) => acc ++ elem }

    moves.filter(move => canPlay(board, player, move._1, move._2, getPositionBetween(move._1, move._2), openCoords))
}

def hasLost(board: Board, player: Stone, openCoords: List[Coord2D]): Boolean = {
    getAvailableMoves(board, player, openCoords).isEmpty
}
