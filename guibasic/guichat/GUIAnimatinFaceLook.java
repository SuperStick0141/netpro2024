package netpro2024.guibasic.guichat;

import java.awt.Color;
import java.awt.Graphics;

class GUIAnimatinFaceLook {// 顔のオブジェクト

	int h = 100;
	int w = 100;

	int xStart = 0;
	int yStart = 0;

	public void setXY(int x, int y) {
		this.xStart = x;
		this.yStart = y;
	}

	public void setSize(int h, int w) {
		this.h = h;
		this.w = w;
	}

	public GUIAnimatinFaceLook() {

	}

	void makeFace(Graphics g) {
		// makeRim(g);
		makeEyes(g, w / 5,0);
		makeNose(g, h / 5);
		makeMouth(g, w / 2,0);
	}

	public void makeFace(Graphics g, String emotion) {
		// **ここにemotion毎の顔を用意する。*///
		if (emotion.equals("normal")) {
			makeEyes(g, w / 5,0);
			makeNose(g, h / 5);
			makeMouth(g, w / 2,0);
		}
		else if (emotion.equals("happy")) {
			makeEyes(g, w / 5,-1);
			makeNose(g, h / 5);
			makeMouth(g, w / 2,2);
		}
		else if (emotion.equals("sad")) {
			makeEyes(g, w / 5,-2);
			makeNose(g, h / 5);
			makeMouth(g, w / 2,-1);
		}
		else if (emotion.equals("angry")) {
			makeEyes(g, w / 5,2);
			makeNose(g, h / 5);
			makeMouth(g, w / 2,-2);
		}
		else
		{
			makeEyes(g, w / 5,0);
			makeNose(g, h / 5);
			makeMouth(g, w / 2,1);
		}

	}

	/*
	 * public void makeRim(Graphics g) { g.drawLine(xStart, yStart, xStart + h,
	 * yStart); g.drawLine(xStart, yStart, xStart, yStart + w);
	 * g.drawLine(xStart, yStart + w, xStart + h, yStart + w); g.drawLine(xStart
	 * + h, yStart, xStart + h, yStart + w); }
	 */

	void makeEyes(Graphics g, int eyeSize,int face) 
	{
		g.setColor(Color.blue);
		g.fillArc(xStart + (h * 2 / 7), yStart + (w * 1 / 3), eyeSize, eyeSize,0, 300);
		g.setColor(Color.black);
		g.drawOval(xStart + (h * 2 / 7), yStart + (w * 1 / 3), eyeSize+face, eyeSize);
		g.drawOval(xStart + (h * 4 / 7), yStart + (w * 1 / 3), eyeSize+face, eyeSize);
	}// makeEyes()

	public void makeNose(Graphics g, int noseSize) 
	{
		g.drawLine(xStart + (h * 1 / 2), yStart + (w * 2 / 4), xStart
				+ (h * 1 / 2), yStart + (w * 2 / 4) + noseSize);
	}// makeNose() end

	public void makeMouth(Graphics g, int mouseSize,int face) 
	{
		int xMiddle = xStart + h / 2;
		int yMiddle = yStart + 3 * w / 4;
		g.drawLine(xMiddle - mouseSize / 2, yMiddle, xMiddle,yMiddle+face);
		g.drawLine(xMiddle, yMiddle+face, xMiddle + mouseSize / 2,yMiddle);
	}
}// FaceObj End