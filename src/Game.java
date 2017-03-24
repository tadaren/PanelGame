import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class Game extends JPanel{

	private final int SIZE = 300;

	private JFrame frame;

	private int count = 0;
	private JLabel timesLabel;
	private StageData sData;
	private boolean[][] stage1;
	private boolean[][] stage2;

	private JPanel[][] panel1;
	private JPanel[][] panel2;

	private JPanel basePlayPanel;

	public static void main(String[] args){
		new Game();
	}
	Game(){
		frame = new JFrame();
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setLocation(50,50);
		JPanel basePanel = new JPanel();
		CardLayout layout = new CardLayout();
		basePanel.setLayout(layout);
		frame.add(basePanel);

		JPanel startPanel = new JPanel();
		basePanel.add(startPanel, "startPanel");
		{
			startPanel.setPreferredSize(new Dimension(210,200));
			JPanel tp = new JPanel();
			tp.add(new JLabel("Panel Game"));
			startPanel.add(tp);
			File file = new File("./stage");
			String[] list = file.list();
			JList<String> stageList = new JList<>(list);
			stageList.setSelectedIndex(0);
			stageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			JScrollPane sp = new JScrollPane();
			sp.getViewport().setView(stageList);
			sp.setPreferredSize(new Dimension(200,100));
			startPanel.add(sp);

			JButton startButton = new JButton("START");
			startButton.addActionListener((ActionEvent e) -> {
				sData = new StageData("./stage/"+stageList.getSelectedValue());
				boolean[][] s = sData.getStage1();
				stage1 = new boolean[s.length][s[0].length];
				for(int i = 0; i < s.length; i++){
					stage1[i] = s[i].clone();
				}
				s = sData.getStage2();
				stage2 = new boolean[s.length][s[0].length];
				for(int i = 0; i < s.length; i++){
					stage2[i] = s[i].clone();
				}

				basePlayPanel = new JPanel();
				JPanel model = new JPanel();
				model.setPreferredSize(new Dimension(SIZE, SIZE));
				model.setLayout(new GridLayout(stage1.length, stage1[0].length));
				panel1 = new JPanel[stage1.length][stage1[0].length];
				for(int i = 0; i < stage1.length; i++){
					for(int j = 0; j < stage1[i].length; j++){
						panel1[i][j] = new JPanel();
						panel1[i][j].setBorder(new LineBorder(Color.BLACK,2));
						if(stage1[i][j])
							panel1[i][j].setBackground(Color.LIGHT_GRAY);
						else
							panel1[i][j].setBackground(Color.DARK_GRAY);
						model.add(panel1[i][j]);
					}
				}
				basePlayPanel.add(model);

				JPanel playingPanel = new JPanel();
				playingPanel.setPreferredSize(new Dimension(SIZE, SIZE));
				playingPanel.setLayout(new GridLayout(stage2.length, stage2[0].length));
				panel2 = new JPanel[stage2.length][stage2[0].length];
				for(int i = 0; i < stage2.length; i++){
					for(int j = 0; j < stage2[i].length; j++){
						panel2[i][j] = new JPanel();
						panel2[i][j].setBorder(new LineBorder(Color.BLACK,2));
						if(stage2[i][j])
							panel2[i][j].setBackground(Color.LIGHT_GRAY);
						else
							panel2[i][j].setBackground(Color.DARK_GRAY);
						playingPanel.add(panel2[i][j]);
					}
				}
				basePlayPanel.add(playingPanel);
				this.add(basePlayPanel, BorderLayout.CENTER);
				playingPanel.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e){
						super.mouseClicked(e);
						if(count != 0){
							int x = -1, y = -1;
							JPanel p = (JPanel) playingPanel.getComponentAt(e.getPoint());
							for(int i = 0; i < panel2.length; i++){
								for(int j = 0; j < panel2[i].length; j++){
									if(panel2[i][j].equals(p)){
										x = i;
										y = j;
										break;
									}
								}
							}
							for(int i = x - 1; i <= x + 1; i++){
								for(int j = y - 1; j <= y + 1; j++){
									try{
										stage2[i][j] = !stage2[i][j];
										if(stage2[i][j])
											panel2[i][j].setBackground(Color.LIGHT_GRAY);
										else
											panel2[i][j].setBackground(Color.DARK_GRAY);
									}catch(ArrayIndexOutOfBoundsException er){
									}
								}
							}
							timesLabel.setText("回数 : " + --count);
							if(count == 0){
								boolean b = true;
								for(int i = 0; i < stage1.length; i++){
									for(int j = 0; j < stage1[i].length; j++){
										if(stage1[i][j] != stage2[i][j]){
											b = false;
											break;
										}
									}
								}
								if(b){
									int option = JOptionPane.showConfirmDialog(frame, "CLEAR\nSTART画面に戻りますか", "CLEAR", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
									if(option == JOptionPane.YES_OPTION){
										Game.this.remove(basePlayPanel);
										basePlayPanel = null;
										layout.next(basePanel);
										frame.pack();
									}
								}
							}
						}
					}
				});

				count = sData.getTimes();
				timesLabel.setText("残り回数 : " + count);
				layout.next(basePanel);
				frame.pack();
			});

			stageList.addKeyListener(new KeyAdapter() {
				@Override
				public void keyTyped(KeyEvent e){
					super.keyTyped(e);
					if(e.getKeyCode() == KeyEvent.VK_ENTER){
						startButton.doClick();
					}
				}
			});

			JPanel bp = new JPanel();
			bp.add(startButton);
			startPanel.add(bp);
//			startPanel.add(startButton);
		}
		basePanel.add(this, "playPanel");
		setLayout(new BorderLayout());

		JPanel infoPanel = new JPanel();
		{
			timesLabel = new JLabel();
			infoPanel.add(timesLabel);
		}
		add(infoPanel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		add(buttonPanel, BorderLayout.SOUTH);

		JButton backButton = new JButton("BACK");
		backButton.addActionListener(e -> {
			this.remove(basePlayPanel);
			basePlayPanel = null;
			layout.next(basePanel);
			frame.pack();
		});
		buttonPanel.add(backButton);

		JButton resetButton = new JButton("RESET");
		resetButton.addActionListener(e -> {
			boolean[][] s = sData.getStage2();
			for(int i = 0; i < stage2.length; i++){
				for(int j = 0; j < stage2[i].length; j++){
					stage2[i][j] = s[i][j];
					if(stage2[i][j])
						panel2[i][j].setBackground(Color.LIGHT_GRAY);
					else
						panel2[i][j].setBackground(Color.DARK_GRAY);
				}
			}
			count = sData.getTimes();
			timesLabel.setText("残り回数 : " + count);
		});
		buttonPanel.add(resetButton);

		frame.pack();
		frame.setVisible(true);
	}
}
