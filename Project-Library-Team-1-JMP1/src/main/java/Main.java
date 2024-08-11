import console.ConsoleInteraction;

import java.sql.SQLException;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) throws SQLException {

        Scanner scan = new Scanner(System.in);
        ConsoleInteraction interaction = new ConsoleInteraction();
        interaction.start(scan);

    }
}

