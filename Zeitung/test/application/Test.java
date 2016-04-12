package application;

	
import dialogs.Preferences;
import javafx.application.Application;
import javafx.stage.Stage;


public class Test extends Application {
	@Override
	public void start(Stage primaryStage) {
		Preferences preferences = Preferences.load();
		preferences.showDialog();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
