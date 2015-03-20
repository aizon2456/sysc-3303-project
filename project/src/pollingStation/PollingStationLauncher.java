package pollingStation;

public class PollingStationLauncher implements Runnable {

    private String districtServerAddress;
    private int districtServerPort;

    /**
     *
     * @param args: args[0] = district Server Address
     *              args[1] = district Server Port
     */
    public static void main(String[] args) {

//        if (args.length < 2) {
//            System.out.println("Missing parameters");
//            return;
//        }

        (new Thread(new PollingStationLauncher("127.0.0.1", 2015))).start();
    }

    public PollingStationLauncher(String address, int port) {
        districtServerAddress = address;
        districtServerPort = port;
    }

    @Override
    public void run() {
        PollingStation server = new PollingStation(districtServerAddress, districtServerPort);
        PollingView view = new PollingView(false);
        PollingController controller = new PollingController();

        controller.addModel(server);
        controller.addView(view);
        server.addObserver(view);
        view.addController(controller);
    }
}

