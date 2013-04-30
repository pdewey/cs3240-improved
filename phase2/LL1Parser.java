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
import java.util.Map;
import java.util.Set;

public class LL1Parser
{
	private List<String> origFile;
	private HashMap<String, Set<String>> firstSets;
	private HashMap<String, Set<String>> followSets;
	private LinkedHashMap<String, LinkedHashMap<String, String>> parseTable;
	private String begin;

	private final static Charset ENCODING = StandardCharsets.US_ASCII;

	public LL1Parser()
	{
		begin = null;
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

	public Map<String, Set<String>> getFirstSets()
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


	public void createFirstSets()
	{
		HashMap<String, Set<String>> map = new HashMap<String,Set<String>>();
		HashSet<String> keys = new HashSet<String>();
		for(String str : origFile)
		{	
			//str = replaceSpace(str);
			String[] splitString = (str.split("::="));
			
			for(int i=0; i<splitString.length; i++){
				splitString[i] = splitString[i].trim();
			}
			//REMOVE SPACES
			//splitString[0]= splitString[0].substring(0, splitString[0].length() - 3);

			Set<String> set = getTerm(splitString[0],splitString[1]);
			//terminating conditions are now in
			if(keys.contains(splitString[0])){
				set = combineSet(map.get(splitString[0]), set);
			}
			else{
				keys.add(splitString[0]);
			}
			map.put(splitString[0], set);
			
			keys.add(splitString[0]);
		}

		//HashSet<String> keys= (HashSet<String>) map.keySet();

		for(String key : keys)
		{
			HashSet<String> value=getstuff(map,key);
			map.put(key, value);
		}
		firstSets=map;
	}

	// Creates the follow sets for the input file grammar
	public void createFollowSets()
	{
		HashMap<String, Set<String>> result = new HashMap<String, Set<String>>();
		String[] splitSpace, splitEquals;
		int oldSize = 0, index = 0;
		String nonTerm = "";
		boolean isFirst = true, changes = true, hasEpsilon = false;
		Set<String> currTerm;
		Set<String> tempTerm = new HashSet<String>();
		Set<String> addTerm = new HashSet<String>();
		List<String> input = deconstructOr(origFile);
		//System.out.println(input);

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
		while (changes)
		{
			//System.out.println("====================================");
			//System.out.println("While loop: " + whileCount);
			changes = false;
			// Iterate through production rules
			for (String rule : input)
			{
				//System.out.println("Input line: " + inputLine);
				//System.out.println("Input: " + rule);
				splitEquals = rule.split("::=");
				nonTerm = splitEquals[0];
				currTerm = result.get(nonTerm.trim());
				splitSpace = splitEquals[1].trim().split(" ");

				// Add in '>' brackets where needed
				/*for (int i = 0; i < splitSpace.length; i++)
					{
						String checkTerm = "<" + splitSpace[i] + ">";
						if (checkTerm.length() > 0 && result.containsKey(checkTerm))
						{
							splitSpace[i] = "<" + splitSpace[i].trim() + ">";
						}
						splitSpace[i] = splitSpace[i].trim();
					}*/

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
						oldSize = tempTerm.size();
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
							addTerm = firstSets.get(y);
							if (addTerm.contains("<epsilon>")) hasEpsilon = true;
							addTerm.remove("<epsilon>");

							tempTerm = combineSet(tempTerm, addTerm);

							if (hasEpsilon)
							{
								addTerm.add("<epsilon>");
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
				//System.out.println(result);
			}

			//System.out.println("======================================");

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

	public HashSet<String> getstuff(HashMap<String,Set<String>> map, String key)
	{
		HashSet<String> set=(HashSet<String>) map.get(key);
		HashSet<String> set2=new HashSet<String>();

		if(set!=null)
		{	
			for(String str : set)
			{

				if(str.length()==1)
				{
					set2.add(str);
				}
				else if(str.substring(1,str.length()-1).equals("epsilon"))
				{
					set2.add(str);
				}
				else if(str.charAt(0)!='<')
				{
					set2.add(str);
				}
				else
				{
					HashSet<String> set3 = getstuff(map,str);
					for(String t : set3)
					{
						set2.add(t);
					}
				}
			}	
		}
		return set2;
	}

	public HashSet<String> getTerm(String key,String str)
	{
		HashSet<String> set = new HashSet<String>();
		String[] split = str.split("\\|");
		for(String temp : split)
		{
			temp = temp.trim();
			if(!temp.isEmpty())
			{
				if(temp.charAt(0)!='<')
				{
					String[] split2 = temp.split(" ");
					set.add(split2[0]);
				}
				else
				{
					String[] split2 = temp.split(">");
					split2[0] += ">";
					if(!split2[0].equals(key))
					{	
						set.add(split2[0]);
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

	public Map<String, Set<String>> getFollowSets()
	{
		return followSets;
	}
	
	public void createParseTable(){		
		for(int i=0; i<origFile.size(); i++){
			String[] temp = origFile.get(i).split(" ::= ");
			String[] temp2 = temp[1].split("\\| ");
			for(int j=0; j<temp2.length; j++){
				String[] temp3 = temp2[j].trim().split("\\s+");
				if(temp3[0].equals("<epsilon>")){
					addToParseTable(followSets.get(temp[0]), temp3[0], temp[0]);
				}
				else{
					if(temp3[0].charAt(0) == '<'){
						addToParseTable(firstSets.get(temp3[0]), temp2[j], temp[0]);
					}
					else{
						HashSet<String> h = new HashSet<String>();
						h.add(temp3[0]);
						addToParseTable(h, temp2[j], temp[0]);
					}
				}
			}
		}
	}

	private Set<String> combineSet(Set<String> tempTerm, Set<String> currTerm)
	{
		for(String str :currTerm)
		{
			tempTerm.add(str);
		}
		return tempTerm;
	}

	private void addToParseTable(Set<String> hs, String str, String key){
		Set<String> ks = parseTable.keySet();
		if(!ks.contains(key)){
			parseTable.put(key, new LinkedHashMap<String,String>());
		}
		for(String t2 : hs){
			if(t2.charAt(0)!='<' || t2.length() == 1){
				parseTable.get(key).put(t2, str.trim());
			}
		}	
	}

	private List<String> readTextFile(String aFileName) throws IOException
	{
		Path path = Paths.get(aFileName);
		return Files.readAllLines(path, ENCODING);
	}
}