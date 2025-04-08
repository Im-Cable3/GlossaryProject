import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;

import org.junit.Test;

import components.map.Map;
import components.map.Map1L;
import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;
import components.utilities.Reporter;

/**
 * JUnit test for glossary.
 */
public class GlossaryTest {

    //---------------generateGlossaryMap Tests------------------//

    /**
     * Test for generateGlossaryMap using the provided sample input.
     */
    @Test
    public void generateGlossaryMap1() {

        //generates the exprected map
        Map<String, String> expectedMap = new Map1L<>();
        expectedMap.add("meaning",
                "something that one wishes to convey, especially by language");
        expectedMap.add("term", "a word whose definition is in a glossary");
        expectedMap.add("word",
                "a string of characters in a language, which has at least one character");
        expectedMap.add("definition", "a sequence of words that gives meaning to a term");
        expectedMap.add("glossary",
                "a list of difficult or specialized terms, with their definitions, "
                        + "usually near the end of a book");
        expectedMap.add("language",
                "a set of strings of characters, each of which has meaning");
        expectedMap.add("book", "a printed or written literary work");

        Main.generateGlossaryMap("test/testInput/generateGlossaryMap1.txt");
        assertEquals(expectedMap, Main.readGlossaryMap());
        Main.clearGlossaryMap();

    }

    /**
     * Test for generateGlossaryMap using a file with single line definitions.
     */
    @Test
    public void generateGlossaryMap2() {

        //generates the exprected map
        Map<String, String> expectedMap = new Map1L<>();
        expectedMap.add("Word1", "This is a single line definition for word 1");
        expectedMap.add("Word2", "This is a single line definition for word 2");
        expectedMap.add("Word3", "This is a single line definition for word 3");
        expectedMap.add("Word4", "This is a single line definition for word 4");
        expectedMap.add("Word5", "This is a single line definition for word 5");
        expectedMap.add("Word6", "This is a single line definition for word 6");

        Main.generateGlossaryMap("test/testInput/generateGlossaryMap2.txt");
        assertEquals(expectedMap, Main.readGlossaryMap());
        Main.clearGlossaryMap();

    }

    /**
     * Test for generateGlossaryMap using a file with multi-line definitions.
     */
    @Test
    public void generateGlossaryMap3() {

        //generates the exprected map
        Map<String, String> expectedMap = new Map1L<>();

        expectedMap.add("Word1",
                "This is a multi-line definition for word 1. This is a second line!!!");
        expectedMap.add("Word2",
                "This is a multi-line definition for word 2. This is a second line!!!");
        expectedMap.add("Word3",
                "This is a multi-line definition for word 3. This is a second line!!!");
        expectedMap.add("Word4",
                "This is a multi-line definition for word 4. This is a second line!!!");
        expectedMap.add("Word5",
                "This is a multi-line definition for word 5. This is a second line!!!");
        expectedMap.add("Word6",
                "This is a multi-line definition for word 6. This is a second line!!!");

        Main.generateGlossaryMap("test/testInput/generateGlossaryMap3.txt");
        assertEquals(expectedMap, Main.readGlossaryMap());
        Main.clearGlossaryMap();

    }

    //---------------generateTermPage Tests------------------//

    /**
     * Test for generateTermPage using the provided sample input and the word
     * "term".
     */
    @Test
    public void generateTermPage1() {

        SimpleWriter out = new SimpleWriter1L();

        SimpleReader inExpected = new SimpleReader1L(
                "test/testOutputExpected/generateTermPage1/term.html");

        Main.generateGlossaryMap("test/testInput/generateTermPage1.txt");

        String inputTerm = "term";
        String inputDefinition = "a word whose definition is in a glossary";
        String folderLocation = "test/testOutput/generateTermPage1";

        Main.generateTermPage(inputTerm, inputDefinition, folderLocation, out);

        SimpleReader inActual = new SimpleReader1L(
                "test/testOutput/generateTermPage1/term.html");

        boolean isSame = true;

        while (!inActual.atEOS()) {
            String expectedLine = inExpected.nextLine();
            String actualLine = inActual.nextLine();
            if (!expectedLine.equals(actualLine)) {
                isSame = false;
            }
        }

        assertTrue(isSame);

        Main.clearGlossaryMap();
        inExpected.close();
        inActual.close();
        out.close();

    }

    /**
     * Test for generateTermPage using an modified sample input and a term that
     * contains no other terms in its definition.
     */
    @Test
    public void generateTermPage2() {

        SimpleWriter out = new SimpleWriter1L();

        SimpleReader inExpected = new SimpleReader1L(
                "test/testOutputExpected/generateTermPage2/testTerm.html");

        Main.generateGlossaryMap("test/testInput/generateTermPage2.txt");

        String inputTerm = "testTerm";
        String inputDefinition = "A bunch of random text that doesnt contain "
                + "any values from the list of groups of letters";
        String folderLocation = "test/testOutput/generateTermPage2";

        Main.generateTermPage(inputTerm, inputDefinition, folderLocation, out);

        SimpleReader inActual = new SimpleReader1L(
                "test/testOutput/generateTermPage2/testTerm.html");

        boolean isSame = true;

        while (!inActual.atEOS()) {
            String expectedLine = inExpected.nextLine();
            String actualLine = inActual.nextLine();
            if (!expectedLine.equals(actualLine)) {
                isSame = false;
            }
        }

        assertTrue(isSame);

        Main.clearGlossaryMap();
        inExpected.close();
        inActual.close();
        out.close();

    }

    /**
     * Test for generateTermPage using an modified sample input and a term that
     * contains only other terms in its definition.
     */
    @Test
    public void generateTermPage3() {

        SimpleWriter out = new SimpleWriter1L();

        SimpleReader inExpected = new SimpleReader1L(
                "test/testOutputExpected/generateTermPage3/testTerm.html");

        Main.generateGlossaryMap("test/testInput/generateTermPage3.txt");

        String inputTerm = "testTerm";
        String inputDefinition = "Meaning term word definition glossary language "
                + "book term glossary book book term word definition";
        String folderLocation = "test/testOutput/generateTermPage3";

        Main.generateTermPage(inputTerm, inputDefinition, folderLocation, out);

        SimpleReader inActual = new SimpleReader1L(
                "test/testOutput/generateTermPage3/testTerm.html");

        boolean isSame = true;

        while (!inActual.atEOS()) {
            String expectedLine = inExpected.nextLine();
            String actualLine = inActual.nextLine();
            if (!expectedLine.equals(actualLine)) {
                isSame = false;
            }
        }

        assertTrue(isSame);

        Main.clearGlossaryMap();
        inExpected.close();
        inActual.close();
        out.close();

    }

    //---------------checkDefinitionLinks Tests------------------//

    /**
     * Test for checkDefinitionLinks using the provided sample input and the
     * word "term".
     */
    @Test
    public void checkDefinitionLinks1() {

        Main.generateGlossaryMap("test/testInput/checkDefinitionLinks1.txt");

        String definition = "a word whose definition is in a glossary";
        String expected = "a <a href=\"word.html\">word</a> whose <a href=\"def"
                + "inition.html\">definition</a> is in a <a href=\"glossary.ht"
                + "ml\">glossary</a>";

        String actual = Main.checkDefinitionLinks(definition);

        assertEquals(expected, actual);

        Main.clearGlossaryMap();

    }

    /**
     * Test for checkDefinitionLinks using a modified sample input and a
     * definition with no links.
     */
    @Test
    public void checkDefinitionLinks2() {

        Main.generateGlossaryMap("test/testInput/checkDefinitionLinks2.txt");

        String definition = "A bunch of random text that doesnt contain any values "
                + "from the list of groups of letters";
        String expected = "A bunch of random text that doesnt contain any values "
                + "from the list of groups of letters";

        String actual = Main.checkDefinitionLinks(definition);

        assertEquals(expected, actual);

        Main.clearGlossaryMap();

    }

    /**
     * Test for checkDefinitionLinks using a modified sample input and a
     * definition with only links.
     */
    @Test
    public void checkDefinitionLinks3() {

        Main.generateGlossaryMap("test/testInput/checkDefinitionLinks3.txt");

        String definition = "Meaning term word definition glossary language book "
                + "term glossary book book term word definition";
        String expected = "<a href=\"meaning.html\">Meaning</a> <a href=\"ter"
                + "m.html\">term</a> <a href=\"word.html\">word</a> <a hr"
                + "ef=\"definition.html\">definition</a> <a href=\"glossary.ht"
                + "ml\">glossary</a> <a href=\"language.html\">language</a> <a"
                + " href=\"book.html\">book</a> <a href=\"term.html\">term</a>"
                + " <a href=\"glossary.html\">glossary</a> <a href=\"book.ht"
                + "ml\">book</a> <a href=\"book.html\">book</a> <a href=\"te"
                + "rm.html\">term</a> <a href=\"word.html\">word</a> <a hr"
                + "ef=\"definition.html\">definition</a>";

        String actual = Main.checkDefinitionLinks(definition);

        assertEquals(expected, actual);

        Main.clearGlossaryMap();

    }

    //---------------generateIndexPage Tests------------------//

    /**
     * Test for generateIndexPage using the provided sample input.
     */
    @Test
    public void generateIndexPage1() {

        Main.generateGlossaryMap("test/testInput/generateIndexPage1.txt");

        String folderLocation = "test/testOutput/generateIndexPage1";

        Main.generateIndexPage(folderLocation);

        SimpleReader inExpected = new SimpleReader1L(
                "test/testOutputExpected/generateIndexPage1/index.html");
        SimpleReader inActual = new SimpleReader1L(
                "test/testOutput/generateIndexPage1/index.html");

        boolean isSame = true;

        while (!inActual.atEOS()) {
            String expectedLine = inExpected.nextLine();
            String actualLine = inActual.nextLine();
            if (!expectedLine.equals(actualLine)) {
                isSame = false;
            }
        }

        assertTrue(isSame);

        Main.clearGlossaryMap();
        inExpected.close();
        inActual.close();
    }

    /**
     * Test for generateIndexPage using an input with only one term.
     */
    @Test
    public void generateIndexPage2() {

        Main.generateGlossaryMap("test/testInput/generateIndexPage2.txt");

        String folderLocation = "test/testOutput/generateIndexPage2";

        Main.generateIndexPage(folderLocation);

        SimpleReader inExpected = new SimpleReader1L(
                "test/testOutputExpected/generateIndexPage2/index.html");
        SimpleReader inActual = new SimpleReader1L(
                "test/testOutput/generateIndexPage2/index.html");

        boolean isSame = true;

        while (!inActual.atEOS()) {
            String expectedLine = inExpected.nextLine();
            String actualLine = inActual.nextLine();
            if (!expectedLine.equals(actualLine)) {
                isSame = false;
            }
        }

        assertTrue(isSame);

        Main.clearGlossaryMap();
        inExpected.close();
        inActual.close();
    }

    /**
     * Test for generateIndexPage using an input with all similar terms.
     */
    @Test
    public void generateIndexPage3() {

        Main.generateGlossaryMap("test/testInput/generateIndexPage3.txt");

        String folderLocation = "test/testOutput/generateIndexPage3";

        Main.generateIndexPage(folderLocation);

        SimpleReader inExpected = new SimpleReader1L(
                "test/testOutputExpected/generateIndexPage3/index.html");
        SimpleReader inActual = new SimpleReader1L(
                "test/testOutput/generateIndexPage3/index.html");

        boolean isSame = true;

        while (!inActual.atEOS()) {
            String expectedLine = inExpected.nextLine();
            String actualLine = inActual.nextLine();
            if (!expectedLine.equals(actualLine)) {
                isSame = false;
            }
        }

        assertTrue(isSame);

        Main.clearGlossaryMap();
        inExpected.close();
        inActual.close();
    }

    //---------------writeCSS Test------------------//

    /**
     * Tests writeCSS.
     */
    @Test
    public void writeCSSTest() {
        SimpleWriter out = new SimpleWriter1L(
                "test/testOutput/writeCSSTest/testFile.html");

        Main.writeCSS(out);

        SimpleReader inExpected = new SimpleReader1L(
                "test/testOutputExpected/writeCSSTest/testFile.html");
        SimpleReader inActual = new SimpleReader1L(
                "test/testOutput/writeCSSTest/testFile.html");

        boolean isSame = true;
        while (!inActual.atEOS()) {
            String expectedLine = inExpected.nextLine();
            String actualLine = inActual.nextLine();
            if (!expectedLine.equals(actualLine)) {
                isSame = false;
            }
        }

        assertTrue(isSame);

        Main.clearGlossaryMap();
        inExpected.close();
        inActual.close();
        out.close();
    }

    //---------------generateGlossaryFiles Tests------------------//

    /**
     * Test for generateGlossaryFiles using the provided sample input. Since the
     * indivudal methods have been tested already, this test only checks for a
     * matching file structure to ensure all files have been generated, but does
     * not check the individual files.
     */
    @Test
    public void generateGlossaryFiles1() {

        SimpleWriter out = new SimpleWriter1L();

        Main.generateGlossaryMap("test/testInput/generateGlossaryFiles1.txt");

        String folderLocation = "test/testOutput/generateGlossaryFiles1";

        Main.generateGlossaryFiles(folderLocation, out);

        String[] expectedFiles = { "book.html", "definition.html", "glossary.html",
                "index.html", "language.html", "meaning.html", "term.html", "word.html" };

        String[] actualFiles = new File(folderLocation).list();

        //to avoid spotbugs warning
        if (actualFiles == null) {
            Reporter.fatalErrorToConsole(
                    "ERROR: Null file list in generateGlossaryFiles1");
        }

        Arrays.sort(expectedFiles);
        Arrays.sort(actualFiles);

        assertTrue(Arrays.equals(expectedFiles, actualFiles));
        Main.clearGlossaryMap();
        out.close();

    }

    /**
     * Test for generateGlossaryFiles using an input with only one word. Since
     * the indivudal methods have been tested already, this test only checks for
     * a matching file structure to ensure all files have been generated, but
     * does not check the individual files.
     */
    @Test
    public void generateGlossaryFiles2() {

        SimpleWriter out = new SimpleWriter1L();

        Main.generateGlossaryMap("test/testInput/generateGlossaryFiles2.txt");

        String folderLocation = "test/testOutput/generateGlossaryFiles2";

        Main.generateGlossaryFiles(folderLocation, out);

        String[] expectedFiles = { "index.html", "Word.html" };

        String[] actualFiles = new File(folderLocation).list();

        //to avoid spotBugs warning
        if (actualFiles == null) {
            Reporter.fatalErrorToConsole(
                    "ERROR: Null file list in generateGlossaryFiles2");
        }

        Arrays.sort(expectedFiles);
        Arrays.sort(actualFiles);

        assertTrue(Arrays.equals(expectedFiles, actualFiles));
        Main.clearGlossaryMap();
        out.close();

    }
}
