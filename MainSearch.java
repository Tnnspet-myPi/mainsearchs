package mainsearchs;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainSearch
{

    public static void main(String[] args) throws FileNotFoundException, IOException {
    
      File dir = new File("infile.dat");         
  
  File companyName = new File("companies.dat");
    
    System.out.println("Company\t\t\tHit Count\t\tRelevance");
    
      Filesearch search = new Filesearch();
    
   search.searchCompanyName(dir, companyName);

  }

}
