import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;;
import java.awt.image.BufferedImage;

/**
 * Created by Akhil Alluri on 16/11/15.
 * Java Assignment 2 Question 1
 * Hangman.
 */
public class JavaAssn1 extends JFrame {
    static JLabel stat = new JLabel("");
    public JavaAssn1() {
        setSize(400, 500);
        setTitle("Java Assignment 2 Question 1: Akhil Alluri");
        Animator.man.master = new Point(getWidth()/2 + 30,getHeight()/10);
        Animator anim = new Animator();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        add(anim);
        anim.setVisible(true);
        anim.setFocusable(true);
        setVisible(true);
        anim.repaint();
        add(stat,BorderLayout.SOUTH);
    }

    public static void main(String[] args) {
        new JavaAssn1();
    }
}

class Animator extends JPanel implements KeyListener,ActionListener {
    static Hangman man = new Hangman();
    static int speed; //Angular velocity
    double thetha = 0;//Angular position
    int animSmooth = 1; //Step size.
    private BufferedImage buffer;
    public Animator() {
        setBounds(0,0,400,400);
        speed = 10;
        addKeyListener(this);
        setVisible(true);
        repaint();
        animation();
    }

    @Override
    public void invalidate() {
        BufferedImage img = new BufferedImage(
                Math.max(1, getWidth()),
                Math.max(1, getHeight()), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, getWidth(), getHeight());
        if (buffer != null) {
            g2d.drawImage(buffer, 0, 0, this);
        }
        g2d.dispose();
        buffer = img;
        super.invalidate();
    }

    protected BufferedImage getBuffer() {
        if (buffer == null) {
            buffer = new BufferedImage(
                    Math.max(1, getWidth()),
                    Math.max(1, getHeight()), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = buffer.createGraphics();
            g2d.setColor(getBackground());
            g2d.fillRect(0, 0, getWidth(), getHeight());
            g2d.dispose();
        }
        return buffer;
    }

    public void draw(GraphicsAction action) {
        BufferedImage buffer = getBuffer();
        Graphics2D g2d = buffer.createGraphics();
        action.action(g2d);
        g2d.dispose();
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(getBuffer(), 0, 0, this);
    }

    void animation() {
        updateThread.start(); // called back run()
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println(e.getKeyCode());
        int keyCode = e.getKeyCode();
        switch( keyCode ) {
            case KeyEvent.VK_UP:
                if(speed>0)
                speed--;
                System.out.println("UP");
                break;
            case KeyEvent.VK_DOWN:
                // handle down
                speed++;
                break;
            case KeyEvent.VK_R:
                // handle left
                updateThread.resume();
                break;
            case KeyEvent.VK_S:
                // handle right
                updateThread.suspend();
                break;
        }
        JavaAssn1.stat.setText("Slowdown factor: " + Integer.toString(speed) + " Press 'UP' to speed up.");
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    Thread updateThread = new Thread() {
        @Override
        public void run() {
            while (true) {
                man.generator(Math.toRadians(thetha));   // update the (x, y) position
                if(thetha == 70 || thetha == -70)
                    animSmooth = animSmooth *-1;
                thetha += animSmooth;
                draw((Graphics g) -> {
                    g.setColor(Color.black);
                    g.drawLine(50,man.master.y,man.master.x,man.master.y);
                    g.drawLine(50,man.master.y,50,300);
                    g.draw3DRect(10,300,80,20,true);
                    g.fill3DRect(10,300,80,20,true);

                    g.drawLine(man.mainBody.Start.x, man.mainBody.Start.y, man.mainBody.End.x, man.mainBody.End.y);
                    g.fillOval(man.head.x,man.head.y, Hangman.headSize, Hangman.headSize);
                    g.drawOval(man.head.x,man.head.y,40,40);
                    g.drawLine(man.leftLeg.Start.x,man.leftLeg.Start.y,man.leftLeg.End.x,man.leftLeg.End.y);
                    g.drawLine(man.rightLeg.Start.x,man.rightLeg.Start.y,man.rightLeg.End.x,man.rightLeg.End.y);
                    g.drawLine(man.leftHand.Start.x,man.leftHand.Start.y,man.leftHand.End.x,man.leftHand.End.y);
                    g.drawLine(man.rightHand.Start.x,man.rightHand.Start.y,man.rightHand.End.x,man.rightHand.End.y);
                    if(thetha<=-65) {
                        g.setColor(Color.red);
                        g.drawOval(man.head.x,man.head.y,10,10);
                        g.fillOval(man.head.x,man.head.y,10,10);
                        g.drawString("Oww!",man.head.x+50,man.head.y);
                        g.setColor(Color.black);
                    }

                });
                try {
                    Thread.sleep(speed);  // milliseconds
                } catch (InterruptedException ignore) {}
                buffer= null;
            }
        }
    };

    private interface GraphicsAction {
        void action(Graphics g);
    }
}

class Hangman{
    Point master;
    static final int headSize = 40;
    static final int b = 80;
    static final int l = 60;
    static final int h = 20;
    static final int limbLength = 50;
    static final int legLentgh = 30;
    static final double alpha = Math.toRadians(45);
    Point HOB,COM,LOB,legLext,legRext,head,handLext,handRext;
    Line mainBody;
    Line leftLeg,rightLeg,leftHand,rightHand;

    public Hangman() {
        HOB = new Point();
        LOB = new Point();
        COM = new Point();
        legLext = new Point();
        legRext = new Point();
        handLext = new Point();
        handRext = new Point();
        head = new Point();
        mainBody = new Line();
        leftLeg = new Line();
        rightLeg = new Line();
        leftHand = new Line();
        rightHand = new Line();
    }

    public void generator(double thetha) {
        //Find center of mass COM
        COM.x = (int) (b*Math.sin(thetha) + master.x);
        COM.y = (int) (b*Math.cos(thetha) + master.y);
        //Find Head of body HOB
        HOB.x = (int) (COM.x - (h*Math.sin(thetha)));
        HOB.y = (int) (COM.y - h*Math.cos(thetha));
        //Find Leg of body LOB
        LOB.x = (int) (COM.x + l*Math.sin(thetha));
        LOB.y = (int) (COM.y + l*Math.cos(thetha));
        mainBody.setLine(master,LOB);

        head = new Point(HOB.x - headSize/2,HOB.y - headSize/2);

        legRext.x = (int) (LOB.x + legLentgh*Math.sin(thetha+alpha));
        legRext.y = (int) (LOB.y + legLentgh*Math.cos(thetha+alpha));

        legLext.x = (int) (LOB.x + legLentgh*Math.sin(thetha - alpha));
        legLext.y = (int) (LOB.y + legLentgh*Math.cos(thetha - alpha));

        leftLeg.setLine(LOB,legLext);
        rightLeg.setLine(LOB,legRext);

        handLext.x = (int) (COM.x + limbLength*Math.sin(alpha-thetha)); //theta - alpha is correct
        handLext.y = (int) (COM.y + limbLength*Math.cos(alpha-thetha)); // same

        handRext.x = (int) (COM.x + limbLength*Math.sin(alpha+thetha));
        handRext.y = (int) (COM.y + limbLength*Math.cos(alpha+thetha));

        leftHand.setLine(COM,handLext);
        rightHand.setLine(COM,handRext);
    }

}

class Line {
    Point Start,End;
    public Line() {
        Start = new Point();
        End = new Point();
    }
    public void setLine(Point p1,Point p2) {
        Start = p1; End = p2;
    }
}