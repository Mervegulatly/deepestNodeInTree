package deepest_node_in_the_tree;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class BinaryTreeNode extends JFrame implements ActionListener, KeyListener {
    private Node root;
    private Random random;
    private HashSet<Integer> nodeSet;
    private JPanel topPanel, treePanel, infoPanel;
    private JPanel topLeftPanel, topRightPanel;
    private JButton btnAdd, btnFindDeepest;
    private JTextField tf;
    private JLabel labelDeepestNode, ansDeepestNode;
    private JLabel labelDeepestDepth, ansDeepestDepth;

    private static class Node {
        JLabel data;
        Node left, right;
        int x, y;

        Node(int info) {
            data = new JLabel(info + "", SwingConstants.CENTER);
            data.setFont(new Font("Segoe UI", Font.BOLD, 20));
            data.setBorder(BorderFactory.createLineBorder(Color.black));
            data.setOpaque(true);
            data.setBackground(new Color(139, 69, 19)); // Kahverengi tonunda bir renk
            left = right = null;
        }
    }

    public BinaryTreeNode() {
        random = new Random();
        nodeSet = new HashSet<>();
        initialize();
    }

    private void initialize() {
        setSize(1200, 700);
        setLayout(new BorderLayout());
        setTitle("Binary Tree Visualization");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Top Panel for Controls
        topPanel = new JPanel(new BorderLayout());
        topLeftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        topPanel.add(topLeftPanel, BorderLayout.WEST);
        topPanel.add(topRightPanel, BorderLayout.EAST);

        // Tree Panel for Visualization
        treePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawTree(g, root, getWidth() / 2, 50, getWidth() / 4, 50);
            }
        };
        treePanel.setPreferredSize(new Dimension(getWidth(), getHeight() - 300));
        treePanel.setBackground(new Color(245, 222, 179));

        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setPreferredSize(new Dimension(getWidth(), 200));

        tf = new JTextField("");
        tf.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        tf.setPreferredSize(new Dimension(180, 40));
        tf.setBorder(BorderFactory.createLineBorder(new Color(139, 69, 19), 2));
        tf.addKeyListener(this);
        topRightPanel.add(tf);

        // Add Button
        btnAdd = new JButton("Add");
        btnAdd.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnAdd.setBackground(new Color(210, 105, 30));
        btnAdd.setForeground(Color.WHITE);
        btnAdd.setPreferredSize(new Dimension(120, 40));
        btnAdd.setBorderPainted(false);
        btnAdd.setFocusPainted(false);
        btnAdd.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnAdd.addActionListener(this);
        topRightPanel.add(btnAdd);

        // Find Deepest Node Button
        btnFindDeepest = new JButton("Find Deepest");
        btnFindDeepest.setFont(new Font("Segoe UI", Font.BOLD, 18));
        btnFindDeepest.setBackground(new Color(204, 85, 0));
        btnFindDeepest.setForeground(Color.WHITE);
        btnFindDeepest.setPreferredSize(new Dimension(150, 40));
        btnFindDeepest.setBorderPainted(false);
        btnFindDeepest.setFocusPainted(false);
        btnFindDeepest.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnFindDeepest.addActionListener(this);
        topRightPanel.add(btnFindDeepest);

        // Deepest Node Label
        labelDeepestNode = new JLabel("Deepest Node: ");
        labelDeepestNode.setFont(new Font("Segoe UI", Font.BOLD, 20));
        labelDeepestNode.setHorizontalAlignment(SwingConstants.CENTER);
        labelDeepestNode.setAlignmentX(Component.CENTER_ALIGNMENT);
        // Add space before the label
        infoPanel.add(Box.createVerticalStrut(10)); 
        infoPanel.add(labelDeepestNode);

        // Deepest Node Answer
        ansDeepestNode = new JLabel("None");
        ansDeepestNode.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        ansDeepestNode.setForeground(new Color(210, 105, 30)); // Turuncu-kahverengi tonunda metin rengi
        infoPanel.add(ansDeepestNode);

        // Deepest Node Depth Label
        labelDeepestDepth = new JLabel("Deepest Node Depth: ");
        labelDeepestDepth.setFont(new Font("Segoe UI", Font.BOLD, 20));
        labelDeepestDepth.setHorizontalAlignment(SwingConstants.CENTER); // Ortalamak için kullanılır
        labelDeepestDepth.setAlignmentX(Component.CENTER_ALIGNMENT);
        infoPanel.add(labelDeepestDepth);

        // Deepest Node Depth Answer
        ansDeepestDepth = new JLabel("0");
        ansDeepestDepth.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        ansDeepestDepth.setForeground(new Color(210, 105, 30)); // Turuncu-kahverengi tonunda metin rengi
        infoPanel.add(ansDeepestDepth);

        // Add all panels to the main frame
        add(topPanel, BorderLayout.NORTH);
        add(treePanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent evt) {
        if (tf.isEnabled()) {
            try {
                if (evt.getSource() == btnAdd) {
                    addRandomNode();
                } else if (evt.getSource() == btnFindDeepest) {
                    Node deepest = findDeepestNode(root);
                    int depth = findDeepestNodeDepth(root);
                    ansDeepestNode.setText(deepest != null ? deepest.data.getText() : "None");
                    ansDeepestDepth.setText(String.valueOf(depth));
                }
                tf.setText("");
                tf.requestFocusInWindow();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Please Enter Integer.");
            }
        }
    }

    public void addRandomNode() {  //For node distribution
        try {
            int value = Integer.parseInt(tf.getText());
            if (nodeSet.contains(value)) {
                JOptionPane.showMessageDialog(null, "Node with value " + value + " already exists.");
                return;
            }
            Node newNode = new Node(value);
            nodeSet.add(value);

            if (root == null) {
                root = newNode;
            } else {
                Node curr = root;
                while (true) {
                    if (random.nextBoolean()) {
                        if (curr.left == null) {
                            curr.left = newNode;
                            break;
                        } else {
                            curr = curr.left;
                        }
                    } else {
                        if (curr.right == null) {
                            curr.right = newNode;
                            break;
                        } else {
                            curr = curr.right;
                        }
                    }
                }
            }
            repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Please enter a valid integer.");
        }
    }

    private Node findDeepestNode(Node root) {
        if (root == null) return null;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        Node current = null;
        while (!queue.isEmpty()) {
            current = queue.poll();
            if (current.left != null) queue.add(current.left);
            if (current.right != null) queue.add(current.right);
        }
        return current;
    }

    private int findDeepestNodeDepth(Node root) {
        if (root == null) return 0;
        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        int depth = 0;

        while (!queue.isEmpty()) {
            int size = queue.size();
            depth++;
            for (int i = 0; i < size; i++) {
                Node current = queue.poll();
                if (current.left != null) queue.add(current.left);
                if (current.right != null) queue.add(current.right);
            }
        }
        return depth - 1;
    }

    private void drawTree(Graphics g, Node node, int x, int y, int xOffset, int yOffset) {
        if (node == null) return;

        node.x = x;
        node.y = y;

        if (node.left != null) {
            g.drawLine(x, y, x - xOffset, y + yOffset);
            drawTree(g, node.left, x - xOffset, y + yOffset, xOffset / 2, yOffset);
        }
        if (node.right != null) {
            g.drawLine(x, y, x + xOffset, y + yOffset);
            drawTree(g, node.right, x + xOffset, y + yOffset, xOffset / 2, yOffset);
        }

        g.setColor(new Color(210, 105, 30)); // Turuncu-kahverengi tonunda çizim
        g.fillOval(x - 20, y - 20, 40, 40);
        g.setColor(Color.BLACK);
        g.drawOval(x - 20, y - 20, 40, 40);
        g.drawString(node.data.getText(), x - 10, y + 5);
    }

    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    public static void main(String[] args) {
        new BinaryTreeNode();
    }
}
