package phase1;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class TableWalker
{
    private DFA dfa;
    private List<String> input;

    public TableWalker(DFA dfa, List<String> input)
    {
        this.dfa = dfa; 
        this.input = input;
    }

    @SuppressWarnings("unused") // Remove this if work is attempted again
	public List<Token> parse()
    {
    	System.out.println("Entered Parse");
        char curr;
        DFAState currState;
        boolean hasAccept = false, failure = false;
        String lastKnown, identifier;
        int startPos = 0, endPos = 0, index = 0;
        List<Token> output = new LinkedList<Token>();

        for (String s : input)
        {
            currState = dfa.getStart();
            System.out.println(currState.getTransitions());
            identifier = "";
            lastKnown = "";
            hasAccept = false;
            startPos= 0;
            endPos = 0;
            index = 0;
            String currToken = "";

            while (index < s.length() && !failure)
            {
                curr = s.charAt(index);
                if(curr == ' '){
                	index++;
                	continue;
                }
                currToken += Character.toString(curr);

                for (String reg : currState.getTransitions().keySet())
                {
                   if (Pattern.matches(reg, Character.toString(curr)))
                   {
                       currState = currState.getTransitions().get(reg);
                       if (currState.isAccept())
                       {
                    	   System.out.println("Accepts: " + currState.getName());
                           hasAccept = true;
                           endPos = index;
                           identifier = currState.getName();
                           Token acc = new Token(identifier, currToken);
                           output.add(acc);
                           currToken = "";
                       }
                       break;
                   }
                   else
                   {
                       //failure = true;
                   }
                }
                index++;
            }
        }
        
        return output;
    }
}