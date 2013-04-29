package phase2;

import java.io.IOException;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


public class LL1parser
{
    private List<String> origFile;
    private HashMap<String,Set<String>> firstSets;
    private HashMap<String,Set<String>> followSets;

    private final static Charset ENCODING = StandardCharsets.US_ASCII;

	public LL1parser()
	{
		origFile=null;
		firstSets=null;
		followSets=null;
	}
	
	public HashMap<String,Set<String>> getFirstSets()
	{
		return firstSets;
	}
    
    public void inputFile(String file) throws IOException
    {
    	origFile=readTextFile(file);
    }
    
    public List<String> getInputFile()
    {
    	return origFile;
    }
    
    public void createFirstSets()
    {
    	HashMap<String, Set<String>> map=new HashMap<String,Set<String>>();
    	HashSet<String> keys=new HashSet<String>();
    	for(String str : origFile)
    	{	
    		str=replaceSpace(str);
    		String[] splitString = (str.split("::="));
        	//REMOVE SPACES
        	//splitString[0]= splitString[0].substring(0, splitString[0].length() - 3);
        	
        	
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
    	firstSets=map;
    }
    
    
    public String replaceSpace(String str)
    {
    	String ret="";
    	
    	for(int i=0;i<str.length();i++)
    	{
    		if(str.charAt(i)!=' ')
    		{
    			ret+=str.charAt(i);
    		}
    		
    	}
    	return ret;
    }
    
    public HashMap<String, Set<String>> createFirstSets(List<String> origFile)
    {
    	HashMap<String, Set<String>> map=new HashMap<String,Set<String>>();
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
    				HashSet<String> set3=getstuff(map,str);
    				for(String t : set3)
    				{
    					set2.add(t);
    				}
    			}
    		}	
    	}
    	return set2;
    }
    
    public void createFollowSets()
    {
    	
    }
    
    
    
    
	public HashSet<String> getTerm(String key,String str)
	{
		HashSet<String> set=new HashSet<String>();
		String[] split=str.split("\\|");
		for(String temp : split)
		{
			if(!temp.isEmpty())
			{
				if(temp.charAt(0)!='<')
				{
					String[] split2=temp.split("<");
					set.add(split2[0]);
				}
				else
				{
					String[] split2=temp.split(">");
					split2[0]+=">";
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
    
	private HashSet<String> combineSet(HashSet<String> set1, HashSet<String> set2)
	{
		for(String str :set2)
		{
			set1.add(str);
		}
		return set1;
	}
    
	
    
    private List<String> readTextFile(String aFileName) throws IOException
	{
		Path path = Paths.get(aFileName);
		return Files.readAllLines(path, ENCODING);
	}
}
