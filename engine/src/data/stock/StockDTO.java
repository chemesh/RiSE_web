package data.stock;

public class StockDTO {

    private  final String symbol;
    private final String company;
    private final int price;
    private final int cycle;


    public StockDTO(String symbol, String company, int price, int cycle) {
        this.symbol = symbol;
        this.company = company;
        this.price = price;
        this.cycle = cycle;
    }

    public StockDTO(Stock s){
        this(s.getSymbol(), s.getCompany(), s.getPrice(), s.getCycle());
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompany() {
        return company;
    }

    public int getPrice() {
        return price;
    }

    public int getCycle() {
        return cycle;
    }
}

