package pollingStation;

import constants.Constants;

import java.util.Observable;

public class PollingStationServer extends Observable {

    private String delimiter = "|";
    private String endMessage = "\0";
    private PollingStationConnection stationConnection;

    public PollingStationServer(String districtServerAddress, int districtServerPort) {
        stationConnection = new PollingStationConnection(districtServerAddress, districtServerPort);
    }

    /**
     * Register a voter
     * @param firstName: voter's first name
     * @param lastName: voter's last name
     * @param sin: voter's Social Insurance Number
     * @param login: voter's login name
     * @param password: voter's password
     */
    public void register(String firstName, String lastName, String sin, String login, String password) {
        String response = stationConnection.sendMessage(Constants.packetType.REGISTER + delimiter + firstName + delimiter
                + lastName + delimiter + sin + delimiter + login + delimiter + password + endMessage);

        updateObservers(response);
    }

    /**
     * voter login
     * @param login: login name
     * @param password: voter's password
     */
    public void login(String login, String password) {
        String response = stationConnection.sendMessage(Constants.packetType.LOGIN + delimiter + login + delimiter
                + password + endMessage );

        notifyObservers(response);
    }

    /**
     * register vote
     */
    public void vote(String login, String candidateSIN) {
        String response = stationConnection.sendMessage(Constants.packetType.VOTE + delimiter + login + delimiter
                + candidateSIN + endMessage);

        notifyObservers(response);
    }

    private void updateObservers(String response) {
        setChanged();
        notifyObservers(response);
    }
}
