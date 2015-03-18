package pollingStation;

import java.util.Observable;
import java.util.Observer;

public class PollingView implements Observer {


    public void addController(PollingController controller) {
        // add controllers to buttons and fields
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
