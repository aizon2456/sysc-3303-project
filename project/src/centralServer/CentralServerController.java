package centralServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CentralServerController implements ActionListener{

	CentralServer centralServer;
	CentralServerView centralServerView;
	
	public CentralServerController(int centralServerPort){
		centralServer = new CentralServer(centralServerPort);
		centralServerView = new CentralServerView(this);
		centralServer.setCentralServerView(centralServerView);
        centralServer.runServerRun();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(!(arg0.getSource() instanceof JButton)){return;}
		centralServer.districtRequest(centralServerView.getSelectedDistrict());
	}
}
