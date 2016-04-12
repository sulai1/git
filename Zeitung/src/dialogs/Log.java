package dialogs;

import java.io.IOException;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class Log {

	public static void alertMessage(String string) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setContentText(string);
		alert.show();
	}

	public static boolean confirm(String string) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.setContentText(string);
		Optional<ButtonType> res = alert.showAndWait();
		return res.isPresent()&&res.get().equals(ButtonType.APPLY);
		
	}

	public static void log(IOException e) {
		// TODO Auto-generated method stub
		
	}

}
