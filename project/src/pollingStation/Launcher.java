package pollingStation;

public class Launcher implements Runnable {

    private String districtServerAddress;
    private int districtServerPort;

    /**
     *
     * @param args: args[0] = district Server Address
     *              args[1] = district Server Port
     */
    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Missing parameters");
            return;
        }

        (new Thread(new Launcher(args[0], Integer.parseInt(args[1])))).start();
    }

    public Launcher(String address, int port) {
        districtServerAddress = address;
        districtServerPort = port;
    }

    @Override
    public void run() {
        PollingStationServer server = new PollingStationServer(districtServerAddress, districtServerPort);
        PollingView view = new PollingView();
        PollingController controller = new PollingController();

        controller.addModel(server);
        view.addController(controller);
    }
}

