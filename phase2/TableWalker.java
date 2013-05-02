package phase2;

import java.util.*;

public class TableWalker
{
	private Stack<String> stack;
	private LinkedHashMap<String, LinkedHashMap<String, String>> table;
	private LinkedList<Token> tokens;
	
	public TableWalker(List<String> inputFile, 
			LinkedHashMap<String, LinkedHashMap<String, String>> table)
	{
		this.table = table;
		this.tokens = createTokens(inputFile);
		stack = new Stack<String>();
	}
	
	public boolean parse()
	{
		String currType = "", currValue = "";
		int currIndex = 0;
		boolean result = true;
		stack.push("$");
		
		while (!stack.isEmpty())
		{
			
			
		}
		
		return result;
	}
	
	private LinkedList<Token> createTokens(List<String> input){
		LinkedList<Token> toRet = new LinkedList<Token>();
		for(String s : input){
			String[] temp = s.split(" ", 2);
			toRet.add(new Token(temp[0], temp[1]));
		}
		for(Token t : toRet){
			System.out.println(t.toString());
		}
		return toRet;
	}
	
	private String appendStrings(List<String> input)
	{
		String result = "";
		for (String s : input)
		{
			result += s + " ";
		}
		
		return result.trim();
	}
}
