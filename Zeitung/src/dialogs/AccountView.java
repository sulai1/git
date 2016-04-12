package dialogs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.PriorityQueue;
import java.util.Queue;

import application.MainWindow;
import forms.AccountForm;
import fx.view.SimpleTable;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.converter.LocalDateStringConverter;
import structs.Account;

public class AccountView extends BorderPane {

	private static final LocalDateStringConverter DATE_CONV = new LocalDateStringConverter();
	protected static final BigDecimal maxDiff = BigDecimal.ZERO;
	private SimpleTable<Account> table;
	private Queue<Long> removed = new PriorityQueue<>();
	private int count;
	private VBox bounds;
	private MainWindow window;
	private boolean altered = false;

	public AccountView(MainWindow window) {
		this.window = window;
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
		table.addColumn("Date", cb -> "" + DATE_CONV.toString(cb.getDate()));
		table.selectionModelProperty().get().setCellSelectionEnabled(false);
		setCenter(table);
		initInterface();
		
		ListChangeListener<Account> l = new ListChangeListener<Account>() {
			
			@Override
			public void onChanged(Change<? extends Account> arg0) {
				altered = true;
			}
		};
		table.getItems().addListener(l);
	}

	private void initInterface() {
		bounds = new VBox();
		initButtons();
		setRight(bounds);
	}

	private void initButtons() {
		Button add = new Button("add");
		Button edit = new Button("edit");
		Button delete = new Button("delete");
		Button transaction = new Button("transaction");

		// add button creates a new PersonDialog with a new Person
		add.setOnAction(c -> {
			add();
		});

		// edit button creates a new PersonDialog with an existing Person
		edit.setOnAction(c -> {
			edit();
		});

		// simply remove the column
		delete.setOnAction(c -> {
			delete();
		});
		transaction.setOnAction(c -> {
			createTransaction();
		});

		// create a box and add the buttons
		bounds.getChildren().add(add);
		bounds.getChildren().add(edit);
		bounds.getChildren().add(delete);
		bounds.getChildren().add(transaction);
	}

	private void createTransaction() {
		int index = table.getSelectionModel().getSelectedIndex();
		if (index == -1)
			return;
		long id = table.getItems().get(index).getId();
;
		window.getTransactionView().showDialog(id);
	}

	private void delete() {
		int index = table.getSelectionModel().getSelectedIndex();
		if (index == -1)
			return;
		Account account = table.getItems().get(index);
		table.getItems().remove(account);
		removed.add(account.getId());
	}

	private void add() {
		long id;
		if (removed.isEmpty())
			id = ++count;
		else
			id = removed.poll();

		AccountForm f = new AccountForm(window,id);
		f.getDialogPane().getScene().getWindow().sizeToScene();
		f.showAndWait();
		Account result = f.getResult();
		if (result != null)
			table.getItems().add(result);
	}

	private void edit() {
		int index = table.getSelectionModel().getSelectedIndex();
		Account result = table.getSelectionModel().getSelectedItem();
		if (result == null)
			return;
		AccountForm f = new AccountForm(window,result,false);
		f.showAndWait();
		result = f.getResult();
		if (result != null)
			table.getItems().set(index, result);
	}

	public void saveTable(PrintStream ps) {
		try {
			table.store(ps);
			altered = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void saveTable() throws FileNotFoundException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("res"));
		File file = fileChooser.showSaveDialog(window.getScene().getWindow());
		saveTable(new PrintStream(file));
	}
	public void loadTable() throws FileNotFoundException {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setInitialDirectory(new File("res"));
		File file = fileChooser.showSaveDialog(window.getScene().getWindow());
		loadTable(new BufferedReader(new FileReader(file)));
	}
	
	public void loadTable(BufferedReader br) {
		try {
			table.clear();
			table.load(br, s->{
				Account acc = new Account();
				acc.setId(Long.parseLong(s[0]));
				acc.setFirstname(s[1]);
				acc.setLastname(s[2]);
				acc.setImageFile(s[3]);
				acc.setNotes(s[4]);
				acc.setDate(DATE_CONV.fromString(s[5]));
				return acc;
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		altered = false;
	}

	public boolean altered() {
		return altered;
	}
}
