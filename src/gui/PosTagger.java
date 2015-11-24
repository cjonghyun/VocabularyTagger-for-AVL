package gui;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

import taggerengine.Parser;
import taggerengine.Highlight;

import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class PosTagger extends JFrame implements ActionListener, FocusListener{
	private Parser parser;
	private final JFileChooser fc;
	private int returnVal ;
	private JSplitPane splitPane;
	private JPanel originalTextPane;
	private JPanel filePanel;
	private JLabel loadLabel;
	private JTextField filePath;
	private JButton openButton;
	private JTextPane originalText;
	private JButton parseButton;
	private JPanel taggedTextPane;
	private JTextPane taggedText;
	private JScrollPane originalScroll;
	private JScrollPane taggedScroll;
	private JButton exportButton;
	private boolean fileLoaded = false;
	private File inputFile;
	public PosTagger(){
		this.setSize(1200,900);
		splitPane = new JSplitPane();
		fc = new JFileChooser();
		loadLabel = new JLabel("Load File"); 
		openButton = new JButton("File");
		filePath = new JTextField(20);
		filePath.setEditable(false);
		originalTextPane = new JPanel();
		filePanel = new JPanel();
		originalText = new JTextPane();
		originalText.setEditable(true);
		originalTextPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		originalScroll = new JScrollPane(originalText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		parseButton = new JButton("Analyze");
		taggedText = new JTextPane();
		exportButton = new JButton("Export to CSV File");
		taggedText.setEditable(false);
		taggedScroll = new JScrollPane(taggedText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);		
		taggedTextPane = new JPanel();
		taggedTextPane.setBorder(BorderFactory.createEmptyBorder(40, 10, 0, 10));

		originalTextPane.setLayout(new BorderLayout());
		filePanel.add(loadLabel);
		filePanel.add(filePath);
		filePanel.add(openButton);
		
		originalTextPane.add(filePanel,BorderLayout.NORTH);
		originalTextPane.add(originalScroll,BorderLayout.CENTER);
		originalTextPane.add(parseButton,BorderLayout.SOUTH);
		taggedTextPane.setLayout(new BorderLayout());
		taggedTextPane.add(taggedScroll,BorderLayout.CENTER);
		taggedTextPane.add(exportButton,BorderLayout.SOUTH);
		splitPane.setLeftComponent(originalTextPane);
		splitPane.setRightComponent(taggedTextPane);
		splitPane.setDividerLocation(700);
		this.add(splitPane);
		originalText.addFocusListener(this);
		openButton.addActionListener(this);
		parseButton.addActionListener(this);
		exportButton.addActionListener(this);
	}
    private void appendToPane(JTextPane tp, String msg, Color c)
    {
    	
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Background, c);
        aset = sc.addAttribute(aset, StyleConstants.Alignment, StyleConstants.ALIGN_JUSTIFIED);

        int len = tp.getDocument().getLength();
        tp.setCaretPosition(len);
        tp.setCharacterAttributes(aset, false);
        tp.replaceSelection(msg);
    }

	@Override
	public void actionPerformed(ActionEvent e) {
	    if (e.getSource() == openButton) {
	    	returnVal = fc.showOpenDialog(openButton);
	    	 if (returnVal == JFileChooser.APPROVE_OPTION) {
	    		try{
	    			inputFile = fc.getSelectedFile();
	                filePath.setText(inputFile.getPath());
	    			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
	    		    String line = reader.readLine();
	    		    String temp="";
	    		    while(line != null) {
	    		    	temp += line + "\n";
	    		        line = reader.readLine();
	    		    }

	    			originalText.setText(temp);
	                taggedText.setText("");
	                originalText.setCaretPosition(0);
	                fileLoaded = true;
	                reader.close();
/*	                inputFile = fc.getSelectedFile();
	                filePath.setText(inputFile.getPath());
	                Scanner sc = new Scanner(inputFile);
	                String temp="";
	                while(sc.hasNextLine()){
	                	temp+=sc.nextLine()+"\n";
	                }
	                originalText.setText(temp);
	                originalText.setCaretPosition(0);
	                fileLoaded = true;
	                taggedText.setText("");
	                sc.close();*/
	    		}
	    		catch (IOException e2){
	    			fileLoaded = false;
		           	originalText.setText("Can't load the file");

	    			e2.printStackTrace();
	    		}
            }
	    	else {
	    		fileLoaded = false;
	    	}
	    }
	    if(e.getSource() == parseButton){ 
	    	if(fileLoaded){
		        parser = new Parser(inputFile.getPath(),true);
	    	}
	    	else{
	    		parser = new Parser(originalText.getText(),false);
	    	}
	        List<Highlight> highlighted = parser.getHighlighted();
	        try{
	            StyleContext styleContext = new StyleContext();
	            Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
	            Style avlStyle = styleContext.addStyle("ConstantWidth", null);
	            Style pvlStyle = styleContext.addStyle("ConstantWidth", null);
	            StyleConstants.setBackground(avlStyle, Color.YELLOW);
	            StyleConstants.setBackground(pvlStyle, Color.ORANGE);

	            int len = highlighted.size();
	            originalText.setText("");
	            for(int i=0;i<len;i++){
	            	boolean type = highlighted.get(i).getFlag();
	            	boolean eos = highlighted.get(i).isEndofSentence();
	            	String breaker = null;
	            	if(eos){
	            		breaker = "\n\n";
	            	}
	            	else
	            		breaker = " ";
	            	if(type){		            		
	            		if(highlighted.get(i).getListName().equals("AVL"))
	            			appendToPane(originalText, highlighted.get(i).getWord() , Color.YELLOW);
	            		else
		                    appendToPane(originalText, highlighted.get(i).getWord() , Color.GREEN);


	            	}
	            	else{
	                    appendToPane(originalText, highlighted.get(i).getWord() , Color.WHITE);
	            	}
	            	if(i < len -1){
	            		if(!(highlighted.get(i+1).getWord().equals(".") || highlighted.get(i+1).getWord().equals(",")))
	            			appendToPane(originalText, breaker , Color.WHITE);
	            	}

	            }

	            originalText.setCaretPosition(0);
	            taggedText.setEditable(true);
	            taggedText.setText("");
	            
		        String output = parser.getAVL().toString();
                appendToPane(taggedText, "AVL \n", Color.YELLOW);
		    	appendToPane(taggedText, output, Color.WHITE);
		    	
		    	output = parser.getPVL().toString();
		    	appendToPane(taggedText, "PVL \n", Color.GREEN);
		    	appendToPane(taggedText, output, Color.WHITE);				    	
		        taggedText.setCaretPosition(0);
		        taggedText.setEditable(false);
	        }
	        catch (Exception e2){
	        	e2.printStackTrace();
	        }
    	
	    }
	    if(e.getSource() == exportButton){
            JFileChooser exportFile = new JFileChooser();
            exportFile.showSaveDialog(this);
            
            int returnVal = exportFile.showSaveDialog(this);
            if(returnVal == JFileChooser.APPROVE_OPTION){
            	String filePath = exportFile.getSelectedFile().getPath();
            	if(!filePath.endsWith(".csv"))
            		filePath += ".csv";
            	parser.exportToCSV(filePath);
            }
	    }
	}
	@Override
	public void focusGained(FocusEvent e) {
		filePath.setText(null);
		fileLoaded = false;
	}
	@Override
	public void focusLost(FocusEvent e) {
	
	}
}
