object BoardTUI {
  //This appears much better when run in a terminal rather than the IntelliJ CLI

  private def topBorder(size: Int) = "┏━━" + "┳━━" * (size - 1) + "┓"

  private def middleBorder(size: Int) = "┣━━" + "╋━━" * (size - 1) + "┫"

  private def bottomBorder(size: Int) = "┗━━" + "┻━━" * (size - 1) + "┛"

  def show(board: List[(Coord2D, Stone)], lstOpenCoords: List[Coord2D], size: Int): Unit = board match {
    case Nil => ()
    case ((r, c), stone) :: xs =>
      if (r == 0 && c == 0) println(topBorder(size))

      if (!lstOpenCoords.contains((r, c)))
        print(s"${if (c == 0) "┃" else ""}${String(stone)}┃")
      else
        print(s"${if (c == 0) "┃" else ""}  ┃")

      if (xs.isEmpty) println("\n" + bottomBorder(size))
      else if (c == size - 1) println("\n" + middleBorder(size))

      show(xs, lstOpenCoords, size)
  }
}