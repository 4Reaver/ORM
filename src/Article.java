import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Article extends Entity {
	private List<String> fields = new ArrayList<String>(
			Arrays.asList("title", "text"));
			
	public Article(int id) {
		super(id);
	}
	
	@Override
	public List<String> getFields() {
		return this.fields;
	}
	
	public void setFields(List<String> newFields) {
		this.fields = newFields;
	}
}
