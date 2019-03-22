import com.jfoenix.controls.JFXButton;
import javafx.scene.control.Tooltip;

/**
 * This class is used to represent all of the buttons in the boroughs pane.
 * It extends the JFXButton class (a Button with material design effects) by adding
 * a private string field which registers the borough of the button. It is used
 * to avoid setting the borough based on the button text, as localization of the app
 * would then change the button text and thus crash the application.
 *
 * @author Alexis Dumon, Federico Barbero, Martin Todorov and Maximilian Ghazanfar
 * @version 1.0
 */
public class BoroughButton extends JFXButton
{
    // the name of the borough this button will be representing
    private String boroughName;

    /**
     * Constructor for objects of class BoroughButton
     */
    public BoroughButton(String text, String boroughName)
    {
        super(text);
        this.boroughName = boroughName;
        setTooltip(new Tooltip(boroughName));
    }

    /**
     * @return the name of the borough this button is representing
     */
    public String getBoroughOfButton()
    {
        return boroughName;
    }
}
