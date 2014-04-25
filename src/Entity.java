import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class Entity {
	private int id;
	private List<String> fieldsList;
	private List<String> values;
	private boolean isLoaded = false;
	private boolean[] isModified;
	
	public Entity() {
	}
	
	public Entity(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public abstract List<String> getFields();
	
	private void initialize() {
		if ( this.fieldsList == null ) {
			int size;
			
			this.fieldsList = this.getFields();
			size = this.fieldsList.size();
			this.values = new ArrayList<String>(size);
			this.isModified = new boolean[size];
			
			for ( int i = 0; i < size; i++ ) {
				this.values.add(null);
			}
		}
	}
	
	public void load() {
		Connection connection = Postgresql.getConnection();
		String className = this.getClass().getSimpleName().toLowerCase();
		int index;
		
		this.initialize();
		
		try {
			PreparedStatement preparedStatement 
					= connection.prepareStatement("SELECT * FROM " + className
					+ " WHERE " + className + "_id=?");
			ResultSet result = null;
			
			preparedStatement.setInt(1, this.id);
			result = preparedStatement.executeQuery();
			this.isModified = new boolean[fieldsList.size()];
			
			result.next();
			for ( String field : fieldsList ) {
				index = fieldsList.indexOf(field);
				values.set(index, result.getString(className + "_" + field));
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
		int fieldsModified = 0;
		
		if ( this.id == 0 ) {
			StringBuilder fields = new StringBuilder();
			List<String> newValues = new ArrayList<String>();
			StringBuilder valuesMold = new StringBuilder();
			
			for ( String field : fieldsList ) {
				int index = fieldsList.indexOf(field);
				
				if ( isModified[index] ) {
					fields.append(className + "_" + field + ", ");
					valuesMold.append("?, ");
					newValues.add(values.get(index));
					fieldsModified += 1;
				}
			}
			fields = cut2LastChar(fields);
			valuesMold = cut2LastChar(valuesMold);
			
			try {
				PreparedStatement preparedStatement 
						= connection.prepareStatement("INSERT INTO " 
							+ className + " (" + fields + ") values ("
							+ valuesMold + ")");
				
				for ( int i = 1; i <= fieldsModified; i++ ) {
					preparedStatement.setString(i, newValues.get(i-1));
				}
				
				preparedStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			StringBuilder updateData = new StringBuilder();
			
			for ( String field : fieldsList ) {
				int index = fieldsList.indexOf(field);
				
				if ( isModified[index] ) {
					updateData.append(className + "_" + field + "=?, ");
					fieldsModified += 1;
				}
			}
			updateData = cut2LastChar(updateData);
			
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
	}
	
	public String getValue(String fieldName) {
		this.initialize();
		
		int index = this.fieldsList.indexOf(fieldName);
		
		if ( index == -1 ) {
			throw new IllegalArgumentException(); 
		} else if ( !isLoaded ) {
			this.load();
		}
		
		return values.get(index);
	}
	
	public void setValue(String fieldName, String value) {
		this.initialize();
		
		int index = fieldsList.indexOf(fieldName);
		
		if ( index == -1 ) {
			throw new IllegalArgumentException(); 
		}
		
		this.values.set(index, value);
		this.isModified[index] = true;
	}
	
	private StringBuilder cut2LastChar(StringBuilder data) {
		if ( data.length() > 2 ) {
			data.setLength(data.length() - 2);
		}
		return data;
	}
	
	public static void main(String[] args) {
		//Article at = new Article(1);
		//Category cat = new Category(1);
		//Tag tag = new Tag(2);
		
		//at.setValue("text", "some new text");
		//at.save();
		
		/*Article at2 = new Article(2);
		at2.setValue("title", "newTitle");
		at2.setValue("text", "intresting text");
		at2.save();
		
		at2.setValue("title", "Third title");
		at2.setValue("text", "Very interesting content with some freakin' \"quotes\"");
		at2.save();*/
		
		Article at3 = new Article();
		at3.setValue("title", "Title3");
		at3.setValue("text", "Text ' for 3");
		at3.save();
		
		Postgresql.closeConnection();
	}
}
