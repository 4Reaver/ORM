import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tag extends Entity {
	private List<String> fields = new ArrayList<String>(
			Arrays.asList("value"));
	
	public Tag(int id) {
		super(id);
	}
	
	@Override
	public List<String> getFields() {
		return fields;
	}
	
	public void setFields(List<String> newFields) {
		this.fields = newFields;
	}
}
