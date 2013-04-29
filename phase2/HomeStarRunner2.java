package phase2;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class HomeStarRunner2 {

	public static void main(String[]args) throws IOException
	{		
		LL1Parser parse = new LL1Parser();
		//HashSet<String> str=parse.getTerm("diff|union|inters");
		
		/*HashSet<String> set1=new HashSet<String>();
		HashSet<String> set2=new HashSet<String>();
		HashSet<String> set3=new HashSet<String>();

		HashMap<String, Set<String>> map=new HashMap<String,Set<String>>();
		set2.add("begin");
		set2.add("<pawl>");
		set1.add("<hello>");
		set3.add("nice");
		set3.add("koitus");
		map.put("<cool>",set1);
		map.put("<hello>",set2);
		map.put("<pawl>",set3);*/
		
		//HashSet<String> str= parse.getstuff(map,"<cool>");

		/*ArrayList<String> list=new ArrayList<String>();
		list.add("<cool>:=<hello>| <epsilon>");
		list.add("<hello> :=begin|<pawl>");
		list.add("<pawl>:=nice |koitus");*/
		
        /*(System.out.println(args[0]);*/
		parse.inputFile("testGrammar.txt");
		parse.createFirstSets();
		parse.createFollowSets();
		System.out.println("Follow sets: " + parse.getFollowSets());
		System.out.println("First sets: " +parse.getFirstSets());
		parse.finishParseTable();
		/*
	    HashMap<String, HashSet<String>> map = parse.getFollowSets();
		Set<String> keys = map.keySet();
		parse.createFollowSets();
	    HashMap<Token, HashSet<Token>> map2 = parse.getFirstSets();
		Set<Token> keys2 = map2.keySet();

		System.out.println("First Sets");

	    
	    for(Token key : keys2)
		{
			System.out.println(key.getValue() + " : " + setToString2(map2.get(key)));
		}	
		
        
        
        
		System.out.println("\nFollow Sets");

        for(String key : keys)
		{
			System.out.println(key + " : " + setToString(map.get(key)));
		}		
		
		//FinalDriver fd = new FinalDriver("phase2/grammar.txt", "phase2/script.txt", "phase2/token_spec.txt", "phase2/output");
		//fd.start();*/
	}
		
		
		
		
		
	
	public static String setToString(Set<String> set)
	{
		String ret="";
		
		for(String s : set)
		{
			String s2=s;
			ret+=s2;
			ret+=" ";
		}	
		return ret;
	}	
	
	public static String setToString2(Set<Token> set)
	{
		String ret="";
		
		for(Token s : set)
		{
			String s2=s.getValue();
			ret+=s2;
			ret+=" ";
		}	
		return ret;
	}	
}