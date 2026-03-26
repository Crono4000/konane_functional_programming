
type Coord2D = (Int, Int) //(row, column)
type Board = ParMap[Coord2D, Stone]

//Enum in Scala 3
enum Stone:
    case Black, White

def TransformIntCoord2D(number: Int, size: Int): Coord2D = (number % size, number / size)
def getStone(number: Int): Stone = if (number % 2 == 0) Stone.Black else Stone.White
def generateFullBoard(size: Int): Board = parMap.map((0 to (size*size)).toList.map(x => (TransformIntCoord2D(x, size), getStone(x))))

def play(board:Board, player: Stone, coordFrom:Coord2D, coordTo:Coord2D, lstOpenCoords:List[Coord2D]): (Option[Board], List[Coord2D])
{

}
