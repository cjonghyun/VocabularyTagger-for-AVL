package taggerengine;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.util.TreeMap;
import java.io.File;
import java.io.FileNotFoundException;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Parser {
	MaxentTagger tagger;
//	PosList words;
	PosList avl, pvl;
	Map<String,String> irregularVerbs;
	List<Highlight> highlighted;
	public  Parser(String inputFile){
			tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
			avl = new PosList("AVL");
			pvl = new PosList("PVL");
			defaultSetting();
			try{
				readList("AVL.txt", avl);
				readList("PVL.txt",pvl);
				highlighted = new ArrayList<Highlight>();
				Boolean flag;
				// tag POS by using Stanford pos tagger
				List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(inputFile)));
				for (List<HasWord> sentence : sentences){
					List<TaggedWord> tSentence = tagger.tagSentence(sentence);
					for(TaggedWord word : tSentence){
						flag = avl.contains(word.word().toLowerCase(), word.tag().substring(0, 1).toLowerCase());
						if(flag)
							highlighted.add(new Highlight(word.word(), avl.getName(), flag));
						else{
							flag = pvl.contains(word.word().toLowerCase(), word.tag().substring(0, 1).toLowerCase());
							if(flag)
								highlighted.add(new Highlight(word.word(), pvl.getName(), flag));
							else
								highlighted.add(new Highlight(word.word(), null, flag));
						}
					}
					highlighted.get(highlighted.size()-1).setEndOfSentence();
				}
			}
			catch(FileNotFoundException e){
				e.printStackTrace();
				return;
			}
			avl.trim();
			avl.sort();
			
			pvl.trim();
			pvl.sort();
	}
	private void defaultSetting(){
		irregularVerbs = new TreeMap<String,String>();
		irregularVerbs.put("display","displayed");
		irregularVerbs.put("permit","permitted");
		irregularVerbs.put("convey","conveyed");
		irregularVerbs.put("survey","surveyed");
		irregularVerbs.put("stem","stemmed");
		irregularVerbs.put("transmit","transmitted");
		irregularVerbs.put("map","mapped");
		irregularVerbs.put("fulfil","fulfilled");
		irregularVerbs.put("confer","conferred");
		irregularVerbs.put("incur","incurred");
	}
	private void readList(String filename, PosList list){
		Scanner sc_words;
		try {
			sc_words = new Scanner(new File(filename));
			String temp[];
			int id=1;
			while(sc_words.hasNextLine()){
				temp = sc_words.nextLine().toString().split("\t");
				Pos p = new Pos(id,temp[0],temp[1]);
				if(irregularVerbs.get(temp[0]) !=null){
					p.setPast(irregularVerbs.get(temp[0]));
				}
				list.add(p);
				id+=1;
			}
			sc_words.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	public PosList getAVL(){
		return avl;
	}
	public PosList getPVL(){
		return pvl;
	}
	public void exportToCSV(String fileName){
		String result = avl.csvForm() + "\n" +pvl.csvForm();
		ExportFile csv = new ExportFile(fileName,result);
		csv.exportCsv();
	}
	public String toString(){
		return avl.toString() + "\n" + pvl.toString();
	}
	public List<Highlight> getHighlighted(){
		return highlighted;
	}
}