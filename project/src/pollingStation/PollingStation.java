package pollingStation;

import constants.Constants;

import java.util.Arrays;
import java.util.Observable;

public class PollingStation extends Observable {

    private String delimiter = String.valueOf(Constants.PACKET_DELIMITER);
    private PollingStationConnection stationConnection;

    public PollingStation(String districtServerAddress, int districtServerPort) {
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

//        updateObservers(response);
        parseRegistration(response);
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

        parseLogin(response);
        return response;
    }

    /**
     * register vote
     */
    public String vote(String login, String candidateSIN) {
        String response = stationConnection.sendMessage(Constants.packetType.VOTE + delimiter + login + delimiter
                + candidateSIN + Constants.PACKET_END);

//        updateObservers(response);
        return response;
    }

    public void parseLogin(String response) {
        String[] packetDataInformation = response.trim().split(Constants.PACKET_DELIMITER);

        String[] result = new String[0];
        System.out.println(Arrays.toString(packetDataInformation));
        if (packetDataInformation[0].contains(Constants.returnCodes.WRONG_CREDENTIALS.name())) {
            result = new String[] {Constants.returnCodes.WRONG_CREDENTIALS.name()};
        } else if (packetDataInformation[0].contains(Constants.returnCodes.SUCCESS.name())) {
            if (response.length() < 2) {
                return; // SOMETHING WENT WRONG
            }

            result = new String[] {Constants.returnCodes.SUCCESS.name()};
        }
        // TODO: complete login setup
        updateObservers(result);
    }

    public String[] parseRegistration(String response) {
        String[] packetDataInformation = response.trim().split(Constants.PACKET_DELIMITER);

        String[] result = new String[0];
        System.out.println(Arrays.toString(packetDataInformation));
        if (packetDataInformation[0].contains(Constants.returnCodes.NON_EXISTENT.name())) {
            result = new String[] {Constants.returnCodes.NON_EXISTENT.name()};
        }

        return result;
    }



    private void updateObservers(String[] response) {
        setChanged();
        notifyObservers(response);
    }
}
