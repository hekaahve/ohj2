package treenirekisteri;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *  Vastuualueet:                        
 *  - pit‰‰ yll‰ liikerekisteri‰         
 *  liikkeiden osalta eli osaa lis‰t‰    
 *  poistaa liikkeen                     
 *  - lukee, etsii ja kirjoittaa movement tiedostoon                           
 * @author heini
 * @version 12 Mar 2020
 * @version 27 Apr 2020
 *
 */
public class Movements implements Iterable<Movement> {
    private static final int MAX_MOV = 4;
    private int lkm = 0; 
    private Movement alkiot[];
    private boolean muutettu = false;
    
    /**
     * Luodaan Movements-taulukko, jossa on alkioita, jotka
     * sis‰lt‰v‰t movement-olioita
     */
    public Movements() {
        alkiot = new Movement[MAX_MOV]; //lis‰t‰‰n taulukkoon alkioita
    }
    
    /**
     * Luokka tulosten iteroimiseksi.
     * @author heini
     * @version 21 Apr 2020
     *
     */
    public class MovementsIterator implements Iterator<Movement> {
        private int kohdalla = 0;
        
        /**
         * Onko olemassa viel‰ seuraavaa liikett‰
         * @see java.util.Iterator#hasNext()
         * @return true jos on viel‰ liikkeit‰
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }


        /**
         * Annetaan seuraava liike
         * @return seuraava liike
         * @throws NoSuchElementException jos seuraava alkiota ei en‰‰ ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Movement next() throws NoSuchElementException {
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
     * Palautetaan iteraattori liikkeist‰‰n
     * @return liike iteraattori
     */
    @Override
    public Iterator<Movement> iterator() {
        return new MovementsIterator();
    }
    
    /**
     * Palauttaa liikkeiden lukum‰‰r‰n
     * @return liikkeiden lukum‰‰r‰, jotka ovat movements-taulukossa
     */
    public int getLkm() {
        return lkm;
    }
    
    /**
     * tallennetaan liiketiedosto
     * @param kansio minne kansioon tallennetaan
     * @throws SailoException jos joku menee pieleen
     */
    public void tallenna(String kansio) throws SailoException {
        if ( !muutettu ) return;
        
        File ftied = new File(kansio + "/movements.dat");
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
                Movement movement = anna(i);
                f.println(movement.toString());
            }
           } catch ( FileNotFoundException ex ) {
               throw new SailoException("Tiedoston " + ftied.getName() + "ei aukea");
           } catch ( IOException ex ) {
               throw new SailoException("Tiedoston " + ftied.getName() + "kirjoittamisessa");
           }
        
        muutettu = false;
    }
    
    /**
     * Luetaan liikkeen tiedot tiedostosta
     * @param kansio kansio josta luetaan tiedosto
     * @throws SailoException jos joku menee pieleen
     * 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * #import java.io.*;
     * #import java.util.*;
     *  Movements movements = new Movements();
     *  Movement run = new Movement(); run.rekisteroi(); run.taytaMov();
     *  Movement row = new Movement(); row.rekisteroi(); row.taytaMov();
     *  String kansio = "testitreeni";
     *  String tiedNimi = kansio+ "/movements.dat";
     *  File ftied = new File(tiedNimi);
     *  ftied.delete();
     *  movements.lueTiedostosta(kansio); #THROWS SailoException
     *  movements.lisaa(run);
     *  movements.lisaa(row);
     *  movements.tallenna(kansio); 
     *  movements = new Movements(); //poistetaan vanhat luomalla uusi
     *  movements.lueTiedostosta(kansio); //johon ladataan tiedot tiedostosta
     * 
     *  Iterator<Movement> i = movements.iterator();
     *  i.next().toString() === run.toString();
     *  i.next().toString() === row.toString();
     *  i.hasNext() === false;
     *  
     *  movements.lisaa(run);
     *  movements.tallenna(kansio);
     *  ftied.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String kansio) throws SailoException {
        File ftied = new File(kansio + "/movements.dat");
        try ( BufferedReader fi = new BufferedReader(new FileReader(ftied.getCanonicalPath()))){
            
            while (true) {
                String  rivi = fi.readLine();
                if (rivi == null) break;
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Movement move = new Movement();
                move.parse(rivi); 
                lisaa(move);
            }

        } catch (FileNotFoundException e) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch (IOException e) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
        
    }
    
    /**
     * Lis‰‰ uuden liikkeen (movement) liikkeet-taulukkoon
     * @param liike lis‰tt‰v‰ liike
     * @example
     * <pre name="test">
     * Movements movements = new Movements();
     * Movement run = new Movement(); 
     * Movement squat = new Movement();
     * Movement pullUp = new Movement();
     * movements.getLkm() === 0;
     * movements.lisaa(run); movements.getLkm() === 1;
     * movements.lisaa(squat); movements.getLkm() === 2;
     * movements.lisaa(pullUp); movements.getLkm() === 3;
     * movements.lisaa(pullUp); movements.getLkm() === 4;
     * </pre>
     */
    public void lisaa(Movement liike){
        Movement alkiot2[];
        if (lkm>=alkiot.length) {
            alkiot2 = new Movement[lkm+1];
            for (int i = 0; i<alkiot.length; i++) {
                alkiot2[i] = alkiot[i];
            }
            alkiot = alkiot2;
        }
        alkiot[lkm] = liike;
        lkm++;
        muutettu = true;
    }
    
    /**
     * Poistaa liikkeen
     * @param j poistettava liike
     * @return tosi jos lˆytyi poistettava tietue
     * 
     * #THROWS SailoException  
     * Movements movements = new Movements();
     * Movement run = new Movement(), squat = new Movement(), pullUp = new Movement(); 
     * run.rekisteroi();  squat.rekisteroi(); pullUp.rekisteroi();
     * run.taytaMov(); squat.taytaMov(); pullUp.taytaMov();
     * int id1 = run.gettunnusNro(); 
     * movements.lisaa(run); movements.lisaa(squat); movements.lisaa(pullUp);
     * movements.poistaLiike(id1+1) === 1; 
     * movements.annaId(id1+1) === null; movements.getLkm() === 1; 
     * movements.poista(id1) === 1; movements.getLkm() === 0; 
     */
    public int poistaLiike(int j) {
        int ind = etsiId(j);
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    }

    /**
     * Etsii liikkeen id:n perusteella
     * @param j etsitt‰v‰ liike
     * @return montako liikett‰ lˆytyi
     */
    private int etsiId(int j) {
        for (int i = 0; i < lkm; i++) 
            if (j == alkiot[i].gettunnusNro()) return i; 
        return -1; 
    }
    
    /**
     * Hakee liikkeen id:n perusteella
     * @param id liikkeen id
     * @return lˆydetty liike
     */
    public Movement annaId(int id) { 
        for (Movement movement : this) { 
            if (id == movement.gettunnusNro()) return movement; 
        } 
        return null; 
    } 

    /**
     * Hakee liikkeen liikeId:n perusteella oikeat liikkeet 
     * @param liikeId liikkeen id
     * @return lˆydetyt liikkeet
     * @example
     * <pre name="test">
     * Movements movements = new Movements();
     * Movement run = new Movement(), squat = new Movement(), pullUp = new Movement(); 
     * run.rekisteroi();  squat.rekisteroi(); pullUp.rekisteroi();
     *
     * run.taytaMov(); squat.taytaMov(); pullUp.taytaMov();
     * movements.lisaa(run); movements.lisaa(squat); movements.lisaa(pullUp);
     * 
     *  Movement uusi = new Movement();
     *  uusi.rekisteroi(); uusi.taytaMov();
     *  uusi = movements.haeLiike(run.gettunnusNro());
     *  movements.lisaa(uusi);
     *  
     *  run.getmovNimi() === "run";
     *  uusi.getmovNimi() === "run";
     * </pre>
     */
    public Movement haeLiike(int liikeId) {
        for (int i=0; i<lkm; i++) {
            if (alkiot[i].gettunnusNro() == liikeId) {
                return (alkiot[i]);
            }
        }
        return null;
    }
    
    /**
     * Palauttaa paikassa i olevan liikkeen
     * @param i liikkeen paikka 
     * @return liike (movement)
     * @throws IndexOutOfBoundsException jos annetaan luku, joka ei ole
     * taulukossa 
     */
    public Movement anna(int i) throws IndexOutOfBoundsException {
        if(i<0 || lkm<i)  throw new IndexOutOfBoundsException("Laiton indeksi: " + i);     
        return alkiot[i];
    }
    
    /**
     * @param args ei k‰ytˆss‰
     */
    public static void main(String args[]){
        Movements movements = new Movements();
        Movement run = new Movement(), 
                squat = new Movement(),
                pullUp = new Movement();
        
        run.rekisteroi();
        squat.rekisteroi();
        pullUp.rekisteroi();
        
        run.taytaMov();
        squat.taytaMov();
        pullUp.taytaMov();
        
        movements.lisaa(run);
        movements.lisaa(squat);
        movements.lisaa(pullUp);
                
        System.out.println("=====Movements testi=====");
        
        for(int i = 0; i<movements.getLkm(); i++) {
            Movement movement = movements.anna(i);
            System.out.println("Liike nro " + i);
                movement.tulosta(System.out);

            }
            
        
    }

    
   

   
}
