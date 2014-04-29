import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Category extends Entity {
	private List<String> fields = new ArrayList<String>(
			Arrays.asList("title"));
	
	public Category() {
		super();
	}
	
	public Category(int id) {
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
