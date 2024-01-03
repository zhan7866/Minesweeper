import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter 1, 2, or 3 to choose a difficulty level: ");
        System.out.println("1. Easy (5x5, 5 mines, 5 flags)");
        System.out.println("2. Medium (9x9, 12 mines, 12 flags)");
        System.out.println("3. Hard (20x20, 40 mines, 40 flags)");

        int choice = scanner.nextInt();

        int rows = 0;
        int columns = 0;
        int mines = 0;

        if (choice == 1) {
            rows = 5;
            columns = 5;
            mines = 5;
        } else if (choice == 2) {
            rows = 9;
            columns = 9;
            mines = 12;
        } else if (choice == 3) {
            rows = 20;
            columns = 20;
            mines = 40;
        } else {
            System.out.println("Invalid choice -- exiting!!!");
            scanner.close();
            return;
        }

        System.out.println("Enable debug mode? (y/n)");
        boolean debugMode = scanner.next().equalsIgnoreCase("y");

        Minefield minefield = new Minefield(rows, columns, mines);
        boolean firstMove = true;

        scanner.nextLine();

        while (!minefield.gameOver()) {
            System.out.println(minefield.toString());

            if (debugMode) {
                System.out.println("Debug mode: ");
                minefield.printMinefield();
            }
            System.out.println("Flags remaining: " + minefield.numFlags);

            System.out.print("Enter your move [row SPACE col] or (row SPACE col SPACE f) to place a flag: \n");
            String inputLine = scanner.nextLine();
            String[] inputs = inputLine.split(" ");
            int row = Integer.parseInt(inputs[0]);
            int col = Integer.parseInt(inputs[1]);

            boolean flag = false;
            if (inputs.length == 3 && inputs[2].equalsIgnoreCase("f")) {
                flag = true;
            }
            // should optimize and bring this out of the loop when time !!!
            if (firstMove && !flag) {
                minefield.revealStart(row, col);
                minefield.guess(row, col, false);
                firstMove = false;
            } else {
                boolean hitMine = minefield.guess(row, col, flag);
                if (hitMine) {
                    System.out.println("You hit a mine! Game over :( ");
                    break;
                }
                if (minefield.numFlags == 0){
                    System.out.println("You placed all the flags -- game is over!!!");
                    break;
                }
            }
        }
        System.out.println("Here's the minefield:");
        minefield.printMinefield();
        scanner.close();
    }
}