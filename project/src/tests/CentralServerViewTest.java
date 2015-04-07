package tests;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import centralServer.CentralServerView;

public class CentralServerViewTest {


	@Test
	public void testCentralView (){
		System.out.println("starting test case");

		CentralServerView view = new CentralServerView(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				System.out.println("Action: "+ arg0);
			}});
		view.update(null, new String[]{"Ontario","Manitoba"});

		Map<String,Map<String,Integer>> data = new HashMap<String, Map<String, Integer>>();
		Map<String,Integer> data2 = new HashMap<String, Integer>();

		data2.put("Walter", 123);
		data2.put("Steve",  111);
		data2.put("Arnold", 100);

		data.put("Ontario", data2);

		view.update(null, data);

		System.out.println("District Selected: "+view.getSelectedDistrict());
	}
}

