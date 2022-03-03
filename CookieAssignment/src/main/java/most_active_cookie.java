import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.time.*;

public class most_active_cookie {

    private static final Option ARG_DATE = new Option("d","date",true,"Specify date option (yyyy-MM-dd)");

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        pw.println();
        formatter.printUsage(pw, 100, "java -jar CookiesAPP.jar [options] input");
        formatter.printOptions(pw,100, options, 2, 5);
        pw.close();
    }

    private static boolean validateDate(String date){
        try{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate.parse(date, formatter);
            return true;

        } catch(DateTimeException e) {
            return false;
        }
    }

    private static boolean validateFile(String file){
        try {
            Scanner sc = new Scanner(new File("src/main/"+file));
        } catch (Exception e){
            return false;
        }
        return true;

    }

    private static boolean isSameDate(String day1, String day2) throws DateTimeParseException{
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

        LocalDate date1 = LocalDate.parse(day1,formatter1);
        LocalDate date2 = OffsetDateTime.parse(day2,formatter2).toLocalDate();

        return date1.isEqual(date2);
    }

    private static List<String> mostActiveCookie(List<List<String>> list){
        List<String> cookies = new ArrayList<>();

        // Storing occurrences in HashMap
        Map<String, Integer> map = new HashMap<>();
        for(List<String> l: list){
            if(map.containsKey(l.get(0))){
                map.put(l.get(0), map.get(l.get(0)) + 1);
            } else {
                map.put(l.get(0), 1);
            }
        }

        // Checking which elements have the most occurrences by iterating through the HashMap set
        int max = Integer.MIN_VALUE;
        for(Map.Entry<String, Integer> val : map.entrySet()){
            if(val.getValue() > max){
                max = val.getValue();
            }
        }

        // Iterating through map again to add keys to most active cookies
        for(Map.Entry<String, Integer> val : map.entrySet()){
            if(val.getValue() == max){
                cookies.add(val.getKey());
            }
        }
        return cookies;
    }

    public static void main(String[] args) {
        CommandLineParser clp = new DefaultParser();
        Options options = new Options();
        options.addOption(ARG_DATE);
        try {
            CommandLine cl = clp.parse(options, args);
            // Invalid input checks
            if(cl.getArgList().size() < 1) {
                System.out.println("Not enough arguments.");
                printHelp(options);
                throw new Exception();
            }

            String file = cl.getArgList().get(0); // File name

            String line;
            List<List<String>> list = new ArrayList<>();

            // Date option specified
            if(cl.hasOption(ARG_DATE.getLongOpt())) {
                // Validate date input
                String input = cl.getOptionValue(ARG_DATE.getLongOpt());
                if(!validateDate(input)){
                    System.out.println("Invalid date. Format should be (yyyy-MM-dd)");
                    printHelp(options);
                    throw new Exception("Invalid date");
                }

                try{
                    // Validating file
                    if(!validateFile(file)){
                        throw new Exception("Error: File does not exist");
                    }

                    // Reading csv file and writing to list
                    Scanner sc = new Scanner(new File("src/main/"+file));
                    boolean first_line = true;
                    while(sc.hasNext()){
                        // Skipping header
                        if(first_line) {
                            first_line = false;
                            sc.next();
                            continue;
                        }
                        line = sc.next();
                        // Split csv line with regex "," and store into String list
                        List<String> values = Arrays.asList(line.split(","));

                        // Parse timestamp
                        try {
                            // Check if cookie date matches input date
                            if(isSameDate(input,values.get(1))) {
                                list.add(values);
                            }
                        } catch (DateTimeParseException e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    sc.close();

                    // Get most active cookie
                    List<String> activeCookies = mostActiveCookie(list);
                    if(!activeCookies.isEmpty()){
                        for(String s: activeCookies){
                            System.out.println(s);
                        }
                    } else {
                        System.out.println("No active cookies");
                    }


                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                printHelp(options);
            }
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
