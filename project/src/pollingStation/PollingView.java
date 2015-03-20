package pollingStation;

import constants.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class PollingView implements Observer {
    private JFrame frame;
    private JPanel VotingPanel, MainMenuPanel, RegisterPanel, ErrorPanel;
    private JTextField ErrorMessage;
    private JButton register, login, vote;
    private JButton mainRegistration, mainLogin;
    private JTextField firstNameField, lastNameField, sinField, loginField, passwordField;
    private int state = 0; // TODO: use this to indicate which menu we are looking at

    private JPanel registrationPanel;
    private Container mainPanel;

    public PollingView(boolean test, String[] Candidates){
        //Create and set up the window.
        frame = new JFrame("Polling Station");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setMinimumSize(new Dimension(400, 400));

        createMainPanel();
        createRegistrationPanel();


        //Display the window.
//        frame.pack();
        frame.setVisible(true);
        showMainMenu();
//        showReigisterMenu();
    }

    private void createMainPanel() {

        //Set up the content pane.
        mainPanel = new Container();
        SpringLayout mainLayout = new SpringLayout();
        mainPanel.setLayout(mainLayout);

        mainLogin       = new JButton("Login");
        mainRegistration = new JButton("Registration");

        mainPanel.add(mainLogin);
        mainPanel.add(mainRegistration);

        //Adjust constraints for the label so it's at (5,5).
        mainLayout.putConstraint(SpringLayout.WEST, mainLogin,
                150,
                SpringLayout.WEST, mainPanel);
        mainLayout.putConstraint(SpringLayout.NORTH, mainLogin,
                150,
                SpringLayout.NORTH, mainPanel);

        //Adjust constraints for the text field so it's at
        //(<label's right edge> + 5, 5).
        mainLayout.putConstraint(SpringLayout.WEST, mainRegistration,
                130,
                SpringLayout.WEST, mainPanel);
        mainLayout.putConstraint(SpringLayout.NORTH, mainRegistration,
                125,
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
        registrationPanel.add(register);

    }

    public void showMainMenu(){
        frame.setContentPane(mainPanel);
        frame.pack();
    }

    public void showReigisterMenu(){
        frame.setContentPane(registrationPanel);
        frame.pack();
    }

    private void showVoteMenu(){
        MainMenuPanel.setLocation(500, 500);
        ErrorPanel.setLocation(500,500);
        VotingPanel.setLocation(0,0);
        RegisterPanel.setLocation(500,500);
    }
    private void showError(String error){
        ErrorMessage.setText(error);
        MainMenuPanel.setLocation(500, 500);
        ErrorPanel.setLocation(0,0);
        VotingPanel.setLocation(500,500);
        RegisterPanel.setLocation(500,500);
    }


    public void addController(ActionListener controller) {

        mainLogin.addActionListener(controller);
        mainRegistration.addActionListener(controller);

//        vote.addActionListener(controller);
        register.addActionListener(controller);
//        login.addActionListener(controller);
    }

    public String[] getRegistrationFields() {

        return new String[] {firstNameField.getText(), lastNameField.getText(),
                sinField.getText(), loginField.getText(), passwordField.getText()};
    }

    public String[] loginPopup() {
        JTextField username = new JTextField();
        JTextField password = new JPasswordField();
        Object[] message = {
                "Username:", username,
                "Password:", password
        };

        JOptionPane.showConfirmDialog(null, message, "Login", JOptionPane.OK_CANCEL_OPTION);
        return new String[] {username.getText(), password.getText()};
    }

    @Override
    public void update(Observable o, Object arg) {

        if(arg==Constants.returnCodes.SUCCESS){

        } else if(arg == Constants.returnCodes.NON_EXISTENT){

        } else if(arg == Constants.returnCodes.ALREADY_REGISTERED){

        } else if(arg == Constants.returnCodes.WRONG_CREDENTIALS){

        } else if(arg == Constants.returnCodes.ALREADY_VOTED) {

        } else if (arg == Constants.packetType.NO_RESPONSE) {
            System.out.println("NO RESPONSE");
        } else {
            System.out.println("Unknown Response: "+ arg.toString());
        }
    }
}
