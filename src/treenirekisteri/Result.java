package treenirekisteri;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * 
 *  Vastuualueet:                                  
 *  - ei tiedä treenirekisteristä, eikä             
 * käyttöliittymästä)                              
 * - tietää tuloksen lisäämisen eli result-kentän                                               
 * - osaa tarkistaa tietyn kentän oikeellisuuden (syntaksin)                  
 * - osaa muuttaa 3|3000|Cal| 24.5.2019 merkkijonon
 * tuloksen tiedoiksi                         
 * - osaa antaa merkkijonona i:n kentän tiedot     
 * - osaa laittaa merkkijonon i:neksi kentäksi                                                   
 * @author heini
 * @version 12 Mar 2020
 * @version 27 April 2020
 *
 */
public class Result implements Comparable<Result> {
    
    private int resultId;
    private String result;//tuloksen määrä, esim. 5000 m
    private String pvm;
    private int liikeId; //liikkeen id
    
    private static int seuraavarId = 1;
    
    /**
     * Tuloksen alustus
     */
    public Result() {
        //ei tartte mitään
    }
    
    /**
     * Vertailija tuloksen lajittelua varten, lajittelee päivämäärän mukaan
     */
    @Override
    public int compareTo(Result resu) {
        return pvm.compareTo(resu.pvm);
    }

    
    /**
     * Laittaa treeniin syötetyn tulostiedon ja päivämäärän.
     * Väliaikaisesti näyttää aina tämän päivän
     * @param text päivämäärä joka viedään tulokselle
     */
    public void paivays(String text) {
        this.pvm = text;
    }
    
    /**
     * Selvittää tuloksen tiedot | erotellusta merkkijonosta
     * @param rivi josta tuloksen tiedot otetaan
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        resultId = Mjonot.erota(sb, '|', resultId);
        if ( resultId>= seuraavarId ) seuraavarId = resultId + 1;
        result = Mjonot.erota(sb, '|', result);
        pvm = Mjonot.erota(sb, '|', pvm);
        liikeId = Mjonot.erota(sb, '|', liikeId);
    }

    
    /**
     * @return palauttaa tuloksen idn
     */
    public int getRestultId() {
        return resultId;
    }
    
    /**
     * @return palauttaa tuloksen arvon
     */
    public String getResult() {
        return result;
    }
    
    /**
     * @return palauttaa tuloksen päivämäärän
     */
    public String getPvm() {
        return pvm;
    }
       
    /**
     * @param liikkeenId asetetaan liikeid
     */
    public void setMovementId(int liikkeenId) {
        this.liikeId = liikkeenId;
        
    }
    
    /**
     * Asetetaan tulokselle arvo käyttöliittymän puolella tekstikenttään
     * @param text tuloksen arvo
     */
    public void setTulos(String text) {
        result = text;
        
    }
       
    /**
     * @return palauttaa liikeIdn
     */
    public int getLiikeId() {
        return liikeId;
    }
    /**
     * Rekisteröi tuloksen id-numeron
     * @return tuloksen id
     * @example
     * <pre name="test">
     * Result reeni = new Result();
     * reeni.getRestultId() === 0;
     * reeni.rekisteroi();
     * Result reeni2 = new Result();
     * reeni2.rekisteroi();
     * int n1 = reeni.getRestultId();
     * int n2 = reeni2.getRestultId();
     * n2 === n1+1;
     * </pre>
     */
    public int rekisteroi() {
        this.resultId = seuraavarId;
        seuraavarId++;
        return resultId;
    }
    
    /**
     * Muuttaa tuloksen merkkijonoksi
     */
    @Override
    public String toString() {
        return resultId +"| "+ result + "| "+ 
                pvm+ "|" + liikeId;
    }
    
    /**
     * @param out tietovirta, johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%03d", resultId) +"| "+ result + "| "+
                pvm);
    }
    
    /**
     * Tulostetaan tuloksen tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream (os));
    }
    
    /**
     * @param args ei käytössä
     *
     */
    public static void main(String args[]) {
        Result reeni = new Result();
        Result reeni1 = new Result();
        Result reeni2 = new Result();
        
        reeni.rekisteroi();
        reeni1.rekisteroi();
        reeni2.rekisteroi();
        
        reeni.paivays("1.3.2020");//reenin tulos
        reeni1.paivays("11.3.2020");
        reeni2.paivays("1.10.2020");
        
        reeni.tulosta(System.out);
        reeni1.tulosta(System.out);
        reeni2.tulosta(System.out);
    }

   
}
