#if __has_include("RCTBridgeModule.h")
#import "RCTBridgeModule.h"
#else
#import <React/RCTBridgeModule.h>
#endif

#import <UIKit/UIKit.h>
#import <IQAudioRecorderController/IQAudioRecorderViewController.h>
#import <IQAudioRecorderController/IQAudioRecorderConstants.h>
#import <IQAudioRecorderController/IQAudioCropperViewController.h>

@interface RNVoiceRecorder : NSObject <RCTBridgeModule>

@end
  
