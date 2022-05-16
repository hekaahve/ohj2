package fxTreenirekisteri;

import java.net.URL;
import java.util.Collection;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import treenirekisteri.Movement;
import treenirekisteri.Result;
import treenirekisteri.SailoException;
import treenirekisteri.Treenirekisteri;
import treenirekisteri.Type;

/**
 * Luokka kyttliittymn tapahtumien hoitamiseksi
 * @author heini
 * @version 15 Jan 2020
 * @version 27 Apr 2020
 *
 */
public class TreenirekisteriGUIController implements Initializable {
    @FXML private ScrollPane panelResult;
    @FXML private ListChooser<Result> chooserResults;
    @FXML private ComboBoxChooser<Movement> movementChooser;
    @FXML private TextField textResult;
    @FXML private TextField textType;
    @FXML private TextField textMove;
    
    /**
     * Alustetaan ohjelma
     */
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();     
    }
    
    /**
     * Ksitelln uuden liikkeen lisminen "add movement"
     * @throws SailoException jos ei mahdu
     */
    @FXML private void handleAddMovement() throws SailoException {
        ModalController.showModal(TreenirekisteriGUIController.class.getResource("AddMovementGUIView.fxml"), "AddMovement", null, rekisteri);   
        tallenna();
        movementChooser.clear();
        chooserResults.clear();
        int index = chooserResults.getSelectedIndex();
        hae(0);
        naytaMovements();
        chooserResults.setSelectedIndex(index);
        tallenna();
        
    }
    
    /**
     * Käsitellään tulosten haku tietyn liikkeen perusteella
     */
    @FXML private void handleHaku() {
        hae(0);

    }
    
    /**
     * Käsitellään uuden tuloksen lisminen "Add result"
     */
    @FXML private void handleAddResult() {
       
        try {
                Result uusi = new Result();
                uusi = AddresultGUIController.handleTulos(null, uusi, rekisteri);    
                if ( uusi == null || uusi.getPvm() == null || uusi.getResult() == null) return;
                uusi.rekisteroi();
                rekisteri.lisaa(uusi);
                hae(uusi.getRestultId());
            } catch (Exception e) {
                    Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
                    return;
        }
        tallenna();
   
    }
    
    /**
     * Käsitellään tuloksen muokkaaminen
     */
    @FXML private void handleEdit() {
        
        resultKohdalla = AddresultGUIController.handleTulos(null, resultKohdalla, rekisteri);    
        if ( resultKohdalla == null || resultKohdalla.getPvm() == null || resultKohdalla.getResult() == null) return;
        tallenna();
        hae(0);
    }
    
    /**
     * Käsitellään tuloksen poisto
     */
    @FXML private void handleDeleteresult() {
        poistaResult();
    }

    
    /**
     * Ksitelln ohjeiden aukaisu menuvalikosta "help" "Instructions"
     */
    @FXML private void handleInstructions() {
        ModalController.showModal(TreenirekisteriGUIController.class.getResource("InstructionsGUIView.fxml"), "Instructions", null, "");
    }
    
    /**
     * Käsitellään lopetusäsky
     */
    @FXML private void handleExit() {
        if ( !Dialogs.showQuestionDialog("Exit", "Haluatko poistua?: ", "Kyllä", "Ei") )
            return;
        tallenna();
        Platform.exit();
    }
    
    
    /**
     * Nollaa valitut tulokset ja näyttää kaikki 
     */
    @FXML private void handleOverview() {
        
        movementChooser.clear();
        chooserResults.clear();
        int index = chooserResults.getSelectedIndex();
        hae(0);
        naytaMovements();
        chooserResults.setSelectedIndex(index);
        tallenna();
    }
 
    
 //========ei käyttöliittymään suoraan liittyvää koodia=========

    private Treenirekisteri rekisteri;
    private Result resultKohdalla;
    
    
    /**
     * Tietojen tallennus
     */
    private void tallenna() {
        try {
            rekisteri.tallenna();
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Tallennuksessa tuli virhe "+e.getMessage());
        }
    }
    
    /**
     * Tarkistetaan, onko tallennus tehty
     * @return true jos saa sulkea sovelluksen, false jos ei saa
     */
    public boolean voikoSulkea() {
        tallenna();
        return true;
    }

    
    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan StringGridin tilalle
     * yksi iso tekstikentt, johon voidaan tulostaa tulosten ja liikkeiden tiedot.
     * Alustetaan mys tuloslistan kuuntelija 
     */
    private void alusta() {
        
       chooserResults.addSelectionListener(e -> naytaResult());
    }
    
    /**
     * Hakee tulosten tiedot listaan. Jos liikelajittelulistasta valitaan liike, listaa
     * ainoastaan kyseistä liikettä sisältävät tulokset
     * @param rnro tuloksen numero, joka aktivoidaan haun jlkeen
     */
    protected void hae(int rnro) {
        int nro = rnro;
        chooserResults.clear();

        int index = 0;
        
        Movement liike = movementChooser.getSelectedObject();
        int tunnus = -1;
        if (liike != null) tunnus = liike.gettunnusNro();
        Collection<Result> results;
        
        try {            
            results = rekisteri.etsi(tunnus);
            
            int i = 0;
            for (Result result:results) {
                if (result.getRestultId() == nro) index = i;
                Movement tulosliike = rekisteri.haeLiike(result.getLiikeId());
                String tulos = tulosliike.getmovNimi()+" | "+ result.getPvm();
                chooserResults.add(tulos, result);//rekisteristä haetaan liike, joka haetaan resultin idn perusteella
                i++;
            }
  
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Tuloksen hakemisessa ongelmia! " + e.getMessage());
        }

        chooserResults.setSelectedIndex(index); //tst tulee muutosviesti joka nytt tuloksen
    }
    
    /**
     * Hakee liikkeet pikkunan valikkoon tulosten lajittelua varten
     */
    private void naytaMovements() {
        for (int i = 0; i<rekisteri.getMovements(); i++) {
            movementChooser.add(rekisteri.annaMovement(i).getmovNimi(), rekisteri.annaMovement(i));
        }        
    }
    
  /**
   * Poistaa valitun tuloksen
   */
    private void poistaResult() {
        Result result = resultKohdalla;
        if ( result == null ) return;
        Movement tulosliike = rekisteri.haeLiike(result.getLiikeId());
        String tulos = tulosliike.getmovNimi()+" | "+ result.getPvm();
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko tulos: " + tulos, "Kyllä", "Ei") )
            return;
        rekisteri.poistaResult(result);
        int index = chooserResults.getSelectedIndex();
        hae(0);
        chooserResults.setSelectedIndex(index);
        tallenna();
    }

    
    /**
     * Näyttää listasta valitun tuloksen tiedot, 
     * kenttiin
     */
    protected void naytaResult() {
        resultKohdalla = chooserResults.getSelectedObject();
        Movement liikkeet = rekisteri.haeLiike(resultKohdalla.getLiikeId());
        Type tyyppi = rekisteri.haeType(liikkeet.getTypeid());
        if (resultKohdalla == null) return;
        textResult.setText(resultKohdalla.getResult());
        textType.setText(tyyppi.gettypeNimi());
        textMove.setText(liikkeet.getmovNimi());
    }
    
    
    /**
     * Luetaan tiedosto
     */
    public void lueTiedosto() {
        try {
            rekisteri.lueTiedosto();
            hae(0);
        } catch (SailoException e) {
            Dialogs.showMessageDialog(e.getMessage());
        }
    }

    /**
     * 
     * @param rekisteri treenirekisteri jota ksitelln
     * @throws SailoException jos ei mahdu
     */
    public void setTreenirekisteri(Treenirekisteri rekisteri) throws SailoException {
        this.rekisteri = rekisteri;
        
        lueTiedosto();
        naytaResult();
        naytaMovements();
        
    }

}
