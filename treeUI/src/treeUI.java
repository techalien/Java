import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Akhil Alluri on 7/11/15.
 * Algorithms Assignment 2 : Question 1
 * Trees Binary
 * Input : Standard tree format.
 * Output : Suggestions and traversals.
 */

public class treeUI extends JFrame{


    public treeUI() {
        //Setup Frame
        setSize(1206,505);
        setTitle("Tree: Question 1 Assignment 2 By Akhil Alluri");
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Initialize panels
        Mypanel input = new Mypanel();
        JPanel panel = new JPanel(); //Button Panel
        OutPanel output = new OutPanel();

        //Initialize buttons
        JButton b = new JButton("Inorder");  //Inorder button
        JButton pre = new JButton("PreOrder");//PreOrder traversal button
        JButton post = new JButton("PostOrder");//PostOrder traversal button
        JButton mirror = new JButton("Mirror");//Mirron tree generator
        JButton level = new JButton("LevelOrder");//LevelOrder traversal
        JButton reset = new JButton("Reset Input"); //Reset input panel
        JButton reset2 = new JButton("Reset Output");//Reset output panel
        StringBuilder label1 = new StringBuilder();

        reset.setBackground(Color.RED);
        reset2.setBackground(Color.RED);
        b.setBackground(Color.CYAN);
        pre.setBackground(Color.CYAN);
        level.setBackground(Color.CYAN);
        post.setBackground(Color.CYAN);
        //Set action listeners to buttons.

        //Reset input pane
        reset.addActionListener(e1 -> {
            input.nodes = new ArrayList<>();
            output.res = new ArrayList<>();
            input.repaint();
        });

        //Reset output panes
        reset2.addActionListener(e1 -> {
            output.res = new ArrayList<>();
            output.repaint();
        });


        //Call postOrder
        post.addActionListener(e3 -> {
            output.clear();
            if(input.nodeSelected) {
                output.res = Tree.postOrder(input.selected);
                output.traversalCall(input.nodes.size());
            }
            else
                JOptionPane.showMessageDialog(null,"Select root. Click on a node in input panel to select.");
        });

        //Call level order
        level.addActionListener(e2 -> {
            output.clear();
            if(input.nodeSelected) {
                output.res = Tree.levelOrder(input.selected);
                output.traversalCall(input.nodes.size());
            }
            else
                JOptionPane.showMessageDialog(null,"Select root. Click on a node in input panel to select.");
        });


        //Call inorder
        b.addActionListener(e -> {
            output.clear();
            if(input.nodeSelected) {
                output.res = Tree.inorder(input.selected);
                output.traversalCall(input.nodes.size());
            }
            else
                JOptionPane.showMessageDialog(null,"Select root. Click on a node in input panel to select.");
        });

        pre.addActionListener(e -> {
            output.clear();
            if(input.nodeSelected) {
                output.res = Tree.preOrder(input.selected);
                output.traversalCall(input.nodes.size());
            }
            else
                JOptionPane.showMessageDialog(null,"Select root. Click on a node in input panel to select.");
        });

        mirror.addActionListener(e -> {
            output.clear();
            if(input.nodeSelected) {
                Tree.Node newTree = Tree.Node.deepCopy(input.selected);
                Tree.Node mir = Tree.mirrorBinaryTreeIterative(newTree);
                mir = Tree.resetPoints(mir);
                if (mir == null)
                    System.out.println("this is the problem null");
                output.res = Tree.inorder(mir);
                output.traversalCall(input.nodes.size());
            }
            else
                JOptionPane.showMessageDialog(null,"Select root. Click on a node in input panel to select.");
        });

        //Setup input panel.
        input.add(reset, BorderLayout.SOUTH);
        label1.append("<html><h3>Input</h3></html>");
        input.add(new JLabel(label1.toString()),BorderLayout.NORTH);
        add(input);

        //Setup output panel
        label1 = new StringBuilder();
        label1.append("<html><h3>Output</h3></html>");
        output.add(new JLabel(label1.toString()), BorderLayout.NORTH);
        output.add(reset2, BorderLayout.SOUTH);
        add(output);

        //Setup button panel
        panel.setSize(200,400);
        panel.setLayout(new GridLayout(0,1));
        panel.add(b); panel.add(pre); panel.add(post);  panel.add(level); panel.add(mirror);
        panel.setBounds(1004,0,200,500);
        add(panel);

        //Display panels and frame.
        setResizable(false);

        reset.setVisible(true);
        reset2.setVisible(true);
        input.setVisible(true);
        output.setVisible(true);
        panel.setVisible(true);
        //setBackground(Color.black);
        setVisible(true);

        output.init();
        //Init message dialog.
        label1 = new StringBuilder();
        label1.append("<html>" +
                "<h4>Read Before proceeding!</h4>" +
                "<ul>" +
                "<li><p>Ensure nodes satisfy tree levels (i.e) Child is below parent and left child left of parent, right child right of parent. </p></li>" +
                "<li><p>Click on input panel to create a node. Use the output options for tree traversals. </p></li>" +
                "<li><p>Drag from node to node to crate an edge relation between them.</p></li>" +
                "<li><p>Use mirror function only after completing tree input.</p></li>" +
                "</ul>" +
                "</html>");
        JOptionPane.showMessageDialog(null,label1.toString());
    }
    public static void main(String[] args) {

        try {
            // Set System L&F
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch (UnsupportedLookAndFeelException e) {
            // handle exception
        } catch (IllegalAccessException | InstantiationException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        new treeUI();
    }
}

class Mypanel extends JPanel implements MouseListener {
    int nodeCounter = 1;
    Point start; Point end;
    ArrayList<Tree.Node> nodes;
    Tree.Node selected;
    boolean nodeSelected = false;
    //public static Graphics2D gd;
    public Mypanel() {
        nodes = new ArrayList<>();
        setSize(400,400);
        //setVisible(true);
        addMouseListener(this);
        setLayout(new BorderLayout());
        setBounds(0,0,500,500);
        start = new Point(0,0);
        end = new Point(0,0);
        setBackground(Color.CYAN);
        //gd = (Graphics2D) this.getGraphics();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int data;
        Point click = e.getPoint();
        Point clickPadded = new Point();
        clickPadded.x = click.x - Tree.Node.radius;
        clickPadded.y = click.y - Tree.Node.radius;
        boolean alreadyExists = false;
        if(!nodes.isEmpty()) {
            for(Tree.Node node : nodes) {
                if(node.isInside(clickPadded)) {
                    alreadyExists = true;
                    if(!nodeSelected) {
                        selected = node;
                        nodeSelected = true;
                        drawNodeColor(node);
                    }
                    else {
                        drawNode(selected);
                        selected = node;
                        nodeSelected = true;
                        drawNodeColor(node);
                    }
                }
            }
        }
        if(!alreadyExists) {
            data = nodeCounter++;
            Tree.Node temp = new Tree.Node(data, e.getPoint()); //Create new node from point of click.
            nodes.add(temp);
            drawNode(temp);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        start = e.getPoint(); //Assign press location to start. For line draw.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        end = e.getPoint();
        if((start.x >= end.x+20 || start.x <= end.x+20) && (start.y != end.y))
            connect();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void connect() {
        Tree.Node t1 = new Tree.Node();
        Tree.Node t2 = new Tree.Node();
        start.x = start.x - Tree.Node.radius;
        start.y = start.y - Tree.Node.radius;
        end.x = end.x  - Tree.Node.radius;
        end.y = end.y  -Tree.Node.radius;
        boolean b1 =false,b2= false; //To check if both ends have been matched.

        //Check if nodes exist.
        if(nodes.size() > 0) {
            //Matching against all the nodes.
            for (Tree.Node node1 : nodes) {
                //Match starting of line to node.
                if (node1.isInside(start)) {
                    t1 = node1;
                    b1 = true;
                    //Shift Line to node center.
                    start.x = node1.getPoint().x + Tree.Node.radius;
                    start.y = node1.getPoint().y + Tree.Node.radius;
                }
                //Match ending of line to node.
                else if (node1.isInside(end)) {
                    t2 = node1;
                    b2 = true;
                    //Shift line to node center.
                    end.x = node1.getPoint().x + Tree.Node.radius;
                    end.y = node1.getPoint().y + Tree.Node.radius;
                    // System.out.println("Node " + t2.getData() + " " + b2);
                } else
                    System.out.println("Outside");
            }
        }
        //If line drawn matches the nodes.
        if(b1 && b2) {
            //If the nodes have capacity to be filled
            if(!t1.full() || !t2.full()) {
                //t1 is the parent
                if (t1.getPoint().y < t2.getPoint().y && !t1.full()) {
                    if (t2.getPoint().x < t1.getPoint().x)
                        t1.left = t2;
                    else
                        t1.right = t2;
                    t2.line = new Tree.Line(start, end);
                    System.out.println("Connected");
                    drawLine(start, end);
                }

                //t2 is the parent
                else if(t2.getPoint().y < t1.getPoint().y && !t2.full()){
                    if (t1.getPoint().x < t2.getPoint().x)
                        t2.left = t1;
                    else
                        t2.right = t1;
                    t1.line = new Tree.Line(start,end);
                    System.out.println("Connected");
                    drawLine(start, end);
                }

                //Binary violation
                else {
                    JOptionPane.showMessageDialog(null,"Invalid edge creation. Binary tree only.");
                }
            }
        }
        else if((start.x != end.x +40 ) && (start.y != end.y+40 ))
            JOptionPane.showMessageDialog(null,"Invalid edge. Try to connect the nodes as precisely as possible. Try draggin from center to center of nodes.\n If you are trying to create a node, ignore this and click again.");

        start = null; end = null;
    }

    //Draw individual node.
    public void drawNode(Tree.Node node) {
        Graphics2D gd = (Graphics2D) this.getGraphics();

        gd.setColor(Color.white);
        gd.fillOval(node.getPoint().x,node.getPoint().y,2* Tree.Node.radius,2* Tree.Node.radius);
        gd.setColor(Color.black);
        gd.drawOval(node.getPoint().x,node.getPoint().y,2* Tree.Node.radius,2* Tree.Node.radius);
        gd.drawString(Integer.toString(node.getData()),node.getPoint().x + Tree.Node.radius,node.getPoint().y + Tree.Node.radius);
    }

    public void drawNodeColor(Tree.Node node) {
        Graphics2D gd = (Graphics2D) this.getGraphics();
        gd.setColor(Color.red);
        gd.fillOval(node.getPoint().x,node.getPoint().y,2* Tree.Node.radius,2* Tree.Node.radius);
        gd.drawOval(node.getPoint().x,node.getPoint().y,2* Tree.Node.radius,2* Tree.Node.radius);
        gd.setColor(Color.black);
        gd.drawString(Integer.toString(node.getData()),node.getPoint().x + Tree.Node.radius,node.getPoint().y + Tree.Node.radius);
    }

    //Draw set of nodes at once.
    public void drawNodes(ArrayList<Tree.Node> nodes) {
        for (Tree.Node node1 : nodes) {
            System.out.println("Drawing");
            drawNode(node1);
            if (node1.line != null)
                drawLine(node1.line.start, node1.line.end);
        }
    }

    //Draw Line
    public void drawLine(Point p1,Point p2) {
        Graphics2D gd = (Graphics2D) this.getGraphics();
        gd.drawLine(p1.x,p1.y,p2.x,p2.y);
    }
    //Clear panel.
    public void clear() {
        Graphics2D gd = (Graphics2D) this.getGraphics();
        gd.clearRect(0, 20, getWidth(), getHeight() - 50);
    }
}

//Output panel.
class OutPanel extends JPanel {

    ArrayList<Tree.Node> nodes;
    ArrayList<Tree.Node> res;

    public OutPanel() {
        nodes = new ArrayList<>();
        setSize(400,400);
       // setVisible(true);
        setLayout(new BorderLayout());
        setBounds(502,0,500,500);
    }

    public void drawNode(Tree.Node node) {
        Graphics2D gd = (Graphics2D) this.getGraphics();

        gd.setColor(Color.DARK_GRAY);
        gd.fillOval(node.getPoint().x,node.getPoint().y,2* Tree.Node.radius,2* Tree.Node.radius);
        gd.drawOval(node.getPoint().x,node.getPoint().y,2* Tree.Node.radius,2* Tree.Node.radius);
        gd.setColor(Color.white);
        gd.drawString(Integer.toString(node.getData()),node.getPoint().x + Tree.Node.radius,node.getPoint().y + Tree.Node.radius);
    }
    public void drawNodeColor(Tree.Node node) {
        Graphics2D gd = (Graphics2D) this.getGraphics();
        gd.setColor(Color.red);
        gd.fillOval(node.getPoint().x,node.getPoint().y,2* Tree.Node.radius,2* Tree.Node.radius);
        gd.drawOval(node.getPoint().x,node.getPoint().y,2* Tree.Node.radius,2* Tree.Node.radius);

        gd.setColor(Color.black);
        gd.drawString(Integer.toString(node.getData()),node.getPoint().x + Tree.Node.radius,node.getPoint().y + Tree.Node.radius);
    }

    public void clear() {
        Graphics2D gd = (Graphics2D) this.getGraphics();
        gd.clearRect(0, 20, getWidth(), getHeight() - 50);
    }

    public void init() {
        Graphics2D g = (Graphics2D) this.getGraphics();
        g.draw3DRect(0,0,400,400,true);
    }

    public void drawoutputNodes(ArrayList<Tree.Node> nodes) {
        for (Tree.Node node : nodes) {
            System.out.println("Drawing");

            drawNodeColor(node);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            drawNode(node);
            if (node.line != null)
                drawLine(node.line.start, node.line.end);
        }
    }

    public void outputMessage() {
        StringBuilder message = new StringBuilder();
        message.append("<html><h4> Result:</h4><br><p>");

        for (Tree.Node re : res) {
            message.append(Integer.toString(re.getData())).append(" ");
        }

        message.append("</p></html>");
        JOptionPane.showMessageDialog(null,message.toString());
    }

    public void drawLine(Point p1,Point p2) {
        Graphics2D gd = (Graphics2D) this.getGraphics();
        gd.drawLine(p1.x,p1.y,p2.x,p2.y);
    }

    public void traversalCall(int size) {
        if(size > 0) {
            if (res.size() < size) {
                JOptionPane.showMessageDialog(null, "You have selected a subtree. \nInvalid nodes : When child nodes are placed ABOVE parent nodes. OR two LEFT or RIGHT nodes.");
            }
            drawoutputNodes(res);
            outputMessage();
        }
    }
}


class Tree {
    //Node root;
    /*public Node retRoot() {
        return root;
    }*/
    public static class Line{
        public Point start;
        public Point end;
        public Line(Point p1,Point p2) {
            start = p1;
            end = p2;
        }
    }

    public static class Node {
        Node left;
        Node right;
        int data;
        Point p;
        Line line;
        public static int radius = 20;

        public Node() {
            line = null;
        }

        public Node(int value,Point x, Node left, Node right) {
            this.data = value;
            this.left = left;
            this.right = right;
            this.p = new Point(x);
        }

        public boolean full() {
            return this.left !=null && this.right!=null;
        }
        public int getData() {
            return data;
        }
        public Node(Node x) {
            this.data = x.data;
            this.p = x.p;
            this.line = x.line;
            this.left = x.left;
            this.right = x.right;
        }
        public Node(int data,Point p) {
            this.data = data;
            this.p = p;

        }

        public void setLine(Point x) {
            this.line = new Line(new Point(this.p.x + radius ,this.p.y +radius),x);
        }

        public Point getPoint() {
            return p;
        }
        public Node getLeft() {
            return left;
        }
        public Node getRight() {
            return right;
        }

        public static Node deepCopy(Node node) {
            if(node == null)
                return node;
            System.out.println(node.data + " deep");
            return new Node(node.data, node.p,deepCopy(node.left),deepCopy(node.right));
        }

        public boolean isInside(Point p) {
            return (Math.sqrt(Math.pow(p.x - this.p.x, 2) + Math.pow(p.y - this.p.y, 2)) <= radius);
        }
    }

    public static Tree.Node resetPoints(Tree.Node root) {
        Tree.Node temp = root;
        Queue<Tree.Node> queue = new LinkedList<>();
        temp.p.x = 200; temp.p.y = 20;
        int parax = 100;
        int paray = 80;
        while(temp!=null) {
            if(temp.left != null) {
                temp.left.p.x = temp.p.x - parax;
                temp.left.p.y = temp.p.y + paray;
                temp.left.setLine(new Point(temp.p.x + Node.radius,temp.p.y + Node.radius));
            }
            if(temp.right != null) {
                temp.right.p.x = temp.p.x + parax;
                temp.right.p.y = temp.p.y + paray;
                temp.right.setLine(new Point(temp.p.x + Node.radius,temp.p.y + Node.radius));
            }
            if(temp.left!=null)
                queue.add(temp.left);
            if(temp.right!=null)
                queue.add(temp.right);

            temp = queue.poll();
            parax -= 10;
            System.out.println(parax);
        }
        return root;
    }

    public static ArrayList<Tree.Node> levelOrder(Tree.Node node) {
        Tree.Node temp = node;
        ArrayList<Tree.Node> res = new ArrayList<>();
        Queue<Tree.Node> queue = new LinkedList<>();
        while(temp!=null) {
            res.add(temp);
            if(temp.left!=null)
                queue.add(temp.left);
            if(temp.right!=null)
                queue.add(temp.right);

            temp = queue.poll();
        }
        return res;
    }

    public static ArrayList<Tree.Node> inorder(Tree.Node node) {
        Stack<Tree.Node> s = new Stack<>();
        ArrayList<Tree.Node> nodelist = new ArrayList<>();
        while (node != null) {
            s.push(node);
            node = node.getLeft();
        }
        while (s.size() > 0) {
            node = s.pop();
            nodelist.add(node);
            System.out.println(node.getData());

            if (node.getRight() != null) {
                node = node.getRight();

                while (node != null) {
                    s.push(node);
                    node = node.getLeft();
                }
            }
        }
        return nodelist;
    }

    public static ArrayList<Tree.Node> preOrder(Tree.Node node) {
        Stack<Tree.Node> s = new Stack<>();
        ArrayList<Tree.Node> nodelist = new ArrayList<>();

        s.push(node);

        while(!s.empty()) {
            node = s.pop();
            nodelist.add(node);

            if(node.right != null)
                s.push(node.right);
            if(node.left != null)
                s.push(node.left);
        }
        return nodelist;
    }

    public static ArrayList<Tree.Node> postOrder(Tree.Node node) {
        Stack<Tree.Node> s1 = new Stack<>();
        Stack<Tree.Node> s2 = new Stack<>();
        ArrayList<Tree.Node> res = new ArrayList<>();
        s1.push(node);

        while(!s1.empty()) {
            node = s1.pop();
            s2.push(node);

            if(node.left != null)
                s1.push(node.left);
            if(node.right!=null)
                s1.push(node.right);
        }

        while(!s2.empty()) {
            node = s2.pop();
            res.add(node);
        }
        return res;
    }

    public static Tree.Node mirrorBinaryTreeIterative(Tree.Node root){
        Tree.Node node = new Tree.Node(root);
        if(root == null || (root.left == null && root.right == null)) {
            node = root;
            return node;
        }

        Tree.Node parent;
        Stack<Tree.Node> treeStack = new Stack<>();
        treeStack.push(node);

        while(!treeStack.empty()){
            parent = treeStack.pop();

            Tree.Node temp = parent.right;
            parent.right = parent.left;
            parent.left = temp;

            if(parent.right != null)
                treeStack.push(parent.right);
            if(parent.left != null)
                treeStack.push(parent.left);
        }
        return node;
    }
}