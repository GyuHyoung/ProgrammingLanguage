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
		//���� Ž�� �Լ�
		findSubFiles(dir, word, subfiles);
		if (b) {
			JOptionPane.showMessageDialog(null, "�˻� �Ϸ�", "SEARCH FINISHED", JOptionPane.OK_OPTION);
		}

	}
	//���� Ž�� �Լ�
	public static void findSubFiles(File parentFile, String word, Vector<File> subFiles) {
		if (b == false) {
			return;
		}
		if (parentFile.isFile()) {
			
			String filename = parentFile.getName();
			System.out.println("file : " + filename);
			// ���ϵ� ī�� ����
			if (match(filename.toString(), word) == true) {
				subFiles.add(parentFile);

				SearchWindow.changelabel(parentFile.getParent());
				SearchWindow.ThreadResult(parentFile);
				// System.out.println(parentFile);
			}

		}

		//���丮�� ���, ���� ����Ʈ�� �߰����� �ʴ´�.
		//�׸��� ���丮 �ȿ� �ִ� ��ҵ鿡 ���� ��������� ������ Ž���Ѵ�.
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

			//��������� ���� Ž��
			File[] childFiles = parentFile.listFiles();
			for (File childFile : childFiles) {
				//��������� ���� Ž��
				findSubFiles(childFile, word, subFiles);
			}
		}
		

	}

	//���ϵ� ī�� ����
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
