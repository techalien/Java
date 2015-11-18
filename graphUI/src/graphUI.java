import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Akhil Alluri on 7/11/15.
 * Algorithms Assignment 2 : Question 2
 * Graphs Traversal
 * Input : Standard Graph undirected format.
 * Output : Suggestions and traversals.
 * Suggestions: Incomplete input. Or prompts.
 */
public class graphUI extends JFrame {
    public graphUI() {
        setSize(1206,505);
        setLocationRelativeTo(null);
        setTitle("Graph: Question 2 Assignment 2 By Akhil Alluri");
        setLayout(null);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        StringBuilder label1 = new StringBuilder();
        InputPanel input = new InputPanel();
        OutputPanel output = new OutputPanel();
        JPanel ButtonPanel = new JPanel();
        ButtonPanel.setBounds(1004,0,200,500);

        JButton bft = new JButton("BFT");
        JButton dft = new JButton("DFT");
        JButton ds = new JButton("Dsearch");
        JButton reset = new JButton("Reset Input"); //Reset input panel
        JButton reset2 = new JButton("Reset Output");
        reset.setBackground(Color.RED);
        reset2.setBackground(Color.RED);
        bft.setBackground(Color.CYAN);
        dft.setBackground(Color.CYAN);
        ds.setBackground(Color.CYAN);
        ds.addActionListener(e3 -> {
            output.clear();
            if(input.nodeSelected) {
                ArrayList<Graph.GraphNode> res = Graph.dSearch(input.selected);
                output.res = res;
                for (Graph.GraphNode re : res) System.out.println(re.getData());
                output.traversalCall();
                input.visitedReset();
            }
            else {
                JOptionPane.showMessageDialog(null,"Select a source node");
            }
        });

        dft.addActionListener(e2 -> {
            output.clear();
            if(input.nodeSelected) {
                dfs.res = new ArrayList<>();
                input.selected.level = 0;
                dfs.depthFirstSearch(input.selected);
                ArrayList<Graph.GraphNode> res = dfs.res;
                output.res = res;
                for (Graph.GraphNode re : res) System.out.println(re.getData());
                output.traversalCall();
                input.visitedReset();
            }
            else {
                JOptionPane.showMessageDialog(null,"Select a source node");
            }
        });
        bft.addActionListener(e -> {
            output.clear();
            if(input.nodeSelected) {
                ArrayList<Graph.GraphNode> res = Graph.breadthFirst(input.selected);
                output.res = res;
                for (Graph.GraphNode re : res) System.out.println(re.getData());
                output.traversalCall();
                input.visitedReset();
            }
            else {
                JOptionPane.showMessageDialog(null,"Select a source node");
            }
        });
        //Reset input pane
        reset.addActionListener(e1 -> {
            input.nodes = new ArrayList<>();
            input.selected = null;
            input.nodeSelected = false;
            output.res = new ArrayList<>();
            input.repaint();
        });

        reset2.addActionListener(e -> {
            output.res = new ArrayList<>();
            output.repaint();
        });

        input.add(reset, BorderLayout.SOUTH);
        label1.append("<html><h3>Input</h3></html>");
        input.add(new JLabel(label1.toString()),BorderLayout.NORTH);
        add(input);

        output.add(reset2, BorderLayout.SOUTH);
        label1 = new StringBuilder();
        label1.append("<html><h3>Output</h3></html>");
        output.add(new JLabel(label1.toString()),BorderLayout.NORTH);
        add(output);

        ButtonPanel.setLayout(new GridLayout(0,1));
        ButtonPanel.add(bft); ButtonPanel.add(dft); ButtonPanel.add(ds);
        add(ButtonPanel);
        setResizable(false);


        ButtonPanel.setVisible(true);
        reset.setVisible(true);
        reset2.setVisible(true);
        input.setVisible(true);
        output.setVisible(true);
        setVisible(true);
        repaint();
        label1 = new StringBuilder();
        label1.append("<html>" +
                "<h4>Read Before proceeding!</h4>" +
                "<ul>" +
                "<li><p>This is a Graph traversal demonstration. . </p></li>" +
                "<li><p>Click on input panel to create a node. Use the output options for graph traversals. </p></li>" +
                "<li><p>Drag from node to node to crate an edge relation between them.</p></li>" +
                "<li><p>Clicking a node selects it for use in single source algorithms. </p></li>" +
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
        new graphUI();
    }
}

class InputPanel extends JPanel implements MouseListener {
    int nodeCounter = 1;
    Point start,end;
    ArrayList<Graph.GraphNode> nodes;
    Graph.GraphNode selected;
    boolean nodeSelected = false;
    public InputPanel() {
        nodes = new ArrayList<>();
        setBounds(0,0,500,500);
        addMouseListener(this);
        setLayout(new BorderLayout());
        setBackground(Color.CYAN);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int data;
        Point click = e.getPoint();
        Point clickPadded = new Point();
        clickPadded.x = click.x - Graph.GraphNode.radius;
        clickPadded.y = click.y - Graph.GraphNode.radius;
        boolean alreadyExists = false;
        if(!nodes.isEmpty())
            for (Graph.GraphNode node : nodes) {
                if (node.isInside(clickPadded)) {
                    alreadyExists = true;
                    if(!nodeSelected) {
                        selected = node; //Later on use.
                        nodeSelected = true;
                        drawNodeColor(node);
                    }
                    else{
                        drawNode(selected);
                        selected = node; //Later on use.
                        nodeSelected=true;
                        drawNodeColor(node);
                    }
                    break;
                }
            }
        if(!alreadyExists) {
            data = nodeCounter++;
            Graph.GraphNode temp = new Graph.GraphNode(data,click);
            nodes.add(temp);
            drawNode(temp);
        }
        else
            JOptionPane.showMessageDialog(null,"You have selected node" + selected.getData());
    }

    public void connect() {
        Graph.GraphNode n1 = new Graph.GraphNode();
        Graph.GraphNode n2 = new Graph.GraphNode();
        start.x = start.x - Graph.GraphNode.radius;
        start.y = start.y - Graph.GraphNode.radius;
        end.x = end.x - Graph.GraphNode.radius;
        end.y = end.y - Graph.GraphNode.radius;
        boolean m1,m2; m1 = false; m2 = false;

        if(!nodes.isEmpty()) {
            for(Graph.GraphNode node : nodes) {
                if(node.isInside(start)) {
                    n1 = node;
                    m1 = true;

                    start.x = node.getPoint().x + Graph.GraphNode.radius;
                    start.y = node.getPoint().y + Graph.GraphNode.radius;
                }
                else if(node.isInside(end)) {
                    n2 = node;
                    m2 = true;

                    end.x = node.getPoint().x + Graph.GraphNode.radius;
                    end.y = node.getPoint().y + Graph.GraphNode.radius;
                }

                if(m1 && m2)
                    break;
            }
        }

        if(m1 && m2) {
            if(!n1.isConnected(n2)) {
                Graph.GraphNode.connect(n1, n2);
                n1.connections.add(new Line(start,end));
                drawLine(start,end);
            }
            else
                JOptionPane.showMessageDialog(null,"Already connected!");
        }
        else
            JOptionPane.showMessageDialog(null,"You cannot connect empty space!");
    }

    public void drawNode(Graph.GraphNode node) {
        Graphics2D gd = (Graphics2D) this.getGraphics();
        gd.setColor(Color.white);
        gd.fillOval(node.getPoint().x,node.getPoint().y,2* Graph.GraphNode.radius,2* Graph.GraphNode.radius);
        gd.setColor(Color.black);
        gd.drawOval(node.getPoint().x,node.getPoint().y,2* Graph.GraphNode.radius,2* Graph.GraphNode.radius);
        gd.drawString(Integer.toString(node.getData()),node.getPoint().x + Graph.GraphNode.radius,node.getPoint().y + Graph.GraphNode.radius);
    }

    public void drawNodeColor(Graph.GraphNode node) {
        Graphics2D gd = (Graphics2D) this.getGraphics();
        gd.setColor(Color.red);
        gd.fillOval(node.getPoint().x,node.getPoint().y,2* Graph.GraphNode.radius,2* Graph.GraphNode.radius);
        gd.drawOval(node.getPoint().x,node.getPoint().y,2* Graph.GraphNode.radius,2* Graph.GraphNode.radius);

        gd.setColor(Color.black);
        gd.drawString(Integer.toString(node.getData()),node.getPoint().x + Graph.GraphNode.radius,node.getPoint().y + Graph.GraphNode.radius);
    }

    public void drawLine(Point p1,Point p2) {
        Graphics2D gd = (Graphics2D) this.getGraphics();
        gd.drawLine(p1.x,p1.y,p2.x,p2.y);
    }

    public void visitedReset() {
        for (Graph.GraphNode node : nodes) {
            node.visited = false;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        start = e.getPoint();
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
    /*public void clear() {
        Graphics2D gd = (Graphics2D) this.getGraphics();
        gd.clearRect(0, 20, getWidth(), getHeight() - 50);
    }*/
}

class OutputPanel extends JPanel {
    ArrayList<Graph.GraphNode> res;

    public OutputPanel() {
        res = new ArrayList<>();
        setLayout(new BorderLayout());
        setBounds(502,0,500,500);
        //setBackground(Color.CYAN);
    }

    public void drawNode(Graph.GraphNode node) {
        Graphics2D gd = (Graphics2D) this.getGraphics();

        gd.setColor(Color.DARK_GRAY);
        gd.fillOval(node.getPoint().x,node.getPoint().y,2* Graph.GraphNode.radius,2* Graph.GraphNode.radius);
        gd.drawOval(node.getPoint().x,node.getPoint().y,2* Graph.GraphNode.radius,2* Graph.GraphNode.radius);
        gd.setColor(Color.white);
        gd.drawString(Integer.toString(node.getData()) + " Level: " + Integer.toString(node.level),node.getPoint().x + Graph.GraphNode.radius,node.getPoint().y + Graph.GraphNode.radius);
    }
    public void drawNodeColor(Graph.GraphNode node) {
        Graphics2D gd = (Graphics2D) this.getGraphics();
        gd.setColor(Color.red);
        gd.fillOval(node.getPoint().x,node.getPoint().y,2* Graph.GraphNode.radius,2* Graph.GraphNode.radius);
        gd.drawOval(node.getPoint().x,node.getPoint().y,2* Graph.GraphNode.radius,2* Graph.GraphNode.radius);

        gd.setColor(Color.black);
        gd.drawString(Integer.toString(node.getData()),node.getPoint().x + Graph.GraphNode.radius,node.getPoint().y + Graph.GraphNode.radius);
    }

    public void clear() {
        Graphics2D gd = (Graphics2D) this.getGraphics();
        gd.clearRect(0, 20, getWidth(), getHeight() - 50);
    }

    /*public void init() {
        Graphics2D g = (Graphics2D) this.getGraphics();
        g.draw3DRect(0,0,400,400,true);
    }*/

    public void drawoutputNodes(ArrayList<Graph.GraphNode> nodes) {
        for (Graph.GraphNode node : nodes) {
            System.out.println("Drawing");

            drawNodeColor(node);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            drawNode(node);
            ArrayList<Line> lines = node.connections;
            for (Line line : lines) {
                drawLine(line.Start, line.End);
            }
        }
    }

    public void outputMessage() {
        StringBuilder message = new StringBuilder();
        message.append("<html><h4> Result:</h4><br><p>");

        for (Graph.GraphNode re : res) {
            message.append(Integer.toString(re.getData())).append(" ");
        }

        message.append("</p></html>");
        JOptionPane.showMessageDialog(null,message.toString());
    }

    public void drawLine(Point p1,Point p2) {
        Graphics2D gd = (Graphics2D) this.getGraphics();
        gd.drawLine(p1.x,p1.y,p2.x,p2.y);
    }

    public void traversalCall() {


        drawoutputNodes(res);
        outputMessage();
    }
}

class Graph {
    public static class GraphNode {
        int data;
        int level;
        Point p; //Position
        ArrayList<Line> connections;
        ArrayList<GraphNode> AdjacencyList;
        boolean visited;
        static int radius = 20;
        public GraphNode(int data,Point p) {
            visited = false;
            this.data = data;
            this.p = p;
            connections = new ArrayList<>();
            AdjacencyList = new ArrayList<>();
        }

        public GraphNode() {

        }

        public int getData() {
            return data;
        }

        public Point getPoint() {
            return p;
        }

        public boolean isInside(Point p) {
            return (Math.sqrt(Math.pow(p.x - this.p.x, 2) + Math.pow(p.y - this.p.y, 2)) <= radius);
        }

        public boolean isConnected(GraphNode target) {
            for (GraphNode aAdjacencyList : AdjacencyList) {
                if (aAdjacencyList.getData() == target.getData())
                    return true;
            }
            return false;
        }



        public static void connect(GraphNode n1,GraphNode n2) {
            n1.AdjacencyList.add(n2);
            n2.AdjacencyList.add(n1);
        }
    }

    public static ArrayList<GraphNode> breadthFirst(GraphNode source) {
        Queue<GraphNode> queue = new LinkedList<>();
        ArrayList<GraphNode> res = new ArrayList<>();
        source.visited = true;
        source.level = 0;
        queue.add(source);

        while(!queue.isEmpty()) {
            GraphNode temp = queue.poll();
            res.add(temp);

            ArrayList<GraphNode> adj = temp.AdjacencyList;
            adj.stream().filter(anAdj -> !anAdj.visited).forEach(anAdj -> {
                anAdj.visited = true;
                anAdj.level = temp.level + 1;
                queue.add(anAdj);
            });
        }
        return res;
    }
    public static ArrayList<GraphNode> dSearch(GraphNode source) {
        Stack<GraphNode> stack = new Stack<>();
        ArrayList<GraphNode> res = new ArrayList<>();
        source.visited = true;
        source.level = 0;
        stack.push(source);

        while(!stack.isEmpty()) {
            GraphNode temp = stack.pop();
            res.add(temp);
            ArrayList<GraphNode> adj = temp.AdjacencyList;
            adj.stream().filter(anAdj -> !anAdj.visited).forEach(anAdj -> {
                anAdj.level = temp.level + 1;
                anAdj.visited = true;
                stack.push(anAdj);
            });
        }
        return res;
    }
}

class dfs {
    static ArrayList<Graph.GraphNode> res = new ArrayList<>();
    public static void depthFirstSearch(Graph.GraphNode source) {
        source.visited = true;
        //res.add(source);
        ArrayList<Graph.GraphNode> adj = source.AdjacencyList;
        adj.stream().filter(anAdj -> !anAdj.visited).forEach(anAdj -> {
            anAdj.level = source.level + 1;
            depthFirstSearch(anAdj);
        });
        res.add(source);
    }
}


class Line {
    Point Start;
    Point End;
    public Line(Point p1,Point p2) {
        Start = p1;
        End = p2;
    }
    public Point getStart() {
        return Start;
    }
    public Point getEnd() {
        return End;
    }
}