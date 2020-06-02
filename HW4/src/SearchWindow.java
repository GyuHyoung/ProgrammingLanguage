import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class SearchWindow extends JFrame {
	static JTable table;
	JScrollPane scroll;
	JPanel btnPanel, dirPanel;
	JButton btn_remove, btn_cancel;
	JLabel curDir;
	static JLabel curDirStr;

	String path = new String();
	String search = new String();

	Thread t;

	String[] header = { "파일명", "파일크기", "파일수정일", "파일 위치" };

	// 버튼이 눌러지면 만들어지는 새 창을 정의한 클래스
	SearchWindow(String path, String search) {

		this.path = path;
		this.search = search;

		setTitle("검색하기");

		setLayout(new FlowLayout());

		curDir = new JLabel("현재 검색 폴더 : ");
		curDirStr = new JLabel(path);

		dirPanel = new JPanel();
		dirPanel.add(curDir);
		dirPanel.add(curDirStr);

		DefaultTableModel model = new DefaultTableModel(null, header);
		table = new JTable(model);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getColumn("파일 위치").setPreferredWidth(300);
		
		JScrollPane sc = new JScrollPane(table);

		btn_remove = new JButton("파일삭제");
		btn_remove.addActionListener(new EventHandler());

		btn_cancel = new JButton("검색취소");
		btn_cancel.addActionListener(new EventHandler());

		btnPanel = new JPanel();
		btnPanel.add(btn_remove);
		btnPanel.add(btn_cancel);

		add(dirPanel);
		add(sc);
		add(btnPanel);

		setSize(600, 600);
		// setResizable(false);

		setVisible(true);

		SearchThread st = new SearchThread(path, search);
		t = new Thread(st);

		t.start();

	}

	public static void changelabel(String str) {
		curDirStr.setText(str);
	}

	public static void ThreadResult(File f) {

		DefaultTableModel m = (DefaultTableModel) table.getModel();

		// "파일명", "파일크기", "파일수정일", "파일 위치"
		m.insertRow(table.getModel().getRowCount(),
				new Object[] { f.getName(), f.length(), f.lastModified(), f.getAbsolutePath() });

		table.updateUI();
	}

	// EVENT HANDER CLASS
	class EventHandler implements ActionListener {

		// ACTION PERFORMED METHOD
		@Override
		public void actionPerformed(ActionEvent e) {

			if (e.getActionCommand() == "파일삭제") {

				deletefile();

			}

			if (e.getActionCommand() == "검색취소") {
				try {
					SearchThread.b = false;
					t.join();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

		}

	}

	public synchronized void deletefile() {
		try {
			int row = table.getSelectedRow();
			String fpath = (String) table.getValueAt(row, 3);

			File delFile = new File(fpath);
			System.out.println(delFile.toString());

			// 파일존재여부확인
			if (delFile.exists()) {
				// 파일이 디렉토리인지 확인
				if (delFile.isDirectory()) {
					System.out.println("파일삭제 실패 - 디렉토리");
				}
				if (delFile.delete()) {

					DefaultTableModel m = (DefaultTableModel) table.getModel();
					m.removeRow(row);
					System.out.println("파일삭제 성공");

				} else {
					System.out.println("파일이 존재하지 않습니다.");
				}
			}

		} catch (ArrayIndexOutOfBoundsException e) {
			JOptionPane.showMessageDialog(null, "파일이 존재하지 않습니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
		}

	}

}
