import java.util.Scanner;

public class TicTacToe {
    private static final int SIZE = 3;
    private static final int EMPTY = 0;
    private static final int PLAYER = 1;
    private static final int AI = -1;

    private static int[][] board = new int[SIZE][SIZE];
    private static int playerSymbol; // 1 or -1
    private static int aiSymbol;
    private static boolean playerFirst;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initializeBoard();

        // Pilih simbol
        System.out.print("Pilih simbol kamu (X/O): ");
        char symbol = scanner.next().toUpperCase().charAt(0);
        if (symbol == 'X') {
            playerSymbol = PLAYER;
            aiSymbol = AI;
        } else {
            playerSymbol = AI;
            aiSymbol = PLAYER;
        }

        // Pilih siapa jalan duluan
        System.out.print("Apakah kamu ingin jalan duluan? (y/n): ");
        playerFirst = scanner.next().equalsIgnoreCase("y");

        printBoard();

        while (true) {
            if (playerFirst) {
                playerMove(scanner);
                if (checkGameOver(playerSymbol)) break;

                aiMove();
                if (checkGameOver(aiSymbol)) break;
            } else {
                aiMove();
                if (checkGameOver(aiSymbol)) break;

                playerMove(scanner);
                if (checkGameOver(playerSymbol)) break;
            }
        }

        scanner.close();
    }

    private static void initializeBoard() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = EMPTY;
    }

    private static void printBoard() {
        System.out.println("\nPapan:");
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                char c = ' ';
                if (board[i][j] == playerSymbol) c = (playerSymbol == PLAYER ? 'X' : 'O');
                else if (board[i][j] == aiSymbol) c = (aiSymbol == PLAYER ? 'X' : 'O');
                System.out.print(" " + c + " ");
                if (j < SIZE - 1) System.out.print("|");
            }
            System.out.println();
            if (i < SIZE - 1) System.out.println("---+---+---");
        }
    }

    private static void playerMove(Scanner scanner) {
        while (true) {
            System.out.print("Masukkan baris (1-3): ");
            int row = scanner.nextInt() - 1;
            System.out.print("Masukkan kolom (1-3): ");
            int col = scanner.nextInt() - 1;

            if (row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == EMPTY) {
                board[row][col] = playerSymbol;
                break;
            } else {
                System.out.println("Langkah tidak valid. Coba lagi.");
            }
        }
        printBoard();
    }

    private static void aiMove() {
        System.out.println("Komputer bergerak...");
        int bestScore = Integer.MIN_VALUE;
        int moveRow = -1, moveCol = -1;

        if (board[1][1] == EMPTY) {
            board[1][1] = aiSymbol;
            System.out.println("Komputer ke baris 2, kolom 2");
            printBoard();
            return;
        }

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = aiSymbol;
                    int score = minimax(0, false);
                    board[i][j] = EMPTY;

                    if (score > bestScore) {
                        bestScore = score;
                        moveRow = i;
                        moveCol = j;
                    }
                }
            }
        }

        board[moveRow][moveCol] = aiSymbol;
        System.out.println("Komputer ke baris " + (moveRow + 1) + ", kolom " + (moveCol + 1));
        printBoard();
    }

    private static boolean checkGameOver(int symbol) {
        if (checkWin(symbol)) {
            if (symbol == playerSymbol)
                System.out.println("Kamu menang!");
            else
                System.out.println("Komputer menang!");
            return true;
        } else if (isBoardFull()) {
            System.out.println("Hasil seri.");
            return true;
        }
        return false;
    }

    private static boolean checkWin(int symbol) {
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] + board[i][1] + board[i][2] == symbol * 3) return true;
            if (board[0][i] + board[1][i] + board[2][i] == symbol * 3) return true;
        }
        if (board[0][0] + board[1][1] + board[2][2] == symbol * 3) return true;
        if (board[0][2] + board[1][1] + board[2][0] == symbol * 3) return true;
        return false;
    }

    private static boolean isBoardFull() {
        for (int[] row : board)
            for (int cell : row)
                if (cell == EMPTY)
                    return false;
        return true;
    }

    private static int minimax(int depth, boolean isMax) {
        if (checkWin(aiSymbol)) return 10 - depth;
        if (checkWin(playerSymbol)) return depth - 10;
        if (isBoardFull()) return 0;

        int bestScore = isMax ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == EMPTY) {
                    board[i][j] = isMax ? aiSymbol : playerSymbol;
                    int score = minimax(depth + 1, !isMax);
                    board[i][j] = EMPTY;
                    bestScore = isMax ? Math.max(score, bestScore) : Math.min(score, bestScore);
                }
            }
        }
        return bestScore;
    }
}
