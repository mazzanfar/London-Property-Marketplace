import java.util.Queue;
import java.util.LinkedList;
import java.util.ArrayList;
import javafx.scene.layout.*;

/**
 * Write a description of class Panes here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class Panes
{
    private ArrayList<Pane> panes;
    private Pane welcomePane, boroughsPane, statisticsPane, mapPane;
    private int pointer;
    
    /**
     * Constructor for objects of class Panes
     */
    public Panes()
    {
        panes = new ArrayList<Pane>();
        
        welcomePane = new WelcomePane();
        boroughsPane = new BoroughsPane();
        statisticsPane = new StatisticsPane();
        mapPane = new MapPane();
        
        panes.add(welcomePane);
        panes.add(boroughsPane);
        panes.add(statisticsPane);
        panes.add(mapPane);
        
        pointer = 0;
    }
    
    public Pane getCurrent()
    {
        return panes.get(pointer);
    }
    
    public Pane getCurrent(int pointer)
    {
        return panes.get(pointer);
    }
    
    public Pane next()
    {
        if(pointer == panes.size() - 1) {
            pointer = 0;
        } else {
            pointer ++;
        }
        
        return getCurrent();
    }
    
    public Pane prev()
    {
        if(pointer == 0) {
            pointer = panes.size() - 1;
        } else {
            pointer --;
        }
        
        return getCurrent();
    }
}
