package phase1;

public class Token
{
    private String id;
    private String value;

    public Token(String id, String value)
    {
        this.id = id;
        this.value = value;
    }

    public String getId()
    {
        return id;
    }

    public String getValue()
    {
        return value;
    }
}