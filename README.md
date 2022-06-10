# Epifi Exercise

A sample app which allows users to fill their PAN number and Date of Birth. This was built as the take home exercise for my interview with [EpiFi](https://fi.money/).

## Tech stack

The app currently uses an MVVM architecture where the ViewModel exposes two hot flows (uiState and uiEvent). These flows are consumed by our View (BankFragment) while it is active using the `flowWithLifecycle` apis. 

The libraries that this app is using as of now:

- [AppCompat](https://developer.android.com/jetpack/androidx/releases/appcompat) + [Fragments](https://developer.android.com/jetpack/androidx/releases/fragment) + [Material Components](https://material.io/develop/android) + [ViewPager2](https://developer.android.com/jetpack/androidx/releases/viewpager2) for UI
- [FlowBindings](https://github.com/ReactiveCircus/FlowBinding) to expose a reactive stream of events from android views
- [Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) for hot flows
- [JUnit4](https://junit.org/junit4/) for unit tests

The test coverage is _decent_, but not ideal. The app does not have a lot of moving parts, and the business logic end of things is tested properly.

Date of Birth is validated using a `DateValidator` instance. The `DateValidator` is tested with multiple scenarios with correct and incorrect dates.

PAN number is validated using a `PANValidator` instance. The `DateValidator` is tested with multiple scenarios with correct and incorrect PAN numbers.

## Things missing from tech stack

### Dependency Injection
Currently the app does not instantiate a lot of classes and hence does not require a full blown dependency injection solution (Dagger/Hilt) or even a service locator (Koin). 

### Navigation
The app only consists of one screen and hence does not require a navigation library (AndroidX Navigation).


### Persistence/Database
The app does not persist any user data so it does not require a database (Room/SQLDelight).


## Screenshots

### Light Theme

<img src="https://user-images.githubusercontent.com/10516866/173055954-382ac858-c2c5-412a-ab43-627442ff4e1d.jpeg" width="200px" />  <img src="https://user-images.githubusercontent.com/10516866/173055951-bd781d35-f4ca-4327-9626-78a55e69a927.jpeg" width="200px" /> <img src="https://user-images.githubusercontent.com/10516866/173055944-42b8e169-411f-4a41-87ec-9847dbbd57e3.jpeg" width="200px" />
  
<img src="https://user-images.githubusercontent.com/10516866/173056818-2e24a84b-bbce-4d1d-b7c2-dd07ce32ff93.jpeg" width="200px" /> <img src="https://user-images.githubusercontent.com/10516866/173056811-2ea7a6b0-1084-494b-b53e-8216d4d8fe9d.jpeg" width="200px" /> <img src="https://user-images.githubusercontent.com/10516866/173056803-c89e5bbb-68e0-43e6-bfb5-6d705afc2c24.jpeg" width="200px" />

## Download

You can download the debug build [here](https://0x0.st/oMUp.apk)

# License

```
Copyright (c) 2022 Aditya Wasan
Permission is hereby granted, free of charge, to any
person obtaining a copy of this software and associated
documentation files (the "Software"), to deal in the
Software without restriction, including without
limitation the rights to use, copy, modify, merge,
publish, distribute, sublicense, and/or sell copies of
the Software, and to permit persons to whom the Software
is furnished to do so, subject to the following
conditions:
The above copyright notice and this permission notice
shall be included in all copies or substantial portions
of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF
ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
DEALINGS IN THE SOFTWARE.
```
