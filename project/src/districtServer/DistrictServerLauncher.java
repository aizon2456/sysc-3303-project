package districtServer;

public class DistrictServerLauncher {
	
	/** @param args String name describing the district of the server: Ottawa-Carleton, Gatineau
	 */
	public static void main(String[] args) {
//		SwingUtilities.invokeLater(new Runnable() {
//            @Override
//            public void run() {                                           
//            	new DistrictServer();
//            }
//        });  
		new DistrictServer(args);
	}
}
