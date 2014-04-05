public class Category extends Entity {
	private String title;
	
	public Category(int id) {
		super(id);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
