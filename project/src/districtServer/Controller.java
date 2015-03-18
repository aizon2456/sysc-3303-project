package districtServer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 
 * @author Nikola Neskovic (100858043), Ian Wong, Kevin Rosengren (100848909), Jonathon Penny
 * @since March 17th 2015
 */
public class Controller implements ActionListener {
	
	View view;
	DistrictServer districtServer;
	
	public Controller(String[] commandLineArguments){
		districtServer = new DistrictServer(commandLineArguments[0]);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
