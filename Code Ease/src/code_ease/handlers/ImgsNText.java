package code_ease.handlers;

import javax.swing.Icon;

public class ImgsNText {

	
	
	private String name;
	private Icon image;
	
	public ImgsNText(String text, Icon icon){
		name = text;
		image = icon;
	}
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	public Icon getImage(){
		return image;
	}
	public void setImage(Icon img){
		this.image = img;
	}

}
