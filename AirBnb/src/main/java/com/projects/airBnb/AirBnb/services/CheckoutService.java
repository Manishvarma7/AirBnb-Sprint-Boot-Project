package com.projects.airBnb.AirBnb.services;

import com.projects.airBnb.AirBnb.entities.Booking;

public interface CheckoutService {

    String getCheckoutSession(Booking booking, String successUrl, String failureUrl);

}
