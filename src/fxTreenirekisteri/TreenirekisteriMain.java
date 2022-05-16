package fxTreenirekisteri;
	
import javafx.application.Application;
import javafx.stage.Stage;
import treenirekisteri.Treenirekisteri;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * @author heini
 * @version 15 Jan 2020
 * PÃ¤Ã¤ohjelma Treenirekisteri-ohjelman kÃ¤ynnistÃ¤miseksi 
 */
public class TreenirekisteriMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		    final FXMLLoader ldr = new FXMLLoader(getClass().getResource("PaaikkunaGUIView.fxml"));
		    final Pane root = (Pane)ldr.load();
		    final TreenirekisteriGUIController rekisteriCtrl = (TreenirekisteriGUIController)ldr.getController();
		    
		    Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("treenirekisteri.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
			
			  primaryStage.setOnCloseRequest((event) -> {
                  if ( !rekisteriCtrl.voikoSulkea() ) event.consume();
              });

			
			Treenirekisteri rekisteri = new Treenirekisteri();
			rekisteriCtrl.setTreenirekisteri(rekisteri);
			
		} catch(Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
	}
	
	/**
	 * Käynnistetään käyttöliittymä
	 * @param args kommentointirivin parametrit
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
