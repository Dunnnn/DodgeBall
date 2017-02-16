import javax.swing.JFrame;



public class DodgeBall
{
	public static void main(String[] args)
	{
		
		JFrame frame = new JFrame("DodgeBall!");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		

		frame.getContentPane().add(new GamePanel());
		
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);
				
	}
	
}
