import javax.swing.JOptionPane;

public class MyException extends Exception {
	
	MyException(String msg){ 
		super(msg);
		System.out.println(msg);
        //JOptionPane err = new JOptionPane();
        JOptionPane.showMessageDialog(null, msg, "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
    }
	
	


}
