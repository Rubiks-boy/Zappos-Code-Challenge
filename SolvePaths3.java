import java.util.ArrayList;

public class SolvePaths3 
{

    final int ROWS = 6, COLS = 6;
    private char [][] pUnsolved;
    ArrayList<Character> COLORS = new ArrayList<Character>();
    //{'r', 'o', 'y', 'g', 'b'};
    int[][] outsideXY;
    int[] numOutside;
    char[][] solution;
    boolean [][] stillInPlay;
    char currentColor;
    ArrayList<Boolean> colorUsed = new ArrayList<Boolean>();
    int usingOutside = 2;
    int index;

    public SolvePaths3(char [][] puzzle)
    {
        pUnsolved = new char[ROWS][COLS];
        for(int r = 0; r < ROWS; r++)
            for(int c = 0; c < COLS; c++)
            {
                pUnsolved[r][c] = puzzle[r][c];
                if(puzzle[r][c] != '.')
                {
                    boolean colorExists = false;
                    for(int i = 0; i < COLORS.size(); i++)
                    {
                        if(COLORS.get(i) == puzzle[r][c])
                            colorExists = true;
                    }

                    if(!colorExists)
                        COLORS.add(puzzle[r][c]);
                }
            }

        for(int i = 0; i < COLORS.size(); i++)
            colorUsed.add(false);

            //for(char curr: COLORS)
            //System.out.print(curr);
            //System.out.println();
        numOutside = new int[COLORS.size()];
        for(int i = 0; i < COLORS.size(); i++)
            numOutside[i] = 0;
    }

    public char[][] solve()
    {
        //solution
        solution = new char[ROWS][COLS];
        for(int r = 0; r < ROWS; r++)
            for(int c = 0; c < COLS; c++)
                solution[r][c] = pUnsolved[r][c];

        //modified ones are in CAPS

        //color, then x1 y1, x2, y2, c
        outsideXY = new int[COLORS.size()][4];

        setXY();

        stillInPlay = new boolean[ROWS][COLS];
        for(int r = 0; r < ROWS; r++)
            for(int c = 0; c < COLS; c++)
                stillInPlay[r][c] = true;

        for(int i = 0; i < COLORS.size() && notAllUsed(); i++)
        {
            if(!colorUsed.get(i))
            {
                currentColor = COLORS.get(i);
                printSolution();
                searchForOutside();
                findOuterPath();
            }
        }

        return solution;
    }

    public boolean notAllUsed()
    {
        for(int i = 0; i < COLORS.size(); i++)
        {
            if(colorUsed.get(i) == false)
                return true;
        }
        return false;
    }

    public void searchForOutside()
    {
        for(int i = 0; i < COLORS.size(); i++)
            numOutside[i] = 0;
        for(int r = 0; r < ROWS; r++)
        {
            for(int c = 0; c < COLS; c++)
            {
                if(stillInPlay[r][c] == true)
                {
                    if(solution[r][c] != '.')
                    {
                        //determine color
                        //System.out.println("Finding outside " + r + " " + c);
                        int i;
                        for(i = 0; i < COLORS.size(); i++)
                            if(COLORS.get(i) == solution[r][c])
                                break;

                        if(isOuter(r, c))
                        {

                            //System.out.println("Outer found: " + r + " " + c);
                            //it's outside

                            numOutside[i]++;
                            //System.out.println(COLORS.get(i) + ": " + numOutside[i]);
                        }

                        else if((pUnsolved[r-1][c] != '.' && isOuter(r-1, c)) ||
                        (pUnsolved[r+1][c] != '.' && isOuter(r+1, c)) ||
                        (pUnsolved[r][c-1] != '.' && isOuter(r, c-1)) ||
                        (pUnsolved[r][c+1] != '.' && isOuter(r, c+1)))
                        {

                            //System.out.println("Outer found: " + r + " " + c);
                            //it's outside

                            numOutside[i]++;
                            //System.out.println(COLORS.get(i) + ": " + numOutside[i]);
                        }

                    }
                }
            }
        }
    }

    public boolean isOuter(int r, int c)
    {
        if((((r > 0 && 
                    stillInPlay[r-1][c] == false) || (r < 5 && stillInPlay[r+1][c] == false))
            ||
            ((c > 0 && stillInPlay[r][c-1] == false )|| (c < 5 && stillInPlay[r][c+1] == false))
            ||
            ((r == 0) || (r == 5) || (c == 0) || (c == 5))))
            return true;
        else return false;
    }

    public void findOuterPath()
    {
        boolean foundOutside = false;
        //find one to find outer of
        int index;
        for(index = 0; index < numOutside.length; index++)
        {
            if(numOutside[index] == 2)
            {
                usingOutside = 2;
                foundOutside = true;
                //get x and y of each
                int x1, y1;
                int x2, y2;
                x1 = outsideXY[index][0];
                y1 = outsideXY[index][1];
                x2 = outsideXY[index][2];
                y2 = outsideXY[index][3];

                //DO MOVE
                setEndpoints(index);

                if(move(x1, y1, x2, y2, index)) 
                {
                    colorUsed.set(index, true);
                    break;
                }
            }
        }
        if(!foundOutside)
        {
          
            for(index = 0; index < numOutside.length; index++)
            {
                searchForOutside();
                if(numOutside[index] == 1)
                {
                    //System.out.println(index);
                    usingOutside = 1;
                    //System.out.println("color: " + currentColor + " & usingOutside set to 1");
                    foundOutside = true;
                    //get x and y of each
                    int x1, y1;
                    int x2, y2;
                    x1 = outsideXY[index][0];
                    y1 = outsideXY[index][1];
                    x2 = outsideXY[index][2];
                    y2 = outsideXY[index][3];

                    //DO MOVE
                    setEndpoints(index);
                    move(x1, y1, x2, y2, index);
                }
            }
        }

    }
    //0 - valid
    //1 - hit other point
    //2 - invalid
    public int moveValid(int x1, int y1, int x2, int y2)
    {
        if(x1 == x2 && y1 == y2)
            return 1;
        if(x1 < 0 || y1 < 0 || y1 > 5 || x1
        > 5)
            return 2;
        if(!stillInPlay[x1][y1])
            return 2;
        if( pUnsolved[x1][y1] == currentColor)
            return 2;
        if(solution[x1][y1] == '.' || solution[x1][y1] == currentColor)
        {
            if(x1 == 0 || y1 == 0 || x1 == 5 || y1 == 5)
                return 0;
            if(y1 == y2)
            {
                if(x1 + 1 == x2 || x1 - 1 == x2)
                    return 0;
                    if(x1 + 2 == x2 || x1 - 2 == x2)
                    return 0;
            }
            if(x1 == x2)
            {
                if(y1 - 1 == y2 || y1 + 1 == y2)
                    return 0;
                    if(y1 - 2 == y2 || y1 + 2 == y2)
                    return 0;
            }

            // if(usingOutside == 2)
            //{
            int numBordering = 0;
            int numBorderingCorner = 0;
            if(solution[x1+1][y1] == currentColor)
                numBordering++;
            if(solution[x1-1][y1] == currentColor)
                numBordering++;
            if(solution[x1][y1+1] == currentColor)
                numBordering++;
            if(solution[x1][y1-1] == currentColor)
                numBordering++;
            if(!stillInPlay[x1+1][y1+1] && solution[x1+1][y1+1] != currentColor)
                numBorderingCorner++;
            if(!stillInPlay[x1-1][y1+1] && solution[x1-1][y1+1] != currentColor)
                numBorderingCorner++;
            if(!stillInPlay[x1-1][y1-1] && solution[x1-1][y1-1] != currentColor)
                numBorderingCorner++;
            if(!stillInPlay[x1+1][y1-1] && solution[x1+1][y1-1] != currentColor)
                numBorderingCorner++;

            if(numBordering > 1) 
                return 2;
            else if(numBordering == 1)
            {
                if(numBorderingCorner >= 1)
                    return 0;
            }
            if(!stillInPlay[x1-1][y1]  || !stillInPlay[x1+1][y1]  || !stillInPlay[x1][y1-1] || !stillInPlay[x1][y1+1] )
            {
                printSolution();
                return 0;
            }
            //}
            /*else {
            System.out.println("1");
            return 0;
            }*/
        }
        return 2;
    }

    public boolean pathGood()
    {
        for(int r = 0; r < 6; r++)
        {
            for(int c = 0; c < 6; c++)
            {
                if(stillInPlay[r][c])
                {
                    int borderedNotPlay = 0;
                    if(r==0 || !stillInPlay[r-1][c])
                        borderedNotPlay++;
                    if(r==5 || !stillInPlay[r+1][c])
                        borderedNotPlay++;
                    if(c==0 || !stillInPlay[r][c-1])
                        borderedNotPlay++;
                    if(c==5 || !stillInPlay[r][c+1])
                        borderedNotPlay++;
                    if(borderedNotPlay == 4 || (pUnsolved[r][c] == '.' && borderedNotPlay >= 3))
                        return false;
                }
            }
        }
        if(!notAllUsed())
        {
            for(int r = 0; r < ROWS; r++)
            {
                for(int c = 0; c < COLS; c++)
                {
                    if(solution[r][c] == '.')
                        return false;
                }
            }
        }
        if(!allFalse())
        {
        int numColorsUsed = 0;
        for(int i = 0; i < COLORS.size(); i++)
            if(colorUsed.get(i))
                numColorsUsed++;
        searchForOutside();
        findOuterPath();
        int numColorsUsedAfter = 0;
        for(int i = 0; i < COLORS.size(); i++)
            if(colorUsed.get(i))
                numColorsUsedAfter++;
        if(numColorsUsedAfter == numColorsUsed)
            return false;
        }
        return true;
    }
    
    public boolean allFalse()
    {
        for(int r = 0; r< 6; r++)
            for(int c =0 ; c<6; c++)
                if(stillInPlay[r][c])
                    return false;
         return true;
    }

    public void setEndpoints(int i)
    {
        stillInPlay[outsideXY[i][0]][outsideXY[i][1]] = false;
        stillInPlay[outsideXY[i][2]][outsideXY[i][3]] = false;
        //printSolution();
    }

    public boolean move(int x1, int y1, int x2, int y2, int i)
    {

        solution[x1][y1] = COLORS.get(i);
        currentColor = COLORS.get(i);

        printSolution();
        int [] result = new int[4];
        result[0] = moveValid(x1+1, y1, x2, y2);
        result[1] = moveValid(x1-1, y1, x2, y2);
        result[2] = moveValid(x1, y1+1, x2, y2);
        result[3] = moveValid(x1, y1-1, x2, y2);
        int result0 = result[0];
        int result1 = result[1];
        int result2 = result[2];
        int result3 = result[3];
        for(int j = 0; j < 4; j++)
        {
            if(result[j] == 1)//perform move
            {
                setEndpoints(i);
                stillInPlay[x1][y1] = false;
                solution[x1][y1] = currentColor;
                if(pathGood()) {
                    //printSolution();
                    colorUsed.set(index, true);
                    return true;
                }
                else
                {
                    stillInPlay[x1][y1] = true;
                    //solution[x1][y1] = pUnsolved[x1][y1];
                }
            }
        }

        if(currentColor == 'r' && x1 ==1&&y1==1)
        {
            int asdf = 5;
        }
        if(result[0] == 0)
        {
            stillInPlay[x1][y1] = false;
            boolean end = move(x1+1, y1, x2, y2, i);

            printSolution();
            if(end == true)
                return true;
            //solution[x1][y1] = pUnsolved[x1][y1];

            if(pUnsolved[x1][y1] == '.')
            {
                stillInPlay[x1][y1] = true;
                //return false;
            }
        }
        if(result[1] == 0)
        {
            if(currentColor == 'y')
            {
                int asdf = 5;
            }
            stillInPlay[x1][y1] = false;
            boolean end = move(x1-1, y1, x2, y2, i);

            printSolution();
            if(end == true)
                return true;
            //solution[x1][y1] = pUnsolved[x1][y1];
            if(pUnsolved[x1][y1] == '.')
            {
                stillInPlay[x1][y1] = true;
                //return false;
            }
        }
        if(result[2] == 0)
        {
            stillInPlay[x1][y1] = false;
            boolean end = move(x1, y1+1, x2, y2, i);

            printSolution();
            if(end == true)
                return true;
            //solution[x1][y1] = pUnsolved[x1][y1];
            if(pUnsolved[x1][y1] == '.')
            {
                stillInPlay[x1][y1] = true;
                //return false;
            }
        }
        if(result[3] == 0)
        {
            stillInPlay[x1][y1] = false;
            boolean end = move(x1, y1-1, x2, y2, i);

            printSolution();
            if(end == true)
                return true;
            //solution[x1][y1] = pUnsolved[x1][y1];
            if(pUnsolved[x1][y1] == '.')
            {
                stillInPlay[x1][y1] = true;
                //return false;
            }
        }

        solution[x1][y1] = pUnsolved[x1][y1];
        stillInPlay[x1][y1] = true;
        stillInPlay[x2][y2] = true;
        return false;
    }

    public void setXY()
    {
        int numOutside[] = new int[COLORS.size()];
        for(int i = 0; i < COLORS.size(); i++)
            numOutside[i] = 0;

        for(int r = 0; r <6; r++)
        {
            for(int c = 0; c<6; c++)
            {
                if(pUnsolved[r][c] != '.')
                {
                    int i;
                    for(i = 0; i < COLORS.size(); i++)
                        if(COLORS.get(i) == pUnsolved[r][c])
                            break;
                    if(numOutside[i] == 0)
                    {
                        outsideXY[i][0] = r;
                        outsideXY[i][1] = c;
                    }
                    else
                    {
                        outsideXY[i][2] = r;
                        outsideXY[i][3] = c;
                    }

                    numOutside[i]++;
                    //for(int j = 0; j < 5; j++)
                    //System.out.println(numOutside[j]);
                }

            }
        }
        /* for(int r = 0; r < 5; r++)
        {
        for(int c = 0; c < 4; c++)
        System.out.print(outsideXY[r][c]);
        System.out.println();
        }*/
    }

    public void printSolution()
    {
        /*int r;
        for(r = 0; r < ROWS; r++)
        {
            for(int c = 0; c < COLS; c++)
                System.out.print((char)solution[r][c]);
            System.out.println();}

        for(r = 0; r < ROWS; r++)
        {
            for(int c = 0; c < COLS; c++)
                System.out.print(stillInPlay[r][c]);
            System.out.println();
        }*/
    }
}

