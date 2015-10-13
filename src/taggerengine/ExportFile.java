package taggerengine;
import java.io.FileInputStream;
import java.io.FileWriter;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
public class ExportFile {
	private String fileName;
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "id,word,pos,count";
    PosList result;
	public ExportFile(String fileName,PosList result){
		this.fileName = fileName;
		this.result = result;
	}
	public void exportExcel(){
		try{
			POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream("excelfile.xls"));
			HSSFWorkbook workbook = new HSSFWorkbook(fs);
			}
			catch(Exception e){
				e.printStackTrace();
			}
	}
	public void exportCsv(){
		FileWriter fileWriter = null;
		try{
			fileWriter = new FileWriter(fileName);
			fileWriter.append(FILE_HEADER.toString());
			fileWriter.append(NEW_LINE_SEPARATOR);
			
			for(Pos p:result.getList()){
				fileWriter.append(String.valueOf(p.getId()));
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(p.getWord());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(p.getTag());
				fileWriter.append(COMMA_DELIMITER);
				fileWriter.append(String.valueOf(p.getCount()));
				fileWriter.append(NEW_LINE_SEPARATOR);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
		try{
			fileWriter.flush();
			fileWriter.close();
		}
		catch(Exception e){
			System.out.println("Error with closing fileWriter");
			e.printStackTrace();
		}
	}
}
