package com.Observer_design_pattern.Observer;
import com.Observer_design_pattern.Observable.*;

public interface Observer {
    public void update(StockName deviceType,int currentStock);
}
