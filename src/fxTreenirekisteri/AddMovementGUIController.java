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
 * Luokka k‰yttˆliittym‰n tapahtumien hoitamiseksi
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
     *  K‰sitell‰‰n uuden liikkeen lis‰‰minen
     */
    @FXML private void handleAdd() {
        uusiMovement();
        
    }
    
    /**
     * K‰sitell‰‰n liikkeen muokkaaminen
     */
    @FXML void handleEdit() {
        editMovement();
    }
    
    /**
     * K‰sitell‰‰n liikkeen ja liikkeen sis‰lt‰v‰n tuloksen poisto
     */
    @FXML void handleRemove() {
        removeMovement();
    }
    
    /**
     * K‰sitell‰‰n toiminnon peruuttaminen
     */
    @FXML private void handleCancel() {
        ModalController.closeStage(Cancel);
    }
    
    /**
     * yl‰palkin home-painikkeen k‰sittelu
     */
    @FXML private void handleHome() {
        ModalController.closeStage(Cancel);
    }
    
    /**
     * K‰sitell‰‰n uuden tuloksen lis‰‰minen "Add result"
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
     * K‰sitell‰‰n ohjeiden aukaisu menuvalikosta "help" "Instructions"
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
     * N‰ytt‰‰ liikkeen nimen listchooserissa
     */
    public void naytaMovement() {
        selected = chooserMovements.getSelectedObject();
        if ( selected == null ) return; 
        textName.setText(selected.getmovNimi());
        
    }
    
    /**
     * Hakee liikkeen tiedot listaan
     * @param rnro tuloksen numero, joka aktivoidaan haun j‰lkeen
     */
    protected void hae(int rnro) {
        chooserMovements.clear();
        int index = 0;
            
            for(int i = 0; i<rekisteri.getMovements(); i++) {
               Movement move = rekisteri.annaMovement(i);//haetaan rekisterist‰ valittavan liikkeen id
                if (move.gettunnusNro() == rnro) index = i;
                chooserMovements.add(move.getmovNimi(), move);//lis‰t‰‰n n‰yttˆlistaan liike
          
        }
        chooserMovements.setSelectedIndex(index); //t‰st‰ tulee muutosviesti joka n‰ytt‰‰ tuloksen
    }
    
    /**
     * Lis‰t‰‰n uusi liike, jota aletaan editoimaan
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
     * Muokkaa jo olemassa olevaa listasta valittua liikett‰
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
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko liike: " + movement.getmovNimi(), "Kyll‰", "Ei") )
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
