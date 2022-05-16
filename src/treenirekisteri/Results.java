package treenirekisteri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Treenirekisterin tulokset, joka osaa mm. lisätä uuden tuloksen, poistaa ja etsiä
 * 
 * @author heini
 * @version 12 Mar 2020
 * @version 26 Apr 2020
 *
 */
public class Results implements Iterable<Result> {
    
    private static final int MAX_RESULTS = 4;
    private int lkm = 0;
    private Result alkiot[];
    private boolean muutettu = false;
    
    /**
     * Alkioiden alustus
     */
    public Results() {
        alkiot = new Result[MAX_RESULTS];
    }
    
    
    /**
     * Luokka tulosten iteroimiseksi.
     * @author heini
     * @version 21 Apr 2020
     *
     */
    public class ResultsIterator implements Iterator<Result> {
        private int kohdalla = 0;


        /**
         * Onko olemassa vielä seuraavaa tulosta
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä tuloksia
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava tulos
         * @return seuraava tulos
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Result next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
            return anna(kohdalla++);
        }


        /**
         * Tuhoamista ei ole toteutettu
         * @throws UnsupportedOperationException aina
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Me ei poisteta");
        }
    }
    
    /**
     * Palautetaan iteraattori tuloksistaan
     * @return tulos iteraattori
     */
    @Override
    public Iterator<Result> iterator() {
        return new ResultsIterator();
    }
    
    /**
     * tallennetaan liiketiedosto
     * @param kansio kansio
     * @throws SailoException jos joku menee pieleen
     * 
     */
    public void tallenna(String kansio) throws SailoException {
        if ( !muutettu ) return; 
        
        File ftied = new File(kansio +"/results.dat");
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
                Result result = anna(i);
                f.println(result.toString());
            }
           } catch ( FileNotFoundException ex ) {
               throw new SailoException("Tiedoston " + ftied.getName() + "ei aukea");
           }catch ( IOException ex ) {
               throw new SailoException("Tiedoston " + ftied.getName() + "kirjoittamisessa ongelma");
           }
        
        muutettu = false;
    }
    
    /**
     * Luetaan tuloksen tiedot tiedostosta
     * @param kansio josta luetaan tiedosto
     * @throws SailoException jos joku menee pieleen
     *  @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.*;
     * #import java.util.*;
     * Results results = new Results();
     * Result reeni = new Result();
     * Result reeni1 = new Result();
     * 
     * reeni.rekisteroi();
     * reeni1.rekisteroi();
     *
     * reeni.paivays("1.3.2020");
     * reeni1.paivays("11.3.2020");
     * 
     *  String kansio = "testitreeni";
     *  String tiedNimi = kansio+ "/results.dat";
     *  File ftied = new File(tiedNimi);
     *  ftied.delete();
     *  results.lueTiedostosta(kansio); #THROWS SailoException
     *  results.lisaa(reeni);
     *  results.lisaa(reeni1);
     *  results.tallenna(kansio); 
     *  results = new Results(); //poistetaan vanhat luomalla uusi
     *  results.lueTiedostosta(kansio); //johon ladataan tiedot tiedostosta
     * 
     *  Iterator<Result> i = results.iterator();
     *  i.next().toString() === reeni.toString();
     *  i.next().toString() === reeni1.toString();
     *  i.hasNext() === false;
     *  
     *  results.lisaa(reeni);
     *  results.tallenna(kansio);
     *  ftied.delete() === true;
     */
    public void lueTiedostosta(String kansio) throws SailoException {       
        File ftied = new File(kansio + "/results.dat");
        try ( BufferedReader fi = new BufferedReader(new FileReader(ftied.getCanonicalPath()))){
            
            while (true) {
                String rivi = fi.readLine();
                if (rivi == null) break;
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Result result = new Result();
                result.parse(rivi); 
                lisaa(result);
            }

        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException e) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
        
    }
    
    /**
     * Etsitään tulokset, jotka sisältävät tiettyjä liikkeitä
     * @param liike liike, jonka perusteella halutaan etsiä tulos
     * @return tietorakenteen löytyneistä tuloksista
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.*;
     * #import java.util.*;
     * Results results = new Results();
     * Result reeni = new Result(); reeni.parse("1| 1000| 2020-04-16|4");
     * Result reeni2 = new Result(); reeni2.parse("2| 50000| 2020-04-17|7");
     * Result reeni3 = new Result(); reeni3.parse("3| 45| 2020-04-20|4");
     * results.lisaa(reeni); results.lisaa(reeni2); results.lisaa(reeni3);
     * 
     * List<Result> loytyneet;
     * loytyneet = (List<Result>)results.etsi(7);
     * loytyneet.get(0) == reeni2 === true; 
     * loytyneet.size() === 1;
     * 
     * loytyneet = (List<Result>)results.etsi(4);
     * loytyneet.get(0) == reeni === true; 
     * loytyneet.get(1) == reeni3 === true; 
     * loytyneet.size() === 2;
     * 
     * </pre>
     */
    public Collection<Result> etsi(int liike) {
        List<Result> loytyneet = new ArrayList<Result>(); 
        for (Result result : this) {
            if (result.getLiikeId() == liike || liike == -1)
                loytyneet.add(result);
        }
        Collections.sort(loytyneet);
        return loytyneet;
    }
    

    /** 
     * Poistaa tuloksen jolla on valittu tunnusnumero  
     * @param id poistettavan tuloksen tunnusnumero 
     * @return 1 jos poistettiin, 0 jos ei löydy 
     * #THROWS SailoException  
     * Results results = new Results();
     * Result reeni = new Result(); reeni.rekisteroi();
     * Result reeni1 = new Result(); reeni1.rekisteroi();
     * reeni.paivays("1.3.2020");
     * reeni1.paivays("11.3.2020");
     * 
     * int id1 = reeni.getRestultId(); 
     * results.lisaa(reeni); results.lisaa(reeni1); 
     * results.poista(id1+1) === 1; 
     * results.annaId(id1+1) === null; results.getLkm() === 1; 
     * results.poista(id1) === 1; results.getLkm() === 0; 
     * </pre> 
     *  
     */ 
    public int poistaResult(int id) { 
        int ind = etsiId(id);
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    } 
    
    
    /**
     * Poistaa kaikki tulokset, jossa on kyseinen poistettava liike
     * @param id poistettavan liikkeen id
     * 
     * #THROWS SailoException  
     * Results results = new Results();
     * Result reeni = new Result(); reeni.rekisteroi();
     * Result reeni1 = new Result(); reeni1.rekisteroi();
     * reeni.paivays("1.3.2020");
     * reeni1.paivays("11.3.2020");
     * reeni.setMovementId(1); reeni1.setMovementId(2);
     * 
     * int id1 = reeni.getLiikeId(); 
     * results.lisaa(reeni); results.lisaa(reeni1); 
     * results.poistaLiiketulokset(id1+1); 
     * results.etsi(id1+1) === null; results.getLkm() === 1; 
     * results.poista(id1); results.getLkm() === 0; 
     * </pre> 
     *  
     */ 
    public void poistaLiiketulokset(int id) {
        Collection<Result> tulokset;
        tulokset = etsi(id);

        int ind = tulokset.size();
        if (ind <= 0) return; 
        int j = 0;
            for (int i = 0; i < lkm; i++) {
                
                if (alkiot[i].getLiikeId() != id) {
                    Result apu = alkiot[j];
                    alkiot[j] = alkiot[i];
                    alkiot[i] = apu;
                    j++;
                }      
            }
            
            for (int i = 0; i<lkm; i++) {
                if(alkiot[i].getLiikeId() == id) {
                    alkiot[i] = null;
                }
            }
            
       lkm = j;
       muutettu = true; 
    }

    /** 
     * Etsii tuloksen id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return löytyneen tuloksen indeksi tai -1 jos ei löydy 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Results results = new Results();
     * Result reeni = new Result(); reeni.rekisteroi();
     * Result reeni1 = new Result(); reeni1.rekisteroi();
     *
     * reeni.paivays("1.3.2020");
     * reeni1.paivays("11.3.2020");
     * 
     * int id1 = reeni.getRestultId(); 
     * results.lisaa(reeni); results.lisaa(reeni1); 
     * results.etsiId(id1+1) === 1; 
     * results.etsiId(id1) === 0; 
     * </pre> 
     */ 
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getRestultId()) return i; 
        return -1; 
    } 
    
    
    /**
     * Etsii tuloksen id:n perusteella
     * @param id tuloksen id
     * @return tulos
     * @example
     * <pre name="test">
     * Results results = new Results();
     * Result reeni = new Result(); reeni.rekisteroi();
     * Result reeni1 = new Result(); reeni1.rekisteroi();
     *
     * reeni.paivays("1.3.2020");
     * reeni1.paivays("11.3.2020");
     * int id1 = reeni.getRestultId(); 
     * results.lisaa(reeni); results.lisaa(reeni1); 
     * results.annaId(id1  ) == reeni === true; 
     * results.annaId(id1+1) == reeni1 === true; 
     * </pre>
     */
    public Result annaId(int id) { 
        for (Result result : this) { 
            if (id == result.getRestultId()) return result; 
        } 
        return null; 
    } 
    
    /**
     * Lisätään uusi tulos
     * @param tulos lisättävä tulos
     * @example
     * <pre name="test">
     * Results results = new Results();
     * Result reeni = new Result(); 
     * Result reeni2 = new Result();
     * Result reeni3 = new Result();
     * results.getLkm() === 0;
     * results.lisaa(reeni); results.getLkm() === 1;
     * results.lisaa(reeni2); results.getLkm() === 2;
     * results.lisaa(reeni3); results.getLkm() === 3;
     * results.lisaa(reeni); results.getLkm() === 4;
     * </pre>
     */
    public void lisaa(Result tulos){
        Result[] alkiot2; 
        if (lkm>= alkiot.length){
            alkiot2 = new Result[lkm+1];
            for (int i = 0; i<alkiot.length; i++) {
                alkiot2[i] = alkiot[i];
            }
            alkiot = alkiot2;    
        }
        alkiot[lkm] = tulos;
        lkm++;
        muutettu = true;
    }
    
    /**
     * Palauttaa paikassa i:n olevan tuloksen
     * @param i tuloksen paikka
     * @return Result tulos
     * @throws IndexOutOfBoundsException Jos on laiton indeksi
     */
    public Result anna(int i)throws IndexOutOfBoundsException {
        if (i < 0 || lkm < i ) throw new IndexOutOfBoundsException("Laiton indeksi");
        return alkiot[i];
    }
    
    /**
     * @return tuloksen lukumäärä
     */
    public int getLkm() {
        return lkm;
    }
    /**
     * @param args ei käytössä
     */
   public static void main (String args [] ) {      
       Results results = new Results();
       
       Result reeni = new Result();
       Result reeni1 = new Result();
       Result reeni2 = new Result();
       Result reeni3 = new Result();
       
       reeni.rekisteroi();
       reeni1.rekisteroi();
       reeni2.rekisteroi();
       reeni3.rekisteroi();
       
       reeni.paivays("1.3.2020");//reenin tulos
       reeni1.paivays("11.3.2020");
       reeni2.paivays("1.10.2020");  
       reeni3.paivays("1.10.2020");  
       
       reeni.setMovementId(2);
       reeni1.setMovementId(2);
       reeni2.setMovementId(2);
       reeni3.setMovementId(4);
       
       reeni.setTulos("90");
       reeni1.setTulos("91");
       reeni2.setTulos("92");
       reeni3.setTulos("92");
     
       results.lisaa(reeni);
       results.lisaa(reeni1);
       results.lisaa(reeni2);
       results.lisaa(reeni3);
       
       for(int i = 0; i<results.getLkm(); i++) {
           Result result = results.anna(i);
           System.out.println("Tulos nro " + i);
               result.tulosta(System.out);

           }

       results.poistaLiiketulokset(2);
       System.out.println(results.getLkm());
               
       System.out.println("=====Results testi=====");
       
       for(int i = 0; i<results.getLkm(); i++) {
           Result result = results.anna(i);
           System.out.println("Tulos nro " + i);
               result.tulosta(System.out);

           }
    
    }



   

   

}
