package application;

import java.math.BigDecimal;

import dialogs.AccountView;
import dialogs.TransactionView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainWindow extends TabPane {

	private static final BigDecimal maxDiff = new BigDecimal("0.1");
	private AccountView accountView;
	private TransactionView transactionView;

	public MainWindow() {
		
		initMenu();
		
		//add the account view
		accountView = new AccountView(this);
		Tab tab = new Tab("Accounts");
		tab.setContent(accountView);
		getTabs().add(tab);
		
		//add the transaction view
		transactionView = new TransactionView(this);
		tab = new Tab("Transactions");
		tab.setContent(transactionView);
		getTabs().add(tab);
	}

	private void initMenu() {
		MenuBar bar = new MenuBar();
		Menu menu = new Menu("Load");
		getChildren().add(bar);

		menu.getItems().add(new MenuItem("hui"));
		bar.getMenus().add(menu);
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
}
