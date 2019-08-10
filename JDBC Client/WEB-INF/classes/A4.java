import java.sql.*;
import java.lang.*;
import java.io.PrintWriter;
import java.awt.BorderLayout;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.UnavailableException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/* Name: Aaron Fye       
 * Course: CNT 4714 – Fall 2018 – Project Four     
 * Assignment title:  A Three-Tier Distributed Web-Based Application     
 * Date:  November 30, 2018 
 * Adapted from SurveyServlet.java
 * */
public class A4 extends HttpServlet{  
	
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	int col;
	private ResultSetMetaData rsmd;
    int fired = 0;

	
	
	public void init( ServletConfig config ) throws ServletException
		   {
		      // attempt database connection and create Statement
		      try 
		      {
		         Class.forName("com.mysql.jdbc.Driver");
				 connection = DriverManager.getConnection("jdbc:mysql://localhost:3312/Project4", "root", "root" );
		         statement = connection.createStatement();
		      } // end try
		      // for any exception throw an UnavailableException to 
		      // indicate that the servlet is not currently available
		      catch ( Exception exception ) 
		      {
		         exception.printStackTrace();
		         throw new UnavailableException( exception.getMessage() );
		      } // end catch
		   }  // end method init 

		   // process survey response
	protected void doPost( HttpServletRequest request,  HttpServletResponse response )
	        throws ServletException, IOException 
	  {
	     String clientName = request.getParameter( "statement" );
	     try {
	    	if(clientName.substring(0, 2).toLowerCase().equals("se")) {
	    		resultSet = statement.executeQuery(clientName);
	    		rsmd = resultSet.getMetaData();
		        col = rsmd.getColumnCount();
	    	}
	    	else {
	    		System.out.println("test");
	    		fired = 1; 
	    		statement.executeUpdate(clientName);
	    	}
	        
	     } catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     
	     response.setContentType( "text/html" );
	     PrintWriter out = response.getWriter();
	     // send HTML page to client
	     // start HTML document
	     out.println( "<!DOCTYPE html>" ); 
	     out.println( "<html lang='en'>" );
	     out.println( "<head>\r\n" + 
	     		"    <title>A4</title>\r\n" + 
	     		"    <meta charset=\"utf-8\" />\r\n" + 
	     		"    <style type=\"text/css\">\r\n" + 
	     		"        body{background-color: grey; color:black;}\r\n" + 
	     		"    </style>\r\n" + 
	     		"</head>\r\n");

	     out.println( "<body>\r\n" + 
	     		"    <center>\r\n" + 
	     		"    <h1>Welcome to the Fall 2018 Project4 Enterprise System</h1>\r\n" + 
	     		"    <h1>A Remote Database Management System</h1>\r\n" + 
	     		"    <hr>\r\n" + 
	     		"    <p>You are connected to the Project4 Enterprise System database.</p>\r\n" + 
	     		"    <p>Please ener any valid SQL query or update statement.</p>\r\n" + 
	     		"    <p>If no query/update command is provided, the Execute button will display all supplier information in the database.</p>\r\n" + 
	     		"    <p>All execution results will appear below.</p>\r\n" + 
	     		"    <br>\r\n" + 
	     		"    <form action = \"/Project4/A4\" method = \"post\">\r\n" + 
	     		"        <textarea id=\"statement\" class=\"text\" cols=\"50\" rows =\"15\" name=\"statement\"></textarea>\r\n" + 
	     		"        <p>\r\n" + 
	     		"            <input type = \"submit\" value = \"Execute Command\" />\r\n" + 
	     		"            <input type = \"reset\" value = \"Clear Form\" />\r\n" + 
	     		"        </p>\r\n" + 
	     		"    </form>\r\n" + 
	     		"    <hr>    \r\n" + 
	     		"    <h4>Database Results:</h4>\r\n");
	     if(clientName.substring(0, 2).toLowerCase().equals("se")) {
	     		try {
	     			out.println("<table style=\"width:100%; border: 1px solid black;\">");
	     			out.println("<tr style=\"border: 1px solid black;\">");
					for(int i=1; i<=col; i++) {
						out.println("<th style=\"border: 1px solid black; \">" + rsmd.getColumnName(i) + "</th>");
					}
					out.println("</tr>");
					while(resultSet.next()) {
						out.println("<tr style=\"border: 1px solid black;\">");
						for(int j=1; j<=col; j++) {
							out.println("<th style=\"border: 1px solid black; background-color:white;\">" + resultSet.getString(j) + "</th>");
						}
						out.println("</tr>");
					}
					out.println("</table>");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	     }
	     else {
	    	 if(fired == 1) {
	    		 out.println("<div style=\"width:500px; height:100px; border:5px solid green; background-color: grey;\"> The statement executed successfully.");
	    		 try {
	    			 out.println("<br>"+statement.getUpdateCount()+" row(s) changed");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	    		 if(clientName.substring(0,9).equals("update sh")||
	    				clientName.substring(0,14).equals("insert into sh")||
	    					clientName.substring(0,15).equals("replace into sh")) {
	    			 try {
						statement.executeUpdate("update suppliers set status = status + 5 where snum in (select snum from shipments where quantity > 100)");
		    			 out.println("<br> BUSINESS LOGIC DETECTED <br>"+statement.getUpdateCount()+" row(s) changed");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    			out.println();
		    	 }

	    	 }
	    	 else {
	    		 out.println("it did not update.");
	    	 }
	     }
	     		out.println("</center>\r\n </body> </html>" );
	     out.close();  // close stream to complete the page
	  }//end doGet() method

		   // close SQL statements and database when servlet terminates
		   public void destroy()
		   {
		      // attempt to close statements and database connection
		      try 
		      {
		         statement.close();
		         connection.close();
		      } // end try
		      // handle database exceptions by returning error to client
		      catch( SQLException sqlException ) 
		      {
		         sqlException.printStackTrace();
		      } // end catch
		   } // end method destroy
		} // end class SurveyServlet


