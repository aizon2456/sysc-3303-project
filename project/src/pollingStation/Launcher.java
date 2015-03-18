package pollingStation;

public class Launcher implements Runnable {

    /** @param args String name describing the district of the server: Ottawa-Carleton, Gatineau
     */
    public static void main(String[] args) {
        (new Thread(new Launcher())).start();
    }

    @Override
    public void run() {
        String districtServerAddress = "127.0.0.1";
        int districtServerPort = 2014;
        PollingStationServer server = new PollingStationServer(districtServerAddress, districtServerPort);
        PollingView view = new PollingView();
        PollingController controller = new PollingController();

        controller.addModel(server);

        view.addController(controller);
        server.login("LOGIN-NAME", "PASSWORD-NAME");
    }
}

