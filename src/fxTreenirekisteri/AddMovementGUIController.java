package fxTreenirekisteri;


import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import treenirekisteri.Movement;
import treenirekisteri.Result;
import treenirekisteri.Treenirekisteri;
import treenirekisteri.Type;

/**
 * Luokka käyttöliittymän tapahtumien hoitamiseksi
 * @author heini
 * @version 15 Jan 2020
 * @version 27 Apr 2020
 *
 */
public class AddMovementGUIController implements ModalControllerInterface<Treenirekisteri> {

@FXML Button Cancel;
private Treenirekisteri rekisteri;
@FXML private ListChooser<Movement> chooserMovements;
private Movement selected;
@FXML private TextField textName;
@FXML private ComboBoxChooser<Type> typeChooser;

    /**
     * Asetetaan liikelista
     */
    @Override
    public void handleShown() {
        chooserMovements.clear();
        chooserMovements.addSelectionListener(e -> naytaMovement());
        
        hae(0);
        haeTypes();
    }
    
    /**
     * @return palauttaa listchooserin
     */
    public ListChooser<Movement> getList() {
        return chooserMovements;
    }
    
  
    /**
     *  Käsitellään uuden liikkeen lisääminen
     */
    @FXML private void handleAdd() {
        uusiMovement();
        
    }
    
    /**
     * Käsitellään liikkeen muokkaaminen
     */
    @FXML void handleEdit() {
        editMovement();
    }
    
    /**
     * Käsitellään liikkeen ja liikkeen sisältävän tuloksen poisto
     */
    @FXML void handleRemove() {
        removeMovement();
    }
    
    /**
     * Käsitellään toiminnon peruuttaminen
     */
    @FXML private void handleCancel() {
        ModalController.closeStage(Cancel);
    }
    
    /**
     * yläpalkin home-painikkeen käsittelu
     */
    @FXML private void handleHome() {
        ModalController.closeStage(Cancel);
    }
    
    /**
     * Käsitellään uuden tuloksen lisääminen "Add result"
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
    
    }
   
    /**
     * Käsitellään ohjeiden aukaisu menuvalikosta "help" "Instructions"
     */
    @FXML private void handleInstructions() {
        ModalController.showModal(TreenirekisteriGUIController.class.getResource("InstructionsGUIView.fxml"), "Instructions", null, "");
    }
    
    /**
     * Hakee tyyppilistaan valittavat tyypit
     */
    private void haeTypes() {
        for (int i = 0; i<rekisteri.getTypes(); i++) {
            typeChooser.add(rekisteri.annaType(i).gettypeNimi(), rekisteri.annaType(i));
        }
        
    }

    /**
     * Näyttää liikkeen nimen listchooserissa
     */
    public void naytaMovement() {
        selected = chooserMovements.getSelectedObject();
        if ( selected == null ) return; 
        textName.setText(selected.getmovNimi());
        
    }
    
    /**
     * Hakee liikkeen tiedot listaan
     * @param rnro tuloksen numero, joka aktivoidaan haun jälkeen
     */
    protected void hae(int rnro) {
        chooserMovements.clear();
        int index = 0;
            
            for(int i = 0; i<rekisteri.getMovements(); i++) {
               Movement move = rekisteri.annaMovement(i);//haetaan rekisteristä valittavan liikkeen id
                if (move.gettunnusNro() == rnro) index = i;
                chooserMovements.add(move.getmovNimi(), move);//lisätään näyttölistaan liike
          
        }
        chooserMovements.setSelectedIndex(index); //tästä tulee muutosviesti joka näyttää tuloksen
    }
    
    /**
     * Lisätään uusi liike, jota aletaan editoimaan
     */
    protected void uusiMovement() {
        
        Movement move = new Movement();
        try {
                 
            move.rekisteroi();
            
            move.setNimi(textName.getText());
            move.setTypeid(typeChooser.getSelectedObject().getId());  
            if (move.getmovNimi() == null || move.getTypeid() == 0) return;
            rekisteri.lisaa(move);
        } catch (Exception e) {
            Dialogs.showMessageDialog("Valitse tulostyyppi! Tyyppi on nyt:" + e.getMessage());
            return;
        }
        hae(move.gettunnusNro());      

    }
    
    /**
     * Muokkaa jo olemassa olevaa listasta valittua liikettä
     */
    private void editMovement() {
    try {
       selected = chooserMovements.getSelectedObject();
       selected.setNimi(textName.getText());
       if (selected.getmovNimi() == null || selected.getTypeid() == 0) return;
       selected.setTypeid(typeChooser.getSelectedObject().getId()); } catch (Exception e) {
           Dialogs.showMessageDialog("Valitse tulostyyppi! Tyyppi on nyt:" + e.getMessage());
           return;
       }
       hae(selected.gettunnusNro()); 
        
    }
    
    /**
     * Poistaa valitun liikkeen
     */
    private void removeMovement() {
        Movement movement = selected;
        if ( movement == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko liike: " + movement.getmovNimi(), "Kyllä", "Ei") )
            return;
        rekisteri.poistaLiike(movement);
        int index = chooserMovements.getSelectedIndex();
        hae(0);
        chooserMovements.setSelectedIndex(index);
        
 }

    
    /**
     * Hakee rekisterin
     */
    @Override
    public Treenirekisteri getResult() {
        return rekisteri;
    }

    @Override
    public void setDefault(Treenirekisteri arg0) {
        rekisteri = arg0;
        
    }
    
}
