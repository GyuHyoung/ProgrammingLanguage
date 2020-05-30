import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class SearchWindow extends JFrame{
    
	JPanel contentPanel,btnPanel;
	JButton btn_remove,btn_cancel;
    JTextArea content;
   
    String path = new String();
    String search = new String();
    
	// 버튼이 눌러지면 만들어지는 새 창을 정의한 클래스
    SearchWindow(String path, String search) {
    	
    	this.path = path;
    	this.search = search;
    	
        setTitle("검색하기");
        
        setLayout( new FlowLayout() );
        
        content = new JTextArea(25, 30);
//        content.setSize(500,500);
        btn_remove = new JButton("파일삭제");
        btn_cancel = new JButton("검색취소");
        
        btn_remove.addActionListener(new EventHandler());
        btn_cancel.addActionListener(new EventHandler());
        
        contentPanel = new JPanel();
        btnPanel = new JPanel();
        
        //setContentPane(contentPanel);
           
        contentPanel.add(content);
        btnPanel.add(btn_remove);
        btnPanel.add(btn_cancel);
        
        
        add(contentPanel);
        add(btnPanel);
        setSize(500,500);
        
        setResizable(false);
        setVisible(true);
        
        Thread t = new Thread(new SearchThread(path, search));
        
        t.start();
    }
    
    
  //EVENT HANDER CLASS
    class EventHandler implements ActionListener{
 
    	//ACTION PERFORMED METHOD
    	@Override
        public void actionPerformed(ActionEvent e) {
    		if(e.getActionCommand() == "파일삭제") {
    			
    		}
    		if(e.getActionCommand() == "검색취소") {
    			
    		}
        
      
        }   	
        
    }
}

