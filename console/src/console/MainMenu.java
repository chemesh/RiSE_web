package console;


import data.stock.Stock;
import data.tradeOrder.order.LMTOrder;
import data.tradeOrder.order.MKTOrder;
import data.tradeOrder.order.TradeDTO;
import engine.Engine;
import engine.RiseEngineV1;
import exception.InvalidActionException;
import exception.InvalidStockSymbol;
import exception.UnloadedDataException;

import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MainMenu {

    private static Engine eng;
    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        boolean end = false;
        eng = new RiseEngineV1();


        System.out.println("Welcome to Ritzpa Stock Exchange!");


        while (!end) {

            try {
                System.out.println("(press enter to continue)\n");
                System.in.read();

                System.out.println("What would you like to do today?\n" +
                        "please enter the option number and then Enter\n\n" +

                        "1) Load Existing system file (.xml)\n" +
                        "2) Show stocks\n" +
                        "3) Show info on a single stock\n" +
                        "4) Send new trade order\n" +
                        "5) Show trade orders\n" +
                        //"6) Save System data to XML file\n"+
                        "6) Exit\n");


                int op = input.nextInt();

                switch (op) {
                    case (1):   loadXml();  break;
                    case (2):   showStocks();   break;
                    case (3):   showSingleStockInfo();  break;
                    case (4):   makeNewTradeOrder();    break;
                    case (5):   showTradeOrders();  break;
                    //case(6):    saveSystemData(); break;
                    case (6):   end = validateExit();         break;
                    default:    throw new InvalidActionException();
                }
            } //try

            catch (InvalidActionException | InvalidStockSymbol e) {
                System.out.println(e.getMessage());
                input.nextLine();
            }
            catch (InputMismatchException e){
                System.out.println("Error! Invalid Input was Entered!\n" +
                        "Please enter the operation number as detailed");
                input.nextLine();
            }

            catch (Exception e){
                System.out.println(e.getMessage());
            }
        } //while

        System.out.println("Goodbye!");

    } //main

    private static void loadXml() {
        System.out.println("Please type in the full path for the .xml file: ");
        String filePath="";
        try{
            input.nextLine();
            filePath = input.nextLine();
            eng.loadXML(filePath);
            System.out.println("Data loaded successfully!\n");
        }
        catch (IndexOutOfBoundsException e){
            //e.printStackTrace(); //debug
            throw new IllegalArgumentException("'"+filePath+"' is not a full path!");
        }
        catch (FileNotFoundException e){
            //e.printStackTrace(); //debug
            System.out.println("Error! File: "+filePath+" couldn't be found");
        }
        catch(IllegalArgumentException e){
            //e.printStackTrace(); //debug
            System.out.println("Error! "+e.getMessage());
        }


    }

    private static void showStocks() throws UnloadedDataException {

        if (!eng.isDataLoaded()) {throw new UnloadedDataException();}

        List<String> infoList = eng.getStocksInfo();

        for (String st : infoList) {
            System.out.println(st);
        }
    }

    private static void showSingleStockInfo() throws UnloadedDataException, InvalidStockSymbol {

        if (!eng.isDataLoaded()) {throw new UnloadedDataException();}

        System.out.println("Please type in the stock symbol as shown in the system, " +
                "or enter 'q' and Enter to go back to main menu: ");
        String sym = input.next().toUpperCase();
        if (sym.equals("Q"))
            return;

        System.out.println(eng.getInDepthStockInfo(sym) + '\n');
    }

    private static void makeNewTradeOrder()
            throws UnloadedDataException, InvalidStockSymbol, InvalidActionException {
        if (!eng.isDataLoaded()) {throw new UnloadedDataException();}

        System.out.println("Please choose from the following:\n" +
                "1) make LMT trade order\n" +
                "2) make MKT trade Order\n" +
                "3) go back to main menu\n");

        int op = input.nextInt();

        switch (op) {
            case (1):   tradeResults(makeLMTOrder()); break;
            case (2):   tradeResults(makeMKTOrder()); break;
            case (3):   return;
            default:    throw new InvalidActionException();
        }
    }

    private static void showTradeOrders() throws UnloadedDataException{
        if (!eng.isDataLoaded()) {throw new UnloadedDataException();}

        try{
            List<Stock> stocks = eng.getStockList();

            for (Stock s : stocks)
            {
                System.out.println(eng.getInDepthStockInfo(s.symbol));
                System.out.println("--Active Buying Trade Orders-- ");
                System.out.println(eng.getStockBuyersListInfo(s.symbol));
                System.out.println("--Active Selling Trade Orders-- ");
                System.out.println(eng.getStockSellersListInfo(s.symbol)+"\n");
            }

        } catch (InvalidStockSymbol e) {
            e.printStackTrace();
        }
    }

    private static TradeDTO makeLMTOrder() throws InvalidStockSymbol {
        System.out.println("Please enter your order in the following format, or enter 'q' to go back:\n" +
                "[action (s=sell/b=buy)] [SYMBOL] [quantity] [price limit]\n" +
                "(i.e. s goOgL 30 120)\n");

        String action = input.next().toLowerCase();
        if (action.equals("q"))
            return null;

        else if (!action.equals("s") && !action.equals(("b")))
            throw new IllegalArgumentException("Action operation isn't recognized." +
                    "accepted inputs are 's' or 'b'");

        Stock s = eng.getStock(input.next());

        try {
            int quantity = input.nextInt();
            if (quantity <= 0)
                throw new IllegalArgumentException("Illegal quantity." +
                        "Needs to be bigger than 0");
            int price = input.nextInt();

            LMTOrder o = new LMTOrder(action, s, quantity, price);
            return eng.addOrder(o);
        }
        catch (InputMismatchException e)
        {
            input.nextLine();
            throw new IllegalArgumentException("Error! Illegal Input. Please Enter an integer bigger than 0");
        }




    }

    private static TradeDTO makeMKTOrder() throws InvalidStockSymbol{

        System.out.println("Please enter your order in the following format, or enter 'q' to go back:\n" +
                "[action (s=sell/b=buy)] [SYMBOL] [quantity]\n" +
                "(i.e. b tSlA 420 )\n");

        String action = input.next().toLowerCase();
        if (action.equals("q"))
            return null;

        else if (!action.equals("s") && !action.equals(("b")))
            throw new IllegalArgumentException("Action operation isn't recognized");

        Stock s = eng.getStock(input.next());

        try {
            int quantity = input.nextInt();
            if (quantity <= 0)
                throw new IllegalArgumentException("Illegal quantity." +
                        "Needs to be over 0");


            MKTOrder o = new MKTOrder(action, s, quantity);
            return eng.addOrder(o);
        }
        catch (InputMismatchException e){
            input.nextLine();
            throw new IllegalArgumentException("Error! Illegal Input. Please Enter an integer bigger than 0");
        }

    }

    private static void tradeResults(TradeDTO dto)
    {

        if (dto.isEmpty()) {
            System.out.println("RiSE couldn't make the requested Trade. \n" +
                    "The Trade Order has been added to the system's Orders Book ");
            return;
        }

        if (dto.isOrderFullyMade()){
            System.out.println("Trade Order was fully completed.");
        }
        else if (dto.isOrderPartiallyMade()){
            System.out.println("Trade Order was partially completed.\n" +
                    "remaining Stock quantity left in Order: "+dto.getQuantityLeft());
        }
        System.out.println(dto.toString());
    }

    private static boolean validateExit(){
        System.out.println("Are you sure you want to exit RiSE - Ritzpa Stock Exchange? (y/n)");
        String op = input.next().toLowerCase();
        if (op.equals("y")){
            return true;
        }
        else if (op.equals("n")){
            return false;
        }
        else{
            throw new IllegalArgumentException("Error! please type in 'y'(yes) or 'n'(no)");
        }
    }
}


