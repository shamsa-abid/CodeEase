package lums.cbrs.completion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;

public class CodeCompletion {

	
	
	private static final String CodeEaseDatabaseURL = "F:\\";
	
	private ArrayList<String> readFileMethodNames = new ArrayList<String>();
	private ArrayList<String> writeFileMethodNames = new ArrayList<String>();
	
	private ArrayList<String> readFileFriendMethodNames = new ArrayList<String>();
	private ArrayList<String> writeFileFriendMethodNames = new ArrayList<String>();
	
	private ArrayList<String> ExtractFileNameMethodNames = new ArrayList<String>();
	private ArrayList<String> ExtractFileNameFriendMethodNames = new ArrayList<String>();
	
	private ArrayList<String> minMethodNames = new ArrayList<String>();
	private ArrayList<String> minFriendMethodNames = new ArrayList<String>();
	private ArrayList<String> maxMethodNames = new ArrayList<String>();
	private ArrayList<String> maxFriendMethodNames = new ArrayList<String>();

	public CodeCompletion()
	{
		readFileMethodNames.add("readFile(String fileName)");
		readFileMethodNames.add("readFile()");
		readFileMethodNames.add("readFile()");
		readFileMethodNames.add("readSmallTextFile(String aFileName)");		
		readFileMethodNames.add("readLargerTextFile(String aFileName)");
		readFileMethodNames.add("readLargerTextFileAlternate(String aFileName)");
		
		
		
		readFileFriendMethodNames.add("getFileNameWithoutExtensiotn(File file)");
		readFileFriendMethodNames.add(" getFileNameWithoutExtension(File file)");
		readFileFriendMethodNames.add("getFilepath()");		
		readFileFriendMethodNames.add("copyFile(File source, File destination)");	
		readFileFriendMethodNames.add("writeFileAsBytes(String fullPath, byte[] bytes)");	
		readFileFriendMethodNames.add("readPropertiesFile(String canonicalFilename)");	
		readFileFriendMethodNames.add("readPropertiesFileAsMap(String filename, String delimiter)");	
		readFileFriendMethodNames.add("readFileAsListOfStrings(String filename)");	
		readFileFriendMethodNames.add(" readFileAsString(String filename)");	
		
	
		writeFileMethodNames.add("writeFile(String fileName)");
		writeFileMethodNames.add("writeFile()");
		writeFileMethodNames.add("writeSmallTextFile(List<String> aLines, String aFileName)");
		writeFileMethodNames.add("writeLargerTextFile(String aFileName, List<String> aLines)");
		writeFileMethodNames.add("writeToTextFile(String fileName, String content)");
		writeFileMethodNames.add("writeFile()");
		
		
		writeFileFriendMethodNames.add("getFileNameWithoutExtensiotn(File file)");
		writeFileFriendMethodNames.add(" getFileNameWithoutExtension(File file)");
		writeFileFriendMethodNames.add("getFilepath()");		
		writeFileFriendMethodNames.add("copyFile(File source, File destination)");	
		writeFileFriendMethodNames.add("writeFileAsBytes(String fullPath, byte[] bytes)");	
		writeFileFriendMethodNames.add("readPropertiesFile(String canonicalFilename)");	
		writeFileFriendMethodNames.add("readPropertiesFileAsMap(String filename, String delimiter)");	
		writeFileFriendMethodNames.add("readFileAsListOfStrings(String filename)");	
		writeFileFriendMethodNames.add(" readFileAsString(String filename)");
			
	
		
		ExtractFileNameMethodNames.add("extractFileName( String filePathName )");
		ExtractFileNameMethodNames.add("extractFileName( String filePath )");
		ExtractFileNameMethodNames.add("extractFileName( String filePath )");		
		ExtractFileNameMethodNames.add("FileName( String strfullPath)");
		ExtractFileNameMethodNames.add("getFileName(URL extUrl)");
		ExtractFileNameMethodNames.add("getFileNameFromUrl(URL url)");
		ExtractFileNameMethodNames.add("getFileName(String url)");
		
		
		ExtractFileNameFriendMethodNames.add("getFileNameWithoutExtensiotn(File file)");
		ExtractFileNameFriendMethodNames.add(" getFileNameWithoutExtension(File file)");
		ExtractFileNameFriendMethodNames.add("getFilepath()");		
		ExtractFileNameFriendMethodNames.add("copyFile(File source, File destination)");	
		ExtractFileNameFriendMethodNames.add("writeFileAsBytes(String fullPath, byte[] bytes)");	
		ExtractFileNameFriendMethodNames.add("readPropertiesFile(String canonicalFilename)");	
		ExtractFileNameFriendMethodNames.add("readPropertiesFileAsMap(String filename, String delimiter)");	
		ExtractFileNameFriendMethodNames.add("readFileAsListOfStrings(String filename)");	
		ExtractFileNameFriendMethodNames.add(" readFileAsString(String filename)");	
		
		minMethodNames.add("getMinValue(int[] array)");
		minMethodNames.add("getMinFloat(float[] data)");
		minMethodNames.add("getMin(int[] inputArray)");
		minMethodNames.add("getMin(int[] numbers, int a, int n)");
		minMethodNames.add("minValue(int[] numbers)");
		
		
		minFriendMethodNames.add("Average()");
		minFriendMethodNames.add("find_sum(int [ ] value)");
		minFriendMethodNames.add("concatinate(int [] intArray ,int [] intArray2)");
		minFriendMethodNames.add("ReverseArray(int [] intArray)");
		minFriendMethodNames.add("RemoveElement(int [] intArray, int element)");
		
		
		maxMethodNames.add("getMaxValue(int[] array)");
		maxMethodNames.add("maxValue(char[] chars)");
		maxMethodNames.add("arrayMax(double[] arr)");
		maxMethodNames.add("maxValue(final int[] intArray)");
		maxMethodNames.add("getMaxFloat(float[] data)");
		maxMethodNames.add("getMax(int[] inputArray)");
		maxMethodNames.add("findMinMax(int[] arr)");
		maxMethodNames.add("Maxmin()");
		maxMethodNames.add("minmax0(int[] a)");
		maxMethodNames.add("minmax1(int[] a)");
		maxMethodNames.add("minmax2(int[] a)");
		
		
		
		maxFriendMethodNames.add("Average()");
		maxFriendMethodNames.add("find_sum(int [ ] value)");
		maxFriendMethodNames.add("concatinate(int [] intArray ,int [] intArray2)");
		maxFriendMethodNames.add(" ReverseArray(int [] intArray)");
		maxFriendMethodNames.add("RemoveElement(int [] intArray, int element)");
	}
	public ArrayList<String> getFileReadMethodBody(int elementNumber) 
	{
		ArrayList<String> result = new ArrayList<String>();		
		switch(elementNumber)
		{
		case 1:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ReadFile\\readFile1.txt");
			break;
		case 2:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ReadFile\\readFile2.txt");
			break;
		case 3:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ReadFile\\readFile3.txt");
			break;
		case 4:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ReadFile\\readFile4.txt");
			break;
		case 5:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ReadFile\\readFile5.txt");
			break;	
		case 6:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ReadFile\\readFile6.txt");
			break;
		}
		return result;
	}
	
	public ArrayList<String> getFileReadFriendMethodBody(int elementNumber) 
	{
		ArrayList<String> result = new ArrayList<String>();		
		switch(elementNumber)
		{
		case 1:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate1.txt");
			break;
		case 2:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate2.txt");
			break;
		case 3:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate3.txt");
			break;
		case 4:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate4.txt");
			break;
		case 5:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate5.txt");
			break;	
		case 6:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate6.txt");
			break;
		case 7:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate7.txt");
			break;
		case 8:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate8.txt");
			break;
		case 9:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate9.txt");
			break;
		}
		return result;
	}
	
	
	
	public ArrayList<String> getFileWriteMethodBody(int elementNumber)
	{
		ArrayList<String> result = new ArrayList<String>();		
		switch(elementNumber)
		{
		case 1:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\WriteFile\\writeFile1.txt");
			break;
		case 2:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\WriteFile\\writeFile2.txt");
			break;
		case 3:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\WriteFile\\writeFile3.txt");
			break;
		case 4:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\WriteFile\\writeFile4.txt");
			break;
		case 5:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\WriteFile\\writeFile5.txt");
			break;	
		case 6:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\WriteFile\\writeFile6.txt");
			break;
		}
		return result;
	}
	
	public ArrayList<String> getFileWriteFriendMethodBody(int elementNumber) 
	{
		ArrayList<String> result = new ArrayList<String>();		
		switch(elementNumber)
		{
		case 1:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate1.txt");
			break;
		case 2:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate2.txt");
			break;
		case 3:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate3.txt");
			break;
		case 4:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate4.txt");
			break;
		case 5:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate5.txt");
			break;	
		case 6:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate6.txt");
			break;
		case 7:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate7.txt");
			break;
		case 8:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate8.txt");
			break;
		case 9:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate9.txt");
			break;
		}
		return result;
	}
	
	
	public ArrayList<String> getExtractFileNameMethodBody(int elementNumber) 
	{
		ArrayList<String> result = new ArrayList<String>();		
		switch(elementNumber)
		{
		case 1:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractFileName\\ExtractFileName1.txt");
			break;
		case 2:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractFileName\\ExtractFileName2.txt");
			break;
		case 3:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractFileName\\ExtractFileName3.txt");
			break;
		case 4:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractFileName\\ExtractFileName4.txt");
			break;
		case 5:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractFileName\\ExtractFileName5.txt");
			break;	
		case 6:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractFileName\\ExtractFileName6.txt");
			break;
		case 7:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractFileName\\ExtractFileName7.txt");
			break;
		}
		return result;
	}
	
	public ArrayList<String> getExtractFileNameFriendMethodBody(int elementNumber) 
	{
		ArrayList<String> result = new ArrayList<String>();		
		switch(elementNumber)
		{
		case 1:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate1.txt");
			break;
		case 2:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate2.txt");
			break;
		case 3:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate3.txt");
			break;
		case 4:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate4.txt");
			break;
		case 5:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate5.txt");
			break;	
		case 6:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate6.txt");
			break;
		case 7:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate7.txt");
			break;
		case 8:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate8.txt");
			break;
		case 9:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\ExtractReadWriteFriend\\Alternate9.txt");
			break;
		}
		return result;
	}
	
	public ArrayList<String> getMinMethodBody(int elementNumber) 
	{
		ArrayList<String> result = new ArrayList<String>();		
		switch(elementNumber)
		{
		case 1:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Min\\Min1.txt");
			break;
		case 2:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Min\\Min2.txt");
			break;
		case 3:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Min\\Min3.txt");
			break;
		case 4:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Min\\Min4.txt");
			break;
		case 5:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Min\\Min5.txt");
			break;
	
		}
		return result;
	}
	public ArrayList<String> getMinFriendMethodBody(int elementNumber) 
	{
		ArrayList<String> result = new ArrayList<String>();		
		switch(elementNumber)
		{
		case 1:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\MinFriend\\Alternate1.txt");
			break;
		case 2:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\MinFriend\\Alternate2.txt");
			break;
		case 3:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\MinFriend\\Alternate3.txt");
			break;
		case 4:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\MinFriend\\Alternate4.txt");
			break;
		case 5:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\MinFriend\\Alternate5.txt");
			break;
			
			
		}
		return result;
	}
	public ArrayList<String> getMaxMethodBody(int elementNumber) 
	{
		ArrayList<String> result = new ArrayList<String>();		
		switch(elementNumber)
		{
		case 1:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Max\\Max1.txt");
			break;
		case 2:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Max\\Max2.txt");
			break;
		case 3:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Max\\Max3.txt");
			break;
		case 4:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Max\\Max4.txt");
			break;
		case 5:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Max\\Max5.txt");
			break;
		case 6:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Max\\Maxmin.txt");
			break;
		case 7:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Max\\Maxmin2.txt");
			break;
		case 8:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Max\\Maxmin3.txt");
			break;
		case 9:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Max\\Maxmin4.txt");
			break;
		case 10:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\Max\\Maxmn5.txt");
			break;
		}
		return result;
	}
	public ArrayList<String> getMaxFriendMethodBody(int elementNumber) 
	{
		ArrayList<String> result = new ArrayList<String>();		
		switch(elementNumber)
		{
		case 1:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\MaxFriend\\Alternate1.txt");
			break;
		case 2:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\MaxFriend\\Alternate2.txt");
			break;
		case 3:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\MaxFriend\\Alternate3.txt");
			break;
		case 4:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\MaxFriend\\Alternate4.txt");
			break;
		case 5:
			result = fileRead(CodeEaseDatabaseURL + "CodeEaseDatabase\\MaxFriend\\Alternate5.txt");
			break;
		}
		return result;
	}
	
	public static ArrayList<String> fileRead(String pathName){
		File f;
		String s;
		FileReader fr = null;
		BufferedReader br = null;
		ArrayList<String> sl = new ArrayList<String>();
		try {
			//Class.getResource(fileName);
			f = new File(pathName);
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			while ((s = br.readLine()) != null) {
				sl.add(s);
				System.out.println(s);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)
					br.close();
				if (fr != null)
					fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sl;
	}
	
	public void setMethodNames(ArrayList<String> methodNames) {
		this.readFileMethodNames = methodNames;
	}

	public ArrayList<String> getReadFileMethodNames()
	{
		return readFileMethodNames;
	}
	public ArrayList<String> getReadFileFriendMethodNames()
	{
		return readFileFriendMethodNames;
	}
	public ArrayList<String> getWriteFileMethodNames()
	{
		return writeFileMethodNames;
	}
	public ArrayList<String> getWriteFileFriendMethodNames()
	{
		return writeFileFriendMethodNames;
	}
	public ArrayList<String> getMinMethodNames()
	{
		return minMethodNames;
	}
	public ArrayList<String> getMaxMethodNames()
	{
		return maxMethodNames;
	}
	public ArrayList<String> getMinFriendMethodNames()
	{
		return minFriendMethodNames;
	}
	public ArrayList<String> getMaxFriendMethodNames()
	{
		return maxFriendMethodNames;
	}
	public ArrayList<String> getExtractFileNameMethodNames()
	{
		return ExtractFileNameMethodNames;
	}
	public ArrayList<String> getExtractFileNameFriendMethodNames()
	{
		return ExtractFileNameFriendMethodNames;
	}
	
	public static void main(String args[])
	{}
	
	

}
