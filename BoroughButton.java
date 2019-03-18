import com.jfoenix.controls.JFXButton;

/**
 * Write a description of class BoroughButton here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class BoroughButton extends JFXButton
{
    private String boroughName;
    
    public BoroughButton(String btnText, String boroughName)
    {
        super(btnText);
        this.boroughName = boroughName;
    }
    
    public String getBoroughName()
    {
        return boroughName;
    }
}