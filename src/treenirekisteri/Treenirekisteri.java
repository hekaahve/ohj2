/**
 * 
 */
package treenirekisteri;

import java.util.Collection;

/**
 * @author heini
 * @version 4 Mar 2020
 * @version 26 Apr 2020
 *  Vastuualueet:                                                                                    
 * - huolehtii Movements, Types ja add result -luokkien
 *  v‰lisest‰ yhteistyˆst‰ ja v‰litt‰‰ n‰it‰ tietoja pyydett‰ess‰                                                                            
 * - lukee ja kirjoittaa Treenirekisterin tiedostoon pyyt‰m‰ll‰ apua avustajiltaan                                            
 * 
 */
public class Treenirekisteri {
    
    Types types = new Types();
    Movements movements = new Movements();
    Results results = new Results();
    String kansio = "treeni";
    
    /**
     * Muodostaja
     */
    public Treenirekisteri() {
        //
    }
    
    /**
     * Lis‰‰ uuden tuloksen treenirekisteriin
     * @param tulos tulos joka lis‰t‰‰n
     * @example 
     * <pre name="test">
     * Treenirekisteri rekisteri = new Treenirekisteri();
     * Result reeni = new Result(), reeni2 = new Result();
     * reeni.rekisteroi(); reeni2.rekisteroi();
     * rekisteri.getResults() === 0;
     * rekisteri.lisaa(reeni); rekisteri.getResults() === 1;
     * rekisteri.lisaa(reeni2); rekisteri.getResults() === 2;
     * rekisteri.lisaa(reeni); rekisteri.getResults() === 3;
     * rekisteri.getResults() === 3;
     * rekisteri.annaResult(0) === reeni;
     * rekisteri.annaResult(1) === reeni2;
     * rekisteri.annaResult(2) === reeni;
     * rekisteri.annaResult(3) === null; 
     * rekisteri.lisaa(reeni); rekisteri.getResults() === 4;
     * rekisteri.annaResult(4);     #THROWS IndexOutOfBoundsException 
     * rekisteri.lisaa(reeni); 
     * </pre>
     */
    public void lisaa(Result tulos) {
        results.lisaa(tulos);
        
    }
    
    /**
     * Tallentaa tiedostoon
     * @throws SailoException jos joku menee pieleen
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        
        try {
            results.tallenna(kansio);
        } catch (SailoException e) {
           virhe = virhe + e.getMessage();
        }
        
        try {
            movements.tallenna(kansio);
        } catch (SailoException e) {
            virhe = virhe + e.getMessage();
        }
        
        try {
          types.tallenna(kansio);
        } catch (SailoException e) {
            virhe = virhe + e.getMessage();
        }
                       
        if ( virhe.length() > 0 )
            throw new SailoException(virhe);
        
    }
    
    /**
     * Luetaan tiedostot
     * @throws SailoException jos joku menee pieleen
     */
    public void lueTiedosto() throws SailoException {
        results.lueTiedostosta(kansio);
        movements.lueTiedostosta(kansio);
        types.lueTiedostosta(kansio);
        
    }

    
    /**
     * @param liikeId palauta liike
     * @return arraylist
     */
    public Movement haeLiike(int liikeId) {
        return movements.haeLiike(liikeId);
    }
    
    /**
     * @param typeId palauta tyyppi 
     * @return arraylist
     */
    public Type haeType(int typeId) {
        return types.haeType(typeId);
    }
    
    
    /**
     * @param tyyppi lis‰tt‰v‰ tyyppi
     * @throws SailoException poikkeus
     */
    public void lisaa(Type tyyppi) throws SailoException {
        types.lisaa(tyyppi);
    }
    
    /**
     * @param liike lis‰tt‰v‰ liike
     * 
     * @example 
     * <pre name="test">
     * Treenirekisteri rekisteri = new Treenirekisteri();
     * Movement run = new Movement(); 
     * Movement squat = new Movement();
     * Movement pullUp = new Movement();
     * rekisteri.getMovements() === 0;
     * 
     * rekisteri.getMovements() === 0;
     * rekisteri.lisaa(run); rekisteri.getMovements() === 1;
     * rekisteri.lisaa(squat); rekisteri.getMovements() === 2;
     * rekisteri.lisaa(pullUp); rekisteri.getMovements() === 3;
     * rekisteri.getMovements() === 3;
     * rekisteri.annaMovement(0) === run;
     * rekisteri.annaMovement(1) === squat;
     * rekisteri.annaMovement(2) === pullUp;
     * rekisteri.annaMovement(3) === null; 
     * rekisteri.lisaa(run); rekisteri.getMovements() === 4;
     * rekisteri.annaMovement(4);     #THROWS IndexOutOfBoundsException 
     * rekisteri.lisaa(run); 
     */
    public void lisaa(Movement liike){
        movements.lisaa(liike);
        
    }
    
    /**
     * Poistaa tuloksista tuloksen 
     * @param result tulos jokapoistetaan
     * @return montako tulosta poistettiin
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   Treenirekisteri rekisteri = new Treenirekisteri();
     *   Result result = new Result(); result.rekisteroi();
     *   result.setMovementId(4); result.setTulos("100");
     *   result.paivays("2020-4-4");
     *   rekisteri.lisaa(result);
     *   
     *   Collection<Result> loyt = rekisteri.etsi(result.getLiikeId());
     *   loyt.size() === 1;
     *   rekisteri.poistaResult(result) === 1;
     *   loyt.size() === 1;
     * </pre>
     */
    public int poistaResult(Result result) {
        if ( result == null ) return 0;
        int ret = results.poistaResult(result.getRestultId());  
        return ret; 
    }
    
    /** 
     * Poistaa t‰m‰n liikkeen
     * @param movement poistettava liike
     * @return montako liikett‰ poistettiin
     * 
     */ 
    public int poistaLiike(Movement movement) { 
        if (movement == null) return 0;
        results.poistaLiiketulokset(movement.gettunnusNro());
        int ret = movements.poistaLiike(movement.gettunnusNro());  
        return ret;
    } 

    
    /**
     * Palauttaa rekisterin tuloksien lukum‰‰r‰n
     * @return Results lukum‰‰r‰
     */
    public int getResults() {      
        return results.getLkm();
    }
    
    /**
     * Palauttaa rekisterin liikkeiden lkm
     * @return Movements liikkeiden lukum‰‰r‰
     */
    public int getMovements() {      
        return movements.getLkm();
    }
    
    /**
     * Palauttaa rekisterin liiketyyppien lukum‰‰r‰
     * @return Types tyyppien lukum‰‰r‰
     */
    public int getTypes() {      
        return types.getLkm();
    }
    
    
    /**
    * Palauttaa i:n tuloksen   
    * @param i monesko tulos palautetaan
    * @return viite i:teen tulokseen
    * @throws IndexOutOfBoundsException jos i v‰‰rin
    */
    public Result annaResult(int i) throws IndexOutOfBoundsException {
        return results.anna(i);
    }
    
    /**
     * Palauttaa i:n liikkeen
     * @param i monesko liike palautetaan
     * @return viite i:teen liikkeeseen
     */
    public Movement annaMovement(int i) {
        return movements.anna(i);
    }
    
    /**
     * Palauttaa i:n tyypin
     * @param i moneski tyyppi palautetaan
     * @return viite i:teen tyyppiin
     */
    public Type annaType(int i) {
        return types.anna(i);
    }
    
    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien tulosten viitteet 
     * @param liike hakuehto  
     * @return tietorakenteen lˆytyneist‰ tuloksista 
     * @throws SailoException Jos jotakin menee v‰‰rin
     * @example 
     * <pre name="test">
     *   #THROWS CloneNotSupportedException, SailoException
     *   #import java.io.*;
     *   #import java.util.*;
     *   Treenirekisteri rekisteri = new Treenirekisteri();
     *   Result result = new Result(); result.rekisteroi();
     *   result.setMovementId(4); result.setTulos("100");
     *   result.paivays("2020-4-4");
     *   rekisteri.lisaa(result);
     *   Collection<Result> loytyneet = rekisteri.etsi(4);
     *   loytyneet.size() === 1;
     *   Iterator<Result> it = loytyneet.iterator();
     *   it.next() == result === true; 
     * </pre>
     */ 
    public Collection<Result> etsi(int liike) throws SailoException { 
        return results.etsi(liike); 
    } 

   
    /**
     * @param args ei k‰ytˆss‰'
     */
    public static void main(String[] args) {
        
        var rekisteri = new Treenirekisteri();
        
        Type kg = new Type("kg"), reps = new Type("reps");
        Type cal = new Type("cal"), m = new Type("m");
        
        Movement run = new Movement();
        Movement squat = new Movement();
        
        Result tulos = new Result();
        Result tulos1 = new Result();
        
        kg.rekisteroi();
        reps.rekisteroi();
        cal.rekisteroi();
        m.rekisteroi();
        
        run.rekisteroi();
        squat.rekisteroi();
        
        tulos.rekisteroi();
        tulos1.rekisteroi();
 
        
        run.taytaMov();
        squat.taytaMov();
        
       
        
        //lis‰t‰‰n tyyppi
        try {
            rekisteri.lisaa(kg);
            rekisteri.lisaa(reps);
            rekisteri.lisaa(cal);
            rekisteri.lisaa(m);
            
        } catch (SailoException e) {
            System.err.println(e.getMessage());
        }
        
            rekisteri.lisaa(run);
            rekisteri.lisaa(squat);

            rekisteri.lisaa(tulos);
            rekisteri.lisaa(tulos1);
            
  
        
      System.out.println("=====Treenirekisteri testi=====");
        
        for(int i = 0; i<rekisteri.getResults(); i++) {
            Result res = rekisteri.annaResult(i);
            System.out.println("Tulos paikassa " + i);
            res.tulosta(System.out);

            }
    }

   
   
  

}
