package phase2;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


public class LL1Parser
{
	private List<String> origFile;
	private LinkedHashMap<Token, HashSet<Token>> firstSets;
	private LinkedHashMap<String, HashSet<String>> followSets;
	private LinkedHashMap<String, LinkedHashMap<String, String>> parseTable;
	private String begin;

	private final static Charset ENCODING = StandardCharsets.US_ASCII;

	public LL1Parser()
	{
		begin=null;
		origFile = null;
		firstSets = null;
		followSets = null;
		parseTable = new LinkedHashMap<String, LinkedHashMap<String, String>>();
	}

	public LinkedHashMap<String, LinkedHashMap<String, String>> getParseTable()
	{
		return parseTable;
	}

	public String getBegin()
	{
		return begin;
	}

	public LinkedHashMap<Token, HashSet<Token>> getFirstSets()
	{
		return firstSets;
	}

	public void inputFile(String file) throws IOException
	{
		origFile = readTextFile(file);
	}

	public List<String> getInputFile()
	{
		return origFile;
	}

	private  HashSet<String> combineSet2(HashSet<String> set1, Set<Token> addTerm)
	{
		for(Token str :addTerm)
		{
			set1.add(str.getValue());
		}
		return set1;

	}


	public void createFirstSets()
	{
		int i = 0;
		LinkedHashMap<Token, HashSet<Token>> map = new LinkedHashMap<Token, HashSet<Token>>();
		HashSet<Token> keys = new HashSet<Token>();
		Token t;
		for(String str : origFile)
		{	
			str = replaceSpace(str);
			String[] splitString = (str.split("::="));
			//REMOVE SPACES
			//splitString[0]= splitString[0].substring(0, splitString[0].length() - 3);

			HashSet<Token> set = getTerm(splitString[0],splitString[1]);
			//terminating conditions are now in

			//        	for(Token t2 : set){
			//        		if(t2.getValue().charAt(0)!='<' || t2.getValue().length() == 1){
			//        			parseTable.get(splitString[0]).put(t2.getValue(), value);
			//        		}
			//        	}

			t = new Token(splitString[0],false,i==0);
			if(i==0)
			{
				begin=splitString[0];
			}
			if(keys.contains(t)){
				set = combineSet(map.get(t), set);
			}
			else{
				keys.add(t);
			}
			map.put(t,set);
			i++;
		}

		//HashSet<String> keys= (HashSet<String>) map.keySet();

		for(Token key : keys)
		{
			HashSet<Token> value = getStuff(map,key);
			map.put(key, value);
		}
		firstSets = map;
	}

	/* public void createFollowSets()
    {
        String[] temp;
        List<String> file = origFile;
        HashSet<String> nonterminals = new HashSet<String>();

        for (String s : file)
        {
            temp = s.split(" ");
            nonterminals.add(temp[0]);
        }
        System.out.println(nonterminals);
    }*/


	public String replaceSpace(String str)
	{
		String ret = "";

		for(int i=0;i<str.length();i++)
		{
			if(str.charAt(i)!=' ')
			{
				ret+=str.charAt(i);
			}
		}
		return ret;
	}

	/*
    public LinkedHashMap<String, Set<String>> createFirstSets(List<String> origFile)
    {
    	LinkedHashMap<String, Set<String>> map=new LinkedHashMap<String,Set<String>>();
    	HashSet<String> keys=new HashSet<String>();
    	for(String str : origFile)
    	{	
    		str=replaceSpace(str);
    		String[] splitString = (str.split("="));
        	//REMOVE SPACES
        	splitString[0]= splitString[0].substring(0, splitString[0].length() - 1);

        	HashSet<String> set=getTerm(splitString[0],splitString[1]);
        	//terminating conditions are now in
        	map.put(splitString[0], set);
        	keys.add(splitString[0]);
        }

    	//HashSet<String> keys= (HashSet<String>) map.keySet();

    	for(String key : keys)
    	{
    		HashSet<String> value=getstuff(map,key);
    		map.put(key, value);

    	}
    	return map;
    }
	 */

	public HashSet<Token> getStuff(LinkedHashMap<Token,HashSet<Token>> map, Token key)
	{
		HashSet<Token> set = (HashSet<Token>) map.get(key);
		HashSet<Token> set2 = new HashSet<Token>();

		if(set!=null)
		{	
			for(Token str : set)
			{	
				if(str.getValue().length()==1)
				{
					set2.add(str);
				}
				else if(str.getValue().substring(1,str.getValue().length()-1).equals("epsilon"))
				{
					str.toggle();
					set2.add(str);
				}
				else if(str.getValue().charAt(0)!='<')
				{
					set2.add(str);
				}
				else
				{
					String[] split2 = str.getValue().split(">");
					split2[0]+=">";
					HashSet<Token> set3 = getStuff(map,new Token(split2[0], false, false));
					addToParseTable(set3, str, key.getValue());
					for(Token t : set3)
					{
						set2.add(t);
					}
				}
			}	
		}
		return set2;
	}

	public HashSet<Token> getTerm(String key,String str)
	{
		HashSet<Token> set = new HashSet<Token>();
		String[] split = str.split("\\|");
		for(String temp : split)
		{
			Token t;
			if(!temp.isEmpty())
			{
				if(temp.charAt(0)!='<')
				{
					String[] split2 = temp.split("<");
					t = new Token(split2[0],true,false);
					HashSet<Token> h = new HashSet<Token>();
					h.add(t);
					addToParseTable(h, new Token(temp,false,false), key);
					set.add(t);
				}
				else
				{
					String[] split2 = temp.split(">");
					split2[0]+=">";
					if(!split2[0].equals(key))
					{	
						t = new Token(temp,false,false);
						set.add(t);
					}
					/*
					else
					{
						split2[1]+=">";
						set.add(split2[1]);

					}*/
				}
			}
		}
		return set;
	}



	public HashMap<String, HashSet<String>> getFollowSets()
	{
		return followSets;
	}

	// Creates the follow sets for the input file grammar
	public void createFollowSets()
	{
		LinkedHashMap<String, HashSet<String>> result = new LinkedHashMap<String, HashSet<String>>();
		String[] splitSpace, splitEquals;
		int oldSize = 0, index = 0;
		String nonTerm = "";
		boolean isFirst = true, changes = true, hasEpsilon = false;
		HashSet<String> currTerm, tempTerm = new HashSet<String>();
		Set<Token> addTerm = new HashSet<Token>();
		List<String> input = deconstructOr(origFile);
		System.out.println(input);

		// Pull out non-terminals
		for (String s : input)
		{
			splitSpace = s.split(" ");
			nonTerm = splitSpace[0].trim();
			if (result.containsKey(nonTerm))
				continue;

			result.put(nonTerm, new HashSet<String>());

			if (isFirst)
			{
				result.get(nonTerm).add("$");
				isFirst = false;
			}
		}

		// Compute the follow sets
		int whileCount = 0, inputLine = 0;
		while (changes)
		{
			inputLine = 0;
			System.out.println("====================================");
			System.out.println("While loop: " + whileCount);
			changes = false;
			Token eps=new Token("<epsilon>",true,false);
			
			// Iterate through production rules
			for (String rule : input)
			{
				System.out.println("Input line: " + inputLine);
				System.out.println("Input: " + rule);
				splitEquals = rule.split("::=");
				nonTerm = splitEquals[0];
				currTerm = result.get(nonTerm.trim());
				splitSpace = splitEquals[1].trim().split("( |>)");

				// Add in '>' brackets where needed
				for (int i = 0; i < splitSpace.length; i++)
				{
					if (splitSpace[i].length() > 0 && splitSpace[i].charAt(0) == '<')
					{
						splitSpace[i] = splitSpace[i].trim() + ">";
					}
					splitSpace[i] = splitSpace[i].trim();
				}

				// For each non-terminal
				int j = 0;
				while (j < splitSpace.length)
				{
					String x = splitSpace[j];
					String y = "";
					if (x.length() <= 0)
					{
					}
					else if (x.charAt(0) == '<' && j == splitSpace.length-1
							&& !x.equals("<epsilon>"))
					{
						tempTerm = result.get(x);
						if(tempTerm!=null)
						{
							oldSize = tempTerm.size();
						}	
						tempTerm = combineSet(tempTerm, currTerm);

					}
					else if (x.charAt(0) == '<' && !x.equals("<epsilon>"))
					{
						// x is a non-terminal
						tempTerm = result.get(x);
						oldSize = tempTerm.size();

						// Search for next character
						index = j+1;
						while (index < splitSpace.length-1 && splitSpace[index].length() <= 0)
							index++;

						y = splitSpace[index];

						// Check to see if y is terminal or not
						if (y.charAt(0) == '<' && !y.equals("<epsilon>"))
						{
							Token t=new Token(y,true,true);
							addTerm = firstSets.get(t);
							if(addTerm!=null)
							{	
								if (addTerm.contains(eps))
								{	
									hasEpsilon = true;
									addTerm.remove(eps);
									System.out.println("AddTerm: " + addTerm);
									tempTerm = combineSet2(tempTerm, addTerm);
								}	
							}
							if (hasEpsilon)
							{
								addTerm.add(new Token("<epsilon>",true,false));
								tempTerm = combineSet(tempTerm, currTerm);
							}
						}
						else if (!y.equals("<epsilon>"))
						{
							tempTerm.add(y);
						}

						if (oldSize < tempTerm.size())
						{
							changes = true;
						}
					}

					hasEpsilon = false;
					j++;
				}
				System.out.println(result);
				inputLine++;
			}

			System.out.println("======================================");

			whileCount++;
		}
		followSets = result;
		//System.out.println("\n\n\nFollow Sets = " + followSets);
	}

	// Gets rid of any | branching in the grammar by creating new production rules
	private List<String> deconstructOr(List<String> file)
	{
		List<String> result = new LinkedList<String>();
		String term = "", temp = "";

		for (String s : file)
		{
			String[] splitEquals = s.split("::=");
			term = splitEquals[0];
			String[] splitOr = splitEquals[1].split("\\|");
			temp = term + " ::= ";
			for (int i = 0; i < splitOr.length; i++)
				result.add(temp + splitOr[i]);
		}

		return result;
	}

	public void finishParseTable(){
		Set<Token> firstKeys = firstSets.keySet();
		
		for(Token k : firstKeys){
			for(Token k2 : firstSets.get(k)){
				if(k2.getValue().equals("<epsilon>")){
					addToParseTable2(followSets.get(k.getValue()), k2, k.getValue());
				}
			}
		}
	}

	private <T> HashSet<T> combineSet(HashSet<T> set1, HashSet<T> set2)
	{
		if(set2!=null && set1!=null)
		{
			for(T str :set2)
			{
				set1.add(str);
			}
		}
		else if(set2!=null)
		{
			return set2;
		}
		return set1;
	}
	
	private void addToParseTable2(HashSet<String> hs, Token str, String key){
		Set<String> ks = parseTable.keySet();
		if(!ks.contains(key)){
			parseTable.put(key, new LinkedHashMap<String,String>());
		}
		for(String t2 : hs){
			if(t2.charAt(0)!='<' || t2.length() == 1){
				parseTable.get(key).put(t2, str.getValue());
			}
		}	
	}

	private void addToParseTable(HashSet<Token> hs, Token str, String key){
		Set<String> ks = parseTable.keySet();
		if(!ks.contains(key)){
			parseTable.put(key, new LinkedHashMap<String,String>());
		}
		for(Token t2 : hs){
			if(t2.getValue().charAt(0)!='<' || t2.getValue().length() == 1){
				parseTable.get(key).put(t2.getValue(), str.getValue());
			}
		}	
	}

	private List<String> readTextFile(String aFileName) throws IOException
	{
		Path path = Paths.get(aFileName);
		return Files.readAllLines(path, ENCODING);
	}
}