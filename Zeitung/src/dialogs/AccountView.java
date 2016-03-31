package dialogs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.PriorityQueue;
import java.util.Queue;

import application.MainWindow;
import forms.AccountForm;
import forms.TransactionForm;
import fx.controll.NumberChooser;
import fx.view.SimpleTable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import structs.Account;
import structs.Transaction;

public class AccountView extends BorderPane {

	protected static final BigDecimal maxDiff = BigDecimal.ZERO;
	private SimpleTable<Account> table;
	private Queue<Long> removed = new PriorityQueue<>();
	private int count;
	private VBox bounds;
	private MainWindow view;

	public AccountView(MainWindow view) {
		this.view = view;
		// create the account table
		table = new SimpleTable<Account>();
		table.addColumn("ID", cb -> "" + cb.getId());
		table.addColumn("First Name", cb -> cb.getFirstname());
		table.addColumn("Last Name", cb -> cb.getLastname());
		table.addColumn("Image File", cb -> {
			String file = cb.getImageFile();
			int index = file.lastIndexOf('/');
			if (index == -1) {
				index = file.lastIndexOf('\\');
			}
			return file.substring(index + 1);
		});
		table.addColumn("Notes", cb -> "" + cb.getNotes());
		table.addColumn("Date", cb -> "" + cb.getDate());
		table.selectionModelProperty().get().setCellSelectionEnabled(false);
		setCenter(table);

		initInterface();
	}

	private void initInterface() {
		bounds = new VBox();
		initButtons();
		initSearchFields();
		setRight(bounds);
	}

	private void initSearchFields() {
		// TODO
	}

	private void initButtons() {
		Button add = new Button("add");
		Button edit = new Button("edit");
		Button delete = new Button("delete");
		Button transaction = new Button("transaction");

		// add button creates a new PersonDialog with a new Person
		add.setOnAction(c -> {
			long id;
			if (removed.isEmpty())
				id = ++count;
			else
				id = removed.poll();

			AccountForm f = new AccountForm(id);
			f.getDialogPane().getScene().getWindow().sizeToScene();
			f.showAndWait();
			Account result = f.getResult();
			if (result != null)
				table.getItems().add(result);
		});

		// edit button creates a new PersonDialog with an existing Person
		edit.setOnAction(c -> {
			int index = table.getSelectionModel().getSelectedIndex();
			Account result = table.getSelectionModel().getSelectedItem();
			if (result == null)
				return;
			AccountForm f = new AccountForm(result);
			f.showAndWait();
			result = f.getResult();
			if (result != null)
				table.getItems().set(index, result);
		});

		// simply remove the column
		delete.setOnAction(c -> {
			int index = table.getSelectionModel().getSelectedIndex();
			if (index == -1)
				return;
			Account account = table.getItems().get(index);
			table.getItems().remove(account);
			removed.add(account.getId());
		});
		transaction.setOnAction(c -> {
			int index = table.getSelectionModel().getSelectedIndex();
			if (index == -1)
				return;
			long id = table.getItems().get(index).getId();
;
			view.getTransactionView().showDialog(id);
		});

		// create a box and add the buttons
		bounds.getChildren().add(add);
		bounds.getChildren().add(edit);
		bounds.getChildren().add(delete);
		bounds.getChildren().add(transaction);
	}

}
