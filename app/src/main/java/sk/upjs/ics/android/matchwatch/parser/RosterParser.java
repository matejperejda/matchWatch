package sk.upjs.ics.android.matchwatch.parser;

import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import sk.upjs.ics.android.matchwatch.entity.Person;
import sk.upjs.ics.android.matchwatch.entity.Player;

// number;lastName;firstName;position;shoots;height;weight;birthDate{DAY, MONTH, YEAR};club
public class RosterParser {

    public Scanner scanner;

    private List<Player> players;

    // getters & setters
    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    // constructor
    public RosterParser() {
        players = new ArrayList<>();
    }

    // methods
    public void loadTeamRosterFromFile(String source) {
        int id = 0;

        try {
            scanner = new Scanner(new File(source));
            scanner.useLocale(Locale.ENGLISH);
            scanner.useDelimiter(";|\\n");

            while (scanner.hasNextLine()) {
                /*String line = scanner.nextLine();
                Scanner scannerLine = new Scanner(line);
                scannerLine.useLocale(Locale.ENGLISH);
                scannerLine.useDelimiter(";|\\n");*/

                // ak nacitany riadok, nie je prazdny riadok
                //if (!(line.equals(""))) {
                //while (scannerLine.hasNextLine()) {
                int number = scanner.nextInt();
                String lastName = scanner.next();
                String firstName = scanner.next();
                String position = scanner.next();
                char shoots = scanner.next().charAt(0);
                double height = scanner.nextFloat();
                int weight = scanner.nextInt();
                String[] birthDate = new String[]{scanner.next(), scanner.next(), scanner.next()};
                String club = scanner.next();

                // vytvorenie noveho hraca a pridanie ho do zoznamu hracov
                Player player = new Player(firstName, lastName, birthDate, position, number, shoots, height, weight, club);
                players.add(player);

                // }
                /*} else {
                    Log.i("nacitavame personal", "");

                }*/
                //scannerLine.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("FileNotFoundException", "File: " + source + "was not found!");
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Player player : players) {
            stringBuilder.append("[" + player.toString() + "]; ");
        }
        return stringBuilder.toString();
    }
}
