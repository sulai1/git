package forms;

import java.io.File;
import java.time.LocalDate;

import application.MainWindow;
import fx.controll.Form;
import fx.property.ValueSupplier;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import structs.Account;

public class AccountForm extends Form<Account> {

	public AccountForm(MainWindow window, long id) {
		this(window, id, "", "", "", LocalDate.now());
	}

	public AccountForm(MainWindow window, Account account) {
		this(window,account, true);
	}

	public AccountForm(MainWindow window, Account account, boolean bMandatory) {
		setResizable(true);
		ZTextField fname = new ZTextField(window.preferences());
		ZTextField lname = new ZTextField(window.preferences());
		ZTextField note = new ZTextField(window.preferences());
		fname.getProperty().set(account.getFirstname());
		lname.getProperty().set(account.getLastname());
		note.getProperty().set(account.getNotes());
		disableSubmit(bMandatory);

		//image file field
				
		//property
		ObjectProperty<File> p = new SimpleObjectProperty<>();
		//node
		FileChooser fc = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("image files ", "*.jpg", "*.png", "*.bmp", "*.dds");
		fc.getExtensionFilters().add(filter);
		fc.setSelectedExtensionFilter(filter);
		TextField tf = new TextField("file");
		Button button = new Button("open");
		button.setOnAction(l -> {
			File file = fc.showOpenDialog(this.getOwner());
			if (file == null)
				return;
			p.set(file);
			tf.setText(file.getName());
		});
		HBox hBox = new HBox();
		hBox.getChildren().add(button);
		hBox.getChildren().add(tf);
		
		//create value supplier from node and property
		ValueSupplier<File> valueSupplier = new ValueSupplier<File>() {
			@Override
			public ObjectProperty<File> getProperty() {
				return p;
			}

			@Override
			public Node getGraphic() {
				return hBox;
			}
		};
		addField("FirstName", fname, bMandatory);
		addField("LastName", lname, bMandatory);
		addField("Image File", valueSupplier);
		addField("Note", note);
		Callback<ButtonType, Account> res = new Callback<ButtonType, Account>() {

			@Override
			public Account call(ButtonType param) {
				if (param.getButtonData() != ButtonData.OK_DONE)
					return null;
				account.setFirstname(fname.getProperty().get());
				account.setLastname(lname.getProperty().get());
				account.setNotes(note.getProperty().get());
				// TODO
				if (tf.getText() != null)
					account.setImageFile(tf.getText());
				else
					account.setImageFile("");
				return account;
			}
		};
		setResultConverter(res);
	}

	private AccountForm(MainWindow window, long id, String firstname, String lastname, String noteString,
			LocalDate date) {
		this(window, initAccount(id, firstname, lastname, noteString, date), false);
	}

	private static Account initAccount(long id, String firstname, String lastname, String noteString, LocalDate date) {
		Account account = new Account();
		account.setId(id);
		account.setDate(date);
		account.setFirstname(firstname);
		account.setLastname(lastname);
		account.setNotes(noteString);
		return account;
	}
}