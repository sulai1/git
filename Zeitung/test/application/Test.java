package application;

	
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import structs.Ausweis;


public class Test extends Application {
	@Override
	public void start(Stage primaryStage) {
		BorderPane borderPane = new BorderPane();
		Scene scene = new Scene(borderPane);
		primaryStage.setScene(scene);
		String imgname = "file:C:/Users/sascha/Dropbox/Verkäuferausweise/img/103.jpg";
		ImageView view = new ImageView(new Image(imgname));
		Ausweis ausweis = new Ausweis("t", "a", imgname, 1);
		borderPane.setCenter(view);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
