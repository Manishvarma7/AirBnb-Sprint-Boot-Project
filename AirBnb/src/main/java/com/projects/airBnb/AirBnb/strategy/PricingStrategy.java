package com.projects.airBnb.AirBnb.strategy;



import com.projects.airBnb.AirBnb.entities.Inventory;

import java.math.BigDecimal;
public interface PricingStrategy {

    BigDecimal calculatePrice(Inventory inventory);
}
