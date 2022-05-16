package treenirekisteri;

/**
 * @author heini
 * @version 4 Mar 2020
 * K‰sitetll‰‰n poikkeusviestit
 */
public class SailoException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * @param viesti ilmoittaa viestin, jos 
     */
    public SailoException(String viesti) {
        super(viesti);
    }
    

}
