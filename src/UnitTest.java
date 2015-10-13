import static org.junit.Assert.*;

import org.junit.Test;
import taggerengine.*;

public class UnitTest {
	
	@Test
	public void test() {
		Parser parser = new Parser("testInput.txt");
		
		if(parser != null){
			System.out.println(parser.toString());
		}
	}
}
