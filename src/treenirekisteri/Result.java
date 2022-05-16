package treenirekisteri;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * 
 *  Vastuualueet:                                  
 *  - ei tied� treenirekisterist�, eik�             
 * k�ytt�liittym�st�)                              
 * - tiet�� tuloksen lis��misen eli result-kent�n                                               
 * - osaa tarkistaa tietyn kent�n oikeellisuuden (syntaksin)                  
 * - osaa muuttaa 3|3000|Cal| 24.5.2019 merkkijonon
 * tuloksen tiedoiksi                         
 * - osaa antaa merkkijonona i:n kent�n tiedot     
 * - osaa laittaa merkkijonon i:neksi kent�ksi                                                   
 * @author heini
 * @version 12 Mar 2020
 * @version 27 April 2020
 *
 */
public class Result implements Comparable<Result> {
    
    private int resultId;
    private String result;//tuloksen m��r�, esim. 5000 m
    private String pvm;
    private int liikeId; //liikkeen id
    
    private static int seuraavarId = 1;
    
    /**
     * Tuloksen alustus
     */
    public Result() {
        //ei tartte mit��n
    }
    
    /**
     * Vertailija tuloksen lajittelua varten, lajittelee p�iv�m��r�n mukaan
     */
    @Override
    public int compareTo(Result resu) {
        return pvm.compareTo(resu.pvm);
    }

    
    /**
     * Laittaa treeniin sy�tetyn tulostiedon ja p�iv�m��r�n.
     * V�liaikaisesti n�ytt�� aina t�m�n p�iv�n
     * @param text p�iv�m��r� joka vied��n tulokselle
     */
    public void paivays(String text) {
        this.pvm = text;
    }
    
    /**
     * Selvitt�� tuloksen tiedot | erotellusta merkkijonosta
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
     * @return palauttaa tuloksen p�iv�m��r�n
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
     * Asetetaan tulokselle arvo k�ytt�liittym�n puolella tekstikentt��n
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
     * Rekister�i tuloksen id-numeron
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
     * @param args ei k�yt�ss�
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
