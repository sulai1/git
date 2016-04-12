package application;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import javafx.application.Application;
import javafx.stage.Stage;

@RunWith(Suite.class)
@SuiteClasses({ t1.class })
public class AllTests extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		
	}

}
