import React, { PureComponent } from "react";
import { ViewPropTypes, NativeModules, Platform } from "react-native";
import PropTypes from "prop-types";

const { RNVoiceRecorder } = NativeModules;

class VoiceRecorder extends PureComponent {
  static propTypes = {
    ...ViewPropTypes,

    path: PropTypes.string,
    format: PropTypes.string,

    onCancel: PropTypes.func,
    onDone: PropTypes.func
  };

  static defaultProps = {
    format: 'm4a'
  };

  static Record(props) {
    if (props.format === undefined) props.format = VoiceRecorder.defaultProps.format

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
    if (props.format === undefined) props.format = VoiceRecorder.defaultProps.format;

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
