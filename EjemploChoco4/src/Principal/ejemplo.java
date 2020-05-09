package Principal;

import java.io.File;
import java.io.IOException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.BoolVar;


class CNF{
	int numVariables;
	int numClauses;
	List<List<Integer>> clauses = new ArrayList<List<Integer>>();
}

public class ejemplo {
	static CNF chocoList = new CNF();
  	 
    public static void main(String[] args) throws IOException, ParseException {
      	// 1. Create a Model
        Model model = new Model("problem");
        //parseStream(args[0]);
        
       // parseStream("CNF/LargeAutomotive_wp.cnf");
        parseStream("CNF/ejemplo2.cnf");
        
        ///////////////
        BoolVar[] variables = new BoolVar[chocoList.numVariables];
        for(int i = 0; i < chocoList.numVariables; i++) {
        	variables[i] = model.boolVar("c" + (i+1));
        }
        
        /////
        for(int i=0; i < chocoList.numClauses;i++) {
        	BoolVar[] used = new BoolVar[chocoList.clauses.get(i).size()];
        	int j=0;
        	for(Integer var: chocoList.clauses.get(i)) {
        		if (var < 0)
        			used[j] = variables[(-1*var)-1].not();
        		else
        			used[j] = variables[var-1];
        		
        		j++;
        	}	
     	
        	model.or(used).post();
        }

   //     System.out.println("Start checking");
        if (model.getSolver().solve())
          	System.out.println("SATISFIABLE");
        else
        	System.out.println("UNSATISFIABLE");        
    }
    
    private static void parseStream(String source) throws IOException, ParseException {                                                                
        File file = new File(source);
        Scanner scanner = new Scanner(file);                                                                

    	  // Skip comments
    	  try {                                                                                                 
    	    String token = scanner.next();                                                                      
    	    while (token.equals("c") || token.equals("%") ) {                                                                         
    	        scanner.nextLine();
    	        token = scanner.next();                                                                           
    	    }
    	    
    	    if (!token.equals("p")) {
    	        throw new ParseException("Excepted 'p', but '" + token + "' was found", 0);                                               
    	    }
    	  } catch (NoSuchElementException e) {
    	        throw new ParseException("Header not found", 0);                                                       
    	  }

    	  // Reads header
    	  try {
    	    String cnf = scanner.next();                                                                        
    	    
    	    if (!cnf.equals("cnf")) {                                                                           
    	        throw new ParseException("Expected 'cnf', but '" + cnf + "' was found", 0);                                               
    	    }
    	    
    	    chocoList.numVariables = scanner.nextInt();                                                                   
    	    chocoList.numClauses = scanner.nextInt();
    	  } catch (NoSuchElementException e) {
    	      throw new ParseException("Incomplete header", 0);                                                      
    	  }
    	  
    	  System.out.print(source + " - " + chocoList.numVariables + " vars " + chocoList.numClauses + " clauses: ");                                             
    	  int numClauses = chocoList.numClauses;
    	
    	  
    	  int i=0;
    	  try {
      	      ArrayList<Integer> currentClauses = new ArrayList<Integer>();
      	      while (numClauses > 0) {
    	          Integer literal = scanner.nextInt();
    	          
    	          if (literal != 0)
    	        	  currentClauses.add(literal);
    	      
    	      if (literal == 0) {
    	        numClauses--;
        	      chocoList.clauses.add(currentClauses);
     //   	      System.out.println(currentClauses);
        	      currentClauses = new ArrayList<Integer>();      
    	      }
    	    }
      	      
    	  } catch (NoSuchElementException e) {
    	    throw new ParseException("Incomplete problem: " + numClauses + " clauses are missing", 0);
    	  }
    }
}