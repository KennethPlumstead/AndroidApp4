# SuperPodcast  
Android Development – Assignment 7

SuperPodcast is a simple podcast browsing app built with Jetpack Compose.  
The goal of this project was to practice working with APIs, XML/RSS parsing, media playback, local storage, and modern Android app architecture.

---

## What the app does

- Search for podcasts using the iTunes Search API  
- View podcast details and episode lists (parsed from RSS feeds)  
- Play podcast episodes using ExoPlayer (Media3)  
- Subscribe and unsubscribe to podcasts  
- View saved subscriptions from local storage  
- Navigate between screens using Jetpack Navigation  

---

## Architecture and Technologies

- Language: Kotlin  
- UI: Jetpack Compose (Material 3)  
- Architecture: MVVM (ViewModels and Repository)  
- Networking: Retrofit (REST API), OkHttp  
- RSS Parsing: XML parsing  
- Media Playback: Media3 / ExoPlayer  
- Local Storage: Room (SQLite)  
- Navigation: Jetpack Navigation Compose  

---

## App Screens

- Search Screen  
  Allows users to search for podcasts by keyword.

- Podcast Detail Screen  
  Displays podcast artwork, subscription controls, and a list of episodes.

- Player Screen  
  Plays and pauses podcast episodes with proper playback state handling.

- Subscriptions Screen  
  Displays all podcasts saved locally by the user.

---

## Subscriptions

When a user taps Subscribe, the podcast is saved locally using Room.  
Subscribed podcasts can be viewed later on the Subscriptions screen, even after restarting the app.

---

## How to run the app

1. Clone this repository  
2. Open the project in Android Studio  
3. Allow Gradle to finish syncing  
4. Run the app on an emulator or physical device (API level 24 or higher)

---

## Assignment Notes

This project was created as part of an Android Development course assignment.  
The focus was on functionality, architecture, and demonstrating understanding of core Android concepts.

---

## Author

Kenneth Plumstead  
Mobile Web Development Student
