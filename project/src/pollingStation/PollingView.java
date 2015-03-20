package pollingStation;

import constants.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

public class PollingView implements Observer {
    private JFrame frame;
    private JPanel votingPanel, ErrorPanel;
    private JTextField ErrorMessage;
    private JButton register, vote, cancel;
    private JButton mainRegistration, mainLogin;
    private JTextField firstNameField, lastNameField, sinField, loginField, passwordField;

    private  ButtonGroup candidateRadioButtons;

    private JPanel registrationPanel;
    private Container mainPanel;

    public PollingView(boolean test){
        //Create and set up the window.
        frame = new JFrame("Polling Station");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 400));

        vote = new JButton("Vote");
        cancel = new JButton("Cancel");

        createMainPanel();
        createRegistrationPanel();

        frame.setVisible(true);
//        showMainMenu();
        showRegisterMenu();
//        showVotingMenu();
    }

    private void createMainPanel() {

        //Set up the content pane.
        mainPanel = new Container();
        SpringLayout mainLayout = new SpringLayout();
        mainPanel.setLayout(mainLayout);

        mainLogin = new JButton("Login");
        mainRegistration = new JButton("Registration");

        mainPanel.add(mainLogin);
        mainPanel.add(mainRegistration);

        mainLayout.putConstraint(SpringLayout.WEST, mainLogin, 150,
                SpringLayout.WEST, mainPanel);
        mainLayout.putConstraint(SpringLayout.NORTH, mainLogin, 150,
                SpringLayout.NORTH, mainPanel);

        mainLayout.putConstraint(SpringLayout.WEST, mainRegistration, 130,
                SpringLayout.WEST, mainPanel);
        mainLayout.putConstraint(SpringLayout.NORTH, mainRegistration, 125,
                SpringLayout.NORTH, mainPanel);
    }

    private void createRegistrationPanel() {

        firstNameField = new JTextField();
        lastNameField = new JTextField();
        sinField = new JTextField();
        loginField = new JTextField();
        passwordField = new JTextField();

        String[] labels = {"First Name: ", "Last Name: ", "SIN: ", "Login: ", "Password: "};
        JTextField[] fields = {firstNameField, lastNameField, sinField, loginField, passwordField};

        //Create and populate the panel.
        registrationPanel = new JPanel(new GridLayout(6, 2));
        for (int i = 0; i < labels.length; i++) {
            JLabel l = new JLabel(labels[i]);
            registrationPanel.add(l);
            l.setLabelFor(fields[i]);
            registrationPanel.add(fields[i]);
        }

        register = new JButton("Register");
        registrationPanel.add(cancel);
        registrationPanel.add(register);
    }

    private void showVotingMenu(String[] candidates) { //String[][] candidates

//        String[][] candidates = new String[][] {
//                {"first last name", "sin"},
//                {"first last name", "sin"},
//                {"first last name", "sin"},
//                {"first last name", "sin"},
//                {"first last name 2", "sin2"}
//        };

        votingPanel = new JPanel(new GridLayout(candidates.length + 1, 2));

//        this.candidates = new JRadioButton[candidates.length];
        candidateRadioButtons = new ButtonGroup();
        for (int i = 2; i < candidates.length; i += 2) { // start after returnCode
            JLabel l = new JLabel(candidates[i - 1]);
            votingPanel.add(l);
            JRadioButton btn = new JRadioButton();
            btn.setActionCommand(candidates[i]);
//            this.candidates[i] = btn;
            candidateRadioButtons.add(btn);
            l.setLabelFor(btn);
            votingPanel.add(btn);
        }

        votingPanel.add(cancel);
        votingPanel.add(vote);

        frame.setContentPane(votingPanel);
        frame.pack();
    }

    public void showMainMenu(){
        frame.setContentPane(mainPanel);
        frame.pack();
    }

    public void showRegisterMenu(){
        frame.setContentPane(registrationPanel);
        frame.pack();
    }

    private void showVoteMenu(){
        ErrorPanel.setLocation(500,500);
    }
    private void showError(String error){
        ErrorMessage.setText(error);
        ErrorPanel.setLocation(0,0);
    }


    public void addController(ActionListener controller) {

        mainLogin.addActionListener(controller);
        mainRegistration.addActionListener(controller);

        vote.addActionListener(controller);
        register.addActionListener(controller);
        cancel.addActionListener(controller);
    }

    public String[] getRegistrationFields() {

        return new String[] {firstNameField.getText(), lastNameField.getText(),
                sinField.getText(), loginField.getText(), passwordField.getText()};
    }

    public String getSelectedCandidate() {
        if (candidateRadioButtons == null) return "";
        return candidateRadioButtons.getSelection().getActionCommand();
    }

    public String[] loginPopup() {
        JTextField username = new JTextField();
        JTextField password = new JPasswordField();
        Object[] message = {
                "Username:", username,
                "Password:", password
        };

        JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        if (username.getText().equals("") || password.getText().equals(""))
            return new String [] {"cancel", "cancel"};

        return new String[] {username.getText(), password.getText()};
    }

    private void displayMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    @Override
    public void update(Observable o, Object arg) {

        String[] arguments = (String[])arg;

        if (arguments[0].equals(Constants.returnCodes.LOGIN_SUCCESS.name())) {
            showVotingMenu(arguments);
        } else if (arguments[0].equals(Constants.returnCodes.REG_SUCCESS.name())) {
            displayMessage("Registration Successful");
            showMainMenu();
        }else if (arguments[0].equals(Constants.returnCodes.VOTE_SUCCESS.name())) {
            displayMessage("Vote Successful");
            showMainMenu();
        } else if (arguments[0].equals(Constants.returnCodes.NON_EXISTENT.name())) {
            displayMessage("Voter does not exist");
        } else if (arguments[0].equals(Constants.returnCodes.ALREADY_REGISTERED.name())) {
            displayMessage("Voter already registered");
        } else if (arguments[0].equals(Constants.returnCodes.WRONG_CREDENTIALS.name())) {
            displayMessage("Wrong Registration Credentials");
        } else if (arguments[0].equals(Constants.returnCodes.ALREADY_VOTED.name())) {
            displayMessage("Voter already submitted vote");
        } else if (arguments[0].equals(Constants.returnCodes.LOGIN_EXISTS.name())) {
            displayMessage("Login name is already in use");
        } else if (arguments[0].equals(Constants.packetType.NO_RESPONSE.name())) {
            System.out.println("NO RESPONSE");
        } else {
            System.out.println("Unknown Response: " + Arrays.toString(arguments));
        }
    }
}
