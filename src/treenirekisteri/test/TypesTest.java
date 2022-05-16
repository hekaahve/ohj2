package treenirekisteri.test;
// Generated by ComTest BEGIN
import treenirekisteri.*;
import java.util.*;
import java.io.*;
import static org.junit.Assert.*;
import org.junit.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2020.04.26 18:26:19 // Generated by ComTest
 *
 */
@SuppressWarnings("all")
public class TypesTest {



  // Generated by ComTest BEGIN
  /** testIterator37 */
  @Test
  public void testIterator37() {    // Types: 37
    Types types = new Types(); 
    Type kg = new Type("kg"); kg.rekisteroi(); 
    Type cal = new Type("cal"); cal.rekisteroi(); 
    Type m = new Type("m"); m.rekisteroi(); 
    Type reps = new Type("reps"); reps.rekisteroi(); 
    types.lisaa(kg); 
    types.lisaa(cal); 
    types.lisaa(m); 
    types.lisaa(reps); 
    Iterator<Type> i2 = types.iterator(); 
    assertEquals("From: Types line: 53", kg, i2.next()); 
    assertEquals("From: Types line: 54", cal, i2.next()); 
    assertEquals("From: Types line: 55", m, i2.next()); 
    assertEquals("From: Types line: 56", reps, i2.next()); 
    try {
    assertEquals("From: Types line: 57", kg, i2.next()); 
    fail("Types: 57 Did not throw NoSuchElementException");
    } catch(NoSuchElementException _e_){ _e_.getMessage(); }
    int n = 0; 
    String jnrot[] = { "kg","cal","m","reps"} ; 
    for ( Type type:types ) {
    assertEquals("From: Types line: 63", jnrot[n], type.gettypeNimi()); 
    n++; 
    }
    assertEquals("From: Types line: 67", "kg", kg.gettypeNimi()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testLueTiedostosta139 
   * @throws SailoException when error
   */
  @Test
  public void testLueTiedostosta139() throws SailoException {    // Types: 139
    Types types = new Types(); 
    Type kg = new Type("kg"); kg.rekisteroi(); 
    Type cal = new Type("cal"); cal.rekisteroi(); 
    String kansio = "testitreeni"; 
    String tiedNimi = kansio+ "/types.dat"; 
    File ftied = new File(tiedNimi); 
    ftied.delete(); 
    try {
    types.lueTiedostosta(kansio); 
    fail("Types: 150 Did not throw SailoException");
    } catch(SailoException _e_){ _e_.getMessage(); }
    types.lisaa(kg); 
    types.lisaa(cal); 
    types.tallenna(kansio); 
    types = new Types();  //poistetaan vanhat luomalla uusi
    types.lueTiedostosta(kansio);  //johon ladataan tiedot tiedostosta
    types.lisaa(kg); 
    types.tallenna(kansio); 
    assertEquals("From: Types line: 159", true, ftied.delete()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testHaeType188 */
  @Test
  public void testHaeType188() {    // Types: 188
    Types types = new Types(); 
    Type kg = new Type("kg"); kg.rekisteroi(); 
    Type cal = new Type("cal"); cal.rekisteroi(); 
    Type m = new Type("m"); m.rekisteroi(); 
    Type reps = new Type("reps"); reps.rekisteroi(); 
    types.lisaa(kg); 
    types.lisaa(cal); 
    types.lisaa(m); 
    types.lisaa(reps); 
    Type uusi = new Type(""); 
    uusi.rekisteroi(); 
    uusi = types.haeType(kg.getId()); 
    types.lisaa(uusi); 
    assertEquals("From: Types line: 207", uusi, types.anna(4)); 
    assertEquals("From: Types line: 209", "kg", kg.gettypeNimi()); 
    assertEquals("From: Types line: 210", "kg", uusi.gettypeNimi()); 
  } // Generated by ComTest END
}