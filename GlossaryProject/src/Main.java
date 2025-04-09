import java.util.Comparator;

import components.map.Map;
import components.map.Map1L;
import components.queue.Queue;
import components.queue.Queue1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.Reporter;

/**
 * A simple program to generate a glossary based on an input file containing
 * terms and definitions.
 *
 * @author Caleb Parrott
 *
 */
public final class Main {

    /**
     * No argument constructor--private to prevent instantiation.
     */
    private Main() {
    }

    /**
     * A map with the terms and their definitions.
     */
    private static Map<String, String> glossary = new Map1L<>();

    //---------- TESTING METHODS -----------

    /*
     * Methods for reading and clearing the glossary map due to the map being
     * private. Use for JUnit test cases ONLY. These methods should not be
     * called in main.
     */

    /**
     * Returns the glossary map. Used for testing only. No test cases for this
     * method.
     *
     * @return The glossary map
     * @ensures /result = glossary
     */
    public static Map<String, String> readGlossaryMap() {
        return glossary;
    }

    /**
     * Clears the glossary map. Used for testing only. No test cases for this
     * method.
     *
     * @ensures glossary.size() = 0
     */
    public static void clearGlossaryMap() {
        glossary.clear();
    }

    //---------- END OF TESTING METHODS ----------

    /**
     * Generates the glossary map from the input file. No test cases for this
     * method.
     *
     * @param filename
     *            The file to read the inputs from
     * @requires filename is not null or empty
     * @ensures glossary.size() > 0 and glossary keys are terms and values are
     *          associated definitions
     */
    public static void generateGlossaryMap(String filename) {
        assert filename != null : "Violation of: filename is not null";
        assert !filename.equals("") : "Violation of: filename is not empty";

        SimpleReader in = new SimpleReader1L(filename);
        boolean isAtEOS = false;

        /*
         * Reads the input file unil the end of stream. The first line is the
         * term.
         */
        while (!in.atEOS()) {
            String termLine = in.nextLine();
            StringBuilder defLine = new StringBuilder();

            /*
             * Reads the definition line by line until an empty line is
             * encountered. The empty line indicates the end of the definition.
             */
            String line = in.nextLine();
            while (!line.equals("") && !isAtEOS) {
                defLine.append(line).append(" ");
                if (in.atEOS()) {
                    isAtEOS = true;
                } else {
                    line = in.nextLine();
                }
            }
            /*
             * The definition is trimmed to remove any leading or trailing
             * spaces and converted to a string, and then is added to the
             * glossary map.
             */

            String finalDef = defLine.toString().trim();
            glossary.add(termLine, finalDef);
        }

        /*
         * If glossary map is empty, print an error message and exit the
         * program.
         */
        if (glossary.size() == 0) {
            Reporter.fatalErrorToConsole("EMPTY FILE: No terms found in the input file.");
        }

        //close the input stream
        in.close();
    }

    /**
     * Generates the HTML file for a term.
     *
     * @param term
     *            The term to generate the page for
     * @param definition
     *            The definition of the term
     * @param folderLocation
     *            The location of the folder to save the file in
     * @param consoleOut
     *            The output stream
     * @updates consoleOut.contents
     * @requires term is not null or empty, definition is not null or empty,
     *           folderLocation is not null or empty, and ConsoleOut.is_open.
     * @ensures output file is created in the specified folder location with the
     *          associated term and definition
     */
    public static void generateTermPage(String term, String definition,
            String folderLocation, SimpleWriter consoleOut) {

        assert term != null : "Violation of: term is not null";
        assert !term.equals("") : "Violation of: term is not empty";
        assert definition != null : "Violation of: definition is not null";
        assert !definition.equals("") : "Violation of: definition is not empty";
        assert folderLocation != null : "Violation of: folderLocation is not null";
        assert !folderLocation.equals("") : "Violation of: folderLocation is not empty";

        String filename = term + ".html";
        SimpleWriter out = new SimpleWriter1L(folderLocation + "/" + filename);

        consoleOut.print("Generating " + filename);

        /*
         * Prints the header for the term page. The header includes the title
         * and a return button to the index page.
         */
        out.println("<html>");
        out.println("<head>");
        out.println("<title>" + term + "</title>");

        /*
         * Writes the CSS for the HTML file.
         */
        writeCSS(out);

        /*
         * Prints the header of the term page.
         */
        out.println("</head>");
        out.println("<body>");
        out.println("<div>");
        out.println("<a href=\"index.html\" class=\"button\">Return to Index</a>");
        out.println("<h1 style=\"color: red; font-weight: bold; font-style: italic;\">"
                + term + "</h1>");
        out.println("</div>");

        /*
         * Prints the definition of the term. The definition includes any links
         * if necessary.
         */
        out.println("<div style=\"margin-top: 20px;\">");
        out.println("<p>" + checkDefinitionLinks(definition) + "</p>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
        out.close();
        consoleOut.println("...done.");

    }

    /**
     * A helper method to check if the definition contains words that are also
     * terms in the glossary. Replaces those with link to the respective
     * definition.
     *
     * @param definition
     *            The definition to check
     * @return The definition with links where necessary
     * @requires definition is not null or empty
     * @ensures definition word that match glossary terms are replaced with
     *          links
     */
    public static String checkDefinitionLinks(String definition) {

        assert definition != null : "Violation of: definition is not null";
        assert !definition.equals("") : "Violation of: definition is not empty";

        StringBuilder definitionToPrint = new StringBuilder();

        /*
         * Splits the definition into words and iterates over each word. Removes
         * all punctuation and makes the word lowercase for the case of
         * searching the glossary map.
         */
        String[] words = definition.split(" ");
        for (String word : words) {
            String wordWithoutPunctuation = word.replaceAll("\\p{Punct}", "")
                    .toLowerCase();

            /*
             * Cheks if the word is in the glossary map. If the first part of
             * the word exists as a key in the map, that term is linked in the
             * definition.
             */
            boolean isFound = false;
            for (Map.Pair<String, String> pair : glossary) {
                String key = pair.key().toLowerCase();
                if (wordWithoutPunctuation.startsWith(key) && !isFound) {
                    definitionToPrint.append("<a href=\"").append(pair.key())
                            .append(".html\">").append(word).append("</a> ");
                    isFound = true;
                }
            }
            // If the word is not in the glossary, just append it
            if (!isFound) {
                definitionToPrint.append(word).append(" ");
            }
        }
        //return definition with links, removing any trailing spaces
        return definitionToPrint.toString().trim();
    }

    /**
     * Generates the index page for the glossary.
     *
     * @param folderLocation
     *            The location of the folder to save the file in
     * @requires folderLocation is not null or empty
     * @ensures index.html is created in the specified folder
     */
    public static void generateIndexPage(String folderLocation) {
        SimpleWriter out = new SimpleWriter1L(folderLocation + "/index.html");

        assert folderLocation != null : "Violation of: folderLocation is not null";
        assert !folderLocation.equals("") : "Violation of: folderLocation is not empty";

        /*
         * Prints the header for the index page. The header includes the title.
         */
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Glossary</title>");

        /*
         * Writes the CSS for the HTML file.
         */
        writeCSS(out);

        /*
         * Prints the header of the index page.
         */
        out.println("</head>");
        out.println("<body>");
        out.println("<h1><b>Glossary Index</b></h1>");
        out.println("<div>");
        out.println("<ul>");
        out.println("<!-- List of terms -->");

        /*
         * Adds all the terms to a queue for sorting.
         */
        Queue<String> orderedTerms = new Queue1L<String>();
        for (Map.Pair<String, String> pair : glossary) {
            orderedTerms.enqueue(pair.key());
        }

        /*
         * Sorts the queue of terms in alphabetical order.
         */
        orderedTerms.sort(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });

        // Iterate over the queue and print the terms
        while (orderedTerms.length() != 0) {
            String term = orderedTerms.dequeue();
            out.println("<a href=\"" + term + ".html\"><li>" + term + "</li></a>");
        }

        /*
         * Prints the end of the HTML file.
         */
        out.println("</ul>");
        out.println("</div>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    /**
     * Writes the CSS for the HTML files.
     *
     * @param out
     *            The output stream
     * @updates out.contents
     * @requires out.is_open
     * @ensures out.contents = #out.contents * [CSS script]
     */
    public static void writeCSS(SimpleWriter out) {

        /*
         * "All the CSS for the HTML files is written here. The CSS is all
         * grouped in one method for the sake of consitency across pages.
         */
        out.println("<style>");
        out.println("body {");
        out.println("    background-color: #f0f0f0;");
        out.println("    font-family: Arial, sans-serif;");
        out.println("    padding: 20px;");
        out.println("}");
        out.println("h1 {");
        out.println("    color: #333;");
        out.println("    text-align: center;");
        out.println("    font-size: 2.5em;");
        out.println("    margin-bottom: 20px;");
        out.println("}");
        out.println("div {");
        out.println("    border: 2px solid #007BFF;");
        out.println("    border-radius: 10px;");
        out.println("    padding: 20px;");
        out.println("    background-color: #fff;");
        out.println("    max-width: 800px;");
        out.println("    margin: 20px auto;");
        out.println("    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);");
        out.println("}");
        out.println("a {");
        out.println("    text-decoration: none;");
        out.println("    color: #007BFF;");
        out.println("    font-weight: bold;");
        out.println("}");
        out.println("a:hover {");
        out.println("    text-decoration: underline;");
        out.println("}");
        out.println("ul {");
        out.println("    list-style-type: none;");
        out.println("    padding: 0;");
        out.println("}");
        out.println("li {");
        out.println("    margin-bottom: 10px;");
        out.println("}");
        out.println("ul {");
        out.println("    list-style: none;");
        out.println("    padding: 0;");
        out.println("    display: grid;");
        out.println("    grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));");
        out.println("    gap: 10px;");
        out.println("}");
        out.println("li {");
        out.println("    background-color: #e8f0fe;");
        out.println("    border-radius: 8px;");
        out.println("    text-align: center;");
        out.println("    padding: 10px;");
        out.println("    transition: transform 0.2s;");
        out.println("}");
        out.println("li:hover {");
        out.println("    transform: scale(1.05);");
        out.println("    background-color: #d1e7ff;");
        out.println("}");
        out.println(".button {");
        out.println("    display: inline-block;");
        out.println("    padding: 10px 20px;");
        out.println("    font-size: 1em;");
        out.println("    color: #fff;");
        out.println("    background-color: #007BFF;");
        out.println("    border: none;");
        out.println("    border-radius: 5px;");
        out.println("    text-align: center;");
        out.println("    text-decoration: none;");
        out.println("    transition: transform 0.2s;");
        out.println("}");
        out.println(".button:hover {");
        out.println("    transform: scale(1.05);");
        out.println("    background-color: #0056b3;");
        out.println("}");
        out.println("* {");
        out.println("    transition: all 0.2s ease-in-out;");
        out.println("}");
        out.println("</style>");

    }

    /**
     * Generates the glossary files.
     *
     * @param folderLocation
     *            The location of the folder to save the files in
     * @param consoleOut
     *            The output stream
     * @requires folderLocation is not null or empty, and consoleOut.is_open
     * @updates consoleOut.contents
     * @ensures index.html and term files are created in the specified folder
     */
    public static void generateGlossaryFiles(String folderLocation,
            SimpleWriter consoleOut) {

        assert folderLocation != null : "Violation of: folderLocation is not null";
        assert !folderLocation.equals("") : "Violation of: folderLocation is not empty";

        consoleOut.print("Generating index.html...");

        /*
         * Generates the index page. The index page is the main page of the
         * glossary and contains links to all the terms.
         */
        generateIndexPage(folderLocation);

        consoleOut.println("done.");

        consoleOut.println("Generating glossary files...");

        /*
         * Iterates over the glossary map and generates a file for each term.
         */
        for (Map.Pair<String, String> pair : glossary) {
            String term = pair.key();
            String definition = pair.value();
            generateTermPage(term, definition, folderLocation, consoleOut);
        }

        consoleOut.println("All files written.");

    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments
     */
    public static void main(String[] args) {
        SimpleReader in = new SimpleReader1L();
        SimpleWriter out = new SimpleWriter1L();

        /*
         * Prompt the user for the input file and the folder location to save
         * the files in.
         */
        out.print("Enter the name of the input file: ");
        String inputFile = in.nextLine();

        out.print("Enter the location of the folder to save the files in: ");
        String folderLocation = in.nextLine();

        out.println("Generating files...");

        /*
         * Generates the glossary map for the terms and definitions. The
         * definition is the key and the term is the value. Using that glosssry
         * map the HTML pages are written.
         */
        generateGlossaryMap(inputFile);
        generateGlossaryFiles(folderLocation, out);

        out.println("Now quitting...");

        /*
         * Close input and output streams
         */
        in.close();
        out.close();
    }

}
