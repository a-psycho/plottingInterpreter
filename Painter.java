import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Painter{
	private static final long serialVersionUID = 1L;
    public static void main(String[] args) throws Exception{
        JFrame jFrame=new JFrame();
		myPaint tmp=new myPaint("a.txt");
        jFrame.add(tmp);
        jFrame.setVisible(true);
        jFrame.setSize(700, 500);
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jFrame.setTitle("Paint");
    }
}

class myPaint extends JPanel{
	Parser parser;
	StatementTree tmpTree;
	double originX=0.0,originY=0.0,rot=0.0,scaleX=1.0,scaleY=1.0;
	private final static BasicStroke stokeLine=new BasicStroke(2.0f);

	@Override
	public void paint(Graphics g) {
		Graphics2D graphics=(Graphics2D)g; 
		graphics.setStroke(stokeLine); 
		super.paint(graphics);
		int x,y;
		double tmpa,tmpb,tmpc,tmpd,tmpx,tmpy,t;
		while((tmpTree=parser.nextStatementTree()).sType!=StatementType.NONSTATEMENT){
			if(tmpTree.sType==StatementType.ORIGINSTATEMENT){
				originX=tmpTree.root[0].getExprAnswer(0);
				originY=tmpTree.root[1].getExprAnswer(0);
				graphics.setColor(getcolor());
			}
			else if(tmpTree.sType==StatementType.SCALESTATEMENT){
				scaleX=tmpTree.root[0].getExprAnswer(0);
				scaleY=tmpTree.root[1].getExprAnswer(0);
			}	
			else if(tmpTree.sType==StatementType.ROTSTATEMENT)
				rot=tmpTree.root[0].getExprAnswer(0);
			else{
				tmpa=tmpTree.root[0].getExprAnswer(0);
				tmpb=tmpTree.root[1].getExprAnswer(0);
				tmpc=tmpTree.root[2].getExprAnswer(0);
				for(t=tmpa;t<=tmpb;t+=tmpc){
					tmpx=tmpTree.root[3].getExprAnswer(t)*scaleX;
					tmpy=tmpTree.root[4].getExprAnswer(t)*scaleY;
					tmpd=tmpx;
					tmpx=tmpd*Math.cos(rot)+tmpy*Math.sin(rot)+originX;
					tmpy=tmpy*Math.cos(rot)-tmpd*Math.sin(rot)+originY;
					x=(int)tmpx; y=(int)tmpy;
					graphics.drawLine(x,y,x,y);
				}
			}
		}
	}
	
	public myPaint(String filepath) throws Exception{
		StatementTree tmpTree=null;
		parser=new Parser(filepath);
	}

	int a=-1;
	public Color getcolor(){
		int[] n={188,143,143, 147,112,219, 199,21,133};
		a=(a+1)%3;
		return new Color(n[3*a],n[3*a+1],n[3*a+2]);
	}
}
