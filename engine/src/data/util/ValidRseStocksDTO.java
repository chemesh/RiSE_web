package data.util;

import generated.RseStock;

import java.util.List;

public class ValidRseStocksDTO
{
    private final List<RseStock> rseStocks;
    private final List<String> alerts;

    public ValidRseStocksDTO(List<RseStock> stocks, List<String> alerts){
        this.rseStocks = stocks;
        this.alerts = alerts;
    }

    public List<RseStock> getValidRseStocks(){
        return rseStocks;
    }

    public List<String> getAlerts(){
        return alerts;
    }

}
