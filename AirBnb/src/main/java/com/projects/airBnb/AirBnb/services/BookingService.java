package com.projects.airBnb.AirBnb.services;



import com.projects.airBnb.AirBnb.dto.BookingDto;
import com.projects.airBnb.AirBnb.dto.BookingRequest;
import com.projects.airBnb.AirBnb.dto.GuestDto;
import com.projects.airBnb.AirBnb.dto.HotelReportDto;
import com.projects.airBnb.AirBnb.entities.enums.BookingStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.stripe.model.Event;
public interface BookingService {

    BookingDto initialiseBooking(BookingRequest bookingRequest);

    BookingDto addGuests(Long bookingId, List<Long> guestIdList);

    String initiatePayments(Long bookingId);

    void capturePayment(Event event);

    void cancelBooking(Long bookingId);

    BookingStatus getBookingStatus(Long bookingId);

    List<BookingDto> getAllBookingsByHotelId(Long hotelId);

    HotelReportDto getHotelReport(Long hotelId, LocalDate startDate, LocalDate endDate);

    List<BookingDto> getMyBookings();
}
