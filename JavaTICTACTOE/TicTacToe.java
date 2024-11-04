import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// TicTacToe class to handle the main game logic and frame setup
public class TicTacToe {
    private static final int BOARD_WIDTH = 600;
    private static final int BOARD_HEIGHT = 650; // 50px for the text panel on top

    private JFrame frame;
    private JLabel textLabel;
    private JPanel textPanel;
    private JPanel boardPanel;

    private JButton[][] board;
    private Player playerX;
    private Player playerO;
    private Player currentPlayer;

    private boolean gameOver;
    private int turns;

    public TicTacToe() {
        frame = new JFrame("Tic-Tac-Toe");
        textLabel = new JLabel();
        textPanel = new JPanel();
        boardPanel = new JPanel();
        board = new JButton[3][3];
        playerX = new HumanPlayer("X");
        playerO = new HumanPlayer("O");
        currentPlayer = playerX;

        initializeGame();
    }

    // Initializes the game window and board
    private void initializeGame() {
        frame.setVisible(true);
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        setupTextLabel();
        setupBoardPanel();

        frame.add(textPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
    }

    private void setupTextLabel() {
        textLabel.setBackground(Color.darkGray);
        textLabel.setForeground(Color.white);
        textLabel.setFont(new Font("Arial", Font.BOLD, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Tic-Tac-Toe");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
    }

    private void setupBoardPanel() {
        boardPanel.setLayout(new GridLayout(3, 3));
        boardPanel.setBackground(Color.darkGray);

        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                JButton tile = new JButton();
                board[r][c] = tile;
                tile.setBackground(Color.darkGray);
                tile.setForeground(Color.white);
                tile.setFont(new Font("Arial", Font.BOLD, 120));
                tile.setFocusable(false);

                tile.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        if (gameOver) return;
                        JButton tile = (JButton) e.getSource();
                        if (tile.getText().isEmpty()) {
                            tile.setText(currentPlayer.getSymbol());
                            turns++;
                            checkWinner();
                            if (!gameOver) {
                                togglePlayer();
                                textLabel.setText(currentPlayer.getSymbol() + "'s turn.");
                            }
                        }
                    }
                });

                boardPanel.add(tile);
            }
        }
    }

    private void togglePlayer() {
        currentPlayer = (currentPlayer == playerX) ? playerO : playerX;
    }

    private void checkWinner() {
        // Check horizontal, vertical, and diagonal wins
        for (int r = 0; r < 3; r++) {
            if (isWinningLine(board[r][0], board[r][1], board[r][2])) {
                declareWinner(board[r][0], board[r][1], board[r][2]);
                return;
            }
        }

        for (int c = 0; c < 3; c++) {
            if (isWinningLine(board[0][c], board[1][c], board[2][c])) {
                declareWinner(board[0][c], board[1][c], board[2][c]);
                return;
            }
        }

        if (isWinningLine(board[0][0], board[1][1], board[2][2])) {
            declareWinner(board[0][0], board[1][1], board[2][2]);
            return;
        }

        if (isWinningLine(board[0][2], board[1][1], board[2][0])) {
            declareWinner(board[0][2], board[1][1], board[2][0]);
            return;
        }

        if (turns == 9) {
            declareTie();
        }
    }

    private boolean isWinningLine(JButton tile1, JButton tile2, JButton tile3) {
        return !tile1.getText().isEmpty() && tile1.getText().equals(tile2.getText()) && tile2.getText().equals(tile3.getText());
    }

    private void declareWinner(JButton... winningTiles) {
        for (JButton tile : winningTiles) {
            tile.setForeground(Color.green);
            tile.setBackground(Color.gray);
        }
        textLabel.setText(currentPlayer.getSymbol() + " is the winner!");
        gameOver = true;
    }

    private void declareTie() {
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                board[r][c].setForeground(Color.orange);
                board[r][c].setBackground(Color.gray);
            }
        }
        textLabel.setText("It's a tie!");
        gameOver = true;
    }

    // Player base class with polymorphic behavior
    abstract static class Player {
        private String symbol;

        public Player(String symbol) {
            this.symbol = symbol;
        }

        public String getSymbol() {
            return symbol;
        }
    }

    // HumanPlayer as a concrete class for player type
    static class HumanPlayer extends Player {
        public HumanPlayer(String symbol) {
            super(symbol);
        }
    }

    // Main method to start the game
    public static void main(String[] args) {
        new TicTacToe();
    }
}
