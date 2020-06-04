import java.io.File;
import java.util.Vector;

import javax.swing.JOptionPane;

public class SearchThread implements Runnable {
	static String path = new String();
	String word = new String();
	File dir;
	Vector<File> subfiles = new Vector<File>();
	static boolean b;

	public SearchThread(String path, String word) {
		this.path = path;
		this.word = word;
		dir = new File(path);
	}

	@Override
	public void run() {

		b = true;
		//파일 탐색 함수
		findSubFiles(dir, word, subfiles);
		if (b) {
			JOptionPane.showMessageDialog(null, "검색 완료", "SEARCH FINISHED", JOptionPane.OK_OPTION);
		}

	}
	//파일 탐색 함수
	public static void findSubFiles(File parentFile, String word, Vector<File> subFiles) {
		if (b == false) {
			return;
		}
		if (parentFile.isFile()) {
			
			String filename = parentFile.getName();
			System.out.println("file : " + filename);
			// 와일드 카드 구현
			if (match(filename.toString(), word) == true) {
				subFiles.add(parentFile);

				SearchWindow.changelabel(parentFile.getParent());
				SearchWindow.ThreadResult(parentFile);
				// System.out.println(parentFile);
			}

		}

		//디렉토리일 경우, 파일 리스트에 추가하지 않는다.
		//그리고 디렉토리 안에 있는 요소들에 대해 재귀적으로 파일을 탐색한다.
		else if (parentFile.isDirectory()) {
			if (parentFile.toString() == path) {

				// String filename = parentFile.toString();
				// filename = filename.substring(path.length()+1);

				// if(match(filename, word) == true) {
				// subFiles.add(parentFile);

				SearchWindow.changelabel(parentFile.toString());
				// SearchWindow.ThreadResult(parentFile);

				System.out.println(parentFile);
				// }

			}

			//재귀적으로 파일 탐색
			File[] childFiles = parentFile.listFiles();
			for (File childFile : childFiles) {
				//재귀적으로 파일 탐색
				findSubFiles(childFile, word, subFiles);
			}
		}
		

	}

	//와일드 카드 구현
	static boolean match(String filename, String word) {
		int pos = 0;

		while ((pos < filename.length()) && (pos < word.length())
				&& ((word.charAt(pos) == '?') || (word.charAt(pos) == filename.charAt(pos)))) {
			pos++;
		}

		if (pos == word.length()) {
			return pos == filename.length();
		}

		if (word.charAt(pos) == '*') {
			for (int i = 0; pos + i <= filename.length(); i++) {
				if (match(word.substring(pos + 1), filename.substring(pos + i))) {
					return true;
				}
			}
		}

		return false;

	}

}
