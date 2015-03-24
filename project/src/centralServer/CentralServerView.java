package centralServer;

import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComboBox;
import javax.swing.JFrame;

public class CentralServerView implements Observer{

	private JFrame frame;
	private JComboBox districtMenu;
	
	public CentralServerView(ActionListener actionListener){
		frame = new JFrame();
		frame.setSize(500, 500);
		frame.setVisible(true);
		
		
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}
}
