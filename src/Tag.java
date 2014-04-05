public class Tag extends Entity {
	private String value;
	
	public Tag(int id) {
		super(id);
	}
	
	public String getValue() {
		return value;
	}
	
	public void setValue(String value) {
		this.value = value;
	}
}
