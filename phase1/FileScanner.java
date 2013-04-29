// scanner generator
// read lexical specifications
// generate primitive NFAs
package phase1;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.List;

public class FileScanner{
    private LinkedHashMap<String,String> regexTable = new LinkedHashMap<String,String>();
    private LinkedHashMap<String,String> tokenTable = new LinkedHashMap<String,String>();
    private final static Charset ENCODING = StandardCharsets.US_ASCII;

    public FileScanner(String file) throws IOException{
        List<String> text = readTextFile(file);
        LinkedHashMap<String,String> tempTable = regexTable;
        
        for(int i=0; i<text.size(); i++){
            String currLine = text.get(i);
            String[] splitString = (currLine.split(" "));
            if(splitString.length < 2){
            	tempTable = tokenTable;
            }
            else{
	            String value = splitString[1];
	            for(int j=2; j<splitString.length; j++){
	            		value = value + " " + splitString[j];
	            }
	            tempTable.put(splitString[0],value);
            }
        }
    }

    private List<String> readTextFile(String aFileName) throws IOException{
        Path path = Paths.get(aFileName);
        return Files.readAllLines(path, ENCODING);
    }
    
    public LinkedHashMap<String,String> getRegexTable(){
        return regexTable;
    }
    
    public LinkedHashMap<String,String> getTokenTable(){
        return tokenTable;
    }
}