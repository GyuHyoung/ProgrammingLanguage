import java.io.File;
import java.util.Vector;

public class SearchThread implements Runnable {
	String path = new String();
	String word = new String();
	File dir;
	
	public SearchThread(String path, String word){
		this.path = path;
		this.word = word;
		dir = new File(path);
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Vector<File> subfiles = new Vector<File>();
			findSubFiles(dir, word, subfiles);
			
		}catch(InterruptedException ie) {
			ie.printStackTrace();
		}
	}

	public static void findSubFiles(File parentFile, String word, Vector<File> subFiles) { 
		
		if (parentFile.isFile()) {
			//조건 검사!!!!!!!!!!
			
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
