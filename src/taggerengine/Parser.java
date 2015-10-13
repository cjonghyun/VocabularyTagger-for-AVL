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
	PosList words;
	Map<String,String> irregularVerbs;
	List<Highlight> highlighted;
	public  Parser(String inputFile){
			tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");
			words = new PosList();
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
			
			try{
				Scanner sc_words = new Scanner(new File("words.txt"));
				String temp[];
				int id=1;
				while(sc_words.hasNextLine()){
					temp = sc_words.nextLine().toString().split("\t");
					Pos p = new Pos(id,temp[0],temp[1]);
					if(irregularVerbs.get(temp[0]) !=null){
						p.setPast(irregularVerbs.get(temp[0]));
					}
					words.add(p);
					id+=1;
				}
				highlighted = new ArrayList<Highlight>();
				Boolean flag;
				// tag POS by using Stanford pos tagger
				List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(inputFile)));
				for (List<HasWord> sentence : sentences){
					List<TaggedWord> tSentence = tagger.tagSentence(sentence);
					for(TaggedWord word : tSentence){
						flag = words.contains(word.word().toLowerCase(), word.tag().substring(0, 1).toLowerCase());
						highlighted.add(new Highlight(word.word(), flag));
					}
					highlighted.get(highlighted.size()-1).setEndOfSentence();
				}
				sc_words.close();
			}
			catch(FileNotFoundException e){
				e.printStackTrace();
				return;
			}
			words.trim();
			words.sort();
	}
	public void exportToCSV(String fileName){
		ExportFile csv = new ExportFile(fileName,words);
		csv.exportCsv();
	}
	public String toString(){
		return words.toString();
	}
	public List<Highlight> getHighlighted(){
		return highlighted;
	}
}