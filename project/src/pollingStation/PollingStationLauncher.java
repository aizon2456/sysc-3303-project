package pollingStation;

import constants.Constants;

public class PollingStationLauncher implements Runnable {

    private static final int DEFAULT_SERVER_PORT = 2015;
    private static final String DEFAULT_SERVER_ADDRESS = "127.0.0.1";

    private String districtServerAddress;
    private int args;

    /**
     *
     * @param args: args[0] = district Server Address
     *              args[1] = district Server Port
     */
    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Missing parameters: " +
                    "\nUsing default settings: Address = " + DEFAULT_SERVER_ADDRESS +
                    ", Port = " + DEFAULT_SERVER_PORT);

            (new Thread(new PollingStationLauncher(DEFAULT_SERVER_ADDRESS, DEFAULT_SERVER_PORT))).start();
        } else {

            try {
                int port = Integer.parseInt(args[1]);
                if(port <= 1024 || port >= 65536){
                    System.out.println("Invalid server port number " + port);
                    System.exit(1);
                }

                String serverAddress = args[1];
                if(!serverAddress.matches(Constants.IPV4_REGEX)){
                    System.out.println("Invalid central server IP address! " + serverAddress);
                    System.exit(1);
                }

                (new Thread(new PollingStationLauncher(args[0], port))).start();
            } catch (Exception e) {
               System.out.println("Invalid Port - Must be a number");
            }
        }


    }

    public PollingStationLauncher(String address, int port) {
        districtServerAddress = address;
        args = port;
    }

    @Override
    public void run() {
        PollingStation server = new PollingStation(districtServerAddress, args);
        PollingView view = new PollingView(false);
        PollingController controller = new PollingController();

        controller.addModel(server);
        controller.addView(view);
        server.addObserver(view);
        view.addController(controller);
    }
}

