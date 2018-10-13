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
import java.io.StringReader;
import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class Parser {
//	PosList words;
	PosList avl, pvl, leapG, leapO;
	Map<String,String> irregularVerbs;
	List<Highlight> highlighted;
	static MaxentTagger tagger = new MaxentTagger("taggers/english-left3words-distsim.tagger");

	public  Parser(String input, boolean isFile){
			avl = new PosList("AVL");
			pvl = new PosList("PVL");
			leapG = new PosList("LEAPG");
			leapO = new PosList("LEAPO");
			defaultSetting();
			try{
				readList("AVLRed.txt", avl);
				readList("PVLBlue.txt", pvl);
				readList("LEAPGreen.txt", leapG);
				readList("LEAPOrange.txt", leapO);
				highlighted = new ArrayList<Highlight>();
				Boolean flag;
				// tag POS by using Stanford pos tagger
				List<List<HasWord>> sentences;
				if(isFile)
					sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(input)));
				else
					sentences = MaxentTagger.tokenizeText(new StringReader(input));
				for (List<HasWord> sentence : sentences){
					List<TaggedWord> tSentence = tagger.tagSentence(sentence);
					for(TaggedWord word : tSentence){
					flag = avl.contains(word.word().toLowerCase(), word.tag().substring(0, 1).toLowerCase());
//					highlighted.add(new Highlight(word.word(), avl.getName(), flag));

						if(flag){
							highlighted.add(new Highlight(word.word(), avl.getName(), flag));
//							flag = leapG.contains(word.word().toLowerCase(), word.tag().substring(0, 1).toLowerCase());
						}
						else{
							flag = pvl.contains(word.word().toLowerCase(), word.tag().substring(0, 1).toLowerCase());
							if(flag){
								highlighted.add(new Highlight(word.word(), pvl.getName(), flag));
							}
							else{
								flag = leapG.contains(word.word().toLowerCase(), word.tag().substring(0, 1).toLowerCase());
								if(flag){
									highlighted.add(new Highlight(word.word(), leapG.getName(), flag));
								}
								else{
									flag = leapO.contains(word.word().toLowerCase(), word.tag().substring(0, 1).toLowerCase());
									if(flag){
										highlighted.add(new Highlight(word.word(), leapO.getName(), flag));
									}
									else{
										highlighted.add(new Highlight(word.word(), null, flag));
									}
								}
							}
						}
					}

//					else if(flag){
//						highlighted.add(new Highlight(word.word(), leapG.getName(), flag));
//						flag = leapO.contains(word.word().toLowerCase(), word.tag().substring(0, 1).toLowerCase());
//					}
//					else if(flag){
//						highlighted.add(new Highlight(word.word(), leapO.getName(), flag));
//					}
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
			
			leapG.trim();
			leapG.sort();
			
			leapO.trim();
			leapO.sort();
			
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
				Pos p = new Pos(id,temp[0],temp[1],temp[2]);
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
	public PosList getLEAPG(){
		return leapG;
	}
	public PosList getLEAPO(){
		return leapO;
	}
	public void exportToCSV(String fileName){
		String result = avl.csvForm() + "\n" +pvl.csvForm() + "\n" +leapG.csvForm() + "\n" +leapO.csvForm();
		ExportFile csv = new ExportFile(fileName,result);
		csv.exportCsv();
	}
	public String toString(){
		return avl.toString() + "\n" + pvl.toString() + "\n" + leapG.toString() + "\n" + leapO.toString();
	}
	public List<Highlight> getHighlighted(){
		return highlighted;
	}
}