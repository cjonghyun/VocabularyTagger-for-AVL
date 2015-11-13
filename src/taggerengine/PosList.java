package taggerengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.io.PrintWriter;
import java.io.IOException;

public class PosList {
	String name;
	private List<Pos> pos;
	public PosList(String name){
		this.name = name;
		pos =  new ArrayList<Pos>();
	}
	public String getName(){
		return name;
	}
	public void add(Pos p){
		pos.add(p);
	}

	public boolean contains(String w, String t){
	    for (Pos e : pos) {
	    	if(e.compare(w,t)){
	    		e.addCount();
	    		return true;
	    	}
	    }
	    return false;
	}
	public List<Pos> getList(){
		return pos;
	}
	public boolean isValid(String word){
		if(word.matches(".*[a-zA-Z]+.*") && !word.equals("'s"))
			return true;
		else
			return false;
	}
	public void trim(){
		for (Iterator<Pos> iterator = pos.iterator(); iterator.hasNext(); ) {
			if(iterator.next().getCount()==0)
				iterator.remove();
		}
	}
	public void sort(){
		Collections.sort(pos);
	}
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("ID \t WORD \t\t POS \t COUNT\n");
		for(Pos p : pos){
			sb.append(name + String.format("%04d", p.getId()) + "\t" + String.format("%-20s", p.getWord()) + "\t"+ p.getTag() + "\t" + p.getCount() + "\n");
		}
		return sb.toString();
	}
	public String csvForm(){
		String result;
			result = name + "\n";
			result += "ID,WORD,POS,COUNT\n";
			for(Pos p : pos){
				result += (name + String.format("%04d", p.getId()) + "," + p.getWord() + "," + p.getTag() + "," + p.getCount() + "\n");
			}
		return result;		
	}
}
