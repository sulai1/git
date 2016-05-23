package view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

import application.MainWindow;
import fx.controll.Form;
import fx.controll.NumberChooser;
import fx.view.SimpleTable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import structs.Transaction;

public class TransactionView extends BorderPane {

	NumberFormat curr = NumberFormat.getCurrencyInstance(Locale.GERMANY);
	private SimpleTable<Transaction> table;
	private MainWindow mainWindow;
	private boolean altered = false;

	public TransactionView(MainWindow mainWindow) {
		this.mainWindow = mainWindow;
		setTable(new SimpleTable<Transaction>());
		getTable().addColumn("Account ID", t -> "" + t.getId());
		getTable().addColumn("Product", t -> "" + t.getProductID());
		getTable().addColumn("Count", t -> "" + t.getCount());
		getTable().addColumn("Cost", t -> "" + curr.format(t.getCost().doubleValue()));
		getTable().addColumn("Amount", t -> "" + curr.format(t.getAmount().doubleValue()));
		getTable().addColumn("Date", t -> "" + t.getDate());
		setCenter(getTable());

		ListChangeListener<Transaction> l = new ListChangeListener<Transaction>() {
			@Override
			public void onChanged(Change<? extends Transaction> arg0) {
				altered = true;
			}
		};
		table.getItems().addListener(l);
		
		Button add = new Button("add");
		add.setOnAction(c->{
				showDialog();
		});
		setRight(add);
	}

	public void showDialog() {
		Form<Transaction> transactionForm = new Form<>();
		NumberChooser id = new NumberChooser(DecimalFormat.getIntegerInstance());
		transactionForm.addField("id", id);
		
		showDialog(id.getProperty(), transactionForm);
	}

	public void showDialog(long id) {
		Form<Transaction> transactionForm = new Form<>();
		showDialog(new SimpleObjectProperty<Long>(), transactionForm);
	}
	public void showDialog(ObjectProperty<? extends Number> idProperty, Form<Transaction> transactionForm) {
		BigDecimal price = new BigDecimal(1.25);

		NumberChooser counter = new NumberChooser(DecimalFormat.getIntegerInstance());
		transactionForm.addField("count", counter);

		NumberChooser amount = new NumberChooser(curr);
		transactionForm.addField("amount", amount, true);
		amount.getProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				if (!newValue.equals(0)) {
					BigDecimal count = new BigDecimal(newValue.toString()).divide(price);
					counter.setNumber(count.add(new BigDecimal(count.intValue() / 10)));
				}
			}

		});
		Callback<ButtonType, Transaction> result = new Callback<ButtonType, Transaction>() {

			@Override
			public Transaction call(ButtonType param) {

				BigDecimal cost = calculateCost(price, counter.getNumber().intValue());
				BigDecimal amount2 = new BigDecimal(amount.getNumber().toString());

				if (isValidAmount(cost, amount2)) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("warning");
					String s = String.format(
							"The cost of the is to high for the ammount of products: \n cost:%s amount:%s",
							curr.format(cost.doubleValue()), curr.format(amount2.doubleValue()));
					alert.setContentText(s);
					alert.showAndWait();
					ButtonType result2 = alert.getResult();
					switch (result2.getButtonData()) {
					case CANCEL_CLOSE:
						return null;
					default:
						break;
					}
				}
				return new Transaction(idProperty.get().longValue(), cost, amount2, 1, counter.getNumber().intValue());

			}

		};
		transactionForm.setResultConverter(result);
		transactionForm.showAndWait();
		addTransaction(transactionForm.getResult());
	}

	private void addTransaction(Transaction transaction) {
		if (transaction == null)
			return;
		getTable().addRow(transaction);
	}

	private BigDecimal calculateCost(BigDecimal price, int counter) {
		BigDecimal count = new BigDecimal(counter);
		BigDecimal rabatt = count.subtract(BigDecimal.ONE).multiply(new BigDecimal(0.1))
				.round(new MathContext(1, RoundingMode.FLOOR));
		BigDecimal cost = count.subtract(rabatt).multiply(price);
		return cost;
	}

	private boolean isValidAmount(BigDecimal cost, BigDecimal amount2) {
		BigDecimal subtract = cost.subtract(amount2);
		BigDecimal diff = subtract.divide(cost, RoundingMode.CEILING);
		System.out.println(diff + " " + diff.compareTo(mainWindow.getMaxDiff()));
		return mainWindow.getMaxDiff().compareTo(diff) < 0;
	}

	public SimpleTable<Transaction> getTable() {
		return table;
	}

	public void setTable(SimpleTable<Transaction> table) {
		this.table = table;
	}

	public void loadTable(BufferedReader in) throws IOException, NumberFormatException, ParseException {
		table.load(in, args -> {

			try {
				BigDecimal cost = new BigDecimal(curr.parse(args[3]).doubleValue());
				BigDecimal amount = new BigDecimal(curr.parse(args[4]).doubleValue());
				return new Transaction(Long.parseLong(args[0]), cost, amount, Integer.parseInt(args[1]),
						Integer.parseInt(args[2]));
			} catch (Exception e) {
				Log.alertMessage(e.getMessage());
				return null;
			}
		});
		altered = false;
	}

	public void saveTable(PrintStream out) {
		table.store(out);
		altered = false;
	}

	public boolean altered() {
		return altered;
	}
}
