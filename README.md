# Agribook - Multilingual Farming Social Network

Agribook is a production-oriented, multilingual social network engineered for farmers, agronomy scientists, and agricultural communities.

---

## 🌾 Core Capabilities

1. **Multilingual Architecture**
   - Native support for 6 regional and national languages:
     - **Telugu (తెలుగు)**
     - **English (English)**
     - **Hindi (हिन्दी)**
     - **Kannada (ಕನ್ನಡ)**
     - **Tamil (தமிழ்)**
     - **Malayalam (മലയാളം)**
   - Instant language switching across all screens with persistent database storage.

2. **Security & Authorization**
   - Cryptographic salt generation via `SecureRandom`.
   - SHA-256 password hashing with unique per-user salts.
   - Sliding-window rate-limiting (5 attempts per 60 seconds) preventing brute force on auth endpoints.
   - Strict XSS and markup input sanitization.
   - Role-Based Access Control (`FARMER`, `EXPERT`, `ADMIN`).

3. **Farm Feed & Crop Knowledge Sharing**
   - Real-time crop category filtering: **Paddy**, **Wheat**, **Cotton**, **Chili**, **Sugarcane**, **Organic**, **Soil & Fertilizer**, **Pest Control**.
   - Create post dialog with category selection and photo attachments.
   - Helpful (Like) counts, Advice/Comments bottom sheet, Save (Bookmark) tips, and Report content actions.

4. **Farmer Networking & Discovery**
   - Search directory by farmer name, district/state, or cultivated crops.
   - Connection request flow: Send, Accept, and Decline requests.
   - Direct farmer-to-farmer networking with profile previews (acreage, soil practices, crops).
   - User blocking to prevent unwanted interactions.

5. **Authorized Real-Time Messaging & Agronomy Chat**
   - Private 1-on-1 chat threads with connected farmers and agricultural scientists.
   - Privacy-controlled messaging enforcement (configurable: "All Farmers" vs "Connections Only").
   - Automated agronomic advisory response simulation for verified experts.

6. **User Profile & Agronomic Identity**
   - Comprehensive farmer schema: land holding (acres), village, mandal, district, state, GPS coordinates (`latitude`, `longitude`), primary crops, soil type (e.g. Deep Black Clay, Red Sandy Loam), irrigation source (Canal, Borewell, Drip), and farming interests.
   - Edit Profile dialog with real-time validation and sanitization.
   - Three-tab profile dashboard: **Profile & Land**, **My Crops & Fields**, and **Privacy & Security**.

7. **Manual Crop Record Management ("My Crops")**
   - Add crop details manually: Crop Name, Variety / Seed Type, Season (Kharif, Rabi, Zaid), Sowing Month, Area in Acres, Growth Stage (Vegetative, Flowering, Grain Filling, Harvested), Soil & Fertilizer Notes, and Expected Yield in Quintals.
   - Delete and update crop records with persistent database storage in `CropDetailEntity`.

8. **GPS Location Access & Nearby Farmers Discovery**
   - Runtime location permission flow (`ACCESS_FINE_LOCATION`, `ACCESS_COARSE_LOCATION`) using `rememberLauncherForActivityResult`.
   - "📍 Detect GPS Location" button integrating `FusedLocationProviderClient`.
   - Precise distance calculation using the Haversine formula (accurate to 0.1 km).
   - "Near Me (<50 km)" filter chip to immediately discover and network with local crop peers within 50 kilometers.
   - Instant 1-tap "Connect" / "Send Request" and "Chat" buttons on nearby farmer cards.

9. **Activity Notifications & Privacy Controls**

8. **Protected Admin Moderation Desk**
   - Restricted to `ADMIN` role (`Dr. K. Swaminathan`).
   - Live queue of flagged posts and reported users.
   - Moderation actions: Dismiss report, Delete reported post, or Suspend/Ban account.

---

## 🎨 Design System
- **Theme**: Premium responsive black/dark agricultural UI with electric leaf green (`#00E676`), forest dark surfaces (`#111913`, `#18241C`), golden wheat highlights (`#F59E0B`), and crisp typography.
- Built strictly with **Jetpack Compose** and **Material Design 3 (M3)**.

---

## 🧑‍🌾 Pre-Configured Demo Accounts for Testing
- **Farmer**: `ramesh@farm.org` / `farmer123` (Ramesh Babu - SRI Paddy & Natural Farming)
- **Expert**: `ananya@agri.edu` / `expert123` (Prof. Ananya Rao - Entomology & IPM)
- **Admin**: `admin@agribook.org` / `admin123` (Dr. K. Swaminathan - Agronomy Director)
