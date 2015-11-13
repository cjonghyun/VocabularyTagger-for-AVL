package taggerengine;

public class Highlight{
	String word;
	boolean flag;
	String listName;
	boolean endOfSentence;
	public Highlight(String word, String listName, boolean flag){
		this.word = word;
		this.flag = flag;
		this.listName = listName;
		this.endOfSentence = false;
	}
	public void setEndOfSentence(){
		this.endOfSentence = true;
	}
	public String getListName(){
		return listName;
	}
	public String getWord(){
		return word;
	}
	public boolean getFlag(){
		return flag;
	}
	public void setWord(String word){
		this.word = word;
	}
	public void setFlag(boolean flag){
		this.flag = flag;
	}
	public boolean isEndofSentence(){
		return endOfSentence;
	}
}
