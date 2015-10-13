package taggerengine;

public class Highlight{
	String word;
	boolean flag;
	boolean endOfSentence;
	public Highlight(String word, boolean flag){
		this.word = word;
		this.flag = flag;
		this.endOfSentence = false;
	}
	public void setEndOfSentence(){
		this.endOfSentence = true;
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
