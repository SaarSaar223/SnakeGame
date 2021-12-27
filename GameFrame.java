import javax.swing.JFrame;
import javax.swing.Timer;

public class GameFrame extends JFrame{
	public boolean create = true;
	Timer timer;
	static final int DELAY = 75;
	
	GameFrame(){
		GamePanel panel = new GamePanel();
		
		this.add(panel);
		this.setTitle("Snake");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.pack();
		this.setVisible(true);
		this.setLocationRelativeTo(null); 
	}
	
	}

