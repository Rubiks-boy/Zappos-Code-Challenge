import java.io.File;
import java.util.Scanner;

public class Driver {
    public static void main(String [] args)
    {
        try{
            Scanner file = new Scanner(new File("zapposCodeChallenge.txt"));
            //Scanner file = new Scanner(new File("singleTest.txt"));
            char[][] p = new char[6][6];
            while(file.hasNextLine())
            {
                String size = file.nextLine();
                for(int i = 0; i < 6; i++)
                {
                    String line = file.nextLine();
                    for(int c = 0; c < 6; c++)
                    {
                        p[i][c] = line.charAt(c);
                    }
                }
               if(file.hasNextLine()) file.nextLine();
                System.out.println("Puzzle: ");
                for(int r = 0; r<6; r++)
                {
                    for(int c = 0; c<6; c++)
                        System.out.print(p[r][c]);
                    System.out.println();
                }
                System.out.println();

                System.out.println("Solution: ");
                SolvePaths3 solve = new SolvePaths3(p);
                char [][] solution = solve.solve();
                for(int r = 0; r<6; r++)
                {
                    for(int c = 0; c<6; c++)
                        System.out.print(solution[r][c]);
                    System.out.println();
                }
                System.out.println();
                Scanner in = new Scanner(System.in);
                in.nextLine();
            }

        }
        catch(Exception e) {e.printStackTrace();}
        /* char[][] p = {
        {'.','.','.','.','.','o'},
        {'.','.','.','.','.','.'},
        {'.','y','r','.','.','.'},
        {'o','.','.','g','.','.'},
        {'g','.','.','b','r','.'},
        {'b','.','.','y','.','.'}
        };*/
        /* char[][] p = {
        {'.','.','.','.','.','.'},
        {'.','.','.','.','b','.'},
        {'.','.','r','y','o','g'},
        {'g','b','.','.','.','.'},
        {'r','.','.','.','y','.'},
        {'o','.','.','.','.','.'}
        };*/
        /*char[][] p = {
        {'.','.','.','.','.','g'},
        {'.','.','.','.','.','.'},
        {'b','.','b','o','.','.'},
        {'.','.','.','.','.','g'},
        {'.','.','.','.','r','y'},
        {'o','r','y','.','.','.'}
        };*/
        /*char[][] p = {
        {'.','.','.','r','y','.'},
        {'.','g','.','.','g','.'},
        {'.','.','.','.','.','.'},
        {'b','.','b','r','.','.'},
        {'y','.','.','.','.','.'},
        {'.','.','.','.','.','.'}
        };*/
        /*char[][] p = {
        {'.','.','.','.','.','o'},
        {'.','.','.','.','.','.'},
        {'.','y','r','.','.','.'},
        {'o','.','.','g','.','.'},
        {'g','.','.','b','r','.'},
        {'b','.','.','y','.','.'}
        };*/
        /*SolvePaths3 solve = new SolvePaths3(p);
        char [][] solution = solve.solve();
        for(int r = 0; r<6; r++)
        {
        for(int c = 0; c<6; c++)
        System.out.print(solution[r][c]);
        System.out.println();
        }
        System.out.println();*/
    }
}
