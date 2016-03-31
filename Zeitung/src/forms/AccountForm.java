package forms;

import java.io.File;
import java.time.LocalDate;

import fx.controll.Form;
import fx.controll.TextSupplier;
import fx.property.ValueSupplier;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.util.Callback;
import structs.Account;

public class AccountForm extends Form<Account> {

	public AccountForm(long id) {
		this(id, "", "", "", LocalDate.now());
	}

	public AccountForm(Account account) {
		setResizable(true);
		TextSupplier fname = new TextSupplier();
		TextSupplier lname = new TextSupplier();
		TextSupplier note = new TextSupplier();
		fname.getProperty().set(account.getFirstname());
		lname.getProperty().set(account.getLastname());
		note.getProperty().set(account.getNotes());
		disableSubmit(true);

		FileChooser fc = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("image files ", "*.jpg", "*.png", "*.bmp", "*.dds");
		fc.getExtensionFilters().add(filter);
		fc.setSelectedExtensionFilter(filter);
		Label label = new Label("file");
		Button button = new Button("open");
		ObjectProperty<File> p = new SimpleObjectProperty<>();
		button.setOnAction(l -> {
			File file = fc.showOpenDialog(this.getOwner());
			if (file == null)
				return;
			p.set(file);
			label.setText(file.getName());
		});
		HBox hBox = new HBox();
		hBox.getChildren().add(button);
		hBox.getChildren().add(label);
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
		addField("FirstName", fname, true);
		addField("LastName", lname, true);
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
				File file = p.get();
				if(file!=null)
					account.setImageFile(file.getPath());
				else
					account.setImageFile("");
				return account;
			}
		};
		setResultConverter(res);
	}

	private AccountForm(long id, String firstname, String lastname, String noteString, LocalDate date) {
		this(initAccount(id, firstname, lastname, noteString, date));
	}

	private static Account initAccount(long id, String firstname, String lastname, String noteString,
			LocalDate date) {
		Account account = new Account();
		account.setId(id);
		account.setDate(date);
		account.setFirstname(firstname);
		account.setLastname(lastname);
		account.setNotes(noteString);
		return account;
	}
}