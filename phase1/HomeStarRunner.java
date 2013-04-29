package phase1;

import java.io.IOException;

public class HomeStarRunner {

	public static void main(String[]args) throws IOException
	{		
		DriverNFA dr = new DriverNFA("tests/input1", "tests/spec1", "tests/output");
		dr.start();
	}
}