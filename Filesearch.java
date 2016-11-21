package mainsearchs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;

public class Filesearch {
  /*---------------------------------------------------------*/
  /* Function Name: searchCompanyName */
  /*                                                         */
  /* Description: This is the main function in the class */
  /* that goes through each of the functions */
  /* needed to create the codes. */
  /*                                                         */
  /*---------------------------------------------------------*/

  public void searchCompanyName(File inputFile, File companyFile) throws FileNotFoundException, IOException {

    // format variable
    NumberFormat formatter = new DecimalFormat("#0.000000");
    String format1 = "%-20s %7d %28s\n";
    String format2 = "%-20s %20d\n";

    // local variable
    double totalHit = 0.00;
    double totalRelevance = 0.00;

    // Create an arraylist of arraylist to hold company name and synonyms.
    ArrayList<ArrayList<String>> companyList = processCompanyName(companyFile);

    // Create an arraylist to store article
    ArrayList<String> article = processFile(inputFile);

    // Total words of article without counting "a,an,the,and,or,but"
    double total = article.size();

    // SearchMap match each company name with the hits in the article.
    Map<String, Integer> searchMap = new HashMap<>();

    for (int i = 0; i < companyList.size(); i++) {
      searchMap.put(companyList.get(i).get(0), wordFrequency(companyList.get(i), article));
    }

    // Output for each company by primary name, hit and frequency.
    for (Entry<String, Integer> entry : searchMap.entrySet()) {

      System.out.printf(format1, entry.getKey(), entry.getValue(),
          formatter.format(entry.getValue() * 100 / total) + "%");
      totalHit += entry.getValue();
      totalRelevance += entry.getValue() / total;
    }
    System.out.printf(format1, "Total", (int) totalHit, formatter.format(totalRelevance * 100) + "%");
    System.out.printf(format2, "Total Words", (int) total);
  }

  /*---------------------------------------------------------*/
  /* Function Name: processCompanyName */
  /*                                                         */
  /* Description: This is the function that use scanner to */
  /* store each company's name into a arraylist of arraylist */
  /*---------------------------------------------------------*/
  public ArrayList<ArrayList<String>> processCompanyName(File companyFile) throws FileNotFoundException {

    // Create arraylist of arraylist, the inner arraylist to store
    // primary name and synonyms for each company, and the outter
    // arraylist to hold each company.
    ArrayList<ArrayList<String>> companyList = new ArrayList<>();

    // scanner a line
    Scanner scanner = new Scanner(companyFile);

    while (scanner.hasNextLine()) {

      String companyName = scanner.nextLine();
      String[] split = companyName.split("\t");

      // create a new list for each line scanned
      ArrayList<String> list = new ArrayList<>();

      for (String s : split) {
        list.add(s);
      }
      companyList.add(list);
    }

    scanner.close();
    return companyList;
  }

  /*--------------------------------------------------------- */
  /* Function Name: processFile */
  /*                                                          */
  /* Description: This is the function that use scanner to */
  /* store each words into a arraylist but ignoring */
  /* "a,an,the,and,or,but" */
  /* -------------------------------------------------------- */
  public ArrayList<String> processFile(File inputFile) throws FileNotFoundException {

    ArrayList<String> inputList = new ArrayList<>();

    Scanner scanner = new Scanner(inputFile);

    while (scanner.hasNextLine()) {

      String sentence = scanner.nextLine();
      Scanner wordScanner = new Scanner(sentence);

      while (wordScanner.hasNext()) {
        String word = wordScanner.next();
        
        // if a word start with ' or ", get substring of word
        if (word.charAt(0) == '\''||word.charAt(0) == '\"') {
          word = word.substring(1, word.length());
        }
        
        //if  a word company name is Google's, will eliminate 's
        if (word.length()>2 && word.substring(word.length() - 2).equals("’s")) {
          word = word.substring(0, word.length() - 2);
        }
        //if  a word company name is Google's, will eliminate 's
        if (word.length()>2 && word.substring(word.length() - 2).equals("'s")) {
          word = word.substring(0, word.length() - 2);
        }
        // if a name word contain "", will eliminate it.
        if (word.charAt(word.length() - 1) == '\"' || word.charAt(word.length() - 1) == '.'
            || word.charAt(word.length() - 1) == ',') {
          word = word.substring(0, word.length() - 1);
        }
        // eliminate words that not meet requirement
        if (!word.equals("a") && !word.equals("an") && !word.equals("the") && !word.equals("and")
            && !word.equals("or") && !word.equals("but") && !word.equals("s") && !word.equals("But")
            && !word.equals("And") && !word.equals("The") && !word.equals("An") && !word.equals("A")
            && !word.equals("Or")) {
          inputList.add(word);
        }
      }
      wordScanner.close();
    }

    scanner.close();

    return inputList;
  }

  /*---------------------------------------------------------*/
  /* Function Name: wordFrequency */
  /*                                                         */
  /* Description: This is the function that count frequency */
  /* for each name of company, and then sum up primary name */
  /* count and synonyms count as the frequency, then return */
  /* the value. */
  /* -------------------------------------------------------- */
  public int wordFrequency(ArrayList<String> companyName, ArrayList<String> inputFile) {
    Map<String, Integer> map = new HashMap<>();
    int freq = 0;
    int sum = 0;

    // put each name of a company into map
    for (String w : companyName) {
      map.put(w, freq);
    }

    // match each name of a company and update the count of each name
    for (String w : inputFile) {
      if (map.containsKey(w)) {
        freq = map.get(w);
        map.put(w, ++freq);
      }
    }

    // Sum up frequency for each company
    for (Entry<String, Integer> entry : map.entrySet()) {
      sum += entry.getValue();
    }
    return sum;
  }
}
