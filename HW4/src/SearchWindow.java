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
    
	// ��ư�� �������� ��������� �� â�� ������ Ŭ����
    SearchWindow(String path, String search) {
    	
    	this.path = path;
    	this.search = search;
    	
        setTitle("�˻��ϱ�");
        
        setLayout( new FlowLayout() );
        
        content = new JTextArea(25, 30);
//        content.setSize(500,500);
        btn_remove = new JButton("���ϻ���");
        btn_cancel = new JButton("�˻����");
        
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
    		if(e.getActionCommand() == "���ϻ���") {
    			
    		}
    		if(e.getActionCommand() == "�˻����") {
    			
    		}
        
      
        }   	
        
    }
}

