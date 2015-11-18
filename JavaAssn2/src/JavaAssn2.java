import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Created by Akhil Alluri on 9/11/15.
 * Java Assignment 2
 * TicTacToe
 */
public class JavaAssn2 extends JFrame{
    public static ArrayList<XO> xo = new ArrayList<>();
    public static JLabel status = new JLabel("");
    public JavaAssn2() {
        JPanel backPanel = new JPanel();
        backPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to reset Game?","Warning",JOptionPane.YES_NO_OPTION);
                if(dialogResult == JOptionPane.YES_OPTION){
                    reset();
                }
            }
        });
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(400,400);
        setTitle("TechAlien TicTac");
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        //Tic Tac Layout
        backPanel.setLayout(new GridLayout(3,3,25,25));
        //Add input panels to array.
        xo.add(new XO(new Point(0,0))); xo.add(new XO(new Point(0,1))); xo.add(new XO(new Point(0,2))); xo.add(new XO(new Point(1,0))); xo.add(new XO(new Point(1,1))); xo.add(new XO(new Point(1,2))); xo.add(new XO(new Point(2,0))); xo.add(new XO(new Point(2,1))); xo.add(new XO(new Point(2,2)));
        //Initialize Grid Layout with the input panels.
        xo.forEach(backPanel::add);
        add(backPanel,BorderLayout.CENTER);
        //Game info.
        add(status,BorderLayout.SOUTH);
        setVisible(true);
        backPanel.setBackground(Color.black);
        //Start game with names
        StringBuilder label1;
        label1 = new StringBuilder();
        label1.append("<html>" +
                "<h4>Read Before proceeding!</h4>" +
                "<ul>" +
                "<li><p>This is TicTacToe.</p></li>" +
                "<li><p>Click on Black frame to reset game. </p></li>" +
                "<li><p>Welcome to TechAlien!</p></li>" +
                "</ul>" +
                "</html>");
        JOptionPane.showMessageDialog(null,label1.toString(),"about",JOptionPane.INFORMATION_MESSAGE);
        XO.setPlayerNames();
    }
    public static void reset() {
        XO.counter = 0;
        for (XO aXo : xo) {
            aXo.reset();
            aXo.repaint();
        }
        XO.backendReset(); //Matrix reset
        int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to continue with score?","Score",JOptionPane.YES_NO_OPTION);
        if(dialogResult == JOptionPane.NO_OPTION){
            XO.scoreReset();
            XO.setPlayerNames();
        }
    }


    public static String retGameStatus(String s1,String s2,int p1,int p2) {
        return s1 + ":"+Integer.toString(p1)+"   "+s2+":"+Integer.toString(p2);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (UnsupportedLookAndFeelException ignored) {
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        new JavaAssn2();
    }
}

class XO extends JPanel implements MouseListener {
    static int[][] tictac = new int[3][3]; //Backend for tictac game logic.
    private static player player1 = new player();
    private static player player2 = new player();
    public static String Winner;
    private Point p; //Backend pointer to matrix
    private boolean statx,stato; //To draw or not to draw.
    protected static int counter; //Step logic. Player turns.
    //Reset backend.
    public static void backendReset() {
        for(int i = 0;i<3;i++) for(int j = 0;j<3;j++) tictac[i][j] = 0;
    }

    public static void scoreReset() {
        player1.resetScore();
        player2.resetScore();
    }
    //Reset individual object draw.
    public void reset() {
        statx = false;
        stato = false;
        setBackground(Color.WHITE);
    }
    public static void setPlayerNames() {
        player1.setName(JOptionPane.showInputDialog(null,"Enter name of Player 1"));
        player2.setName(JOptionPane.showInputDialog(null,"Enter name of Player 2"));
        JavaAssn2.status.setText(player1.getName()+" play!");
    }
    public XO(Point p) {
        this.p = p;
        addMouseListener(this);
    }
    //Check if game progress. Win or draw.
    public static int check() {
        for(int i = 0;i<3;i++) {
            if(tictac[i][0] == tictac[i][1] && tictac[i][1] == tictac[i][2]) {
                if(tictac[i][0] == 1)
                    return 1;
                else if(tictac[i][0] ==2)
                    return 2;
            }
            else if(tictac[0][i] == tictac[1][i] && tictac[1][i] == tictac[2][i]) {
                if(tictac[0][i] == 1)
                    return 1;
                else if(tictac[0][i] == 2)
                    return 2;
            }
        }
        if(tictac[0][0] == tictac[1][1] && tictac[1][1] == tictac[2][2]) {
            if(tictac[0][0] == 1)
                return 1;
            else if(tictac[0][0] ==2)
                return 2;
        }
        else if(tictac[0][2] == tictac[1][1] && tictac[1][1] == tictac[2][0]) {
            if(tictac[0][2] == 1)
                return 1;
            else if(tictac[0][2] ==2)
                return 2;
        }
        return 0;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        //Check if box already filled.
        if(tictac[p.x][p.y] == 0) {
            //Player turn.
            if (counter % 2 == 0) {
                stato = true;
                tictac[p.x][p.y] = 1;
                JavaAssn2.status.setText(player2.getName()+"  play! "+JavaAssn2.retGameStatus(player1.getName(),player2.getName(),player1.getScore(),player2.getScore()) + "   Click black frame to reset");
                repaint();
            }
            else {
                statx = true;
                tictac[p.x][p.y] = 2;
                JavaAssn2.status.setText(player1.getName()+" play! "+JavaAssn2.retGameStatus(player1.getName(),player2.getName(),player1.getScore(),player2.getScore()) + "   Click black frame to reset");
                repaint();
            }
        }
        else {
            JOptionPane.showMessageDialog(null, "No cheating!","Cheater!",JOptionPane.INFORMATION_MESSAGE); counter--;
        }
        counter++;
        //Check if game has progressed.
        if(check() == 1 || check()==2) {
            if(check() == 1) {
                Winner = player1.getName();
                player1.incScore();
            }
            else {
                Winner = player2.getName();
                player2.incScore();
            }
            JOptionPane.showMessageDialog(null,Winner + " Wins! \n"+JavaAssn2.retGameStatus(player1.getName(),player2.getName(),player1.getScore(),player2.getScore()),"Winner!",JOptionPane.INFORMATION_MESSAGE);
            JavaAssn2.status.setText(player1.getName()+" play! " + JavaAssn2.retGameStatus(player1.getName(),player2.getName(),player1.getScore(),player2.getScore()) + "   Click black frame to reset");
            JavaAssn2.reset();
        }
        else if(counter == 9) {
            JOptionPane.showMessageDialog(null, "Draw!","Result",JOptionPane.INFORMATION_MESSAGE);
            JavaAssn2.reset();
            JavaAssn2.status.setText(player1.getName()+" play! "+JavaAssn2.retGameStatus(player1.getName(),player2.getName(),player1.getScore(),player2.getScore()) + "   Click black frame to reset");
        }
        repaint();
    }
    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}
    //The game!
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if(statx) {
            this.setBackground(Color.RED);
            g.setColor(Color.WHITE);
            g.drawLine(getWidth()/10,getHeight()/10,getWidth() - getWidth()/10,getHeight() - getHeight()/10);
            g.drawLine(getWidth() -getWidth()/10,getHeight()/10,getWidth()/10,getHeight() - getHeight()/10);
        }
        else if(stato) {
            setBackground(Color.GREEN);
            g.setColor(Color.WHITE);
            g.drawOval(getWidth()/10,getHeight()/10,getWidth() - getWidth()/5,getHeight() - getHeight()/5);
        }
    }
}

class player {
    String Name;
    int Score;

    public String getName() {
        return Name;
    }

    public int getScore() {
        return Score;
    }

    public void setName(String s) {
        Name = s;
    }

    public void incScore() {
        Score++;
    }

    public void resetScore() {
        Score = 0;
    }
}