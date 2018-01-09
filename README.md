# web-crawler
This program creates a web crawler that can find bad links in a web site. The application has three main functions. 
1. It takes a specified HTML file via the command line (arg[0]) and has the application list all links within that file. 
2. For each of the links that are local, the application prints a message indicating if the link is okay or broken. 
3. For each of the links that are links to local HTML files, the application lists all links inside those sub-files and determines if those links are valid. 
Does not check external links to determine if they are okay or broken, it just lists external links as external. I assume that any href beginning with http:// or https:// is an external link. 
I assume the web server is faculty.winthrop.edu.  For example, if the user specified "dannellys", your program would search http://faculty.winthrop.edu/dannellys/. 
It is clear which links belong to the original URL and which links belong to different sub-URLs.
