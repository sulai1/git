package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;

import dialogs.AccountView;
import dialogs.Preferences;
import dialogs.TransactionView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

public class MainWindow extends BorderPane {

	private static final String RES_TRA_CSV = "res/tra.csv";
	private static final String RES_ACC_CSV = "res/acc.csv";
	private TabPane tabPane = new TabPane();
	private static final BigDecimal maxDiff = new BigDecimal("0.1");
	private AccountView accountView;
	private TransactionView transactionView;
	
	private File accFile = new File(RES_ACC_CSV);
	private File traFile = new File(RES_TRA_CSV);
	
	private Preferences preferences;
	
	public MainWindow() throws NumberFormatException, IOException, ParseException {

		preferences = Preferences.load();
		// add the account view
		accountView = new AccountView(this);
		Tab tab = new Tab("Accounts");
		tab.setContent(accountView);
		tabPane.getTabs().add(tab);

		// add the transaction view
		transactionView = new TransactionView(this);
		tab = new Tab("Transactions");
		tab.setContent(transactionView);
		tabPane.getTabs().add(tab);
		setCenter(tabPane);
		initMenu();
		load();
	}

	boolean altered() {
		return transactionView.altered() || accountView.altered();
	}

	private void initMenu() {
		MenuBar bar = new MenuBar();
		Menu menu = new Menu("File");

		// LOAD : item for loading
		MenuItem load = new MenuItem("load");
		load.setOnAction(c -> {
			try {
				load();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		menu.getItems().add(load);

		// SAVE : item for saving
		MenuItem save = new MenuItem("save");
		save.setOnAction(c -> {
			try {
				save();
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
		menu.getItems().add(save);

		//PREFERENCES : opens a form to edit preferences
		MenuItem preferences = new MenuItem("preferences");
		preferences.setOnAction(c -> {
			this.preferences.showDialog();
		});
		menu.getItems().add(preferences);
		
		

		bar.getMenus().add(menu);
		setTop(bar);
	}

	public TransactionView getTransactionView() {
		return transactionView;
	}

	public AccountView getAccountView() {
		return accountView;
	}

	public BigDecimal getMaxDiff() {
		return maxDiff;
	}


	private void load() throws FileNotFoundException, IOException, ParseException {

		InputStreamReader isr = new InputStreamReader(new FileInputStream(accFile), StandardCharsets.UTF_8);
		BufferedReader in = new BufferedReader(isr);
		accountView.loadTable(in);
		in.close();

		isr = new InputStreamReader(new FileInputStream(traFile));
		in = new BufferedReader(isr);
		transactionView.loadTable(in);
		in.close();
	}
	
	public void save() throws FileNotFoundException, UnsupportedEncodingException {
		PrintStream ps = new PrintStream(accFile,"UTF-8");
		accountView.saveTable(ps );
		ps.close();
		
		ps = new PrintStream(traFile);
		transactionView.saveTable(ps);
		ps.close();
	}

	public Preferences preferences() {
		return preferences;
	}
}
