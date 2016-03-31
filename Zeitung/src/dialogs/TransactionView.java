package dialogs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import application.MainWindow;
import forms.TransactionForm;
import fx.controll.NumberChooser;
import fx.view.SimpleTable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.BorderPane;
import javafx.util.Callback;
import structs.Transaction;

public class TransactionView extends BorderPane {

	NumberFormat curr = NumberFormat.getCurrencyInstance(Locale.GERMANY);
	private SimpleTable<Transaction> table;
	private MainWindow mainWindow;

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
	}

	public void showDialog(long id) {
		TransactionForm transactionForm = new TransactionForm();

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

				BigDecimal count = new BigDecimal(counter.getNumber().toString());
				BigDecimal cost = count.subtract(count.multiply(new BigDecimal(0.1))).multiply(price);
				BigDecimal amount2 = new BigDecimal(amount.getNumber().toString());

				BigDecimal subtract = cost.subtract(amount2);
				BigDecimal diff = subtract.divide(cost,RoundingMode.HALF_DOWN);
				System.out.println(diff+" "+diff.compareTo(mainWindow.getMaxDiff()) );
				if (mainWindow.getMaxDiff().compareTo(diff) < 0) {
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("warning");
					String s = String.format(
							"The cost of the is to high for the ammount of products: \n cost:%s amount:%s",
							curr.format(cost.doubleValue()), curr.format(amount2.doubleValue()));
					alert.setContentText(s);
					alert.showAndWait();
					ButtonType result2 = alert.getResult();
					switch (result2.getButtonData()) {
					case APPLY:
						break;
					case BACK_PREVIOUS:
						break;
					case BIG_GAP:
						break;
					case CANCEL_CLOSE:
						return null;
					case FINISH:
						break;
					case HELP:
						break;
					case HELP_2:
						break;
					case LEFT:
						break;
					case NEXT_FORWARD:
						break;
					case NO:
						break;
					case OK_DONE:
						break;
					case OTHER:
						break;
					case RIGHT:
						break;
					case SMALL_GAP:
						break;
					case YES:
						break;
					default:
						break;
					}
				}
				return new Transaction(id, cost, amount2, 1, counter.getNumber().intValue());

			}
		};
		transactionForm.setResultConverter(result);
		transactionForm.showAndWait();
		Transaction transaction = transactionForm.getResult();
		if (transaction != null) {
			getTable().addRow(transaction);
		}

	}

	public SimpleTable<Transaction> getTable() {
		return table;
	}

	public void setTable(SimpleTable<Transaction> table) {
		this.table = table;
	}

}
