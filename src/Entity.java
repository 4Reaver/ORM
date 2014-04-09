import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
	private int id;
	private List<String> values = new ArrayList<String>();
	private boolean isLoaded = false;
	public Boolean[] isModified;
	
	public Entity(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public abstract List<String> getFields();
	
	public void load() {
		Connection connection = Postgresql.getConnection();
		String className = this.getClass().getSimpleName().toLowerCase();
		List<String> fields = this.getFields();
		int index;
		
		try {
			PreparedStatement preparedStatement 
					= connection.prepareStatement("SELECT * FROM " + className
					+ " WHERE " + className + "_id=?");
			ResultSet result = null;
			
			preparedStatement.setInt(1, this.id);
			result = preparedStatement.executeQuery();
			this.isModified = new Boolean[this.getFields().size()];
			
			result.next();
			for ( String field : fields ) {
				index = fields.indexOf(field);
				values.add(index, result.getString(className + "_" + field));
				this.isModified[index] = new Boolean("False");
			}
		
		this.isLoaded = true;
		
		} catch (SecurityException | SQLException 
				| IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	public String getValue(String fieldName) {
		int index = this.getFields().indexOf(fieldName);
		
		if ( index == -1 ) {
			throw new IllegalArgumentException(); 
		} else if ( !isLoaded ) {
			this.load();
		}
		
		return values.get(index);
	}
	
	public static void main(String[] args) {
		Article at = new Article(1);
		Category cat = new Category(1);
		Tag tag = new Tag(2);
		
		System.out.println(cat.getValue("title"));
		System.out.println(tag.getValue("value"));
		
		System.out.println(at.getValue("title"));
		System.out.println(at.getValue("text"));
		
		System.out.println(at.isModified[0]);
		
		Postgresql.closeConnection();
	}
}
