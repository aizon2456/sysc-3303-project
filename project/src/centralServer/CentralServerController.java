package centralServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CentralServerController implements ActionListener{

	CentralServer centralServer;
	CentralServerView centralServerView;
	
	public CentralServerController(int centralServerPort){
		centralServer = new CentralServer(centralServerPort);
		centralServerView = new CentralServerView(this);
		centralServer.setCentralServerView(centralServerView);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
