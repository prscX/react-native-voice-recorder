import React, { PureComponent } from "react";
import { ViewPropTypes, NativeModules, Platform } from "react-native";
import PropTypes from "prop-types";

const { RNVoiceRecorder } = NativeModules;

class VoiceRecorder extends PureComponent {
  static propTypes = {
    ...ViewPropTypes,

    onCancel: PropTypes.func,
    onDone: PropTypes.func
  };

  static defaultProps = {};

  static Record(props) {
    RNVoiceRecorder.Record(
      props,
      (path) => {
        props.onDone && props.onDone(path);
      },
      () => {
        props.onCancel && props.onCancel();
      }
    );
  }

  static Play(props) {
    RNVoiceRecorder.Play(
      props,
      () => {
        props.onDone && props.onDone();
      },
      () => {
        props.onCancel && props.onCancel();
      }
    );
  }
}

export { VoiceRecorder as RNVoiceRecorder };
