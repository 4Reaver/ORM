import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
	private int id;
	private List<String> fields;
	private List<String> values = new ArrayList<String>();
	private boolean isLoaded = false;
	private boolean isCreated;
	private boolean[] isModified;
	
	public Entity() {
		this.isCreated = false;
	}
	
	public Entity(int id) {
		this.id = id;
		this.isCreated = true;
	}
	
	public int getId() {
		return id;
	}
	
	public abstract List<String> getFields();
	
	public void load() {
		Connection connection = Postgresql.getConnection();
		String className = this.getClass().getSimpleName().toLowerCase();
		this.fields = this.getFields();
		int index;
		
		try {
			PreparedStatement preparedStatement 
					= connection.prepareStatement("SELECT * FROM " + className
					+ " WHERE " + className + "_id=?");
			ResultSet result = null;
			
			preparedStatement.setInt(1, this.id);
			result = preparedStatement.executeQuery();
			this.isModified = new boolean[fields.size()];
			
			result.next();
			for ( String field : fields ) {
				index = fields.indexOf(field);
				values.add(index, result.getString(className + "_" + field));
				this.isModified[index] = false;
			}
		
		this.isLoaded = true;
		} catch (SecurityException | SQLException
				| IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	public void save() {
		Connection connection = Postgresql.getConnection();
		String className = this.getClass().getSimpleName().toLowerCase();
		StringBuilder updateData = new StringBuilder();
		
		this.fields = this.getFields();
		int fieldsModified = 0;
		
		for ( String field : fields ) {
			int index = fields.indexOf(field);
			if ( isModified[index] ) {
				updateData.append(className + "_" + field + "=?, ");
				fieldsModified += 1;
			}
		}
		if ( updateData.length() > 2 ) {
			updateData.setLength(updateData.length() - 2);
		}
		
		try {
			PreparedStatement preparedStatement
					= connection.prepareStatement("UPDATE " + className + " SET "
						+ updateData + " WHERE " + className + "_id = ?" );
			
			for ( int i = 1; i <= fieldsModified; i++ ) {
				preparedStatement.setString(i, values.get(i-1));
			}
			preparedStatement.setInt(fieldsModified+1, this.id);
			preparedStatement.execute();
		} catch (SQLException e) {
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
	
	public void setValue(String fieldName, String value) {
		int index = this.getFields().indexOf(fieldName);
		
		if ( index == -1 ) {
			throw new IllegalArgumentException(); 
		} else if ( !isLoaded ) {
			this.load();
		}
		
		this.values.set(index, value);
		this.isModified[index] = true;
	}
	
	public static void main(String[] args) {
		Article at = new Article(1);
		//Category cat = new Category(1);
		//Tag tag = new Tag(2);
		
		at.setValue("title", "newTitleqqq");
		at.save();
		
		Article at2 = new Article(2);
		at2.setValue("title", "newTitle");
		at2.setValue("text", "intresting text");
		at2.save();
		
		at2.setValue("title", "Third title");
		at2.setValue("text", "Very interesting content with some freakin' \"quotes\"");
		at2.save();
		
		Postgresql.closeConnection();
	}
}
