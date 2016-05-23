package structs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Light;
import javafx.scene.effect.Light.Distant;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

public class Ausweis extends VBox {

	static final int A4_WIDTH = 210, A4_HEIGHT = 290;

	public Ausweis(String fname, String name, String imgname, long l) {

		/***************** Page Layout *******************/
		setStyle(
				"-fx-background-color: #CCFF99;-fx-border-width: 3mm;-fx-border-color: #FFFFFF;-fx-padding:3mm;-fx-pref-width: "
						+ A4_WIDTH / 2 + "mm; -fx-pref-height: " + A4_HEIGHT / 2 + "mm;");
		setAlignment(Pos.TOP_CENTER);

		/***************** Title Region ******************/
		// Title Background
		HBox titleBG = new HBox();
		titleBG.setStyle("-fx-background-color: #888888;");
		titleBG.setAlignment(Pos.TOP_CENTER);
		getChildren().add(titleBG);

		// Title
		Text title = new Text("TALKTOGETHER!");
		Font font = Font.font("Comic Sans MS", FontWeight.BOLD, FontPosture.ITALIC, 40);
		title.setFill(Color.DARKGREEN);
		title.setFont(font);
		titleBG.getChildren().add(title);

		/***************** Title Effects *****************/
		// Title shadow
		DropShadow dropShadow = new DropShadow();
		dropShadow.setRadius(5.0);
		dropShadow.setOffsetX(3.0);
		dropShadow.setOffsetY(3.0);
		dropShadow.setColor(Color.color(0, 0, 0));

		// Title lighting
		Distant light = new Light.Distant(-45, 82, Color.WHITE);
		Lighting lighting = new Lighting();
		lighting.setLight(light);
		lighting.setSurfaceScale(2);

		dropShadow.setInput(lighting);
		title.setEffect(dropShadow);

		/***************** Content Text ******************/
		// Subtitle
		Font stfnt = Font.font("Times", 15);
		Text subtitle = new Text("Zeitung von und für Migrantinen und Nicht-Migrantinen");
		subtitle.setFont(stfnt);
		getChildren().add(subtitle);

		// Description
		HBox box = new HBox();
		box.setAlignment(Pos.BASELINE_LEFT);
		getChildren().add(box);

		Text desc = new Text("Verkäuferausweis:");
		Font descFont = Font.font("Times", FontWeight.BOLD, FontPosture.ITALIC, 32);
		desc.setStyle("-fx-underline: true;");
		desc.setFont(descFont);
		box.getChildren().add(desc);

		/******************** Boxes **********************/
		HBox vertSplit = new HBox(20);
		vertSplit.setAlignment(Pos.CENTER);

		/********************* Left **********************/
		VBox left = new VBox(10);
		left.setAlignment(Pos.BASELINE_CENTER);
		vertSplit.getChildren().add(left);
		getChildren().add(vertSplit);

		/********************* Image **********************/
		Image image = new Image(imgname);
		ImageView imageView = new ImageView(image);
		imageView.setFitWidth(140);
		imageView.setFitHeight(160);
		HBox bounds = new HBox();
		bounds.setPadding(new Insets(5, 0, 5, 5));
		bounds.getChildren().add(imageView);
		left.getChildren().add(bounds);

		/********************** Date *********************/
		Font datefnt = Font.font("Times", FontWeight.BOLD, 18);
		Text valid = new Text("gültig bis:");
		valid.setFont(datefnt);
		left.getChildren().add(valid);

		LocalDate localDate = LocalDateTime.now().toLocalDate();
		Text date = new Text(localDate.plus(3, ChronoUnit.MONTHS).toString());
		date.setFont(datefnt);
		HBox b1 = getBox();
		b1.getChildren().add(date);
		left.getChildren().add(b1);

		/********************* Right **********************/
		VBox right = new VBox(5);
		right.setPrefWidth(5000);
		vertSplit.getChildren().add(right);
		right.setAlignment(Pos.TOP_RIGHT);
		right.setPadding(new Insets(10, 5, 10, 0));

		/******************* First Name *******************/
		Font fnameFont = Font.font("Times", FontWeight.BOLD, 27);
		Text tfname = new Text(fname);
		tfname.setFont(fnameFont);
		HBox b = getBox();
		b.getChildren().add(tfname);
		right.getChildren().add(b);

		/********************* Name **********************/
		Font nameFont = Font.font("Times", FontWeight.BOLD, 22);
		Text tname = new Text(name);
		tname.setFont(nameFont);
		b = getBox();
		b.getChildren().add(tname);
		right.getChildren().add(b);
		VBox.setMargin(b, new Insets(0,0,20,0));

		/********************** ID ***********************/
		Font nrfnt = Font.font("Times", FontWeight.BOLD, 92);
		Text nr = new Text(""+l);
		nr.setFont(nrfnt);
		b = getBox();
		b.getChildren().add(nr);
		b.setAlignment(Pos.BASELINE_RIGHT);
		right.getChildren().add(b);

		/********************** Footer ***********************/
		Font ffnt = Font.font("Times", FontWeight.BOLD, 18);
		Text foot = new Text("Verein Salzburg - Kommunikation und Kultur\nwww.talktogether.org\nTel.:0650/2631587");
		foot.setTextAlignment(TextAlignment.CENTER);
		foot.setFont(ffnt);
		foot.setFill(Color.DARKGREEN);
		setSpacing(20);
		getChildren().add(foot);
		autosize();
	}

	private HBox getBox() {
		HBox box = new HBox();
		box.setStyle(getBoxStyle());
		return box;
	}

	private String getBoxStyle() {
		return "-fx-background-color: #FFFFFF;-fx-border-color: #000000, #FFFFFF;-fx-border-width: 1mm;";
	}

}
