package phase2;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import phase1.DriverNFA;

public class FinalDriver
{
    private LinkedHashMap<String, LinkedHashMap<String, String>> parseTable;
    private ArrayList<String> tokens;
    private List<String> inputList;
    private String grammar, begin, input, output;
    private DriverNFA dr;
    private LL1Parser parser;
    private TableWalker walker;
    private boolean accept;
    private final static Charset ENCODING = StandardCharsets.US_ASCII;

	public FinalDriver(String grammar, String input, String spec, String output)
	{
		dr = new DriverNFA(input, spec, output);
		parser = new LL1Parser();
		this.grammar = grammar;
		this.input = input;
		this.output = output;
	}
	
	public void start() throws IOException{
		dr.start();
		tokens = (ArrayList<String>)readTextFile(output);
		parser.inputFile(grammar);
		parser.createFirstSets();
		parser.createFollowSets();
		parser.createParseTable();
		System.out.println("First sets: " + parser.getFirstSets());
		System.out.println("Follow sets: " + parser.getFollowSets());
		parseTable = parser.getParseTable();
		inputList = readTextFile(input);
		walker = new TableWalker(tokens, parseTable);
		accept = walker.parse();
		begin = parser.getBegin();
	}

	public void goDriverGo()
	{
		boolean fin=goDriverGo(begin,0);
		if(fin)
		{
			System.out.println("Pass");
		}		
	}
	
	public boolean goDriverGo(String key,int i)
	{

		if(key.equals("$"))
		{
			return true;
		}
		else
		{
			HashMap<String,String> table=parseTable.get(key);
			//get what we are looking for
			String str=tokens.get(i);
			String[] splitString=str.split(" ");
			String pattern=table.get(splitString[0]);
			if(pattern==null)
			{
				System.out.println("Error with token: "+ splitString[1]);
				return false;
			}

			String[] splitString2=str.split(splitString[1]);
			String temp=splitString2[1];
			String cool = null;
			if(!temp.isEmpty())
			{
				if(temp.charAt(0)!='<')
				{
					String[] split2 = temp.split("<");
					cool=split2[0];
				}
				else
				{
					String[] split2 = temp.split(">");
					split2[0]+=">";
					cool=split2[0];
					
				}
			}
			i++;
			return goDriverGo(cool,i);
		}
	}

	public LinkedHashMap<String, LinkedHashMap<String, String>> getParseTable()
	{
		return parseTable;
	}
	
	public List<String> getTokens()
	{
		return tokens;
	}
	
	public List<String> getInputList()
	{
		return inputList;
	}

	 private List<String> readTextFile(String aFileName) throws IOException
	  {
		  Path path = Paths.get(aFileName);
		  return Files.readAllLines(path, ENCODING);
	  }
}