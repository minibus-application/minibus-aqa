# **minibus-aqa**

Java 9 + TestNG + Maven + Appium
(+ custom webelement decorator)

Basic sample project for automated testing of android [minibus-app](https://github.com/n3gbx/minibus-app) and [minibus-service](https://github.com/n3gbx/minibus-service) REST service

TestNG Run configuration (emulator):

```
-ea
-Demulated=true
-Denv=stage
-Davd.name=<PASTE_HERE>
-Dapp.path=app-stage.apk
```
