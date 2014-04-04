public class Article extends Entity {
	private String text;
	private String title;
	
	public Article(int id) {
		super(id);
	}

	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
}
