# 🏨 StayEase – Hotel Booking Platform (Spring Boot + PostgreSQL)

StayEase is a backend system for a hotel/property rental platform.  
It supports secure authentication, property listings, real-time room availability checks, booking workflows, and pricing strategies.

This project focuses on *backend engineering, **transaction safety, and **correct inventory management*—similar to real-world OTA systems like Airbnb/Booking.com.

---

## 🚀 Features

### 🔐 Authentication
- User registration & login  
- JWT-based authentication  
- Role-based access control

### 🏨 Property & Room Management
- Create and update hotels and room types  
- Real-time search by city, date range, number of guests  
- Dynamic filtering and pagination

### 📦 Inventory System (Core Feature)
- Date-wise inventory tracking for each room type  
- totalCount, bookedCount, reservedCount, surgeFactor  
- Prevents overbooking even under heavy concurrency  

### 🧾 Booking Workflow
- Check availability  
- Initial “reserved” hold  
- Payment confirmation → converts reserved → booked  
- Cancellation restores availability  
- Automatic cleanup of stale bookings (pending too long)

### 💸 Dynamic Pricing (Strategy Pattern)
- Base price  
- Occupancy-based pricing  
- Urgency pricing  
- Holiday/surge pricing  

## 🏗 Architecture
Client (Web/Mobile)
↓
Spring Boot (Controllers)
↓
Service Layer (Business Logic)
↓
Repository Layer (JPA + Custom Queries)
↓
PostgreSQL (Room, Inventory, Booking, Guest)




## 🗄 Database Schema Overview

### *Room Table*
Stores room type, base price, max guests.

### *Inventory Table*
Tracks availability per date:
- room_id  
- date  
- totalCount  
- bookedCount  
- reservedCount  
- closed  
- surgeFactor  

### *Booking Table*
Tracks booking status:
- PENDING → CONFIRMED → CANCELLED

### *BookingGuest Table*
Supports multiple guests per booking.

---

## 🛠 Tech Stack

- *Java 17*
- *Spring Boot*
- *Spring Security + JWT*
- *Spring Data JPA*
- *PostgreSQL*
- *Maven*
- *Lombok*


## 🔮 Future Enhancements

- *💳 Payment Gateway Integration (Stripe/PayPal)*  
  Add secure online payments and booking confirmation.

- *🤖 Recommendations System*  
  Personalized hotel & room suggestions using user behavior and search patterns.

- *⚡ Redis Caching Layer*  
  Faster search results and reduced DB load.

- *🔍 Search Optimization*  
  Improved indexing, pagination, and query performance.

- *📊 Rate Limiting & Analytics Dashboard*  
  Protect APIs from overuse and track business insights.
