package pollingStation;

import constants.Constants;

import java.util.Observable;

public class PollingStationServer extends Observable {

    private String delimiter = String.valueOf(Constants.PACKET_DELIMITER);
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
    public String register(String firstName, String lastName, String sin, String login, String password) {
        String response = stationConnection.sendMessage(Constants.packetType.REGISTER + delimiter + firstName + delimiter
                + lastName + delimiter + sin + delimiter + login + delimiter + password + Constants.PACKET_END);

        updateObservers(response);
        return response;
    }

    /**
     * voter login
     * @param login: login name
     * @param password: voter's password
     */
    public String login(String login, String password) {
        String response = stationConnection.sendMessage(Constants.packetType.LOGIN + delimiter + login + delimiter
                + password + Constants.PACKET_END);

        notifyObservers(response);
        return response;
    }

    /**
     * register vote
     */
    public String vote(String login, String candidateSIN) {
        String response = stationConnection.sendMessage(Constants.packetType.VOTE + delimiter + login + delimiter
                + candidateSIN + Constants.PACKET_END);

        notifyObservers(response);
        return response;
    }

    private void updateObservers(String response) {
        setChanged();
        notifyObservers(response);
    }
}
