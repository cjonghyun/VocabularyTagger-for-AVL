package taggerengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.io.PrintWriter;
import java.io.IOException;

public class PosList {
	private List<Pos> pos = new ArrayList<Pos>();
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
		sb.append("ID \t WORD \t POS \t COUNT\n");

		for(Pos p : pos){
			sb.append(p.getId() + "\t" + p.getWord() + "\t" + p.getTag() + "\t" + p.getCount() + "\n");
		}
		return sb.toString();
	}
	public void out(){
		try{
			PrintWriter writer = new PrintWriter("output.txt", "UTF-8");
			writer.print("ID \t WORD \t POS \t COUNT\n");
//			pos.stream().sorted((e1, e2) -> Integer.compare(e2.getCount(),e1.getCount())).forEach(e ->  writer.println(e.getId() + "\t" + e.getWord() + "\t" + e.getTag() + "\t" + e.getCount() ) );
			for(Pos p : pos){
				writer.print(p.getId() + "\t" + p.getWord() + "\t" + p.getTag() + "\t" + p.getCount() + "\n");
			}
			writer.close();
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
	}
}
