package view;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

import application.MainWindow;
import forms.AccountForm;
import fx.controll.TextSupplier;
import fx.view.SimpleTable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
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
	private ObservableList<Account> allitems;
	private Queue<Long> removed = new PriorityQueue<>();
	private Long count = 0L;
	private VBox bounds;
	private MainWindow window;
	private boolean altered = false;

	private final ListChangeListener<Account> changeListener = new ListChangeListener<Account>() {
		
		@Override
		public void onChanged(Change<? extends Account> arg0) {
			altered = true;
		}
	};		
	
	public AccountView(MainWindow window) {
		this.window = window;
		// create the account table
		table = new SimpleTable<Account>();
		table.addColumn("ID", cb -> "" + cb.getId(), new Comparator<String>() {
			
			@Override
			public int compare(String o1, String o2) {
				return Integer.parseInt(o1)-Integer.parseInt(o2);
			}
		});
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
		
		allitems = table.getItems();
		allitems.addListener(changeListener);
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
		Button print = new Button("print");
		
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
		
		print.setOnAction(c -> {
			print();
		});
		
		TextSupplier search = new TextSupplier();
		search.getProperty().addListener(c->{
			String s = search.getProperty().get();
			if(s.isEmpty())
				table.setItems(allitems);
			else{
				table.setItems(search(s));
			}
		});

		// create a box and add the buttons
		bounds.getChildren().add(add);
		bounds.getChildren().add(edit);
		bounds.getChildren().add(delete);
		bounds.getChildren().add(transaction);
		bounds.getChildren().add(print);
		bounds.getChildren().add(search.getGraphic());
	}

	private void print() {
		window.getPrintPreview().addAll(table.getSelectionModel().getSelectedItems());
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

	public ObservableList<Account> search(String s) {
		ObservableList<Account> l = FXCollections.observableArrayList();
		for(int i=0; i<allitems.size();i++){
			if(allitems.get(i).contains(s))
				l.add(allitems.get(i));
		}
		l.addListener(changeListener);
		return l ;
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
				long l = Long.parseLong(s[0]);
				acc.setId(l);
				acc.setFirstname(s[1]);
				acc.setLastname(s[2]);
				acc.setImageFile(s[3]);
				acc.setNotes(s[4]);
				acc.setDate(DATE_CONV.fromString(s[5]));
				return acc;
			});
			ArrayList<Long> ids = new ArrayList<>();
			for(Account a:allitems)
				ids.add(a.getId());
			ids.sort((o1,o2) -> {
				if(o1.longValue()>o2.longValue()) return -1;
				else if(o1.longValue()<o2.longValue()) return 1;
				else return 0;
			});
			Long maxID = ids.get(0);
			if(maxID>ids.size()){
				long nrMissing = maxID-ids.size()-1;
				int index = 0;
				for (int i = 0; i < nrMissing; i++) {
					while(ids.get(index)-1==ids.get(index+1))index++;
					this.removed.add(ids.get(index)-1);
					index++;
				}
				count = ids.get(0);
			}else
				count = allitems.size()+1L;
		} catch (Exception e) {
			e.printStackTrace();
		}
		altered = false;
	}

	public boolean altered() {
		return altered;
	}
}
