package pollingStation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PollingController implements ActionListener {

    private static final int USERNAME = 0;
    private static final int PASSWORD = 1;

    private PollingStationServer model;
    private PollingView view;

    public PollingController() {

    }

    public void addModel(PollingStationServer model) {
        this.model = model;
    }

    public void addView(PollingView view) {
        this.view = view;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String option = e.getActionCommand();

        System.out.println("Action Performed: " + option);

        if (option.equals("Login")) {
            String[] loginInformation = view.loginPopup();
            model.login(loginInformation[USERNAME], loginInformation[PASSWORD]);
        }
    }
}
