# Leave Schedule and Flight Ticket System (Version 2 - Complete, GUI)

A desktop application built with Java Swing, generated through vibe coding based on a set of user stories.
This is the **second submission (complete version)** and includes a graphical user interface.

## Features

**Leave Schedule (HR manager)**
- View employee leave schedules by year, month, and date
- View employee leave history and remaining leave balance

**Flight Search**
- Search flight schedules by destination (one or multiple), origin, date, and time
- Multi-destination search for frequent traveller members
- Compare ticket prices by destination, date, and departure time (members)

**Booking and Travel Manager**
- Book and cancel flight tickets, including bookings on behalf of visitors
- View booking statuses with a filter: confirmed, booked, waiting for the payment
- Authorize tickets using a booking reference
- View tickets owned by a visitor (e-ticket delivery is simulated with a confirmation message)

**Member Profile**
- Load, update, and save member profile data
- Validate tickets (a "data profil tidak lengkap" message is shown if the profile is incomplete)

## User Interface

The application has four tabs: Cuti (HRD), Cari Penerbangan, Pemesanan & Manajer Travel, and Member.
Origin, destination, date, time, and ID fields are editable drop-down lists, so users can either pick a value
from the list or type it manually.

## Project Structure

The code is split into multiple files, with one class per file:

| File | Description |
|---|---|
| `APS.java` | Entry point; creates the main window and tabs |
| `Data.java` | In-memory data, sample data, and search functions |
| `PanelDasar.java` | Base panel with shared user interface helper methods |
| `TabCuti.java` | Leave schedule tab |
| `TabPenerbangan.java` | Flight search tab |
| `TabPemesanan.java` | Booking and travel manager tab |
| `TabMember.java` | Member profile tab |
| `Cuti.java`, `Penerbangan.java`, `Pengguna.java`, `Pemesanan.java` | Data model classes |

## Requirements

- Java JDK 17 or later

## How to Run

Place all `.java` files in the same folder, then run:

```
javac *.java
java APS
```

## Sample Data

- Member ID 1 (Andi): complete profile
- Member ID 2 (Citra): incomplete profile (useful for testing ticket validation failure)
- User ID 3 (Dedi): visitor, not a member
- Initial bookings: `BK0001` (confirmed) and `BK0002` (waiting for the payment)

## Notes

- All data is stored in memory, so it resets every time the program restarts.
- No database or external library is used.
