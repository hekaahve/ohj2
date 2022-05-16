package fxTreenirekisteri;
import javafx.stage.Stage;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
//import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import treenirekisteri.Movement;
import treenirekisteri.Result;
import treenirekisteri.Treenirekisteri;

/**
 * Luokka käyttöliittymän tapahtumien hoitamiseksi
 * @author heini
 * @version 15 Jan 2020
 * @version 27.4.2020
 *
 */
public class AddresultGUIController implements ModalControllerInterface<Result> {

    @FXML Button Cancel;
    private Treenirekisteri rekisteri;
    @FXML private ListChooser<Movement> chooserMovements;
    @FXML private ListChooser<Result> chooserResults;
    @FXML private TextField textMove;
    @FXML private TextField textResult;
    private Movement selected;
    private Result resultKohdalla;
    //private TextArea areaResult = new TextArea();
    @FXML private DatePicker resultDate;

    /**
     * Asetetaan ikkuna
     */
    @Override
    public void handleShown() {
        chooserMovements.addSelectionListener(e -> naytaMovement());  
             
        selected = rekisteri.haeLiike(resultKohdalla.getLiikeId());
        if (selected != null) textMove.setText(selected.getmovNimi());         
        textResult.setText(resultKohdalla.getResult());
        
    }
    
    /**
     * Asetetaan treenirekisteri
     * @param rekisteri2 rekisteri, joka asetetaan
     */
    private void setRekisteri(Treenirekisteri rekisteri2) {
        this.rekisteri = rekisteri2;
        
    }
    
    /**
     * Haetaan rekisteristä kaikki valittavana olevat liikkeet sivuluetteloon
     * @param i liike paikassa i
     */
    private void haeMovements(int i) {
        chooserMovements.clear();

        int index = 0;
            
            for(int j = 0; j<rekisteri.getMovements(); j++) {
               Movement move = rekisteri.annaMovement(j);//haetaan rekisteristä valittavan liikkeen id
                if (move.gettunnusNro() == i) index = j;
                chooserMovements.add(move.getmovNimi(), move);//lisätään näyttölistaan liike
          
            }
            chooserMovements.setSelectedIndex(index);
        }
    
    /**
     * Näyttää liikkeen nimen tekstikentässä
     */
    public void naytaMovement() {
        selected = chooserMovements.getSelectedObject();
        if ( selected == null ) return; 
        textMove.setText(selected.getmovNimi());        
    }
        
    /**
     * Asetetaan tuloksen oletus
     */
    @Override
    public void setDefault(Result oletus) {
        resultKohdalla = oletus;
        
    }
    
    /**
     * Palauttaa tuloksen
     */
    @Override
    public Result getResult() {
        return resultKohdalla;
    }
    
    /**
     * Käsitellään uuden tuloksen lisÃ¤Ã¤minen
     */
    @FXML private void handleAdd() {
        uusiResult();
    }
    
    /**
     * Käsitellään tuloksen muokkaaminen
     */
    @FXML private void handleEditresult() {
        uusiResult();
    }
    
    /**
     * Käsitellään ohjeiden aukaisu menuvalikosta "help" "Instructions"
     */
    @FXML private void handleInstructions() {
        ModalController.showModal(TreenirekisteriGUIController.class.getResource("InstructionsGUIView.fxml"), "Instructions", null, "");
    }
    
    /**
     * Käsitellään toiminnon peruuttaminen
     */
    @FXML private void handleCancel() {
        ModalController.closeStage(Cancel);
    }
    
    /**
     * lisätään uusi tulos, jota aletaan editoimaan
     * lisätään liikke viereisestä listchooserista ja asetetaan sille tulos
     * käsin kirjoittamalla. 
     * 
     * Jos tulokseen syötetään kirjaimia, heittää poikkeusikkunan eikä lisää tulosta
     * Sulkee ikkunan, kun kelvollinen tulos on syötetty. 
     */
    protected void uusiResult() {
        double tulos = 0;
        try {
        resultKohdalla.setMovementId(selected.gettunnusNro()); // valitun chooserlistan liikkeen ID
        resultKohdalla.setTulos(textResult.getText()); //asettaa tuloksen arvon tekstikenttään syötettynä
        try {
            tulos = Double.parseDouble(textResult.getText());
            } catch (NumberFormatException ex){
                Dialogs.showMessageDialog("Ei kelvollinen tulos:" + textResult.getText());}
        resultKohdalla.paivays(resultDate.getValue().toString());
        } catch (Exception ex){
            Dialogs.showMessageDialog("Syötä kaikki tiedot.");}
        if (tulos <= 0) {
            Dialogs.showMessageDialog("Ei kelvollinen tulos." + textResult.getText());
            resultKohdalla = null;
            return;
        } 
       
        if (resultKohdalla.getPvm() != null && resultKohdalla.getResult() != null || resultKohdalla.getLiikeId() !=0 && 
                resultKohdalla.getPvm() != null) ModalController.closeStage(Cancel); 
    }
    

    /**
     * Lisätään tulosikkunaan liikkeet ja lisätään itse tulos
     * @param modalityStage ikkuna 
     * @param oletus itse tulos
      * @param rekisteri2 rekisteri, joka asetetaan ja haetaan tiedot
     * @return controller, jossa on lisätty tiedot
     */
    public static Result handleTulos(Stage modalityStage, Result oletus, Treenirekisteri rekisteri2) {
        return ModalController.<Result, AddresultGUIController>showModal(
                AddresultGUIController.class.getResource("AddresultGUIView.fxml"),
                                    "Treenirekisteri",
                                   modalityStage, oletus,                                 
                                   ctrl -> {ctrl.setRekisteri(rekisteri2);
                                       ctrl.haeMovements(0);}
                                );
    }


}
