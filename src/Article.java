public class Article extends Entity {
	private String text;
	private String title;
	
	public Article(int id) {
		super(id);
	}

	public String getText() {
		if ( text == null ) {
			this.load();
		}
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
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
