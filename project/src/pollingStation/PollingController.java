package pollingStation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PollingController implements ActionListener {

    PollingStationServer model;

    public PollingController() {

    }

    public void addModel(PollingStationServer model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String option = e.getActionCommand();

        System.out.println("Action Performed: " + option);
    }
}
