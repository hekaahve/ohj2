package treenirekisteri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author heini
 * @version 26 Feb 2020
 * @version 26 Apr 2020
 * Vastuualueet:
 * - pit‰‰ yll‰ tulostyyppirekisteri‰ tulostyyppien osalta
 * eli osaa lis‰t‰ tulostyypin
 * - lukee, etsii ja kirjoittaa types
 * tiedostoon 
 */
public class Types implements Iterable <Type> {
    //private String tiedostonNimi = "";
    private boolean muutettu = false;
    

    
    /** Taulukko tyypeist‰ */
    private final ArrayList<Type> alkiot = new ArrayList<Type>();
    /**
     * Iteraattori tyyppien l‰pik‰ymiseen
     * @return tyyppi-iteraattori
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     *  Types types = new Types();
     *  Type kg = new Type("kg"); kg.rekisteroi();
     *  Type cal = new Type("cal"); cal.rekisteroi();
     *  Type m = new Type("m"); m.rekisteroi();
     *  Type reps = new Type("reps"); reps.rekisteroi();
     *  
     *  types.lisaa(kg);
     *  types.lisaa(cal);
     *  types.lisaa(m);
     *  types.lisaa(reps);
     * 
     *  Iterator<Type> i2 = types.iterator();
     *  i2.next() === kg;
     *  i2.next() === cal;
     *  i2.next() === m;
     *  i2.next() === reps;
     *  i2.next() === kg;  #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  String jnrot[] = {"kg","cal","m","reps"};
     *  
     *  for ( Type type:types ) { 
     *    type.gettypeNimi() === jnrot[n]; 
     *    n++;  
     *  }
     *  
     *  kg.gettypeNimi() === "kg";
     *  
     * </pre>
     */
    @Override
    public Iterator<Type> iterator() {
        return alkiot.iterator();
    } 
    
    /**
     * Types-alustus
     */
    public Types() {
        //ei toistaiseksi mit‰‰n
    }
    
    /** 
     * Palauttaa rekisterin tyyppien lukum‰‰r‰n
     * @return types -lukum‰‰r‰t
     */
    public int getLkm() {
        return alkiot.size();
    }
    
    /**
     * Lis‰‰ uuden tyypin treenirekisterin tietorakenteeseen.
     * Ottaa tyypin omistukseensa. 
     * @param type lis‰tt‰v‰n tyypin viite Huom! tietorakenne muuttuu omistajaksi
     */
    public void lisaa (Type type)  {
        alkiot.add(type);
        muutettu = true;
    }
    
    /**
     * Tallennetaan tyypit tiedostoon
     * @param kansio kansio johon tallennetaan
     * @throws SailoException jos jokin menee pieleen
     */
    public void tallenna(String kansio) throws SailoException {
        if ( !muutettu ) return;
        
        File ftied = new File(kansio + "/types.dat");
        if (!ftied.exists())
            try {
                File polku = new File (kansio);
                polku.mkdirs();
                ftied.createNewFile();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        try ( PrintWriter f = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (int i = 0; i<getLkm(); i++) {
                Type type = anna(i);
                f.println(type.toString());
            }
           } catch ( FileNotFoundException ex ) {
               throw new SailoException("Tiedoston " + ftied.getName() + "ei aukea");
           } catch ( IOException ex ) {
               throw new SailoException("Tiedoston " + ftied.getName() + "kirjoittamisessa");
           }
        
        muutettu = false;
    }
    
    /**
     * Luetaan tyypin tiedot tiedostosta
     * @param kansio josta luetaan
     * @throws SailoException jos joku menee pieleen
     * 
     *  @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.*;
     * #import java.util.*;
     *  Types types = new Types();
     *  Type kg = new Type("kg"); kg.rekisteroi(); 
     *  Type cal = new Type("cal"); cal.rekisteroi(); 
     *  String kansio = "testitreeni";
     *  String tiedNimi = kansio+ "/types.dat";
     *  File ftied = new File(tiedNimi);
     *  ftied.delete();
     *  types.lueTiedostosta(kansio); #THROWS SailoException
     *  types.lisaa(kg);
     *  types.lisaa(cal);
     *  types.tallenna(kansio); 
     *  types = new Types(); //poistetaan vanhat luomalla uusi
     *  types.lueTiedostosta(kansio); //johon ladataan tiedot tiedostosta
     *   
     *  types.lisaa(kg);
     *  types.tallenna(kansio);
     *  ftied.delete() === true;
     * 
     */
    public void lueTiedostosta(String kansio) throws SailoException {
        File ftied = new File(kansio + "/types.dat");
        try ( BufferedReader fi = new BufferedReader(new FileReader(ftied.getCanonicalPath()))){
            
            while (true) {
                String  rivi = fi.readLine();
                if (rivi == null) break;
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Type type = new Type("reps");
                type.parse(rivi); 
                lisaa (type);
            }

        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException e) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
        
    }
    
    /**
     * @param typeId hakee liikkeen typen idn types-listasta
     * @return tyypit
     * @example
     * <pre name="test">
     * 
     * Types types = new Types();
     *  
     *  Type kg = new Type("kg"); kg.rekisteroi();
     *  Type cal = new Type("cal"); cal.rekisteroi();
     *  Type m = new Type("m"); m.rekisteroi();
     *  Type reps = new Type("reps"); reps.rekisteroi();
     *  
     *  types.lisaa(kg);
     *  types.lisaa(cal);
     *  types.lisaa(m);
     *  types.lisaa(reps);
     *  
     *  Type uusi = new Type("");
     *  uusi.rekisteroi();
     *  uusi = types.haeType(kg.getId());
     *  types.lisaa(uusi);
     *  
     *  types.anna(4) === uusi;
     *  
     *  kg.gettypeNimi() === "kg";
     *  uusi.gettypeNimi() === "kg";
     * </pre>
     */
    public Type haeType(int typeId) {
       for (int i = 0; i<alkiot.size(); i++) {
           if (alkiot.get(i).getId() == typeId) return alkiot.get(i);
       }
        return null;
    }
    
    /**
     * Palauttaa viitteen i:teen tyyppiin
     * @param i monennenko tyypin viite halutaan
     * @return tyypin viite, jonka indeksi on i
     */
    public Type anna(int i) {     
        return alkiot.get(i);
    }
    

    
    /**
     * @param args ei k‰ytˆss‰
     */
    public static void main(String args[]){
        Types types = new Types();
        Type kg = new Type("kg"), reps = new Type("reps");
        Type cal = new Type("cal"), m = new Type("m");
        
        
        kg.rekisteroi();
        reps.rekisteroi();
        cal.rekisteroi();
        m.rekisteroi();
        
        types.lisaa(kg);
        types.lisaa(reps);
        types.lisaa(cal);
        types.lisaa(m);
        
        Type uusi = new Type("");
        uusi = types.haeType(3);
        types.lisaa(uusi);
        
        System.out.println(uusi.gettypeNimi());
               
        
        System.out.println("=====Types testi=====");
        
        for(int i = 0; i<types.getLkm(); i++) {
            Type type = types.anna(i);
            System.out.println("Tyyppi nro " + i);
                type.tulosta(System.out);

            }
              
    }
    
   
    
}
