## 🏏 Cricket Score App

An Android application that provides **live cricket scores, upcoming match schedules, and completed match results** using a public Cricket API.
Built with **Java, Retrofit, RecyclerView, and Fragments**, this app demonstrates real-time API integration and modern Android UI design.

---

## 📱 Features

* 🔴 Live match scores
* 🟡 Upcoming match schedules
* 🟢 Completed match results
* 🔄 Refresh button for real-time updates
* 📲 Bottom navigation for easy switching
* 🚀 Splash screen on app launch

---

## ⚙️ How the App Works

1. When the app starts, a **Splash Screen** is displayed for 2 seconds.
2. The user is then redirected to the **Main Screen**.
3. The main screen contains **Bottom Navigation** with three tabs:

    * **Live**
    * **Upcoming**
    * **Completed**
4. Each tab loads a **Fragment**.
5. When a fragment loads:

    * It sends a request to the **Cricket API** using **Retrofit**.
    * The API returns match data in **JSON format**.
    * The data is parsed and filtered.
    * The results are displayed in a **RecyclerView**.
6. The **Refresh button** allows the user to reload the latest match data anytime.

---

## 🔄 Data Flow

```
Cricket API → Retrofit → ApiService → Fragment → RecyclerView → UI
```

---

## 🛠 Technologies Used

* Java
* Retrofit
* RecyclerView
* Fragments
* Android Jetpack
* REST API (Cricket API)

---

## 🧩 Project Structure

* **Activities**

    * SplashActivity
    * MainActivity

* **Fragments**

    * LiveFragment
    * UpcomingFragment
    * CompletedFragment

* **Adapter**

    * MatchAdapter

* **Model**

    * MatchModel

* **API**

    * ApiService
    * ApiClient
    * Constants

---

## ▶ How to Run the App

1. Clone or download the project
2. Open it in **Android Studio**
3. Make sure your **API key** is added inside:

   ```
   Constants.java
   ```
4. Click **Run ▶**
5. Choose an emulator or physical Android device
6. The app will launch with the splash screen and load cricket match data

---

## 🚀 Future Enhancements

* Detailed scorecards
* Player statistics
* Match alerts
* Push notifications
* Dark mode

---

## 👨‍💻 Developed By

**AK – J T Akshay Kanna**
MCA | Android Developer

---
