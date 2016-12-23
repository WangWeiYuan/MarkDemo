package markDemo;

import java.awt.AlphaComposite;  
import java.awt.MouseInfo;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.BasicStroke;  
import java.awt.BorderLayout;  
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;  
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Label;
import java.awt.Point;  
import java.awt.RenderingHints;  
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;  
import java.awt.event.MouseEvent;  
import java.awt.event.MouseMotionAdapter;   
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComponent;  
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class Demo extends JFrame implements ActionListener, Runnable
{  
	static ArrayList<Shape> shapes = new ArrayList<Shape>();                 //����װ����shape
	private static ArrayList<String> filelist = new ArrayList<String>();   //���������ļ���
	private static ArrayList<String> filedata = new ArrayList<String>();
	JButton jb,j,UP;
	public static Label sum;
	public final JComponent jp1;
	public static JLabel jl;                      //�ڴ����ƶ���ʱ���ҵ�����
	static int num =1;
	static int width = 0, height = 0;             //ͼƬʵ�ʵĴ�С
	static double BackGroundX=50, BackGroundY = 50;            //����ͼƬ�Ĵ�С
	static double BackGroundWidth = 600, BackGroundHeight = 500;
	public static int MouseX = 0, MouseY = 0;
	public static int ScreenWidth , ScreenHeight = 0;  //��Ļ����
	public static int FrameWidth = 0, FrameHeight = 0;
	public static int pictureNum = 0;                   //�ڼ���ͼƬ
	public static int wSizeX =900, wSizeY=700;
    public Demo()
    {//����һ��  
    	super("BiaoZhu");
		setSize(wSizeX, wSizeY);
		setLocation(400, 200);
		this.setResizable(false);  //���ڲ������
		
		
		JPanel jp = new JPanel();
		jp.setBackground(Color.white);
		jp.setPreferredSize(new Dimension(200, 30));
		add(BorderLayout.EAST, jp);
		
		MyPanel controlScr = new MyPanel();
		controlScr.setLayout(new GridLayout(4, 1, 1, 40));
		jp.add(controlScr);
		
		sum = new Label();
		controlScr.add(sum);
		
		jb = new JButton("���漰��һ��");
		controlScr.add(jb);
		jb.addActionListener(this);
		
		j = new JButton("����");
		controlScr.add(j);
		j.addActionListener(this);
		
		UP = new JButton("��һ��");
		controlScr.add(UP);
		UP.addActionListener(this);
			
		jp1 = new PaintSurface();
		jl = new JLabel();
		jp1.add(jl);
		      
		add(BorderLayout.CENTER, jp1);
		
		final JPanel jp11 = new JPanel();
		
		jp11.setBackground(Color.pink);	
		add(BorderLayout.SOUTH, jp11);
		jp11.setPreferredSize(new Dimension(50, 30));
		new Thread(this).start();
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);		
		
    }  
   
    public static void main(String[] args) 
    {  
    	Dimension   screensize   =   Toolkit.getDefaultToolkit().getScreenSize();
        ScreenWidth = (int)screensize.getWidth();        
        ScreenHeight = (int)screensize.getHeight();
        String filePath = "data";
        filePicture(filePath);
        writeData();
        
        File  dirFile = new File("result");     //mkdirNameΪ�����ļ���·��
        boolean bFile   = dirFile.exists();
        if( bFile == true )
        {
    		try
    		{
            	File directory = new File("result");//�趨Ϊ��ǰ�ļ���
                String	path = directory.getCanonicalPath();//��ȡ��׼��·��
            	File inF = new File(path+"/record_state.txt");
        		FileReader in;
    			in = new FileReader(inF);
    			BufferedReader b_in = new BufferedReader(in);			
    			pictureNum = Integer.parseInt(b_in.readLine());
    			b_in.close();
    		}
    		catch (IOException e)
    		{
    			;
    		}
        }
        else
        {
        	pictureNum = 0;
        }
        
        
        new Demo();    
    }  
       
    private class PaintSurface extends JComponent
    { 
           	   	
        Point startDrag,endDrag;//�����㣬��ʼק�ĵ㣬����ק�ĵ�  
           
        private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2)
        {  
              return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2),   
                      Math.abs(x1 - x2), Math.abs(y1 - y2));  
        }  
   
        public PaintSurface()
        {
            this.addMouseListener(new MouseAdapter()
            {  
                public void mousePressed(MouseEvent e)
                {//��갴��....  
                    startDrag = new Point(e.getX(),e.getY());   
                    endDrag = startDrag;                     //�ѿ�ʼ�ĵ�������ĵ�  
                    repaint();  
                }  
                public void mouseReleased(MouseEvent e)
                {//����ɿ�  
                    //��һ����״ r = ��һ����,���������makeRectangle����  
                    Shape r = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());  
                    shapes.add(r);               //��make�õ�r����shapes��  
                    startDrag = null;           //������������� 
                    endDrag = null;  
                    repaint();  
                }  
            });  
            //��갴�����ɿ������ˣ������Ǹ�ק���J������  
            this.addMouseMotionListener(new MouseMotionAdapter()
            {  
                public void mouseDragged(MouseEvent e)
                {  
                    
                    endDrag = new Point(e.getX(), e.getY());  
                    repaint();  
                }  
            });
            
        }  

           
        public void paintBackground(Graphics2D g2)
        {

			Toolkit toolkit = Toolkit.getDefaultToolkit();
			Image image2 = toolkit.getImage(filedata.get(pictureNum));
			width = image2.getWidth(null);
			height = image2.getHeight(null);
			g2.drawImage(image2, (int)BackGroundX, (int)BackGroundY, (int)BackGroundWidth, (int)BackGroundHeight, this);
        }  
        
        public void paintMouse(Graphics2D g2)
        {
        	float lineWidth = 1.0f;
			g2.setStroke(new BasicStroke(lineWidth));
			g2.setColor(Color.green);
        	g2.drawLine(0, MouseY, Demo.FrameWidth+400, MouseY);
        	g2.drawLine(MouseX, 0, MouseX, Demo.FrameHeight+400);
        }
  
        public void paint(Graphics g)
        {  
            Graphics2D g2 = (Graphics2D)g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);  
            paintBackground(g2);             //���������  
            paintMouse(g2);
            
            //Color[] colors = {Color.yellow,Color.magenta,Color.cyan,Color.red,Color.blue,Color.pink};  
            //int colorIndex = 0;  
               
           
            g2.setStroke(new BasicStroke(2));
            
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.50f));  
               
            for(Shape s: shapes)
            {
            	float lineWidth = 3.0f;
    			g2.setStroke(new BasicStroke(lineWidth));
                g2.setPaint(Color.green);  
                g2.draw(s);        //�Ȼ����飬����  
                //g2.setPaint(colors[(colorIndex++) % 6]);
                //g2.fill(s);//��ɫ���
            }  
               
            if(startDrag != null && endDrag != null)
            {//�켣��  
                g2.setPaint(Color.green);  
                Shape r = makeRectangle(startDrag.x, startDrag.y, endDrag.x, endDrag.y);  
                g2.draw(r);  
            }  
            
        }  
        
    } 
    
    public void run()
    {
    	 Timer timer = new Timer();
         timer.schedule(new TimerTask() {
                         public void run() 
                         {
                        	 sum.setFont(new   java.awt.Font("Dialog", 1, 25));
                        	 int num = pictureNum;
                        	 sum.setText((num+1)+" / "+filedata.size());
                        	 try
                        	 {
                        		 Point contentPos = jl.getLocationOnScreen();// ����Ļ������
                           		 SwingUtilities.convertPointToScreen(contentPos,jl);
                           		 FrameWidth = (int)contentPos.getX()/2;
                          		 FrameHeight = (int)contentPos.getY()/2;           
                        	 }
                        	 catch(Exception ex)
                        	 {
                        		 
                        	 }                      		            		 
                        	 
                        	 Point point = MouseInfo.getPointerInfo().getLocation();
                        	 MouseX = point.x - FrameWidth;
                        	 MouseY = point.y - FrameHeight;
                         
                         }
         }, 100, 100);
         
        Timer timer1 = new Timer();
 		timer.schedule(new TimerTask()
 		{

 			public void run() 
 			{			
 				//ÿ10ms��һ�ν���
 				repaint();
 			}
 		}, 0, 10);
    }
    
    public void actionPerformed(ActionEvent e)
    {
		if(e.getSource() == jb)
		{
									
			double precentX = width/BackGroundWidth;
			double precentY = height/BackGroundHeight;
			
			String path1 = null;
			File  dirFile = new File("result");     
            boolean bFile   = dirFile.exists();
            if( bFile == true )
            {
               
            }
            else
            {              
               bFile = dirFile.mkdir();
               
            }
            File directory = new File("result");//�趨Ϊ��ǰ�ļ���
            try
            {
                 path1 = directory.getCanonicalPath();//��ȡ��׼��·��
                
            }catch(Exception ex)
            {
            	ex.printStackTrace();
            }           
			File outF = new File(path1+"/leveldb"+pictureNum+".txt");
			try
			{

				FileWriter out = new FileWriter(outF);
				BufferedWriter b_out = new BufferedWriter(out);
                
				for(int a = 0; a < shapes.size(); a++)
				{
					int x1 = Integer.parseInt(new java.text.DecimalFormat("0").format((shapes.get(a).getBounds2D().getX()-50)*precentX));
					int y1 = Integer.parseInt(new java.text.DecimalFormat("0").format((shapes.get(a).getBounds2D().getY()-50)*precentY));
					int x2 = Integer.parseInt(new java.text.DecimalFormat("0").format((shapes.get(a).getBounds2D().getWidth()+shapes.get(a).getBounds2D().getX()-50)*precentX));
					int y2 = Integer.parseInt(new java.text.DecimalFormat("0").format((shapes.get(a).getBounds2D().getHeight()+shapes.get(a).getBounds2D().getY()-50)*precentY));
					
					if(x1 < 0 || x1 > width)
					{
						if(x1 < 0)
						{
							x1 = 0;
						}
						else
						{
							x1 = width;
						}
					}
					if(x2 < 0 || x2 > width)
					{
						if(x2 < 0)
						{
							x2 = 0;
						}
						else
						{
							x2 = width;
						}
					}
					if(y1 < 0 || y1 > height)
					{
						if(y1 < 0)
						{
							y1 = 0;
						}
						else
						{
							y1 = height;
						}
					}
					if(y2 < 0 || y2 > height)
					{
						if(y2 < 0)
						{
							y2 = 0;
						}
						else
						{
							y2 = height;
						}
					}
					b_out.write(x1+"  ");
					b_out.write(y1+"  ");
					b_out.write(x2+"  ");
					b_out.write(y2+"  ");
					b_out.newLine();
				}
								
				b_out.close();
			}
			catch (IOException e1)
			{
				;
			}	
			File outR = new File(path1+"/record_state.txt");
			try
			{

				FileWriter out = new FileWriter(outR);
				BufferedWriter b_out = new BufferedWriter(out);
				String pictureNumber = String.valueOf(pictureNum);
			    b_out.write(pictureNumber);
								
				b_out.close();
			}
			catch (IOException e1)
			{
				;
			}
			
			if(pictureNum != filedata.size()-1)
			{
				pictureNum++;
			}
			if(pictureNum == filedata.size()-1)
			{
				pictureNum = filedata.size() - 1;
			}
			repaint();
			shapes.clear();
		}
		
		if(e.getSource() == j)
		{
			if(shapes.size() != 0)
			{
				shapes.remove(shapes.size()-1);
			}
			
		}
		
		if(e.getSource() == UP)
		{
			if(pictureNum == 0)
			{
				pictureNum = 0;
			}
			else
			{
				pictureNum = pictureNum - 1;
			}
			
			shapes.clear();
			repaint();
		}
			
	}	

    public static void filePicture(String filePath)
    {
    	File root = new File(filePath);
        File[] files = root.listFiles();
        for(File file:files)
        {    
         if(file.isDirectory())
         {
        	 filePicture(file.getAbsolutePath());
        	 filelist.add(file.getAbsolutePath());
         }
         else
         {
        	 filedata.add(file.getAbsolutePath());
        	 
         }    
        }
    }
    
    public static void writeData()
    {
    	File outF = new File("data.txt");
		try
		{

			FileWriter out = new FileWriter(outF);
			BufferedWriter b_out = new BufferedWriter(out);
            
			for(int a = 0; a < filedata.size(); a++)
			{
				
				b_out.write(filedata.get(a));
				b_out.newLine();
			}
							
			b_out.close();

		}
		catch (IOException e1)
		{
			;
		}
    }
}
class MyPanel extends JPanel 
{
	//��ť��
	public Insets getInsets() 
	{
		return new Insets(230, 50, 230, 40);
	}
}