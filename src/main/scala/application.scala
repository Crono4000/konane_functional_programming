import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.layout.*
import javafx.scene.shape.*
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import Konane.*
import javafx.application.Application

import scala.collection.mutable.Map
import javafx.scene.Scene
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.stage.Stage
import javafx.scene.input.MouseEvent
import javafx.scene.Node
import BoardTUI.*
import Konane.*
import Konane.Stone.Black

import scala.collection.mutable

class Tabuleiro(var board: Board, var openCoords: List[Coord2D]) {
  var grid: GridPane = new GridPane()
  var mapa: mutable.Map[Coord2D, (Circle, Rectangle)] = mutable.Map()
  var selected: List[Coord2D] = List()
  var turn: Stone = Stone.Black

  def getCircle(pos: Coord2D): Circle = mapa.getOrElse(pos, (new Circle(7), new Rectangle(8, 9)))._1
  def getSquare(pos: Coord2D): Rectangle = {
    println("Getting " + pos)
    mapa.getOrElse(pos, (new Circle(7), new Rectangle(8, 9)))._2
  }

  def init(): Unit = {
    grid.getChildren().clear();
    mapa = mutable.Map[Coord2D, (Circle, Rectangle)]()
    val lboard: List[(Coord2D, Stone)] = board

    lboard
      .map {case ((linha, coluna), stone) =>
        val peca = new Circle (50 * 0.4)
        val quadrado = new Rectangle (50, 50)
        if (stone == turn)
          {
        peca.setOnMouseClicked ((e: MouseEvent) => {
          selected.map {
            case (k) => {
              val square = getSquare(k)
              getSquare(k).setFill(Color.BEIGE)
              square.setOnMouseClicked((e: MouseEvent) => {})
              k
            }
          }
          val moves = getAvailableMoves(board, stone, openCoords)
          val myMoves = moves.filter (_.coordFrom == (linha, coluna) )
          selected = myMoves.map {case (k, v) => v}
          myMoves.map {
            case ((coordFrom, coordTo)) => {
              getSquare(coordTo).setFill(Color.YELLOW)
              (coordFrom, coordTo)
            }
          }
          myMoves.map {
            case (k, v) => {
              val square = getSquare(v)
              square.setOnMouseClicked ((e: MouseEvent) => {
                  val result: (Option[Board], List[Coord2D]) = play(board, turn, k, v, openCoords)
                  board = result._1.getOrElse(initBoard(6))
                  openCoords = result._2
                  turn = opponent(turn)
                  init()
              })
            }
          }
        })}
        quadrado.setFill (Color.BEIGE)
        peca.setFill (stone match {
          case Stone.White => Color.WHITE
          case Stone.Black => Color.BLACK
        })
        (quadrado, peca, linha, coluna)
  }.foldLeft (grid) {case (g, (quadrado, peca, linha, coluna) ) =>
        g.add (quadrado, coluna, linha)
        if (! openCoords.contains ((linha, coluna) ) ) {
          g.add (peca, coluna, linha)
        }
        mapa += ((linha, coluna) -> (peca, quadrado))
        g
  }
  }
}

class konaneApp extends Application {

  def generateBoard(pos: Coord2D, size: Int, scene: Scene, squareSize: Int): Unit = {
    val teste: Rectangle = new Rectangle(50, 50)
    val teste2: Circle = new Circle(20)
  }

  override def start(primaryStage: Stage): Unit = {
    val tab = Tabuleiro(initBoard(6), List((0,0), (0,1)))
    tab.init()


    val scene = new Scene(tab.grid)
    primaryStage.setScene(scene)
    primaryStage.setTitle("Tabuleiro Funcional em Scala")
    primaryStage.show()
  }
}



object FxApp {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[konaneApp], args: _*)
  }
}
