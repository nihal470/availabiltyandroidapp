package com.example.data.db

import com.example.data.model.BookingEntity
import com.example.data.model.CustomerEntity
import com.example.data.model.LocationEntity
import com.example.data.model.MaintenanceEntity
import com.example.data.model.PaymentEntity
import com.example.data.model.StaffUserEntity
import com.example.data.model.VehicleEntity

object DatabaseSeeder {

    suspend fun seedInitialData(database: TravzDatabase) {
        val vehicleDao = database.vehicleDao()
        val bookingDao = database.bookingDao()
        val customerDao = database.customerDao()
        val maintenanceDao = database.maintenanceDao()
        val staffDao = database.staffDao()
        val locationDao = database.locationDao()
        val paymentDao = database.paymentDao()

        // 1. Seed Locations
        val locations = listOf(
            LocationEntity(1, "Muscat International Airport", "Terminal 1 Arrivals, Seeb, Muscat", "+968 2435 1100"),
            LocationEntity(2, "Ruwi Business Center", "CBD Way 3105, Ruwi, Muscat", "+968 2470 2211"),
            LocationEntity(3, "Al Khuwair City Branch", "Dohat Al Adab St, Al Khuwair", "+968 2448 3322"),
            LocationEntity(4, "Salalah Airport Branch", "Main Terminal, Salalah, Dhofar", "+968 2329 4455"),
            LocationEntity(5, "Sohar Port Branch", "Falaj Al Qabail, Sohar", "+968 2684 5566")
        )
        locationDao.insertAll(locations)

        // 2. Seed Staff Users
        val staffUsers = listOf(
            StaffUserEntity(1, "Tariq Al-Riyami", "tariq.admin@travz.om", "Admin", "+968 9911 2233", "#DC2626"),
            StaffUserEntity(2, "Fatima Al-Zadjali", "fatima.manager@travz.om", "Manager", "+968 9822 3344", "#2563EB"),
            StaffUserEntity(3, "Salem Al-Ghafri", "salem.staff@travz.om", "Staff", "+968 9733 4455", "#059669")
        )
        staffDao.insertAll(staffUsers)

        // 3. Seed Customers
        val customers = listOf(
            CustomerEntity(1, "Sheikh Ahmed Al-Harthy", "+968 9555 1234", "ahmed.harthy@gmail.com", "ID-90182", "Omani"),
            CustomerEntity(2, "Sarah Jenkins", "+968 9444 5678", "sarah.j@turner.co.uk", "PASSPORT-UK9812", "British"),
            CustomerEntity(3, "Rashid Al-Balushi", "+968 9333 9988", "rashid.balushi@omantel.om", "ID-88219", "Omani"),
            CustomerEntity(4, "Mohammed Al-Mamari", "+968 9222 3311", "m.mamari@soharu.edu.om", "ID-77210", "Omani"),
            CustomerEntity(5, "Elena Rostova", "+968 9111 4455", "elena.r@translog.com", "PASSPORT-RU4412", "Russian")
        )
        customerDao.insertAll(customers)

        // 4. Seed Vehicles (Fleet)
        val vehicles = listOf(
            VehicleEntity(
                id = 1,
                make = "Toyota",
                model = "Land Cruiser Prado TXL",
                regNumber = "48291-B",
                category = "SUV",
                modelYear = 2024,
                dailyRate = 42.0,
                status = "Available",
                imageDrawableName = "car_suv_prado_1790193171736",
                transmission = "Automatic",
                seats = 7,
                fuelType = "Petrol",
                mileageKm = 18400
            ),
            VehicleEntity(
                id = 2,
                make = "Nissan",
                model = "Patrol Titanium 4x4",
                regNumber = "19482-A",
                category = "4x4",
                modelYear = 2024,
                dailyRate = 60.0,
                status = "Booked",
                imageDrawableName = "car_4x4_patrol_1790193195572",
                transmission = "Automatic",
                seats = 8,
                fuelType = "Petrol V8",
                mileageKm = 12900
            ),
            VehicleEntity(
                id = 3,
                make = "Toyota",
                model = "Camry Grande V6",
                regNumber = "73910-M",
                category = "Sedan",
                modelYear = 2024,
                dailyRate = 25.0,
                status = "Booked",
                imageDrawableName = "car_sedan_camry_1790193184188",
                transmission = "Automatic",
                seats = 5,
                fuelType = "Petrol",
                mileageKm = 24100
            ),
            VehicleEntity(
                id = 4,
                make = "Hyundai",
                model = "Accent GL Smart",
                regNumber = "31092-D",
                category = "Economy",
                modelYear = 2023,
                dailyRate = 14.0,
                status = "Available",
                imageDrawableName = "car_economy_accent_1790193207687",
                transmission = "Automatic",
                seats = 5,
                fuelType = "Petrol",
                mileageKm = 36000
            ),
            VehicleEntity(
                id = 5,
                make = "Mitsubishi",
                model = "Pajero GLS 3.8",
                regNumber = "58210-H",
                category = "SUV",
                modelYear = 2023,
                dailyRate = 38.0,
                status = "Available",
                imageDrawableName = "car_suv_prado_1790193171736",
                transmission = "Automatic",
                seats = 7,
                fuelType = "Petrol",
                mileageKm = 41200
            ),
            VehicleEntity(
                id = 6,
                make = "Nissan",
                model = "Altima 2.5 SV",
                regNumber = "90214-K",
                category = "Sedan",
                modelYear = 2024,
                dailyRate = 22.0,
                status = "Maintenance",
                imageDrawableName = "car_sedan_camry_1790193184188",
                transmission = "Automatic",
                seats = 5,
                fuelType = "Petrol",
                mileageKm = 29500
            ),
            VehicleEntity(
                id = 7,
                make = "Toyota",
                model = "Land Cruiser 300 VXR",
                regNumber = "88321-A",
                category = "4x4",
                modelYear = 2024,
                dailyRate = 75.0,
                status = "Available",
                imageDrawableName = "car_4x4_patrol_1790193195572",
                transmission = "Automatic",
                seats = 7,
                fuelType = "Twin Turbo",
                mileageKm = 9800
            ),
            VehicleEntity(
                id = 8,
                make = "Kia",
                model = "Pegas 1.4 MPI",
                regNumber = "41209-J",
                category = "Economy",
                modelYear = 2023,
                dailyRate = 13.0,
                status = "Available",
                imageDrawableName = "car_economy_accent_1790193207687",
                transmission = "Automatic",
                seats = 5,
                fuelType = "Petrol",
                mileageKm = 31000
            )
        )
        vehicleDao.insertVehicles(vehicles)

        // 5. Seed Bookings
        val bookings = listOf(
            BookingEntity(
                id = 1,
                bookingCode = "TRV-2026-001",
                customerName = "Sheikh Ahmed Al-Harthy",
                customerPhone = "+968 9555 1234",
                customerEmail = "ahmed.harthy@gmail.com",
                vehicleId = 2, // Nissan Patrol
                pickupDate = "2026-09-22",
                dropoffDate = "2026-09-27",
                pickupTime = "10:00 AM",
                dropoffTime = "10:00 AM",
                pickupLocation = "Muscat International Airport",
                dropoffLocation = "Muscat International Airport",
                dailyRate = 60.0,
                rentalDays = 5,
                subtotal = 300.0,
                vatAmount = 15.0,
                totalAmount = 315.0,
                paymentStatus = "Paid",
                bookingStatus = "Active",
                notes = "VIP client, provided complimentary airport greeting",
                createdAt = System.currentTimeMillis() - 86400000L * 2
            ),
            BookingEntity(
                id = 2,
                bookingCode = "TRV-2026-002",
                customerName = "Sarah Jenkins",
                customerPhone = "+968 9444 5678",
                customerEmail = "sarah.j@turner.co.uk",
                vehicleId = 3, // Toyota Camry
                pickupDate = "2026-09-21",
                dropoffDate = "2026-09-25",
                pickupTime = "02:00 PM",
                dropoffTime = "02:00 PM",
                pickupLocation = "Ruwi Business Center",
                dropoffLocation = "Muscat International Airport",
                dailyRate = 25.0,
                rentalDays = 4,
                subtotal = 100.0,
                vatAmount = 5.0,
                totalAmount = 105.0,
                paymentStatus = "Paid",
                bookingStatus = "Active",
                notes = "Flight QR-112 departure pickup",
                createdAt = System.currentTimeMillis() - 86400000L * 3
            ),
            BookingEntity(
                id = 3,
                bookingCode = "TRV-2026-003",
                customerName = "Rashid Al-Balushi",
                customerPhone = "+968 9333 9988",
                customerEmail = "rashid.balushi@omantel.om",
                vehicleId = 5, // Pajero
                pickupDate = "2026-09-28",
                dropoffDate = "2026-10-03",
                pickupTime = "09:00 AM",
                dropoffTime = "09:00 AM",
                pickupLocation = "Al Khuwair City Branch",
                dropoffLocation = "Al Khuwair City Branch",
                dailyRate = 38.0,
                rentalDays = 5,
                subtotal = 190.0,
                vatAmount = 9.5,
                totalAmount = 199.5,
                paymentStatus = "Partial",
                bookingStatus = "Confirmed",
                notes = "50 OMR advance paid, balance on pickup",
                createdAt = System.currentTimeMillis() - 86400000L
            ),
            BookingEntity(
                id = 4,
                bookingCode = "TRV-2026-004",
                customerName = "Mohammed Al-Mamari",
                customerPhone = "+968 9222 3311",
                customerEmail = "m.mamari@soharu.edu.om",
                vehicleId = 4, // Accent
                pickupDate = "2026-10-02",
                dropoffDate = "2026-10-07",
                pickupTime = "11:00 AM",
                dropoffTime = "11:00 AM",
                pickupLocation = "Sohar Port Branch",
                dropoffLocation = "Muscat International Airport",
                dailyRate = 14.0,
                rentalDays = 5,
                subtotal = 70.0,
                vatAmount = 3.5,
                totalAmount = 73.5,
                paymentStatus = "Unpaid",
                bookingStatus = "Confirmed",
                notes = "Pay on arrival at Sohar desk",
                createdAt = System.currentTimeMillis() - 43200000L
            )
        )
        bookingDao.insertBookings(bookings)

        // 6. Seed Maintenance
        val maintenanceList = listOf(
            MaintenanceEntity(
                id = 1,
                vehicleId = 6, // Nissan Altima
                startDate = "2026-09-20",
                endDate = "2026-09-26",
                reason = "30,000 km periodic service & front brake pad change",
                cost = 85.0,
                status = "InProgress",
                notes = "Bahwan Motors authorized workshop"
            ),
            MaintenanceEntity(
                id = 2,
                vehicleId = 1, // Toyota Prado
                startDate = "2026-10-10",
                endDate = "2026-10-12",
                reason = "Scheduled tire rotation and wheel alignment",
                cost = 35.0,
                status = "Scheduled",
                notes = "Saud Bahwan workshop"
            )
        )
        maintenanceDao.insertAll(maintenanceList)

        // 7. Seed Payments
        val payments = listOf(
            PaymentEntity(1, 1, 315.0, "Credit Card", "2026-09-22", "TXN-778819", "Completed"),
            PaymentEntity(2, 2, 105.0, "Debit Card", "2026-09-21", "TXN-661902", "Completed"),
            PaymentEntity(3, 3, 50.0, "Cash", "2026-09-22", "REC-10023", "Completed")
        )
        paymentDao.insertAll(payments)
    }
}
