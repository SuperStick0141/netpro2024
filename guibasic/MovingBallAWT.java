package netpro2024.guibasic;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

//配列で5つのボールを動かしてみよう

public class MovingBallAWT {
	public static void main(String[] args) {
		FFrame f = new FFrame();
		f.setSize(500, 500);
		f.addWindowListener(new WindowAdapter() 
		{
			public void windowClosing(WindowEvent e) 
			{
				System.exit(0);
			}
		});
		f.show();
	}


	static class FFrame extends Frame implements Runnable {

		Thread th;
		Ball[] Balls;

		private boolean enable = true;
		private int counter = 0;

		FFrame() {
			th = new Thread(this);
			th.start();
		}

		public void run() 
		{
			Balls=new Ball[20];
			for(int N=0;N<20;N++)
			{
				Balls[N] = new Ball();
				Balls[N].setPosition(0, 0);
				Balls[N].setR(20+N);
				Balls[N].setColor(Color.getHSBColor(N*50,N*50,N*50));
			}

			while (enable) 
			{
				try 
				{
					th.sleep(100);
					counter++;
					if (counter >= 200) enable = false;
				} catch (InterruptedException e) {
				}


				for(int N=0;N<20;N++)
				{
					Balls[N].move();
				}

				repaint();  // paint()メソッドが呼び出される
			}
		}


		public void paint(Graphics g) 
		{
			for(int N=0;N<20;N++)
			{
				Balls[N].draw(g);
			}
		}

		// Ball というインナークラスを作る
		class Ball {
			int x;
			int y;
			int r; // 半径
			Color c = Color.RED;

			int xMove = 21;  // 1:+方向  -1: -方向
			int yMove = 21;

			void setColor(Color c) {
				this.c = c;
			}


			void move() 
			{
				Random random = new Random();

				if(xMove>2 || xMove<-2){x+=xMove; }
				if(xMove>0){xMove--;}
				else{xMove++;}
				if(yMove>2 || yMove<-2){y+=yMove; }
				if(yMove>0){yMove--;}
				else{yMove++;}

				if(xMove==-1){xMove=random.nextInt(25);}
				else if(xMove==1){xMove=random.nextInt(25)*-1;}
				if(yMove==-1){yMove=random.nextInt(25);}
				else if(yMove==1){yMove=random.nextInt(25)*-1;}

				if(x>500){x=0;}
				else if(x<0){x=500;}
				if(y>500){y=0;}
				else if(y<0){y=500;}
			}


			void setPosition(int x, int y) {
				this.x = x;
				this.y = y;
			}

			void setR(int r) {
				this.r = r;
			}

			void draw(Graphics g) {
				g.setColor(c);
				g.fillOval(x, y, 2 * r, 2 * r);  // rは半径なので2倍にする
			}

		}//innner class Ball end

	}

}