
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
  Button
} from 'react-native';

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
    return <View style={styles.container}>
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
    </View>;
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  }
});