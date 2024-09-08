import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TicTacToe implements ActionListener {
    JFrame frame;
    CurvedButton[] buttons = new CurvedButton[9];
    CurvedButton restartButton;
    JLabel messageLabel;
    boolean xTurn = true;
    int count = 0;
    int[][] winningCombinations = {
        {0, 1, 2}, {3, 4, 5}, {6, 7, 8},  // Horizontal
        {0, 3, 6}, {1, 4, 7}, {2, 5, 8},  // Vertical
        {0, 4, 8}, {2, 4, 6}  // Diagonal
    };

    // Constructor
    public TicTacToe() {
        frame = new JFrame("Tic Tac Toe");
        frame.setSize(500, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GradientPanel panel = new GradientPanel();
        panel.setLayout(null); // Custom layout

        messageLabel = new JLabel("X's turn");
        messageLabel.setBounds(175, 20, 150, 30); // Centered horizontally
        messageLabel.setFont(new Font("Raleway", Font.BOLD, 26));
        messageLabel.setForeground(Color.WHITE); // Set text color to white for better contrast
        panel.add(messageLabel);

        int buttonSize = 100; // Size of each button
        int gap = 15; // Gap between buttons
        int gridSize = 3; // Number of buttons per row/column
        int totalWidth = gridSize * buttonSize + (gridSize - 1) * gap; // Total width of the grid
        int startX = (frame.getWidth() - totalWidth) / 2; // Calculate starting x position to center the grid
        int startY = 100; // Starting y position for the grid

        for (int i = 0; i < 9; i++) {
            buttons[i] = new CurvedButton(" ");
            int x = startX + (i % gridSize) * (buttonSize + gap); // Calculate x position
            int y = startY + (i / gridSize) * (buttonSize + gap); // Calculate y position
            buttons[i].setBounds(x, y, buttonSize, buttonSize);
            buttons[i].setFont(new Font("Arial", Font.PLAIN, 50)); // Adjust font size for larger buttons
            buttons[i].setForeground(new Color(113, 44, 145));  // Text color for 'X' and 'O'
            buttons[i].setBackground(Color.WHITE);  // Button background color
            buttons[i].setFocusPainted(false);  // Remove the focus border
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
        }

        restartButton = new CurvedButton("Restart");
        restartButton.setBounds(150, 500, 200, 50); // Centered horizontally
        restartButton.setFont(new Font("Arial", Font.PLAIN, 20));
        restartButton.setBackground(Color.WHITE); // Dark background for restart button
        restartButton.setForeground(Color.BLACK); // Text color
        restartButton.setFocusPainted(false);
        restartButton.addActionListener(this);
        panel.add(restartButton);

        frame.add(panel);
        frame.setVisible(true);
    }

    // Button click handler
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == restartButton) {
            resetGame();
            return;
        }

        CurvedButton buttonClicked = (CurvedButton) e.getSource();
        if (buttonClicked.getText().equals(" ")) {
            buttonClicked.setText(xTurn ? "X" : "O");
            buttonClicked.setEnabled(false);
            count++;
            xTurn = !xTurn;
            messageLabel.setText(xTurn ? "X's turn" : "O's turn");

            if (checkWin()) {
                messageLabel.setText((xTurn ? "O" : "X") + " Wins!");
                disableButtons();
            } else if (count == 9) {
                messageLabel.setText("It's a draw!");
            }
        }
    }

    // Check for a win
    private boolean checkWin() {
        for (int[] combo : winningCombinations) {
            if (!buttons[combo[0]].getText().equals(" ") &&
                buttons[combo[0]].getText().equals(buttons[combo[1]].getText()) &&
                buttons[combo[1]].getText().equals(buttons[combo[2]].getText())) {
                return true;
            }
        }
        return false;
    }

    // Disable buttons after the game is over
    private void disableButtons() {
        for (CurvedButton button : buttons) {
            button.setEnabled(false);
        }
    }

    // Reset the game
    private void resetGame() {
        for (CurvedButton button : buttons) {
            button.setText(" ");
            button.setEnabled(true);
        }
        count = 0;
        xTurn = true;
        messageLabel.setText("X's turn");
    }

    // Custom class for curved buttons
    class CurvedButton extends JButton {
        public CurvedButton(String text) {
            super(text);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Curved edges
            g2.setColor(getForeground());
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30); // Border
            super.paintComponent(g);
            g2.dispose();
        }
    }

    // Custom JPanel to handle gradient background
    class GradientPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            GradientPaint gradient = new GradientPaint(0, 0, new Color(128, 82, 236), 
                                                      getWidth(), getHeight(), new Color(209, 97, 255));
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    // Main method to run the game
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TicTacToe());
    }
}
