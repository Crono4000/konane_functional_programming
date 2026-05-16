import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.layout.*
import javafx.scene.shape.*
import javafx.scene.{Parent, Scene}
import javafx.stage.Stage
import Konane.*

class konaneApp extends Application {
  def generateBoard(pos: Coord2D, size: Int, scene: Scene, squareSize: Int): Unit = {
    val teste: Rectangle = new Rectangle(50, 50)
    val teste2: Circle = new Circle(20)
  }

  override def start(primaryStage: Stage): Unit = {
    primaryStage.setTitle("My Hello World App")

    val root = new VBox()
    val teste: Rectangle = new Rectangle(50, 50)
    val teste2: Circle = new Circle(20)
    root.getChildren.addAll(teste, teste2)

    val scene = new Scene(root, 400, 300)
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}

object FxApp {
  def main(args: Array[String]): Unit = {
    Application.launch(classOf[konaneApp], args: _*)
  }
}
