package TestingOnly;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;


public class WaterTank {

	private JFrame frame;
	private WaterTankPanel panel;
	private static boolean increaseLevel = true;
	private int waterLevel = 0;
	private static int waterAnimationBottom = 0;
	private static int waterAnimationTop = 0;
	private static Random rand;
	private int change = 1;

	/**
	 * Launch the application.
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws InterruptedException {
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.acryl.AcrylLookAndFeel");
		} catch (Exception e) { }
		rand = new Random();
		final WaterTank window = new WaterTank();
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
					
		while(true){
			EventQueue.invokeLater(new Runnable() {
				public void run() {
			window.panel.paint(window.panel.getGraphics());
			window.panel.update(window.panel.getGraphics());
			window.frame.repaint();
//			waterAnimation = !isFilling ? 0 : waterAnimation;
				}
			});
			Thread.sleep(30);
			
		}
	}

	/**
	 * Create the application.
	 */
	public WaterTank() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Water tank simulator");
		frame.addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent arg0) {
				if(arg0.getKeyChar() == 't'){
					if(increaseLevel){
						increaseLevel = false;
					}else{
						increaseLevel = waterAnimationBottom == 330 && waterAnimationTop == 330;
					}
				}
				
			}
			
			public void keyReleased(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		panel = new WaterTankPanel();
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		frame.add(panel);
		frame.setSize(270, 440);
		JLabel label = new JLabel("Press T to toggle water flow");
		label.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		frame.add(label);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
	}
	
class WaterTankPanel extends JPanel{
	private static final long serialVersionUID = 1L;

	public WaterTankPanel(){
	}
	
	public void paint 	(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
//     	RenderingHints rh = new RenderingHints(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//     	rh.add(new RenderingHints(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE));
//     	rh.add(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
//     	rh.add(new RenderingHints(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON));
//        g2.setRenderingHints(rh);
     	g2.setColor(interpolateColor(Color.DARK_GRAY, Color.GRAY, 0.5f));
     	g2.fillRoundRect(12,14,242,390,20,20);
     	g2.setColor(interpolateColor(new Color(50,250,255), new Color(50,50,255), 0.500f));
     	g2.fillRoundRect(20, 395 - waterLevel, 225, waterLevel, 20, 20);
     	g2.setColor(Color.DARK_GRAY);
     	g2.fillRoundRect(218, 10, 7, 330, 4, 4);
     	g2.setColor(interpolateColor(new Color(50,250,255), new Color(50,50,255), 0.500f));
     	if(increaseLevel || (waterAnimationTop != 330)){
	     	g2.fillRoundRect(219, 11 + waterAnimationTop, 5, waterAnimationBottom - waterAnimationTop, 4, 4);
	     	if(waterAnimationBottom < 330){
	     		waterAnimationBottom += increaseLevel ? 5 : 10;
	     	}else if(waterAnimationTop < 330){
	     		waterAnimationTop += increaseLevel ? 5 : 10;
	     		waterAnimationBottom = waterAnimationBottom > 330 ? 330 : waterAnimationBottom;
	     	}else if(waterAnimationTop >= 330){
	     		waterAnimationBottom = waterAnimationBottom == 330 ? 0 : waterAnimationBottom;
	     		waterAnimationTop = waterAnimationTop >= 330 ? 0 : waterAnimationTop;
	     		
	     	}
     	}
     	g2.setColor(interpolateColor(Color.DARK_GRAY, Color.GRAY, 0.5f));
     	for(int i = 1; i < 19; i++){
         	if((increaseLevel || waterAnimationTop < 330) && waterLevel > (i * 20)){
         		g2.fillRoundRect(limitAnimation(180 - ((i * 10)) + rand.nextInt(40 + (i * 10)), 40, 200), 385- ((i - 1) * 20), limitAnimation(i, 0, 5), limitAnimation(i, 0, 5), 360, 360);
         	}
     	}
     	g2.setColor(Color.WHITE);
     	g2.drawLine(80, 20, 80, 394);
     	g2.drawLine(81, 20, 81, 394);
     	for(int i = 20; i < 395; i+= 50){
     		g2.drawLine(70, i-1, 91, i-1);
     		g2.drawLine(70, i, 91, i);
     	}
     	waterLevel += change;
     	if(waterLevel == 375 || waterLevel == 0){
     		change = - change;
     	}
  	}
	
	private int limitAnimation(int i, int j, int k){
		return i > j ? (i < k ? i : k) : j;
	}
  	
  	private java.awt.Color interpolateColor(final Color COLOR1, final Color COLOR2, float fraction)
        {            
            final float INT_TO_FLOAT_CONST = 1f / 255f;
            fraction = Math.min(fraction, 1f);
            fraction = Math.max(fraction, 0f);
            
            final float RED1 = COLOR1.getRed() * INT_TO_FLOAT_CONST;
            final float GREEN1 = COLOR1.getGreen() * INT_TO_FLOAT_CONST;
            final float BLUE1 = COLOR1.getBlue() * INT_TO_FLOAT_CONST;
            final float ALPHA1 = COLOR1.getAlpha() * INT_TO_FLOAT_CONST;

            final float RED2 = COLOR2.getRed() * INT_TO_FLOAT_CONST;
            final float GREEN2 = COLOR2.getGreen() * INT_TO_FLOAT_CONST;
            final float BLUE2 = COLOR2.getBlue() * INT_TO_FLOAT_CONST;
            final float ALPHA2 = COLOR2.getAlpha() * INT_TO_FLOAT_CONST;

            final float DELTA_RED = RED2 - RED1;
            final float DELTA_GREEN = GREEN2 - GREEN1;
            final float DELTA_BLUE = BLUE2 - BLUE1;
            final float DELTA_ALPHA = ALPHA2 - ALPHA1;

            float red = RED1 + (DELTA_RED * fraction);
            float green = GREEN1 + (DELTA_GREEN * fraction);
            float blue = BLUE1 + (DELTA_BLUE * fraction);
            float alpha = ALPHA1 + (DELTA_ALPHA * fraction);

            red = Math.min(red, 1f);
            red = Math.max(red, 0f);
            green = Math.min(green, 1f);
            green = Math.max(green, 0f);
            blue = Math.min(blue, 1f);
            blue = Math.max(blue, 0f);
            alpha = Math.min(alpha, 1f);
            alpha = Math.max(alpha, 0f);

            return new Color(red, green, blue, alpha);        
        }
}
}
