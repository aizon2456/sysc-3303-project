package pollingStation;

import constants.Constants;

import java.util.Observable;

public class PollingStation extends Observable {

    private String delimiter = String.valueOf(Constants.PACKET_DELIMITER);
    private PollingStationConnection stationConnection;
    private String currentLogin;

    public PollingStation(String districtServerAddress, int districtServerPort) {
        stationConnection = new PollingStationConnection(districtServerAddress, districtServerPort);
    }

    public void setLogin(String login) {
        currentLogin = login;
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
                + lastName + delimiter + sin + delimiter + login + delimiter + password);

        updateObservers(parseResponse(response));
        return response;
    }

    /**
     * voter username
     * @param username: username name
     * @param password: voter's password
     */
    public String login(String username, String password) {
        String response = stationConnection.sendMessage(Constants.packetType.LOGIN + delimiter + username + delimiter
                + password);

        String[] packetDataInformation = parseResponse(response);

        if (packetDataInformation[0].contains(Constants.returnCodes.WRONG_CREDENTIALS.name())) {
            currentLogin = "";
        } else if (packetDataInformation[0].contains(Constants.returnCodes.LOGIN_SUCCESS.name())) {
            currentLogin = username;
        }

        updateObservers(packetDataInformation);
        return response;
    }

    /**
     * register voteFor
     */
    public String voteFor(String candidateSIN) {
        String response = stationConnection.sendMessage(Constants.packetType.VOTE + delimiter + currentLogin + delimiter
                + candidateSIN);

        updateObservers(parseResponse(response));
        return response;
    }

    private String[] parseResponse(String response) {
        return response.trim().split(Constants.PACKET_DELIMITER);
    }


    private void updateObservers(String[] response) {
        setChanged();
        notifyObservers(response);
    }
}
