package treenirekisteri.test;
// Generated by ComTest BEGIN
import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import java.util.*;
import treenirekisteri.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2020.04.26 18:32:09 // Generated by ComTest
 *
 */
@SuppressWarnings("all")
public class TreenirekisteriTest {



  // Generated by ComTest BEGIN
  /** testLisaa35 */
  @Test
  public void testLisaa35() {    // Treenirekisteri: 35
    Treenirekisteri rekisteri = new Treenirekisteri(); 
    Result reeni = new Result(), reeni2 = new Result(); 
    reeni.rekisteroi(); reeni2.rekisteroi(); 
    assertEquals("From: Treenirekisteri line: 39", 0, rekisteri.getResults()); 
    rekisteri.lisaa(reeni); assertEquals("From: Treenirekisteri line: 40", 1, rekisteri.getResults()); 
    rekisteri.lisaa(reeni2); assertEquals("From: Treenirekisteri line: 41", 2, rekisteri.getResults()); 
    rekisteri.lisaa(reeni); assertEquals("From: Treenirekisteri line: 42", 3, rekisteri.getResults()); 
    assertEquals("From: Treenirekisteri line: 43", 3, rekisteri.getResults()); 
    assertEquals("From: Treenirekisteri line: 44", reeni, rekisteri.annaResult(0)); 
    assertEquals("From: Treenirekisteri line: 45", reeni2, rekisteri.annaResult(1)); 
    assertEquals("From: Treenirekisteri line: 46", reeni, rekisteri.annaResult(2)); 
    assertEquals("From: Treenirekisteri line: 47", null, rekisteri.annaResult(3)); 
    rekisteri.lisaa(reeni); assertEquals("From: Treenirekisteri line: 48", 4, rekisteri.getResults()); 
    try {
    rekisteri.annaResult(4); 
    fail("Treenirekisteri: 49 Did not throw IndexOutOfBoundsException");
    } catch(IndexOutOfBoundsException _e_){ _e_.getMessage(); }
    rekisteri.lisaa(reeni); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testLisaa129 */
  @Test
  public void testLisaa129() {    // Treenirekisteri: 129
    Treenirekisteri rekisteri = new Treenirekisteri(); 
    Movement run = new Movement(); 
    Movement squat = new Movement(); 
    Movement pullUp = new Movement(); 
    assertEquals("From: Treenirekisteri line: 134", 0, rekisteri.getMovements()); 
    assertEquals("From: Treenirekisteri line: 136", 0, rekisteri.getMovements()); 
    rekisteri.lisaa(run); assertEquals("From: Treenirekisteri line: 137", 1, rekisteri.getMovements()); 
    rekisteri.lisaa(squat); assertEquals("From: Treenirekisteri line: 138", 2, rekisteri.getMovements()); 
    rekisteri.lisaa(pullUp); assertEquals("From: Treenirekisteri line: 139", 3, rekisteri.getMovements()); 
    assertEquals("From: Treenirekisteri line: 140", 3, rekisteri.getMovements()); 
    assertEquals("From: Treenirekisteri line: 141", run, rekisteri.annaMovement(0)); 
    assertEquals("From: Treenirekisteri line: 142", squat, rekisteri.annaMovement(1)); 
    assertEquals("From: Treenirekisteri line: 143", pullUp, rekisteri.annaMovement(2)); 
    assertEquals("From: Treenirekisteri line: 144", null, rekisteri.annaMovement(3)); 
    rekisteri.lisaa(run); assertEquals("From: Treenirekisteri line: 145", 4, rekisteri.getMovements()); 
    try {
    rekisteri.annaMovement(4); 
    fail("Treenirekisteri: 146 Did not throw IndexOutOfBoundsException");
    } catch(IndexOutOfBoundsException _e_){ _e_.getMessage(); }
    rekisteri.lisaa(run); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testPoistaResult159 
   * @throws Exception when error
   */
  @Test
  public void testPoistaResult159() throws Exception {    // Treenirekisteri: 159
    Treenirekisteri rekisteri = new Treenirekisteri(); 
    Result result = new Result(); result.rekisteroi(); 
    result.setMovementId(4); result.setTulos("100"); 
    result.paivays("2020-4-4"); 
    rekisteri.lisaa(result); 
    Collection<Result> loyt = rekisteri.etsi(result.getLiikeId()); 
    assertEquals("From: Treenirekisteri line: 168", 1, loyt.size()); 
    assertEquals("From: Treenirekisteri line: 169", 1, rekisteri.poistaResult(result)); 
    assertEquals("From: Treenirekisteri line: 170", 1, loyt.size()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testEtsi252 
   * @throws CloneNotSupportedException when error
   * @throws SailoException when error
   */
  @Test
  public void testEtsi252() throws CloneNotSupportedException, SailoException {    // Treenirekisteri: 252
    Treenirekisteri rekisteri = new Treenirekisteri(); 
    Result result = new Result(); result.rekisteroi(); 
    result.setMovementId(4); result.setTulos("100"); 
    result.paivays("2020-4-4"); 
    rekisteri.lisaa(result); 
    Collection<Result> loytyneet = rekisteri.etsi(4); 
    assertEquals("From: Treenirekisteri line: 262", 1, loytyneet.size()); 
    Iterator<Result> it = loytyneet.iterator(); 
    assertEquals("From: Treenirekisteri line: 264", true, it.next() == result); 
  } // Generated by ComTest END
}