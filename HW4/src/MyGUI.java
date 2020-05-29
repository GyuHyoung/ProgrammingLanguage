import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.border.*;

 
public class MyGUI extends JFrame
{
    JLabel la1,la2;
    public static JTextField t1, t2;
    JPanel pathPanel,searchPanel,buttonPanel;
    JButton b1,b2;
    JTextArea content;
    
    public MyGUI()
    {
    	super( "파일 검색 프로그램" );
          // FlowLayout사용
          setLayout( new FlowLayout() );
          
          // Border로 영역 생성
          EtchedBorder eborder =  new EtchedBorder();
          
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

    class EventHandler implements ActionListener {
    	String path = new String();
    	String search = new String();

    	@Override
        public void actionPerformed(ActionEvent e) {
    		
        	if(e.getActionCommand() == "검색") {
        		path = t1.getText();
        		search = t2.getText();
        		System.out.println(path);
        		System.out.println(search);
        		//예외처리
        		//검색기능 구현
        	}
        	
        }
    }
    
}