/*
NAME: Christina Sadak
PROGRAM: Program 10 Web Crawler 2.0
PURPOSE: goes through all links in given url, checks to see if local or external. If local, determine if good or bad. If good .htm or .html links,
          find all links within those links and determine if good or broken
NOTES: uses code from Dannelly's code. Command link argument must pass full url
*/


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.net.*;
import java.util.*;

public class hw10
{

  public static boolean checkLink(String passLink)
  {

    boolean returnVal=true;

    try
    {
    Socket sock = new Socket ("faculty.winthrop.edu",80);

    // get the reading and writing streams
    InputStream sin = sock.getInputStream();
    BufferedReader fromServer = new BufferedReader(new InputStreamReader(sin));
    OutputStream sout = sock.getOutputStream();
    PrintWriter toServer = new PrintWriter (new OutputStreamWriter(sout));

    // build the request message
    String outmsg2 = "GET ";
    outmsg2 += passLink;
    outmsg2 += " HTTP/1.0\r\n\r\n";

    // send the request to the server
    toServer.print (outmsg2);
    toServer.flush();


    // read the response
    String inputline2 = fromServer.readLine();

    if(inputline2.contains("200"))
    {
      returnVal = true;//returns true if a good link
    }
    else
    {
      returnVal = false;//returns false if a bad link
    }
   }

   catch (Exception e)
   {
      System.out.println(e.getMessage());
   }

   return returnVal;

  }

   public static void main(String[] args) throws IOException
   {
    Document doc   = null;
    Document doc2  = null;
    Elements links = null;
    Elements links2 = null;
    Element  link  = null;
    Element link2 = null;

    // get URL from the user
    System.out.print ("URL to Process: ");
    InputStreamReader stdio = new InputStreamReader(System.in);
    BufferedReader keyboard = new BufferedReader (stdio);
    String url = keyboard.readLine();

    // did it include http:// at the beginning?
    if ( url.indexOf("http://") != 0 )
    {
       url = "http://" + url;
    }
       // go get the HTML file
       try
         {
          doc = Jsoup.connect(url).get();
         }
       catch (Exception e)
         {
          System.out.println("Error: Unable to get that URL.");
          return;
         }
       String title = doc.title();
       System.out.println("title is: " + title);

       // print all links
       links = doc.select("a[href]");//array of things that contain hrefs

       ArrayList<String> listofLinks = new ArrayList<String>();

       System.out.println("There are " + links.size() + " links:");
       for (int i=0; i<links.size(); i++)
         {
          link = links.get(i);//the link being looked at
          String hrefStr = link.attr("href");//gets the reference part of that string (relative address)
//make a new string that has link.attr("abs:href") that gets absolute address and then chop everything until /dannellys.... and pass that to the function that checks okay or bad
          String hrefStrFULL = link.attr("abs:href");

          if(hrefStr.contains("http"))
          {
          System.out.println("External ---- "
                                  + link.attr("href"));
          }
          else
          {
            //if local, determine if good or broken
            //first need to chop string down to everything after .edu
            int startPlace = hrefStrFULL.indexOf(".edu");//finds where .edu starts in the line
        		String newString = hrefStrFULL.substring(startPlace);//chops off the previous line before the .edu index

            startPlace = newString.indexOf("/");//finds the first slash quote after the .edu
        		newString = newString.substring(startPlace);//chops off everything until the first slash after the .edu

            if(checkLink(newString) == true)
            {
              //if function returns true...good link
              System.out.println("Local ---- Good ---- " + link.attr("href"));

              //since the link is okay, throw it in an arrayList to check again later
              if(newString.contains(".htm"))
                  listofLinks.add(newString);
            }
            else
            {
              System.out.println("Local ---- Broken ---- " + link.attr("href"));
            }
          }
         }

         System.out.println();
         System.out.println("DONE WITH LEVEL 0.");
         System.out.println();
         System.out.println("BEGINNING NEXT LEVEL: ");
         System.out.println();

         System.out.println("There were " + listofLinks.size() + " good links found. Now checking these.");
         System.out.println();

         for(int x=0; x<listofLinks.size(); x++)//goes through each good local link from before to check each link within that link's html
         {
           String url2 = listofLinks.get(x);

           if ( url2.indexOf("http://") != 0 )
           {
              url2 = "http://" + "faculty.winthrop.edu" + url2;
           }
            // go get the HTML file
            try
            {
               doc2 = Jsoup.connect(url2).get();
            }
            catch (Exception e)
            {
               System.out.println("Error: Unable to check that URL.");
               return;
            }
            String title2 = doc2.title();
            System.out.println();
            System.out.println("title is: " + title2);

            // print all links
            links2 = doc2.select("a[href]");//array of things that contain hrefs(links)

            System.out.println("There are " + links2.size() + " links:");
            for (int i=0; i<links2.size(); i++)
              {
               link2 = links2.get(i);//the link being looked at
               String hrefStr2 = link2.attr("href");//gets the reference part of that string (relative address)

               //make a new string that has link.attr("abs:href") that gets absolute address and then chop everything until /dannellys.... and pass that to the function that checks okay or bad
               String hrefStrFULL2 = link2.attr("abs:href");

               if(hrefStr2.contains("http"))
               {
               System.out.println("External ---- " + link2.attr("href"));
               }
               else
               {
                 //if local, determine if good or broken
                 //first need to chop string down to everything after .edu
                 int startPlace2 = hrefStrFULL2.indexOf(".edu");//finds where .edu starts in the line
             		 String newString2 = hrefStrFULL2.substring(startPlace2);//chops off the previous stuff before the .edu index

                 startPlace2 = newString2.indexOf("/");//finds the first slash quote after the .edu
             		 newString2 = newString2.substring(startPlace2);//chops off everything until the first slash after the .edu

                 if(checkLink(newString2) == true)
                 {
                   //if function returns true...good link
                   System.out.println("Local ---- Good ---- " + link2.attr("href"));
                 }
                 else
                 {
                   System.out.println("Local ---- Broken ---- " + link2.attr("href"));
                 }
               }
              }
          }

      }//end of main
   }
