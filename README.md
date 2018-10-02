<h1 align="center">

<p align="center">
  <img src="./assets/hero.png"/>
</p>

<p align="center">
  <a href="https://www.npmjs.com/package/react-native-voice-recorder"><img src="http://img.shields.io/npm/v/react-native-voice-recorder.svg?style=flat" /></a>
  <a href="https://github.com/prscX/react-native-voice-recorder/pulls"><img alt="PRs Welcome" src="https://img.shields.io/badge/PRs-welcome-brightgreen.svg" /></a>
  <a href="https://github.com/prscX/react-native-voice-recorder#License"><img src="https://img.shields.io/npm/l/react-native-voice-recorder.svg?style=flat" /></a>
</p>


    ReactNative: Native Voice Recorder (Android/iOS)

If this project has helped you out, please support us with a star 🌟
</h1>
This library is a React Native bridge around native voice recorder libraries. It allows you to record and play voice.


#### iOS

| **[hackiftekhar/IQAudioRecorderController](https://github.com/hackiftekhar/IQAudioRecorderController)**             |
| ----------------- |
| <img src="https://github.com/sagiwei/SGActionView/raw/master/sheet.png" width="437" />                  |


#### Android

| **[adrielcafe/AndroidAudioRecorder](https://github.com/adrielcafe/AndroidAudioRecorder)**             |
| ----------------- |
| <img src="https://raw.githubusercontent.com/adrielcafe/AndroidAudioRecorder/master/demo.gif" />                  |


## 📖 Getting started

`$ npm install react-native-voice-recorder --save`

`$ react-native link react-native-voice-recorder`


* Android
  * Please add below script in your build.gradle

```
buildscript {
    repositories {
        jcenter()
        maven { url "https://maven.google.com" }
        maven { url "https://jitpack.io" }
        ...
    }
}

allprojects {
    repositories {
        mavenLocal()
        jcenter()
        maven { url "https://maven.google.com" }
        maven { url "https://jitpack.io" }
        ...
    }
}
```

  * Please add below user permission to your app `AndroidManifest`:

```
    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
```

> **Note:** Android SDK 27 > is supported

* iOS
    > **iOS Prerequisite:** Please make sure CocoaPods is installed on your system

  * After `react-native link react-native-voice-recorder`, please verify `node_modules/react-native-voice-recorder/ios/` contains `Pods` folder. If does not exist please execute `pod install` command on `node_modules/react-native-voice-recorder/ios/`, if any error => try `pod repo update` then `pod install`
  * After verification, open your project and create a folder 'RNVoiceRecorder' under Libraries.
  * Drag `node_modules/react-native-voice-recorder/ios/pods/Pods.xcodeproject` into RNVoiceRecorder, as well as the RNVoiceRecorder.xcodeproject if it does not exist.
  * Add the `IQAudioRecorderController.framework` & `SCSiriWaveformView.framework` into your project's `Embedded Binaries` and make sure the framework is also in linked libraries.
  * Go to your project's `Build Settings -> Frameworks Search Path` and add `${BUILT_PRODUCTS_DIR}/IQAudioRecorderController` & `${BUILT_PRODUCTS_DIR}/SCSiriWaveformView` non-recursive.

  * Now build your iOS app through Xcode

