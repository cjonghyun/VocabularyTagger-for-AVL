package gui;
import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import taggerengine.Parser;
import taggerengine.Highlight;

import java.util.List;

import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class PosTagger extends JFrame implements ActionListener{
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
	private JTextArea taggedText;
	private JScrollPane originalScroll;
	private JScrollPane taggedScroll;
	private JButton exportButton;
	private boolean fileLoaded = false;
	private File inputFile;
	public PosTagger(){
		this.setSize(900,900);
		splitPane = new JSplitPane();
		fc = new JFileChooser();
		loadLabel = new JLabel("Load File"); 
		openButton = new JButton("File");
		filePath = new JTextField(20);
		filePath.setEditable(false);
		originalTextPane = new JPanel();
		filePanel = new JPanel();
		originalText = new JTextPane();
		originalText.setEditable(false);
		originalTextPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		originalScroll = new JScrollPane(originalText,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		parseButton = new JButton("Analyze");
		taggedText = new JTextArea();
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
		splitPane.setDividerLocation(450);
		this.add(splitPane);
		openButton.addActionListener(this);
		parseButton.addActionListener(this);
		exportButton.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    if (e.getSource() == openButton) {
	    	returnVal = fc.showOpenDialog(openButton);
	    	 if (returnVal == JFileChooser.APPROVE_OPTION) {
	    		try{
	                inputFile = fc.getSelectedFile();
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
	                sc.close();
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
		        parser = new Parser(inputFile.getPath());
		        List<Highlight> highlighted = parser.getHighlighted();
		        String output = parser.toString();
		        try{
			        DefaultStyledDocument document = new DefaultStyledDocument();
		            StyleContext styleContext = new StyleContext();
		            Style defaultStyle = styleContext.getStyle(StyleContext.DEFAULT_STYLE);
		            Style highlightStyle = styleContext.addStyle("ConstantWidth", null);
		            StyleConstants.setForeground(highlightStyle, Color.RED);
		            originalText.setDocument(document);
		            int len = highlighted.size();
		            for(int i=len -1; i>=0;i--){
		            	boolean type = highlighted.get(i).getFlag();
		            	boolean eos = highlighted.get(i).isEndofSentence();
		            	String breaker = null;
		            	if(eos){
		            		breaker = "\n";
		            	}
		            	else
		            		breaker = " ";
		            	if(type){
		            		document.insertString(0, highlighted.get(i).getWord() + breaker, highlightStyle);
		            	}
		            	else{
		            		document.insertString(0, highlighted.get(i).getWord() + breaker + breaker, defaultStyle);
		            	}	

		            }
		            originalText.setCaretPosition(0);
			    	taggedText.setText(output);
			        taggedText.setCaretPosition(0);
		        }
		        catch (Exception e2){
		        	e2.printStackTrace();
		        }
	    	}
	    }
	    if(e.getSource() == exportButton){
            JFileChooser exportFile = new JFileChooser();
//            exportFile.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("CSV File","csv"));
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
}
