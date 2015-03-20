package pollingStation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class PollingController implements ActionListener {

    private static final int USERNAME = 0;
    private static final int PASSWORD = 1;

    private PollingStation model;
    private PollingView view;

    public PollingController() {

    }

    public void addModel(PollingStation model) {
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
            if (loginInformation[0].equals("cancel")) return;
            model.login(loginInformation[USERNAME], loginInformation[PASSWORD]);
        } else if (option.equals("Registration")) {
            view.showRegisterMenu();
        } else if (option.equals("Register")) {
            String[] fields = view.getRegistrationFields();
            System.out.println(Arrays.toString(fields));
            model.register(fields[0], fields[1], fields[2], fields[3], fields[4]);
        } else if (option.equals("Vote")) {
            String selectedCandidate = view.getSelectedCandidate();
            model.voteFor(selectedCandidate);
        } else if (option.equals("Cancel")) {
            view.showMainMenu();
        } else if (option.equals("Debug")) {
           	model.testParse();
        }
    }
}
