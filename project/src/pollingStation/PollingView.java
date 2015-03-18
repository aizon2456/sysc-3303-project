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
    private JButton mainRegister, mainLogin;

    public PollingView(boolean test, String[] Candidates){
        frame = new JFrame();
        frame.setBounds(0,0,500,500);
        frame.setLayout(null);
        frame.setBackground(Color.WHITE);
        frame.setResizable(false);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MainMenuPanel = new JPanel();
        MainMenuPanel.setBounds(0,0,500,500);
        frame.add(MainMenuPanel);
        VotingPanel = new JPanel();
        VotingPanel.setBounds(0,0,500,500);
        frame.add(VotingPanel);
        ButtonGroup candidates = new ButtonGroup();
        JRadioButton candidate[] = new JRadioButton[Candidates.length];
        vote  = new JButton("Vote");
        vote.setBounds(0,0,300,30);
        VotingPanel.add(vote);
        for(int i =0; i<Candidates.length;i++) {
            candidate[i] = new JRadioButton(Candidates[i]);
            candidates.add(candidate[i]);
            candidate[i].setBounds(50,50+(i*30),300,30);
            VotingPanel.add(candidate[i]);
        }

        RegisterPanel = new JPanel();
        RegisterPanel.setBounds(0, 0, 500, 500);
        frame.add(RegisterPanel);
        register = new JButton("Register");
        register.setBounds(0,0,300,30);
        RegisterPanel.add(register);
        //TODO: Create fields for registration

        login = new JButton("Login");

        ErrorPanel = new JPanel();
        ErrorPanel.setBounds(0,0,500,500);
        frame.add(ErrorPanel);
        ErrorMessage = new JTextField("Error");
        //TODO: Add OK button and a display for the error

        mainRegister = new JButton("Register");
        mainLogin    = new JButton("Login");
        mainRegister.setBounds(0,0,100,10);
        mainLogin.setBounds(0, 15, 100, 10);
        MainMenuPanel.add(mainLogin);
        MainMenuPanel.add(mainRegister);

        frame.setVisible(true);
        frame.setEnabled(true);
    }

    private void showMainMenu(){
        MainMenuPanel.setLocation(0, 0);
        ErrorPanel.setLocation(500,500);
        VotingPanel.setLocation(500,500);
        RegisterPanel.setLocation(500,500);
    }
    private void showReigisterMenu(){
        MainMenuPanel.setLocation(500, 500);
        ErrorPanel.setLocation(500,500);
        VotingPanel.setLocation(500,500);
        RegisterPanel.setLocation(0,0);
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
        mainRegister.addActionListener(controller);

        vote.addActionListener(controller);
        register.addActionListener(controller);
        login.addActionListener(controller);
    }

    @Override
    public void update(Observable o, Object arg) {
        if(arg==Constants.returnCodes.SUCCESS){

        }
        else if(arg==Constants.returnCodes.NON_EXISTENT){

        }
        else if(arg==Constants.returnCodes.ALREADY_REGISTERED){

        }
        else if(arg==Constants.returnCodes.WRONG_CREDENTIALS){

        }
        else if(arg==Constants.returnCodes.ALREADY_VOTED){

        }
        else {
            System.out.println("Unknown Response: "+ arg.toString());
        }
    }
}
