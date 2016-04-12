package application;
	
import dialogs.Log;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			MainWindow root = new MainWindow();
			
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("zeitung.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

			scene.getWindow().setOnCloseRequest(c->{
				if(root.altered()) {
					if(Log.confirm("Unsaved content. Do you want to save?"));
						try {
							root.save();
						} catch (Exception e) {
							e.printStackTrace();
						}
				}
			});
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
