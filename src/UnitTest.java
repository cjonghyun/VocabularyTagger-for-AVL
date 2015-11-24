import static org.junit.Assert.*;

import org.junit.Test;
import taggerengine.*;

public class UnitTest {
	
	@Test
	public void test() {
		Parser parser = new Parser("testInput.txt",true);
		
		if(parser != null){
			assert(true);
			System.out.println(parser.toString());
		}
		
		Parser parser2 = new Parser("result break success population", false);
		if(parser2 != null){
			assert(true);
			System.out.println(parser2.toString());
		}		
	}
}
