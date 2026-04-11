import Konane.*
import BoardTUI.*
import MyRandom.MyRandom

def opponent(open: Stone): Stone = open match { case (Stone.White) => Stone.Black case(Stone.Black) => Stone.White} 

object Main {

  def main(args: Array[String]): Unit = {

    val size = 6
    val board0 = initBoard(size)
    val open0 = List((0, 0), (1, 0))
    val rand0 = MyRandom(System.currentTimeMillis())
    println("=== Jogo Konane ===")
    println("Tabuleiro inicial:")
    BoardTUI.show(board0, open0, size)
    Thread.sleep(2000)
    def gameLoop(board: Board, rand: MyRandom, open: List[Coord2D], player: Stone, turn: Int): Unit = {
      println(s"\n--- Jogada $turn [$player] ---")
      playRandomly(board, rand, player, open, randomMove) match {
        case (None, _, _, _) =>
          println(s"Fim do jogo! ${opponent(player)} vence.")
        case (Some(newBoard), newRand, newOpen, dest) =>
          println(s"Jogou para: ${dest.getOrElse("?")}")
          BoardTUI.show(newBoard, newOpen, size)
          Thread.sleep(2000)
          if (turn < 10) gameLoop(newBoard, newRand, newOpen, opponent(player), turn + 1)
          else println("Fim do teste.")
      }
    }

    gameLoop(board0, rand0, open0, Stone.Black, 1)
  }
}