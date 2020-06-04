import javax.swing.JOptionPane;

public class MyException extends Exception {
	
	//경고창과 에러 메세지가 뜬다
	MyException(String msg){ 
		super(msg);
		System.out.println(msg);
		JOptionPane.showMessageDialog(null, msg, "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
    }
	
	


}
