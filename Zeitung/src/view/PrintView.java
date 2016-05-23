package view;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import structs.Account;
import structs.Ausweis;

public class PrintView extends BorderPane{

	private ArrayList<Ausweis> ausweise = new ArrayList<>();
	private int row = 1, column = 1;
	private static final int COLUMNS = 2;
	private GridPane preview = new GridPane();
	private Stage primaryStage;
	
	String folder = "file:C:/Users/sascha/Dropbox/Verkäuferausweise/img/";
	
	public PrintView(Stage primaryStage) {
		this.primaryStage = primaryStage;
		Button clear = new Button("clear");
		Button print = new Button("print");
		
		clear.setOnAction(c->{
			ausweise.clear();
			preview.getChildren().clear();
			row = 1;
			column = 1;
		});
		
		print.setOnAction(c->{
			print();
		});
		
		HBox b = new HBox();
		b.getChildren().add(clear);
		b.getChildren().add(print);
		
		setTop(b);
		setCenter(new ScrollPane(preview));
		
	}
	
	public void add(Ausweis ausweis){
		ausweise.add(ausweis);
		preview.add(ausweis, column, row);
		if(++column>COLUMNS){
			column = 1;
			row++;
		}
	}
	
	public void print() {
        Printer printer = Printer.getDefaultPrinter();
        PageLayout pageLayout = printer.createPageLayout(Paper.A4, PageOrientation.PORTRAIT, Printer.MarginType.HARDWARE_MINIMUM);

        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean successPrintDialog = job.showPrintDialog(primaryStage.getOwner());
            if(successPrintDialog){
            	boolean success = false;
            	for(int i=0; i<ausweise.size();i++){		
            		WritableImage snapshot = ausweise.get(i).snapshot(new SnapshotParameters(), null);
            		ImageView imageView = new ImageView(snapshot);
                    double scaleX = pageLayout.getPrintableWidth() / imageView.getBoundsInParent().getWidth();
                    double scaleY = pageLayout.getPrintableHeight() / imageView.getBoundsInParent().getHeight();
                    imageView.getTransforms().add(new Scale(scaleX, scaleY));
            		success = job.printPage(pageLayout,imageView);
            	}
                if (success) {
                    job.endJob();
                }
            }
        }
    }

	public void addAll(ObservableList<Account> selectedItems) {
		for (Account account : selectedItems) {
			add(new Ausweis(account.getFirstname(), account.getLastname(), folder+account.getImageFile(), account.getId()));
		}
	}
	
}
