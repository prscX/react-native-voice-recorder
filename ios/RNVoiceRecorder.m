
#import "RNVoiceRecorder.h"

@implementation RNVoiceRecorder

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()


RCTResponseSenderBlock _onDoneEditing = nil;
RCTResponseSenderBlock _onCancelEditing = nil;
IQAudioRecorderViewController *_recorder;

-(void)audioRecorderController:(nonnull IQAudioRecorderViewController*)controller didFinishWithAudioAtPath:(nonnull NSString*)filePath {
    if (_onDoneEditing == nil) return;
    
    // Save image.
    id<UIApplicationDelegate> app = [[UIApplication sharedApplication] delegate];
    [((UINavigationController*) app.window.rootViewController) dismissViewControllerAnimated:YES completion:nil];

    _onDoneEditing(@[filePath]);
}

-(void)audioRecorderControllerDidCancel:(nonnull IQAudioRecorderViewController*)controller {
    if (_onCancelEditing == nil) return;
    
    id<UIApplicationDelegate> app = [[UIApplication sharedApplication] delegate];
    [((UINavigationController*) app.window.rootViewController) dismissViewControllerAnimated:YES completion:nil];

    _onCancelEditing(@[]);
}

RCT_EXPORT_METHOD(Record:(nonnull NSDictionary *)props onDone:(RCTResponseSenderBlock)onDone onCancel:(RCTResponseSenderBlock)onCancel) {
    
    _onDoneEditing = onDone;
    _onCancelEditing = onCancel;
    
    _recorder = [[IQAudioRecorderViewController alloc] init];
    _recorder.delegate = self;
    _recorder.title = @"Recorder";
    _recorder.maximumRecordDuration = 10;
    _recorder.allowCropping = YES;
    _recorder.audioFormat = IQAudioFormat_wav;
    
    //    controller.barStyle = UIBarStyleDefault;
    //    controller.normalTintColor = [UIColor magentaColor];
    //    controller.highlightedTintColor = [UIColor orangeColor];

    [self presentBlurredAudioRecorderViewControllerAnimated:_recorder];
}


- (void)presentBlurredAudioRecorderViewControllerAnimated:(nonnull IQAudioRecorderViewController *)audioRecorderViewController
{
    UINavigationController *navigationController = [[UINavigationController alloc] initWithRootViewController:audioRecorderViewController];
    
    navigationController.toolbarHidden = NO;
    navigationController.toolbar.translucent = YES;
    [navigationController.toolbar setBackgroundImage:[UIImage new] forToolbarPosition:UIBarPositionAny barMetrics:UIBarMetricsDefault];
    [navigationController.toolbar setShadowImage:[UIImage new] forToolbarPosition:UIBarPositionAny];
    
    navigationController.navigationBar.translucent = YES;
    [navigationController.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
    [navigationController.navigationBar setShadowImage:[UIImage new]];
    
    navigationController.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    navigationController.modalTransitionStyle = UIModalTransitionStyleCrossDissolve;
    
    audioRecorderViewController.barStyle = audioRecorderViewController.barStyle;        //This line is used to refresh UI of Audio Recorder View Controller

    id<UIApplicationDelegate> app = [[UIApplication sharedApplication] delegate];
    [((UINavigationController*) app.window.rootViewController) presentViewController:navigationController animated:YES completion:nil];

}


@end
  
