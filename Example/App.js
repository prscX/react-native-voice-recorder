
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

        setTimeout(() => {
          RNVoiceRecorder.Play({
            path: path,
            format: 'wav',
            onDone: (play) => {
              console.log('play done: ' + path)
            },
            onCancel: () => {
              console.log('play cancelled')
            }
          });
        }, 1000)
      },
      onCancel: () => {
        console.log('on cancel')
      }
    });
  }

  _onPlay() {
    RNVoiceRecorder.Play({
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