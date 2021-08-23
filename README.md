RiSE Web Application - Ritzpa Stock Exchange web app
-----------------------------------------------------
To launch the app on server please first build the RiSEweb artifact. That will create RiSEweb.war to the project's "out" directory.
URL to deploy: http://localhost:8080/RiSEweb

Overview:
-----------------------
This program was written and made as the third part of the Java Project “RSE” given along with the course.
As a continuation to the first and second projects, this implements all and every logic already manifested in the former parts, with the additions of Extended User Management and a server-client based web_UI.
[NOTICE: this program is planned to run via Apache Tomcat 8.5.x.
Any end every other methods aren’t guarantee to work]

How-to-use:
-----------------------
Drop the RiSEweb.war file in your Tomcat/webapp directory and run tomcat’s Startup.bat on windows machine (on linux/unix machines run Startup.sh)
Then go to http://localhost:8080/RiSEweb on your browser, and Log-in as described. 

The program saves you valuable information on the session created between the browser and the server, meaning once you sign-up you are guaranteed to be logged in with same user you initially signed-up with (unless you love temper with DevTools and cookies).
You can sign-in as a ‘Trader’ who can make active trades on the system, load up stocks information via an XML file (built in accordance to “RSE-V3.xsd” Schema - can be found in testfiles_v3 folder), and issue new companies.
The other option is to sign-in as an ‘Admin’, which doesn’t have ‘Trader’ abilities, but make up for it by having access to the insides of the engine running information.
Upon both loading failure and loading success of the XML file, a pop-up window with the relevant information should appear after the loading.
After the XML data was loaded successfully, the program is ready to use.

1)	The “home” screen:
    In this screen you will be able to see the current logged-on users, as well as the registered stocks information.
    Upon clicking a stock symbol, you will be redirected to the ‘Data’ page with extensive information on this specific Stock.
    Traders will also be able to load the xml file, issue a new stock and managing their account via the ‘home’ page.

2)	The “Account” page: (for Trader role only)
    You can access this page by clicking on the top-left human avatar, as well as the text ‘My account’ below it.
    In this page you will be able to see all of your history active actions in the system, as well as the current account balance.
    You can also charge up your credit balance by clicking the ‘Charge money to credit balance’ drop-down button.

3)	The “Data” page: 
    You can access this page by clicking on any registered stock symbol in the “home” page.
    In this page you will have access to most of the related stock information, as well as its completed trades information.
    Admins will also be able to toggle between the active sellers and buyers lists information.
    Traders can fire new trade orders of this certain stock from this page, by opening the drop-down menu ‘Send New Trade Order’ and filling in the request.
    Traders also have accessibility to their current amount of shares in their possession.

System Information:
-----------------------
**Engine Module**

This Module houses 4 main packages:
- Data – stores 2 sub-packages:

    o data.stock –
        This package holds the Stock.class which is a data frame for information used in the system for the concept “stock”, as you familiar with. Each stock holds 3 respective         lists of trade orders and completed transactions made in “RiSE”.
        The second class in this package is StockList.class which is an encapsulating, self-managing class for a List data structure of Stock.class objects.
    
    o data.tradeOrder – stores additional 2 sub packages:
        ▪ data.tradeOrder.order – 
            this sub package you can find the abstract class Order.class which is the frame of a trade order made in the system. The other classes inside are inherited classes               from Order.class, each for its designed use.
        ▪ data.tradeOrder.list –
            A sub Package which holds classes for managing data structures of “orders” and “completed trades” , in a similar fashion to StockList.class explained earlier.

- engine :
This package holds the backbone of the program which is the Engine interface, designed to answer the overall needs of the user.
This interface is implemented by the class RiseEngineV1.class, which makes the on-site real-time calculations and designed according to the Engine interface and the project necessities. It holds a single StockList.class object for needed information and the main program memory.

- exception:
In this package you will find dedicated exceptions for this system. Mainly used by the methods within MainMenu.class, as well as the main method.

- generated:
A package made by the JAXB API. Holds classes for XML elements used in by RitzpaStockExchangeDescriptor found in XML files built in accordance to testfiles_v3\RSE-V3.xsd Schema.

***update V2:
The “User” class, “Portfolio” class and “Item” class are the main addition to the Engine module in which the “Item” class defines a pair of stock and how many shares from it exists in the user “Portfolio” or holdings, which is a managerial class to hold a map of “Items”.  The “User” class links between the user name and its relevant portfolio.
The newly “UserList” class is a managerial class similar to “StockList” and “OrderList” classes in which it manages a map of users by name.

***update V3:
The “FOKOrder” and “IOCOrder” are the main addition to the Engine module in which the both describe the FOK (Fill Or Kill) trade order and IOC (Immediate Or Cancel) trade order, respectively.
The auxiliary classes “StockDTO”, ” TradeLog”, “UserAction”, ”UserActivityLog” and ”ValidRseStocksDTO” are new additions as well, and their role is to transfer reliable data between the server and the client.

**WebApp Module**

This module responsible for all the graphic content of this program, as well as processing and/or passing the relevant operations performed by the client on the browser, to the server.
All the style and design in this module is as represented several CSS files (which can be found under the “pages” directory.

The main classes in this module are the Servlet classes, each taking control of different part of the system, and managing data between the client and the server.
In this module you can find all the resources used for the visibility and client-side coding such as the .html, .jsp, .js, .png and .css as stated earlier.

I mostly focused on creating as light as possible server and client side computations, by distributing the workload decided on their stronger aspects and design patterns.
For example, the Login page is a static .html file that only sends and receives the minimal needed data to and from the server. 
On the other hand, the ‘Data’ page was a bit trickier since I needed to visualize each stock from the entire stocks on the system.
Writing a static html file for each stock isn’t a realistic solution, and so I had the idea of creating the format for the page in html code on a jsp file, and filling in the dynamic information actively on the server-side, and only then send the whole (or most) of the package to the browser. The vivid on-time information was taken care of by utilizing some javascript code to create custom requests to the server.
There are some known minor bugs and design issues I wanted to deal with, but couldn’t really make up to the dead-line and so this is the final product (as it is now)
I hope you will enjoy mess around and review this system, and I wish to hear for any ideas/concepts/corrections I could and didn’t use in this project.


