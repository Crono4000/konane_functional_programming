import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.layout.*
import javafx.scene.shape.*
import javafx.scene.control.*
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
  grid.setPrefSize(300, 400)
  var mapa: mutable.Map[Coord2D, (Circle, Rectangle)] = mutable.Map()
  var selected: List[Coord2D] = List()
  var selectPosition: Option[Coord2D] = None
  var turn: Stone = Stone.Black
  var history: List[(Board, List[Coord2D], Stone, Option[Coord2D])] = List()

  def getCircle(pos: Coord2D): Circle = mapa.getOrElse(pos, (new Circle(7), new Rectangle(8, 9)))._1

  def getSquare(pos: Coord2D): Rectangle = mapa.getOrElse(pos, (new Circle(7), new Rectangle(8, 9)))._2

  def resetSelectedSquares(): Unit = {
    selected.map {
      case (c) => {
        val square = getSquare(c)
        getSquare(c).setFill(Color.BEIGE)
        square.setOnMouseClicked((e: MouseEvent) => {})
        c
      }
    }
  }

  def passTurn(e: MouseEvent): Unit = {
    if (selectPosition.contains(None))
      return
    selectPosition = None
    turn = opponent(turn)
    init()
  }

  def doMove(coordFrom: Coord2D, coordTo: Coord2D): Unit = {
    history = (board, openCoords, turn, selectPosition) :: history
    val result: (Option[Board], List[Coord2D]) = play(board, turn, coordFrom, coordTo, openCoords)
    board = result._1.getOrElse(initBoard(6))
    openCoords = result._2

    //turn = opponent(turn)
    selectPosition = Some(coordTo)
    init()
  }

  def undo(e: MouseEvent): Unit = {
    val r = history.dropWhile(_._3 != turn)
    if (r.nonEmpty)
    {
      r match {
        case (oldBoard, oldOpenCoords, _, oldSelected) :: rest =>
          board = oldBoard
          openCoords = oldOpenCoords
          selectPosition = oldSelected

          history = rest

          init()
        case _ =>
          return
      }
    }
  }

  def init(): Unit = {
      grid.getChildren().clear();
      mapa = mutable.Map[Coord2D, (Circle, Rectangle)]()
      val lboard: List[(Coord2D, Stone)] = board

      lboard
        .map { case ((linha, coluna), stone) =>
          val peca = new Circle(50 * 0.40)
          val quadrado = new Rectangle(50, 50)
          println("First: " + (stone == turn) + ";Second: " + (selectPosition.contains((linha, coluna))) + ";Third: " + selectPosition.isEmpty)
          if (stone == turn && (selectPosition.contains((linha, coluna)) || selectPosition.isEmpty)) {
            peca.setOnMouseClicked((e: MouseEvent) => {
              resetSelectedSquares()
              val myMoves = getAvailableMoves(board, stone, openCoords).filter(_.coordFrom == (linha, coluna))
              selected = myMoves.map {
                case ((coordFrom, coordTo)) => {
                  val square = getSquare(coordTo)
                  square.setFill(Color.YELLOW)
                  square.setOnMouseClicked((e: MouseEvent) => {doMove(coordFrom, coordTo)})
                  coordTo
                }
              }
            })
          }
          quadrado.setFill(Color.BEIGE)
          peca.setFill(stone match {
            case Stone.White => Color.WHITE
            case Stone.Black => Color.BLACK
          })
          (quadrado, peca, linha, coluna)
        }.foldLeft(grid) { case (g, (quadrado, peca, linha, coluna)) =>
          g.add(quadrado, coluna, linha)
          if (!openCoords.contains((linha, coluna))) {
            g.add(peca, coluna, linha)
          }
          mapa += ((linha, coluna) -> (peca, quadrado))
          g
        }
      val undoButton: Button = new Button("Undo")
      val passButton: Button = new Button("Pass")
      grid.add(undoButton, 1, 7)
      undoButton.setOnMouseClicked(undo)
      grid.add(passButton, 2, 7)
      passButton.setOnMouseClicked(passTurn)
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
