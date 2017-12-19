import java.io.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
class Memory extends JFrame implements ActionListener
{
    JButton button[][]=new JButton[4][4];
    static int ar[][]=new int[4][4];
    ImageIcon i[][]=new ImageIcon[4][4];
    int clicks=0,open=0,pre[]={-1,-1};
    static String naam;
    public static void main(String args[])
    {
        int temp[]=new int[16];
        for(int set=1;set<=8;set++)
        {
            for(int x=1;x<=2;x++)
            {
                int n=(int)(Math.random()*16);
                while(temp[n]!=0)
                {
                    n=(int)(Math.random()*16);
                }
                temp[n]=set;
            }
        }

        for(int x=0;x<=15;x++)
        {
            if(x<=3)
                ar[0][x]=temp[x];
            else if(x<=7)
                ar[1][x-4]=temp[x];
            else if(x<=11)
                ar[2][x-8]=temp[x];
            else if(x<=15)
                ar[3][x-12]=temp[x];
        }
        
        naam=JOptionPane.showInputDialog("Enter your name:");
        Memory ob=new Memory();
        ob.setVisible(true);
    }

    private boolean done()
    {
        for(int x=0;x<=3;x++)
        {
            for(int y=0;y<=3;y++)
            {
                if(button[x][y].isEnabled())
                    return false;
            }
        }
        return true;
    }

    public void actionPerformed(ActionEvent evt)
    {
        for(int x=0;x<=3;x++)
        {
            for(int y=0;y<=3;y++)
            {
                if(evt.getSource()==button[x][y] && (open==1?(!(pre[0]==x && pre[1]==y)):true))
                {
                    clicks++;
                    if(open==0)
                    {
                        open++;
                        showAt(x,y);
                        pre[0]=x;
                        pre[1]=y;
                    }
                    else if(open==1)
                    {
                        showAt(pre[0],pre[1],x,y);
                        if(ar[x][y]==ar[pre[0]][pre[1]])
                        {
                            button[pre[0]][pre[1]].setEnabled(false);
                            button[x][y].setEnabled(false);
                        }

                        java.util.Timer ob=new java.util.Timer();
                        Kaam ob1=new Kaam();
                        ob1.take(x,y);
                        ob.schedule(ob1,750);

                        open=0;
                    }
                }
            }
        }
        if(done())
        {
            try
            {
                Rank ob=new Rank();
                int rank=ob.place(Memory.naam,(55-clicks));
                JOptionPane.showMessageDialog(this,(rank==1?"":"Rank : "+rank+"\n")+"Score : "+(55-clicks));
            }
            catch(IOException e)
            {
            }
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        }
        else if(clicks==50)
        {
            JOptionPane.showMessageDialog(this,"Too many clicks!\nGameOver!");
            this.setDefaultCloseOperation(EXIT_ON_CLOSE);
            for(int x=0;x<=3;x++)
            {
                for(int y=0;y<=3;y++)
                {
                    button[x][y].setEnabled(false);
                }
            }
        }
    }

    class Rank
    {
        public int place(String name,int score)throws IOException
        {
            File f1=new File("e:\\Hall of fame_emma.csv");
            File f2=new File("e:\\temp.csv");

            if(!(f1.exists()))
            {
                FileWriter fw=new FileWriter(f1);
                BufferedWriter bw=new BufferedWriter(fw);
                PrintWriter pw=new PrintWriter(bw);

                pw.close();
                bw.close();
                fw.close();
            }

            FileReader fr=new FileReader(f1);
            BufferedReader fbr=new BufferedReader(fr);

            FileWriter fw=new FileWriter(f2);
            BufferedWriter bw=new BufferedWriter(fw);
            PrintWriter pw=new PrintWriter(bw);

            boolean finished=false;
            int rank=1,i=1;

            fbr.readLine();
            pw.println("Vibhu,50");

            for(int x=2;x<=10;x++)
            {
                try
                {
                    i++;
                    String ar[]=(fbr.readLine()).split(",");
                    if(score>=Integer.parseInt(ar[1]) && rank<2)
                    {
                        pw.println(name+","+score);
                        rank=x;
                        x++;
                    }
                    if(x!=10)
                        pw.println(ar[0]+","+ar[1]);
                }
                catch(Exception e)
                {
                    finished=true;
                    break;
                }
            }

            if(finished && rank<2)
            {
                pw.println(name+","+score);
                rank=i;
            }

            pw.close();
            bw.close();
            fw.close();

            fbr.close();
            fr.close();

            f1.delete();
            f2.renameTo(f1);

            return rank;
        }
    }

    class Kaam extends TimerTask
    {
        int x,y;

        public void take(int a,int b)
        {
            x=a;
            y=b;
        }

        public void run()
        {
            button[x][y].setIcon(null);
            button[pre[0]][pre[1]].setIcon(null);
        }
    }

    private void showAt(int r,int c,int ro,int co)
    {
        for(int x=0;x<=3;x++)
        {
            for(int y=0;y<=3;y++)
            {
                if((x==r && y==c) || (x==ro && y==co))
                    button[x][y].setIcon(i[x][y]);
                else
                    button[x][y].setIcon(null);
            }
        }
    }

    private void showAt(int r,int c)
    {
        for(int x=0;x<=3;x++)
        {
            for(int y=0;y<=3;y++)
            {
                if(x==r && y==c)
                    button[x][y].setIcon(i[x][y]);
                else
                    button[x][y].setIcon(null);
            }
        }
    }

    private Memory()
    {
        super("Memory Game - "+naam);
        setLayout(new GridLayout(4,4));
        for(int x=0;x<=3;x++)
        {
            for(int y=0;y<=3;y++)
            {
                button[x][y]=new JButton();
                add(button[x][y]);
                button[x][y].addActionListener(this);
                i[x][y]=new ImageIcon(getClass().getResource("Shot"+ar[x][y]+".jpg"));
            }
        }
        setSize(700,710);
    }
}