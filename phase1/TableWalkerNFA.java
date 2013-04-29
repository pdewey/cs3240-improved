package phase1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class TableWalkerNFA
{
    private NFA[] nfas;
    private List<String> input;

    public TableWalkerNFA(HashSet<NFA> nfas, List<String> input)
    {
    	Object[] temp = nfas.toArray();
        this.nfas = Arrays.copyOf(temp, temp.length, NFA[].class);
        this.input = input;
    }

    public List<Token> parse()
    {
    	System.out.println("Entered Parse");
        String curr;
        String identifier, tempIdentifier;
        int index = 0, oldIndex = 0, tempIndex = 0;
        List<Token> output = new LinkedList<Token>();
        
        identifier = "";
        tempIdentifier = "";
        index = 0;
        String currToken = "", tempCurrToken = "";
    	List<State> newStates = new ArrayList<State>();
    	List<State> currStates = new ArrayList<State>();
    	
    	ALL:
    	while(index < input.size()){
    		if(tempCurrToken.length() > 0){
    			System.out.println("Accepts: " + tempIdentifier);
	            Token acc = new Token(tempIdentifier, tempCurrToken);
	            output.add(acc);
	            currToken = "";
	            tempCurrToken = "";
	            tempIdentifier = "";
	            currStates = new ArrayList<State>();
	            oldIndex = tempIndex;
	            index = tempIndex;
    		}
            
    		NFAS:
    		for(NFA nfa : nfas){
    			currStates.add(nfa.getStart());
    			MAIN:
    			while(true){
    				curr = input.get(index);
    				newStates = new ArrayList<State>();
    				for(State currState : currStates){
    					if(currState.getTransitionTable().keySet().contains("")){
    	            		newStates.addAll(noMoreEpsilons(currState.getTransitionTable().get("")));
    	            	}
    	            	else{
    	            		newStates.add(currState);
    	            	}
    				}
    				for(State s : newStates){
    					for(String reg : s.getTransitionTable().keySet()){
							if(Pattern.matches(reg, curr)){
								currToken += curr;
								index++;
								currStates = s.getTransitionTable().get(reg);
								if(index < input.size()){
									continue MAIN;
								}
								else{
			        				newStates = new ArrayList<State>();
									break ALL;
								}
							}
						}
    				}
    				if(curr.equals("") || curr.equals(" ")){
						index++;
    					for(State s : newStates){
        					if(s.isAccept()){
                                identifier = s.getName();
                                if(currToken.length() > tempCurrToken.length()){
                                	tempCurrToken = currToken;
                                	tempIdentifier = identifier;
                                	tempIndex = index;
                                }                                
                                break NFAS;
        					}
        				}
    					if(currToken.length() <= 0){
    						oldIndex = index;
    						break NFAS;
    					}
    					currStates = new ArrayList<State>();
        				index = oldIndex;
        				currToken = "";
    					continue NFAS;
    				}
    				if(currToken.length() <= 0){
    					currStates = new ArrayList<State>();
    					continue NFAS;
    				}
    				for(State s : newStates){
    					if(s.isAccept()){
                            identifier = s.getName();
                            if(currToken.length() > tempCurrToken.length()){
                            	tempCurrToken = currToken;
                            	tempIdentifier = identifier;
                            	tempIndex = index;
                            }  
                            currToken = "";
                            currStates = new ArrayList<State>();
                            index = oldIndex;
                            continue NFAS;
    					}
    				}
    				currStates = new ArrayList<State>();
    				index = oldIndex;
    				currToken = "";
    				continue NFAS;
    			}
    		}
    	}
    	
    	for(State currState : currStates){
			if(currState.getTransitionTable().keySet().contains("")){
        		newStates.addAll(noMoreEpsilons(currState.getTransitionTable().get("")));
        	}
        	else{
        		newStates.add(currState);
        	}
		}
    	
    	for(State s : newStates){
			if(s.isAccept()){
                identifier = s.getName();
                if(currToken.length() > tempCurrToken.length()){
                	tempCurrToken = currToken;
                	tempIdentifier = identifier;
                	tempIndex = index;
                }  
				System.out.println("Accepts: " + tempIdentifier);
                Token acc = new Token(tempIdentifier, tempCurrToken);
                output.add(acc);
                break;
			}
		}
        return output;
    }
    
    private List<State> noMoreEpsilons(List<State> tempStates){
    	List<State> toRet = new ArrayList<State>();
    	for(State s : tempStates){
			if(s.getTransitionTable().keySet().contains("")){
				toRet.addAll(noMoreEpsilons(s.getTransitionTable().get("")));
			}
			else{
				toRet.add(s);
			}
		}
		return toRet;
    }
}