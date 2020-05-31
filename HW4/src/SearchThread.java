import java.io.File;
import java.util.Vector;

public class SearchThread implements Runnable {
	String path = new String();
	String word = new String();
	File dir;
	Vector<File> subfiles = new Vector<File>();
	
	public SearchThread(String path, String word){
		this.path = path;
		this.word = word;
		dir = new File(path);
	}
	@Override
	public void run() {
		findSubFiles(dir, word, subfiles);
		
		SearchWindow.ThreadResult(subfiles);
	}

	public static void findSubFiles(File parentFile, String word, Vector<File> subFiles) { 
		
		if (parentFile.isFile()) {
			//조건 검사!!!!!!!!!!
			//와일드 카드 구현
			subFiles.add(parentFile); 
		} 
		
		else if (parentFile.isDirectory()) { 
			subFiles.add(parentFile); 
			
			File[] childFiles = parentFile.listFiles(); 
			
			for (File childFile : childFiles) { 
				findSubFiles(childFile, word, subFiles); 
				} 
			} 
	}
	
}
