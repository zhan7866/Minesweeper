import java.util.Random;

public class Minefield {
    /**
    Global Section
    */

    Cell[][] minefield;
    boolean[][] flagged;
    int numFlags, rows, columns;
    boolean gameOver;

    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE_BRIGHT = "\u001b[34;1m";
    public static final String ANSI_BLUE = "\u001b[34m";
    public static final String ANSI_RED_BRIGHT = "\u001b[31;1m";
    public static final String ANSI_RED = "\u001b[31m";
    public static final String ANSI_GREEN = "\u001b[32m";
    public static final String ANSI_GREY_BG = "\u001b[0m";
    /**
     * Constructor
     * @param rows       Number of rows.
     * @param columns    Number of columns.
     * @param flags      Number of flags, should be equal to mines
     */
    public Minefield(int rows, int columns, int flags) {
        numFlags = flags;
        flagged = new boolean[rows][columns];
        this.rows = rows;
        this.columns = columns;
        minefield = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                minefield[i][j] = new Cell(false, "-");
            }
        }

    }
    /**
     * evaluateField
     *
     * @function when a mine is found, calculate the surrounding 9x9 tiles values
     */
    public void evaluateField() {
        for (int i = 0; i < minefield.length; i++) {
            for (int j = 0; j < minefield[i].length; j++) {
                if (!minefield[i][j].getStatus().equals("M")) {
                    int counter = 0;
                    // Check the surrounding 3x3 tiles
                    for (int k = Math.max(0, i - 1); k <= Math.min(minefield.length - 1, i + 1); k++) {
                        for (int m = Math.max(0, j - 1); m <= Math.min(minefield[i].length - 1, j + 1); m++) {
                            // Check if it's a mine
                            if (minefield[k][m].getStatus().equals("M")) {
                                counter++;
                            }
                        }
                    }
                    minefield[i][j].setStatus(String.valueOf(counter));
                }
            }
        }
    }
    /**
     * createMines
     *
     * @param x       Start x, avoid placing on this square.
     * @param y        Start y, avoid placing on this square.
     * @param mines      Number of mines to place.
     */
    public void createMines(int x, int y, int mines) {
        Random random = new Random();
        int numMines = 0, tempX, tempY;
        while (numMines < mines) {
//            generates random variables for x and y
            tempX = random.nextInt(rows);
            tempY = random.nextInt(columns);
            if (!minefield[tempX][tempY].getRevealed() && !minefield[tempX][tempY].getStatus().equals("M") && tempX != x && tempY != y) {
//                sees if the x and y spots are already revealed
                minefield[tempX][tempY].setStatus("M");
                numMines++;
            }
        }
    }

    /**
     * guess
     *
     * @param x      
     * @param y      
     * @param flag    boolean value that allows the user to place a flag
     * @return false if guess did not hit mine or if flag was placed, true if mine found
     */
    public boolean guess(int x, int y, boolean flag) {
        // check if the guess is in bounds
        if (x < 0 || x >= rows || y < 0 || y >= columns) {
            return false;
        }
        Cell cell = minefield[x][y];

        // placing flag
        if (flag) {
            if (!cell.getRevealed()) {
                if (flagged[x][y]) {
                    flagged[x][y] = false;
                    numFlags++;
                } else if (numFlags > 0) {
                    flagged[x][y] = true;
                    numFlags--;
                }
            }
            return false;
        }
        // alr revealed cell
        if (cell.getRevealed()) {
            return false;
        }
        // reveal the cell
        cell.setRevealed(true);

        // 0 status
        if (cell.getStatus().equals("0")) {
            revealZeroes(x, y);
            return false;
        }
        // ends game if hits mine !!!
        if (cell.getStatus().equals("M")) {
            gameOver = true;
            return true;
        }

        return false;
    }
    /**
     * gameOver
     *
     * @return false if game is not over, otheriwse return true.
     */
    public boolean gameOver() {
        return gameOver;
    }

    /**
     * revealField
     *
     * @param x  
     * @param y   
     */
    public void revealZeroes(int x, int y) {
        Stack1Gen<int[]> stack = new Stack1Gen<>();
        stack.push(new int[] {x, y});
        int[] coordinates;
        int tempX, tempY;
        while (!stack.isEmpty()){
            coordinates = stack.pop();
            tempX = coordinates[0];
            tempY = coordinates[1];
            minefield[tempX][tempY].setRevealed(true);
            // sets revealed to true
            // checks up down left right
            if ((0 <= tempX - 1) && (!minefield[tempX - 1][tempY].getRevealed()) && (minefield[tempX - 1][tempY].getStatus().equals("0"))){
                stack.push(new int[]{tempX - 1, tempY});
            }
            if ((tempX + 1 < rows) && (!minefield[tempX + 1][tempY].getRevealed()) && (minefield[tempX + 1][tempY].getStatus().equals("0"))){
                stack.push(new int[]{tempX + 1, tempY});
            }
            if ((0 <= tempY - 1) && (!minefield[tempX][tempY - 1].getRevealed()) && (minefield[tempX][tempY - 1].getStatus().equals("0"))){
                stack.push(new int[]{tempX, tempY - 1});
            }
            if ((tempY + 1 < columns) && (!minefield[tempX][tempY + 1].getRevealed()) && (minefield[tempX][tempY + 1].getStatus().equals("0"))){
                stack.push(new int[]{tempX, tempY + 1});
            }
        }
    }

    /**
     * revealMines
     *
     * @param x   
     * @param y   
     */
    public void revealMines(int x, int y) {
        Q1Gen<int[]> queue = new Q1Gen<>();
        queue.add(new int[]{x, y});
        int[] coordinates;
        int tempX, tempY;

        while (queue.length() != 0){

            coordinates = queue.remove();
            tempX = coordinates[0];
            tempY = coordinates[1];

            minefield[tempX][tempY].setRevealed(true);
            // sets revealed to true

            if (minefield[tempX][tempY].getStatus().equals("M")){
                break;
            }
            if ((0 <= tempX - 1) && (!minefield[tempX - 1][tempY].getRevealed())){
                queue.add(new int[]{tempX - 1, tempY});
            }
            if ((tempX + 1 < rows) && (!minefield[tempX + 1][tempY].getRevealed())){
                queue.add(new int[]{tempX + 1, tempY});
            }
            if ((0 <= tempY - 1) && (!minefield[tempX][tempY - 1].getRevealed())){
                queue.add(new int[]{tempX, tempY - 1});
            }
            if ((tempY + 1 < columns) && (!minefield[tempX][tempY + 1].getRevealed())){
                queue.add(new int[]{tempX, tempY + 1});
            }
        }
    }

    /**
     * revealStart
     *
     * @param x      
     * @param y     
     */
    public void revealStart(int x, int y) {
        createMines(x, y, numFlags);

        evaluateField();

        guess(x, y, false);
    
        minefield[x][y].setRevealed(true);
        revealMines(x, y);
    
        
    }

    /**
     * printMinefield
     *
     * @fuctnion method should print the entire minefield, regardless if the user revealed
     * 
     * debug purposes !!!!
     */
    public void printMinefield() {
        String output = "  ";

        for (int i = 0; i < columns; i++) {
            output += " " + i;
        }
        output += "\n";
        for (int i = 0; i < rows; i++) {
            output += i + " ";
            for (int j = 0; j < columns; j++) {
                String cellStatus = minefield[i][j].getStatus();
                if ("1".equals(cellStatus)) {
                    output += "" + ANSI_BLUE + cellStatus + ANSI_GREY_BG + " ";
                } else if ("0".equals(cellStatus)) {
                    output += "" + ANSI_YELLOW + cellStatus + ANSI_GREY_BG + " ";
                } else if ("2".equals(cellStatus)) {
                    output += "" + ANSI_GREEN + cellStatus + ANSI_GREY_BG + " ";
                } else if ("3".equals(cellStatus)) {
                    output += "" + ANSI_RED + cellStatus + ANSI_GREY_BG +" ";
                } else if ("M".equals(cellStatus)) {
                    output += "" + ANSI_RED + cellStatus + ANSI_GREY_BG + " ";
                } else {
                    output += cellStatus + " ";
                }
            }
            output += "\n";
        }
        System.out.println(output);
    }

    /**
     * toString
     *
     * @return String representing board !!
     */
    public String toString() {
        String output = "  ";

        for (int i = 0; i < columns; i++) {
            output += " " + i;
        }
        output += "\n";
        for (int i = 0; i < rows; i++) {
            output += i + " ";
            for (int j = 0; j < columns; j++) {
                String cellStatus = minefield[i][j].getStatus();
                if (flagged[i][j]) {
                    output += "F ";
                } else if (minefield[i][j].getRevealed()) {
                    if ("1".equals(cellStatus)) {
                        output += "" + ANSI_BLUE + cellStatus + ANSI_GREY_BG + " ";
                    } else if ("0".equals(cellStatus)) {
                        output += "" + ANSI_YELLOW + cellStatus + ANSI_GREY_BG + " ";
                    } else if ("2".equals(cellStatus)) {
                        output += "" + ANSI_GREEN + cellStatus + ANSI_GREY_BG + " ";
                    } else if ("3".equals(cellStatus)) {
                        output += "" + ANSI_RED + cellStatus + ANSI_GREY_BG + " ";
                    } else if ("M".equals(cellStatus)) {
                        output += "" + ANSI_RED + cellStatus + ANSI_GREY_BG + " ";
                    } else {
                        output += cellStatus + " ";
                    }
                } else {
                    output += "- ";
                }
            }
            output += "\n";
        }
        return output;
    }
}
