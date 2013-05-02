package phase2;

public class Token {
	private String id;
	private String value;
	
	public Token(String id, String value){
		this.setId(id);
		this.setValue(value);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}