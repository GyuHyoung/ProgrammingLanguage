import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.border.*;

 
public class MyGUI extends JFrame
{
    JLabel la1,la2;
    public JTextField t1, t2;
    JPanel pathPanel,searchPanel,buttonPanel;
    JButton b1,b2;
    JTextArea content;
    
    public MyGUI()
    {
    	super( "���� �˻� ���α׷�" );
          
    	// FlowLayout���
        setLayout( new FlowLayout() );
          
        pathPanel = new JPanel();
        searchPanel = new JPanel();
        
        la1 = new JLabel("��θ�");
        la2 = new JLabel("�˻���");
        
        t1 = new JTextField(30);
        t2 = new JTextField(30);
        
        pathPanel.add(la1);
        pathPanel.add(t1);
        
        searchPanel.add(la2);
        searchPanel.add(t2);
        
        buttonPanel = new JPanel();
        b1 = new JButton("�˻�");
        b2 = new JButton("����");
        buttonPanel.add( b1 );
        buttonPanel.add( b2 );
		
        b1.addActionListener(new EventHandler());
        
        b2.addActionListener(new ActionListener() {
      	  public void actionPerformed(ActionEvent e) {
      		  System.exit(0);
      	  }
      	  });
       add(pathPanel);
       add(searchPanel);
       add(buttonPanel);
         
       setSize( 500, 300 );
       setVisible(true);
       setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    //EVENT HANDER CLASS
    class EventHandler implements ActionListener{
    	
    	String path = new String();
    	String search = new String();

    	//FIND EXCEPTION METHOD
    	void findException(int flag, String str) throws MyException{
    		//�ƹ��͵� �Է����� �ʰ� �˻� ��ư�� ���� ���
    		if(str.length() == 0) {
    			throw new MyException("�Էµ� ���� �����ϴ�.");
    		}
    		
    		//�����η� �Էµ��� ���� ���
    		if(flag == 1 && str.indexOf(".") == 0) {
    			throw new MyException("�����η� �Է��Ͽ� �ּ���.");
    		}
    		
    		//�����쿡�� ������� �ʴ� ���ڰ� ������ ���
    		if(flag == 1) {
        		//if(str.contains("\\") || str.contains("/") || str.contains(":")|| str.contains("\"") || str.contains(">")|| str.contains("<")|| str.contains("|")|| str.contains("?")) {
        		//	throw new MyException("��θ��� \\, /, :, \", >, <, |, ? �����ϰ� �Է��Ͽ� �ּ���");
        		//}
    		}
    		else if(flag == 2) {
        		if(str.contains("\\") || str.contains("/") || str.contains(":")|| str.contains("\"") || str.contains(">")|| str.contains("<")|| str.contains("|")) {
        			throw new MyException("�˻���� \\, /, :, \", >, <, | �����ϰ� �Է��Ͽ� �ּ���");
        		}
    		}
    		
    		//�Էµ� ��ΰ� �������� ���� ���
    		File dir = new File(str);
    		if(flag== 1 && !dir.isDirectory()) {
    			throw new MyException("�ش� ���丮�� �������� �ʽ��ϴ�.");
    		}
    		

    		
    		
    	}
    	
    	//ACTION PERFORMED METHOD
    	@Override
        public void actionPerformed(ActionEvent e) {
    		
        	if(e.getActionCommand() == "�˻�") {
        		this.path = t1.getText();
        		this.search = t2.getText();
        		System.out.println(path);
        		System.out.println(search);
        		
        		//����ó��
        		try {
        			
        			findException(1, path);
        			findException(2, search);
        			//�˻��ϴ� â
        			new SearchWindow(path, search);
        			

        			//������ start ���⼭??
        			
        		}
        		catch(MyException mye){
        			//mye.printStackTrace();
        			t1.setText("");
        			t2.setText("");
        			
        		}
      
        	}
    	}   	
        
    }
    
    

    
    
}