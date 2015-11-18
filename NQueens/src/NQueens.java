import javax.swing.*;

/**
 * Created by akhil on 18/11/15.
 */
public class NQueens {
    public static void main(String[] args) {
        //Multisolution works only for BS == NOQ
        int bs = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter Board size"));
        int queens = Integer.parseInt(JOptionPane.showInputDialog(null,"Enter num queens"));

        Queens obj = new Queens(queens,bs);
        obj.BoardReset();
        obj.multiSoln();
    }
}


class Queens extends JPanel{
    int counter;
    int BS,NOQ; //Board Size , Number of Queens
    boolean[][] Board;
    int printUnit;
    public Queens(int numberOfQueens,int boardSize) {
        BS = boardSize; NOQ = numberOfQueens;
        Board = new boolean[BS][BS];
        //printBoard();
    }
    
    public Queens(int n) {
        BS = NOQ = n;
        Board = new boolean[BS][BS];
    }


    void BoardReset() {
        for(int i = 0;i<BS;i++) {
            for(int j = 0;j<BS;j++)
                Board[i][j] = false;
        }
        counter = 0;
    }

    public void printBoard() {
        for(int i = 0;i<BS;i++) {
            System.out.println();
            for (int j = 0; j < BS; j++) {
                if(Board[i][j])
                    System.out.print("1 ");
                else
                    System.out.print("0 ");
            }
        }
        System.out.println();
    }

    public void multiSoln() {
        multisoln();
    }

    public void singleSoln() {
        if(recurSingle(0,0)) {
            System.out.println("Solution");
            printBoard();
        }
        else
            System.out.println("No solution.");
    }

    void multisoln() {
        int solnCounter = 0;
        for(int i = 0;i<=BS-NOQ;i++) {
            for (int j = 0; j < BS; j++) {
                Board[i][j] = true;
                counter++;
                if (recurSingle(i+1, 0)) {
                    solnCounter++;
                    System.out.println("Solution " + solnCounter);
                    printBoard();
                }
                BoardReset();
            }
        }
        System.out.println("Total Solutions: " + solnCounter);
    }

    boolean recurSingle(int row,int col) {
        if(row>=BS || counter==NOQ)
            return true;
        for(int j = col;j<BS;j++) {
            if(place(row,j)) {
                Board[row][j] = true;
                counter++;
                if(recurSingle(row +1,0))
                    return true;
                Board[row][j] = false;
                counter--;
            }
        }
        return false;
    }

    boolean place(int i,int j) {
        for(int x = 0;x<i;x++) //Check only column above place. Going Row wise.
            if(Board[x][j])
                return false;

        for(int l = i,m = j;l>=0 && m>=0;l--,m--)
            if(Board[l][m])
                return false;

        for(int l = i,m = j;l>=0 && m<BS;l--,m++)
            if(Board[l][m])
                return false;
        return true;
    }
}