package tests;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import phase1.FileScanner;

public class TestReadTextFile {
	
	FileScanner fs;
	
	@Before
	public void setup() throws IOException{
		fs = new FileScanner("tests/spec1");
	}
	
	@Test
	public void testDigit(){
		assertEquals("[0-9]", fs.getRegexTable().get("$DIGIT"));
	}

	@Test
	public void testNonZero(){		
		assertEquals("[^0] IN $DIGIT", fs.getRegexTable().get("$NON-ZERO"));
	}
	
	@Test
	public void testChar(){
		assertEquals("[a-zA-Z]", fs.getRegexTable().get("$CHAR"));
	}
	
	@Test
	public void testUpper(){
		assertEquals("[^a-z] IN $CHAR", fs.getRegexTable().get("$UPPER"));
	}
	
	@Test
	public void testLower(){
		assertEquals("[^A-Z] IN $CHAR", fs.getRegexTable().get("$LOWER"));
	}
	
	@Test
	public void testIdentifier(){
		assertEquals("$LOWER ($LOWER|$DIGIT)*", fs.getTokenTable().get("$IDENTIFIER"));
	}
	
	@Test
	public void testInt(){
		assertEquals("($DIGIT)+", fs.getTokenTable().get("$INT"));
	}
	
	@Test
	public void testFloat(){
		assertEquals("($DIGIT)+ \\. ($DIGIT)+", fs.getTokenTable().get("$FLOAT"));
	}
	
	@Test
	public void testAssign(){
		assertEquals("=", fs.getTokenTable().get("$ASSIGN"));
	}
	
	@Test
	public void testPlus(){
		assertEquals("\\+", fs.getTokenTable().get("$PLUS"));
	}
	
	@Test
	public void testMinus(){
		assertEquals("-", fs.getTokenTable().get("$MINUS"));
	}
	
	@Test
	public void testMultiply(){
		assertEquals("\\*", fs.getTokenTable().get("$MULTIPLY"));
	}
	
	@Test
	public void testPrint(){
		assertEquals("PRINT", fs.getTokenTable().get("$PRINT"));
	}
}