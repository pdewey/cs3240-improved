package phase2;

import java.util.*;

public class TableWalker
{
	private String input;
	private Stack<String> stack;
	private LinkedHashMap<String, LinkedHashMap<String, String>> table;
	
	public TableWalker(List<String> inputFile, 
			LinkedHashMap<String, LinkedHashMap<String, String>> table)
	{
		this.table = table;
		this.input = appendStrings(inputFile);
		stack = new Stack<String>();
	}
	
	public boolean parse()
	{
		String[] tokens = input.split(" ");
		String currToken = "";
		int currIndex = 0;
		boolean result = true;
		stack.push("$");
		// Push the starting bitch on the stack
		
		while (!stack.isEmpty())
		{
			
			
		}
		
		return result;
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
