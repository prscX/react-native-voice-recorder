
/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View,
  Button,
  ImageBackground
} from "react-native";

import { RNVoiceRecorder } from 'react-native-voice-recorder'
let recordingPath;

export default class App extends Component<Props> {
  constructor(props) {
    super(props)

    this.state = {
      visible: false
    }
  }

  _onRecord() {
    RNVoiceRecorder.Record({
      format: 'wav',
      onDone: (path) => {
        console.log('record done: ' + path)

        recordingPath = path;
      },
      onCancel: () => {
        console.log('on cancel')
      }
    });
  }

  _onPlay() {
    RNVoiceRecorder.Play({
      path: recordingPath,
      format: "wav",
      onDone: path => {
        console.log("play done: " + path);
      },
      onCancel: () => {
        console.log("play cancelled");
      }
    });
  }

  render() {
    return <ImageBackground source={require("./assets/background.jpg")} style={styles.backgroundImage}>
      <Button onPress={() => {
        this._onRecord()

        // this.setState({ visible: true });
      }} title={'Record'}>
      </Button>
      <Button onPress={() => {
        this._onPlay()

        // this.setState({ visible: true });
      }} title={'Play'}>
      </Button>
    </ImageBackground>;
  }
}

const styles = StyleSheet.create({
  backgroundImage: {
    flex: 1,
    width: null,
    height: null,
    flexDirection: "column",
    justifyContent: "center",
    alignItems: "center"
  },
  container: {
    flex: 1,
    justifyContent: "center",
    backgroundColor: "#F5FCFF"
  }
});