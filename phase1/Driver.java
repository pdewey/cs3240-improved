package phase1;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Driver
{
	DFA dfa;
	String input;
	List<String> text;
    private final static Charset ENCODING = StandardCharsets.US_ASCII;
    TableWalker tw;
	
	public Driver(String input, String rules) throws IOException
	{
		this.input=input;
		FileScanner fs=new FileScanner(rules);
		NFAFactory factory = new NFAFactory(fs.getRegexTable(), fs.getTokenTable());
		HashSet<NFA>nfas = factory.factorize();
		BigNFA theNFA=new BigNFA(nfas);
		DFA dfa=new DFA(theNFA);
		text=readTextFile(input);
	    tw=new TableWalker(dfa,text);
	    ArrayList<String> out = new ArrayList<String>();
		
	    List<Token> list=tw.parse();
	    
	    for(Token t : list)
	    {
	    	String str = t.getId();
	    	str = str.substring(1);
	    	str += " ";
	    	str += t.getValue();
	    	str += "\r\n";
	    	out.add(str);
	    }
	    
		FileWriter writer = new FileWriter("tests/output"); 
		for(String str: out) 
		{
		  writer.write(str);
		}
		writer.close();
	 }
	
	/*public Driver(List<String> text, String rules) throws IOException
	{
		this.text=text;
		FileScanner fs=new FileScanner(rules);
		NFAFactory factory = new NFAFactory(fs.getRegexTable(), fs.getTokenTable());
		HashSet<NFA>nfas = factory.factorize();
		BigNFA theNFA=new BigNFA(nfas);
		nfa=theNFA.getNFA();
		testReadInput();
	}*/
	
	
	
	
	/*public String whatType(String in)
	{
		Name name=whatType(in,nfa.getStart(),0);
		return name.getName();
	}*/
	
//	private void readInput() throws IOException
//	{
//		List<String> text = readTextFile(input);
//		ArrayList<String> out=new ArrayList<String>();
//		for(String s: text)
//		{
//			String[] splitString = (s.split(" "));
//			for(int i=0;i<splitString.length;i++)
//			{
//				//String type=whatType(splitString[i]);
//			//	String fin=type + " "+ splitString[i];
//		//		out.add(fin);
//			}
//		}
//	
//		FileWriter writer = new FileWriter("output.txt"); 
//		for(String str: out) 
//		{
//		  writer.write(str);
//		}
//		writer.close();
//	}
	
    private List<String> readTextFile(String aFileName) throws IOException{
        Path path = Paths.get(aFileName);
        return Files.readAllLines(path, ENCODING);
    }
	
//	private void testReadInput() throws IOException
//	{
//		ArrayList<String> out=new ArrayList<String>();
//		for(String s: text)
//		{
//			String[] splitString = (s.split(" "));
//			for(int i=0;i<splitString.length;i++)
//			{
//				//String type = whatType(splitString[i]);
//				//String fin = type + " "+ splitString[i] + "\n";
//				//out.add(fin);
//			}
//		}
//	
//		FileWriter writer = new FileWriter("output.txt"); 
//		for(String str: out) 
//		{
//		  writer.write(str);
//		}
//		writer.close();
//	}
	
	//NEW ONE
	/*
	public String whatType(String in, DFAState st)
	{
		HashMap<String, List<State>> map = st.getTransitionTable();
		Set<String> keys = st.getTransitionTable().keySet();
		ArrayList<DFAState> states=new ArrayList<DFAState>();
		
		for(String k : keys)
		{
			if(in.isEmpty())
			{
				return st.getName();
			}
			else if(in.substring(0,1).matches(k))
			{
				states.add(k);
			}
			
		}
		
		
		
		
		
	}
	
	
	
		
	public Name whatType(String in, State st, int num)
	{
		num++;
		Name out = new Name(st.getName(), num);
		//System.out.println(st.getName());
		HashMap<String, List<State>> map = st.getTransitionTable();
		Set<String> keys = st.getTransitionTable().keySet();
		HashSet<State> states1 = new HashSet<State>();
		HashSet<State> states2 = new HashSet<State>();

	
		for(String str: keys)
		{
			if(str.isEmpty())
			{
				for(State st2 : st.getTransitionTable().get(str))
				{
					states1.add(st2);
				}
			}
			else if(in.matches(str))
			{
				for(State st2 : st.getTransitionTable().get(str))
				{
					states2.add(st2);
				}
				
			}
		}
		
		if( states1.size()==1 && states2.isEmpty())
		{
			for(State state: states1)
			{
				if(state.isAccept())
				{
					return out;
				}
			}
			
			//return out
		}
	    
		if(states1.isEmpty() && states2.isEmpty())
		{
			return new Name("",num);
		}
		else
		{
			for (State st2 : states1)
			{
				Name curr=whatType(in, st2,num);
				if(curr.getNum()>=num && !curr.getName().isEmpty())
				{
					out.setName(curr.getName());
					out.setNum(curr.getNum());
				}
			}
			
			for (State st3 : states2)
			{
				Name curr=whatType(in.substring(1,in.length()-1), st3,num);
				if(curr.getNum()>=num && !curr.getName().isEmpty())
				{
					out.setName(curr.getName());
					out.setNum(curr.getNum());
				}
			}
			return out;
		}	
	}
	
	*/
}