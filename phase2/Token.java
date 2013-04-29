package phase2;

public class Token
{
	private boolean terminal;
	private String value;
	private boolean begin;
	
	public Token(String value,boolean terminal,boolean begin)
	{
		this.value=value;
		this.terminal=terminal;
	}
	
	@Override
	public int hashCode()
	{
		return value.hashCode();
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof Token) {
			return value.equals(((Token) obj).value);
		}
		return false;
	}
	
	public boolean isBegin()
	{
		return begin;
	}
	
	public boolean isTerminal()
	{
		return terminal;
	}
	
	public String getValue()
	{
		return value;
	}
	
	public void toggle()
	{
		terminal=!terminal;
	}
	
	public void setValue(String value)
	{
		this.value=value;
	}
	
	public String toString()
	{
		return value.toString();
	}
}