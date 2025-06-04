package com.projects.airBnb.AirBnb.strategy;



import com.projects.airBnb.AirBnb.entities.Inventory;

import java.math.BigDecimal;

public class BasePricingStrategy implements PricingStrategy{
    @Override
    public BigDecimal calculatePrice(Inventory inventory) {
        return inventory.getRoom().getBasePrice();
    }
}
