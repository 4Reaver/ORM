import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Entity {
	private int id;

	public Entity(int id) {
		this.id = id;
	}
	
	public int getId() {
		return id;
	}
	
	public void load() {
		Connection connection = Postgresql.getConnection();
		String className = this.getClass().getSimpleName().toLowerCase();
		Field[] fields = this.getClass().getDeclaredFields();
		
		try {
			PreparedStatement preparedStatement 
					= connection.prepareStatement("SELECT * FROM " + className
					+ " WHERE " + className + "_id=?");
			ResultSet result = null;
			
			preparedStatement.setInt(1, this.id);
			result = preparedStatement.executeQuery();
			
			result.next();
			for ( Field field : fields ) {
				String methodName = "set" 
						+ capitalizeFirstLetter(field.getName());
				Method method = this.getClass().getMethod(methodName,
						field.getType());
				
				method.invoke(this, result.getString(className + "_" 
						+ field.getName()));
			}
		} catch (NoSuchMethodException e) {
			System.out.println("No set method for field");
		} catch (SecurityException | IllegalAccessException | SQLException 
				| InvocationTargetException | IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	private String capitalizeFirstLetter(String original){
	    if(original.length() == 0)
	        return original;
	    return original.substring(0, 1).toUpperCase() + original.substring(1);
	}
	
	public static void main(String[] args) {
		Article at = new Article(1);
		Category cat = new Category(1);
		Tag tag = new Tag(2);
		
		System.out.println(at.getTitle());
		System.out.println(at.getText());
		
		System.out.println(cat.getTitle());
		System.out.println(tag.getValue());
		
		Postgresql.closeConnection();
	}
}
