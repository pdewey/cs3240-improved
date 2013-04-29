package tests;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Before;
import org.junit.Test;

import phase1.FileScanner;
import phase1.NFA;
import phase1.NFAFactory;

public class NFAFactoryTest {
	
	FileScanner fs;
	NFAFactory factory;
	HashSet<NFA> nfas;

	@Before
	public void setUp() throws Exception {
		fs = new FileScanner("tests/spec1");
		factory = new NFAFactory(fs.getRegexTable(), fs.getTokenTable());
		nfas = factory.factorize();
	}

	@Test
	public void testSize() {
		assertEquals(8, nfas.size());
	}
	
//	@Test
//	public void testNFAs() {
//		for(NFA n : nfas)
//		{
//			System.out.println(n.toString());
//		}
//	}

}
