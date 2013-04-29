package phase1;

import java.util.*;

public class State
{
	private boolean accept;
	private HashMap<String, List<State>> transitionTable;
	private String name;

	
	public State(boolean accept, HashMap<String, List<State>> table)
	{
		this(accept, table, "");
	}
	
	public State(boolean accept, HashMap<String, List<State>> table, String name){
		this.accept = accept;
		this.transitionTable = table;
		this.name = name;
	}
	
	public boolean isAccept()
	{
		return accept;
	}
	
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name=name;
	}
	
	public HashMap<String, List<State>> getTransitionTable()
	{
		return transitionTable;
	}

    public void setIsAccept(boolean accept)
    {
        this.accept = accept;
    }
    
    public void addTransition(String trans, State toGo)
    {
    	if(transitionTable.containsKey(trans))
    	{
    		transitionTable.get(trans).add(toGo);
    	}
    	else
    	{
    		ArrayList<State> n = new ArrayList<State>();
    		n.add(toGo);
        	transitionTable.put(trans, n);
    	}
    }
}