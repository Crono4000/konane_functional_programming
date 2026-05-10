
package BoardTUI

import Konane.*

object BoardTUI {
  private def lineSeparations(size: Int) = "+---" + "+---" * (size - 1) + "+"

  def show(board: List[(Coord2D, Stone)], lstOpenCoords: List[Coord2D], size: Int): Unit = board match {
    case Nil => ()
    case ((r, c), stone) :: xs =>
      if (r == 0 && c == 0) println(lineSeparations(size))

      if (!lstOpenCoords.contains((r, c)))
        print(s"${if (c == 0) "|" else ""} ${String(stone)} |")
      else
        print(s"${if (c == 0) "|" else ""}   |")

      if (xs.isEmpty || c == size - 1) 
        println("\n" + lineSeparations(size))
      show(xs, lstOpenCoords, size)
  }
}