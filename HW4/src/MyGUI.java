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
    	super( "파일 검색 프로그램" );
          
    	// FlowLayout사용
        setLayout( new FlowLayout() );
          
        pathPanel = new JPanel();
        searchPanel = new JPanel();
        
        la1 = new JLabel("경로명");
        la2 = new JLabel("검색어");
        
        t1 = new JTextField(30);
        t2 = new JTextField(30);
        
        pathPanel.add(la1);
        pathPanel.add(t1);
        
        searchPanel.add(la2);
        searchPanel.add(t2);
        
        buttonPanel = new JPanel();
        b1 = new JButton("검색");
        b2 = new JButton("종료");
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
    		//아무것도 입력하지 않고 검색 버튼을 누를 경우
    		if(str.length() == 0) {
    			throw new MyException("입력된 값이 없습니다.");
    		}
    		
    		//절대경로로 입력되지 않을 경우
    		if(flag == 1 && str.indexOf(".") == 0) {
    			throw new MyException("절대경로로 입력하여 주세요.");
    		}
    		
    		//윈도우에서 허용하지 않는 문자가 존재할 경우
    		if(flag == 1) {
        		//if(str.contains("\\") || str.contains("/") || str.contains(":")|| str.contains("\"") || str.contains(">")|| str.contains("<")|| str.contains("|")|| str.contains("?")) {
        		//	throw new MyException("경로명에서 \\, /, :, \", >, <, |, ? 제외하고 입력하여 주세요");
        		//}
    		}
    		else if(flag == 2) {
        		if(str.contains("\\") || str.contains("/") || str.contains(":")|| str.contains("\"") || str.contains(">")|| str.contains("<")|| str.contains("|")) {
        			throw new MyException("검색어에서 \\, /, :, \", >, <, | 제외하고 입력하여 주세요");
        		}
    		}
    		
    		//입력된 경로가 존재하지 않을 경우
    		File dir = new File(str);
    		if(flag== 1 && !dir.isDirectory()) {
    			throw new MyException("해당 디렉토리가 존재하지 않습니다.");
    		}
    		

    		
    		
    	}
    	
    	//ACTION PERFORMED METHOD
    	@Override
        public void actionPerformed(ActionEvent e) {
    		
        	if(e.getActionCommand() == "검색") {
        		this.path = t1.getText();
        		this.search = t2.getText();
        		System.out.println(path);
        		System.out.println(search);
        		
        		//예외처리
        		try {
        			
        			findException(1, path);
        			findException(2, search);
        			//검색하는 창
        			new SearchWindow(path, search);
        			
        			
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