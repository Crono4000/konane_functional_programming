import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.layout.*
import javafx.scene.shape.*
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import Konane.*

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import javafx.scene.input.MouseEvent

import BoardTUI._
import Konane._

class konaneApp extends Application {

  def generateBoard(pos: Coord2D, size: Int, scene: Scene, squareSize: Int): Unit = {
    val teste: Rectangle = new Rectangle(50, 50)
    val teste2: Circle = new Circle(20)
  }

  override def start(primaryStage: Stage): Unit = {
    val grid = renderBoard(initBoard(6), List((0,0), (0,1)))

    val scene = new Scene(grid)
    primaryStage.setScene(scene)
    primaryStage.setTitle("Tabuleiro Funcional em Scala")
    primaryStage.show()
  }
}

   def renderBoard(board: Board, openCoords: List[Coord2D]): GridPane = {
     val grid: GridPane = new GridPane()
     val lboard: List[(Coord2D, Stone)] = board
     lboard
       .map { case ((linha, coluna), stone) =>
         val peca = new Circle(50 * 0.4)
         val quadrado = new Rectangle(50, 50)
         //quadrado.setStroke(Color.BLACK)
         //quadrado.setStrokeWidth(2)
         peca.setOnMouseClicked((e: MouseEvent) => {
           val moves = getAvailableMoves(board, stone, openCoords)
           val myMoves = moves.filter(_.coordFrom == (linha, coluna))
           println(s"Movimentos possíveis desta peça: $myMoves")
         })
         quadrado.setFill(Color.BEIGE)
         peca.setFill(stone match {
           case Stone.White => Color.WHITE
           case Stone.Black => Color.BLACK
         })
         (quadrado, peca, linha, coluna)
       }
       .foldLeft(grid) { case (g, (quadrado, peca, linha, coluna)) =>
         g.add(quadrado, coluna, linha)
         if (!openCoords.contains((linha, coluna))) {
          g.add(peca, coluna, linha)
         }
         g
       }

     grid

   }



object FxApp {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[konaneApp], args: _*)
  }
}
