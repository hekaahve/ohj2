package treenirekisteri;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Luokka tulostyypin teidoille treenirekisterissÃ¤.
 * Vastuualueet:         
 * - ei tiedä treenirekisteristä, eikä käyttöliittymästä
 * - tietää tuloksen eli result-kentän 
 * - osaa tarkistaa tietyn kentän oikeellisuuden (syntaksin) 
 * - osaa muuttaa 3|Cal| merkkijonon tuloksen tiedoiksi
 * - osaa antaa merkkijonona i:n kentän tiedot             
 * - osaa laittaa merkkijonon i:neksi kentäksi    
 * @author heini
 * @version 26 Feb 2020
 * @version 26 Apr 2020
 *
 */
public class Type {
    private int Id;
    private String typeNimi;
    
    private static int seuraavaId = 1;
    
    
    /**
     * Alustetaan tyyppi oletusarvoon
     * @param typeNimi nimi
     */
    public Type(String typeNimi) {
        this.typeNimi = typeNimi;
    }
    
    /**
     * @return tyypin nimi
     */
    public String gettypeNimi() {
        return typeNimi;
    }
    
    /**
     * @return Tyypin id-numero
     */
    public int getId() {
        return Id;
    }
    

    /**
     * Antaa tyypille seuraavan id:n.
     * @return tyypin uusi id
     * @example
     * <pre name="test">
     *   Type kg = new Type("kg");
     *   kg.getId() === 0;
     *   kg.rekisteroi();
     *   Type reps = new Type("reps");
     *   reps.rekisteroi();
     *   int n1 = kg.getId();
     *   int n2 = reps.getId();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        Id = seuraavaId;
        seuraavaId++;
        return Id;
    }
    
    /**
     * Muuttaa tyypin merkkijonoksi
     */
    @Override
    public String toString() {
        return Id + " " + typeNimi; 
    }
    
    /**
     * Etsii merkkijonosta typen tiedot
     * @param rivi josta etsitään
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        Id = Mjonot.erota(sb, ' ', Id);
        if ( Id>= seuraavaId ) seuraavaId = Id + 1;
        typeNimi = Mjonot.erota(sb, ' ', typeNimi);
        
    }

    /**
     * Tulostetaan tyypin tiedot
     * @param out tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(String.format("%02d", Id) + "  " + typeNimi);
    }
    
    /**
     * Tulostetaan tyypin tiedot
     * @param os tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }

    /**
     * 
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Type kg = new Type("kg"), reps = new Type("reps");
        Type cal = new Type("cal"), m = new Type("m");
        
        kg.rekisteroi();
        reps.rekisteroi();
        cal.rekisteroi();
        m.rekisteroi();
        
        
        kg.tulosta(System.out);
        reps.tulosta(System.out);
        cal.tulosta(System.out);
        m.tulosta(System.out);
        
    }

   
}
