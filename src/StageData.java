import javax.swing.*;
import java.io.*;
import java.util.Random;

class StageData{

	private int x;
	private int y;
	private int times;
	private boolean[][] stage1 = null;
	private boolean[][] stage2 = null;

	StageData(String fileName){
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
			String str = br.readLine();
			int stageType = Integer.parseInt(str);
			str = br.readLine();
			x = Integer.parseInt(str);
			str = br.readLine();
			y = Integer.parseInt(str);
			str = br.readLine();
			times = Integer.parseInt(str);
			switch(stageType){
				case 1: stage1 = readStage(br, x, y);
						stage2 = readStage(br, x, y);
						break;
				case 2: stage1 = readStage(br, x, y);
						stage2 = randomChangeStage(stage1, times);
						break;
				case 3: stage1 = randomStage(x, y);
						stage2 = randomChangeStage(stage1, times);
						break;
			}

		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null,"指定されたファイルが存在しません","ERROR",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"ファイルデータが正しくありません","ERROR",JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}
	}

	private boolean[][] readStage(BufferedReader br, int x, int y) throws IOException {
		boolean[][] stage = new boolean[x][y];
		String str = br.readLine();
		for(int i = 0; i < stage.length; i++){
			for(int j = 0; j < stage[i].length; j++){
				if(str.charAt(j) == '1')
					stage[i][j] = true;
			}
			str = br.readLine();
		}
		return stage;
	}

	private boolean[][] randomChangeStage(boolean[][] stage1, int times){
		boolean[][] stage = new boolean[stage1.length][stage1[0].length];
		for(int i = 0; i < stage1.length; i++){
			stage[i] = stage1[i].clone();
		}
		Random rand = new Random();
		for(int i = 0; i < times; i++){
			int x = rand.nextInt(stage.length);
			int y = rand.nextInt(stage[0].length);
			for(int j = -1; j <= 1; j++){
				for(int k = -1; k <= 1; k++){
					try{
						stage[x+j][y+k] = !stage[x+j][y+k];
					}catch(ArrayIndexOutOfBoundsException e){}
				}
			}
		}
		return stage;
	}

	private boolean[][] randomStage(int x, int y){
		boolean[][] stage = new boolean[x][y];
		Random rand = new Random();
		int yuragi = rand.nextInt(6)-3;
		for(int i = 0; i < x*y+yuragi; i++){
			int m = rand.nextInt(x);
			int n = rand.nextInt(y);
			stage[m][n] = !stage[m][n];
		}
		return stage;
	}

	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public boolean[][] getStage1() {
		return stage1;
	}
	public boolean[][] getStage2() {
		return stage2;
	}

	public int getTimes(){
		return times;
	}
}
