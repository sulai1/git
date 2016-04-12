package dialogs;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import fx.controll.Form;
import fx.controll.TextSupplier;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.ButtonType;
import javafx.util.Callback;

public class Preferences{

	private static final String RES_PROP_TXT = "res/prop.txt";
	private static final String SONDERZEICHEN = "Sonderzeichen";
	
	private ObjectProperty<String> sonderzeichen = new SimpleObjectProperty<>();

	
	private Preferences() {
		
	}

	public Preferences(Properties properties) {
		getSonderzeichen().set(properties.getProperty(SONDERZEICHEN));
	}

	public void setSonderzeichen(String sonderzeichen) {
		this.sonderzeichen.set(sonderzeichen);
	}

	public void showDialog() {
		Form<Properties> preferences = new Form<>();
		TextSupplier textSupplier = new TextSupplier();
		textSupplier.getProperty().bindBidirectional(sonderzeichen);
		preferences.addField("Sonderzeichen", textSupplier,false);
		preferences.setResultConverter(new Callback<ButtonType, Properties>() {
			
			@Override
			public Properties call(ButtonType param) {
				Properties properties = null;
				switch (param.getButtonData()) {
				case OK_DONE:
					properties = save(get());
					break;
				default:
					break;
				}
				return properties;
			}

		});
		preferences.showAndWait();
		
	}

	public ObjectProperty<String> getSonderZeichen() {
		return sonderzeichen;
	}
	
	private Preferences get() {
		return this;
	}	
	public static Properties save(Preferences preferences){
		Properties properties = new Properties();
		properties.setProperty(SONDERZEICHEN, preferences.getSonderzeichen().get());
		try {
			properties.store(new BufferedOutputStream(new FileOutputStream(RES_PROP_TXT)),"comment");
		} catch (IOException e) {
			Log.alertMessage("Could not write file!"+new File(RES_PROP_TXT).getAbsolutePath());
			Log.log(e);
		}
		return properties;
	}
	
	public static Preferences load(){
		
		Properties properties = new Properties();
		try {
			properties.load(new FileReader(RES_PROP_TXT));
			return new Preferences(properties);
		} catch (IOException e) {
			Log.alertMessage("Could not write file!"+new File(RES_PROP_TXT).getPath());
			Log.log(e);
		}
		return new Preferences();
	}

	public static Preferences create() {
		return new Preferences();
	}
	
	public ObjectProperty<String> getSonderzeichen() {
		return sonderzeichen;
	}

}
