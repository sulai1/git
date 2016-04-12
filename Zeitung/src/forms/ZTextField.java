package forms;

import dialogs.Preferences;
import fx.controll.TextSupplier;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class ZTextField extends TextSupplier{


	private int cpos = 0;
	
	public ZTextField(Preferences preferences) {
		String refChars = preferences.getSonderZeichen().get();
		for (int i = 0; i < refChars.length(); i++) {
			Button button = new Button();
			int index = i;
			button.setText(""+refChars.charAt(index));
			((HBox)getGraphic()).getChildren().add(button);
			getTextField().focusedProperty().addListener((x,y,val)->{
				if(!val)
					cpos = getTextField().getCaretPosition();
			});
			button.setOnAction(c->{
				String s = getProperty().get();
				String newString;
				newString = s.substring(0, cpos)+refChars.charAt(index)+s.substring(cpos);
				getProperty().set(newString);
			});
		}
	}
}
