package treenirekisteri;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Vastuualueet: 
 * - ei tied� treenirekisterist�, eik� k�ytt�liittym�st
 * - tiet�� liikkeen eli movementin kent�t (name, result) 
 * - osaa tarkistaa tietyn kent�n oikeellisuuden (syntaksin)    
 * - osaa muuttaa 1|pull up| merkkijonon liikkeen tiedoiksi   
 * - osaa antaa merkkijonona i:n kent�n tiedot
 * - saa laittaa merkkijonon i:neksi kent�ksi
 * @author heini
 * @version 11 Mar 2020
 * @version 27 Apr 2020
 *
 */
public class Movement {
    private String movNimi;
    private int tunnusNro; 
    private static int seuraavaNro = 1;
    private int typeId;
    //private String typeNimi;
    
    /**
     * Liikkeen alustus
     */
    public Movement() {
        // ei tarvii mit��n
    }
    
    /**
     * @param arvottuType asetetaan liikkeelle tyyppiId
     */
    public void setTypeid(int arvottuType) {
        this.typeId = arvottuType;
        
    }
    
    /**
     * @param Type asetetaan liikkeelle tyyppiId
    
    public void setTypeNimi(String Type) {
        this.typeNimi = Type;
        
    }
     */
    
    /**
     * Asetetaan liikkeelle nimi k�ytt�liittym�n puolella tekstikentt��n
     * @param text teksti
     */
    public void setNimi(String text) {
        movNimi = text;
        
    }

    
    /**
     * T�ytet��n liikkeen nimi
     */
    public void taytaMov() {
        movNimi = "run";
    }
    
    //set TypeId (tyypin parametri)
    
    /**
     * @return liikkeen nimi
     */
    public String getmovNimi() {
        return movNimi;
    }
    
    
    /**
     * palauttaa typeIdn
     * @return liikkeen typeid
     */
    public int getTypeid() {
        return typeId;
    }
    
    
    /**
     * @return liikkeen tunnusnumero
     */
    public int gettunnusNro() {
        return tunnusNro;
    }
    
    /**
     * Rekister�id��n liikkeen tunnusnumero juoksevan 
     * numeroinnin mukaan
     * @return liikkeen tunnusnumero
     * @example
     * <pre name="test">
     * Movement run = new Movement();
     * run.gettunnusNro() === 0;
     * run.rekisteroi();
     * Movement pullUp = new Movement();
     * pullUp.rekisteroi();
     * int n1 = run.gettunnusNro();
     * int n2 = pullUp.gettunnusNro();
     * n2 === n1+1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }
    
    /**
     * Muuttaa liikkeen merkkijonoksi
     */
    @Override
    public String toString() {
        return tunnusNro + " |"+ movNimi +"|"+typeId;
    }
    
    /**
     * Liikkeen tulostus
     * @param out tulostus
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%02d", tunnusNro) + " "+ movNimi+"|"+typeId);
    }
    
    /**
     * Selvitt�� liikkeen tiedot merkkijonosta 
     * @param rivi josta liikkeen tiedot otetaan
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        tunnusNro = Mjonot.erota(sb, '|', tunnusNro);
        if ( tunnusNro>= seuraavaNro ) seuraavaNro = tunnusNro + 1;
        movNimi = Mjonot.erota(sb, '|', movNimi);
        typeId = Mjonot.erota(sb, '|', typeId);
        
    }

    
    /**
     * Tulostetaan liikkeen tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }
    
    
    /**
     * @param args ei k�yt�ss�
     * 
     */
    public static void main(String args[]) {
        Movement run = new Movement(), 
                squat = new Movement(),
                pullUp = new Movement();
        
        run.rekisteroi();
        squat.rekisteroi();
        pullUp.rekisteroi();
        
        run.taytaMov();
        squat.taytaMov();
        pullUp.taytaMov();
        
        run.tulosta(System.out);
        squat.tulosta(System.out);
        pullUp.tulosta(System.out);
        
        

    }

    
   
}
