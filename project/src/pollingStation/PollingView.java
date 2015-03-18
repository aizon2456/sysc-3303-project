package pollingStation;

import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class PollingView implements Observer {


    public void addController(ActionListener controller) {
        // add controllers to buttons and fields
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
