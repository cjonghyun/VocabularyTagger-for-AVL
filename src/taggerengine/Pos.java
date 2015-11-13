package taggerengine;

import java.util.Collections;

import simplenlg.framework.*;
import simplenlg.lexicon.*;
import simplenlg.realiser.english.*;
import simplenlg.phrasespec.*;
import simplenlg.features.*;

public class Pos implements Comparable{
	private int id;
	private String word;  // word of the sentence
	private String pastTense;
	private String tag;   // part of speech
	private int count;    // count the frequency of the word with the tagged part of speech
	public Pos(int id, String w, String t){
		this.id = id;
		word = w;
		tag = t;
		count = 0;
		pastTense = exceptions(w);

	}
	private String exceptions(String w){
		String temp= null;

		if(tag.equals("v")){
//			pastTense = getPastTense();			
			if(w.equals("seek")){
				temp = "sought";
			}
			else if(w.equals("foregoes")){
				temp = "forewent";
			}
			else if(w.equals("undertake")){
				temp = "undertook";
			}
			else if(w.equals("grind")){
				temp = "ground";
			}
			else if(w.equals("arise")){
				temp = "arose";
			}
			else if(w.equals("employ")){
				temp = "employed";
			}
			else if(w.endsWith("y")){
				temp = w.substring(0, w.length()-1) + "ied";
			}
			else if(w.endsWith("e")){
				temp = w + "d";
			}
			else{
				temp = w + "ed";
			}
		}
		return temp;
	}
	public void setPast(String s){
		pastTense = s;
	}
	public String getPast(){
		return pastTense;
	}
	public int getId(){
		return id;
	}
	public String getWord(){
		return word;
	}
	public String getTag(){
		return tag;
	}
	public int getCount(){
		return count;
	}
	public void addCount(){
		count = count + 1;
	}
	
	public String getPastTense(){
        Lexicon lexicon = Lexicon.getDefaultLexicon();
        NLGFactory nlgFactory = new NLGFactory(lexicon);
        SPhraseSpec p = nlgFactory.createClause();
        p.setVerb(word);
        p.setFeature(Feature.TENSE, Tense.PAST);
        Realiser realiser = new Realiser(lexicon);
        String past = realiser.realise(p).getRealisation();
    	if(past.contains("-")){
    		int hypen = past.indexOf("-");
    		String trimmed = past.substring(0, hypen) + past.substring(hypen+1);
    		return trimmed;
    	}
    	return past;
	}
	public boolean pluralCheck(String w){
		return false;
	}
	
	/**
	 * check if the word is in the dictionary
	 * @param w
	 * @param t
	 * @return
	 */
	public boolean compare(String w, String t){
		if(tag.equals(t)){
			if(w.equals(word) || (word+"s").equals(w) || (word+"es").equals(w) )
				return true;
			if(word.endsWith("y") && word.length() < w.length()){
				if(w.startsWith(word.substring(0, word.length()-2))){
					if(w.endsWith("ies")){
						return true;
					}
				}
			}
			if(tag.equals("v")){
				if(pastTense == null)
					System.out.println(w + " - " + t);
				if(pastTense.equals(w))
					return true;
			}
		}
		return false;
	}

	@Override
	public int compareTo(Object o) {
		Pos other = (Pos) o;
		int c = other.getCount();
		return c - count;
	}
}
