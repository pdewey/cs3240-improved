package phase1;

import java.util.*;

public class DFAState
{
    private String name;
    private boolean accept;
    private HashSet<State> states;
    private HashMap<String, DFAState> transitionTable;

    public DFAState(String name, boolean accept, HashSet<State> states,
            HashMap<String, DFAState> transitionTable)
    {
        this.name = name;
        this.accept = accept;
        this.transitionTable = transitionTable;
        this.states = states;
    }

    public boolean isAccept()
    {
        return accept;
    }

    public void addState(State s)
    {
        states.add(s);
    }

    public String getName()
    {
        return name;
    }

    public HashMap<String, DFAState> getTransitions()
    {
        return transitionTable;
    }

    public HashSet<State> getStates()
    {
        return states;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        return this.states.equals(((DFAState)obj).states);
    }
}