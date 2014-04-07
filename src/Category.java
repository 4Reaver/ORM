public class Category extends Entity {
	private String title;
	
	public Category(int id) {
		super(id);
	}

	public String getTitle() {
		if ( title == null ) {
			this.load();
		}
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
}
