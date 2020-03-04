
#import "RNVoiceRecorder.h"

@implementation RNVoiceRecorder

- (dispatch_queue_t)methodQueue
{
    return dispatch_get_main_queue();
}
RCT_EXPORT_MODULE()


RCTResponseSenderBlock _onDoneVoiceEditing = nil;
RCTResponseSenderBlock _onCancelVoiceEditing = nil;
IQAudioRecorderViewController *_recorder;
IQAudioCropperViewController *_player;

-(void)audioRecorderController:(nonnull IQAudioRecorderViewController*)controller didFinishWithAudioAtPath:(nonnull NSString*)filePath {
    if (_onDoneVoiceEditing == nil) return;
    
    // Save image.
    id<UIApplicationDelegate> app = [[UIApplication sharedApplication] delegate];
    [((UINavigationController*) app.window.rootViewController) dismissViewControllerAnimated:YES completion:nil];

    _onDoneVoiceEditing(@[filePath]);
}

-(void)audioRecorderControllerDidCancel:(nonnull IQAudioRecorderViewController*)controller {
    if (_onCancelVoiceEditing == nil) return;
    
    id<UIApplicationDelegate> app = [[UIApplication sharedApplication] delegate];
    [((UINavigationController*) app.window.rootViewController) dismissViewControllerAnimated:YES completion:nil];

    _onCancelVoiceEditing(@[]);
}

RCT_EXPORT_METHOD(Record:(nonnull NSDictionary *)props onDone:(RCTResponseSenderBlock)onDone onCancel:(RCTResponseSenderBlock)onCancel) {
    
    _onDoneVoiceEditing = onDone;
    _onCancelVoiceEditing = onCancel;

    NSString *format = [props objectForKey: @"format"];
    
    _recorder = [[IQAudioRecorderViewController alloc] init];
    _recorder.delegate = self;
    _recorder.title = @"Recorder";
    // _recorder.maximumRecordDuration = 10;
    _recorder.allowCropping = YES;
    
    //    controller.barStyle = UIBarStyleDefault;
    //    controller.normalTintColor = [UIColor magentaColor];
    //    controller.highlightedTintColor = [UIColor orangeColor];

    if ([format isEqualToString: @"wav"]) {
        _recorder.audioFormat = IQAudioFormat_wav;
    }
    
    [self presentBlurredAudioRecorderViewControllerAnimated:_recorder];
}


RCT_EXPORT_METHOD(Play:(nonnull NSDictionary *)props onDone:(RCTResponseSenderBlock)onDone onCancel:(RCTResponseSenderBlock)onCancel) {
    
    _onDoneVoiceEditing = onDone;
    _onCancelVoiceEditing = onCancel;
    
    NSString *path = [props objectForKey: @"path"];
    NSString *format = [props objectForKey: @"format"];
    
    _player = [[IQAudioCropperViewController alloc] initWithFilePath:path];
    _player.delegate = self;
    _player.title = @"Recorder";
    
    //    controller.barStyle = UIBarStyleDefault;
    //    controller.normalTintColor = [UIColor magentaColor];
    //    controller.highlightedTintColor = [UIColor orangeColor];
    
    if ([format isEqualToString: @"wav"]) {
        _recorder.audioFormat = IQAudioFormat_wav;
    }
    
    [self presentBlurredAudioCropperViewControllerAnimated:_recorder];
}


- (void)presentBlurredAudioCropperViewControllerAnimated:(nonnull IQAudioCropperViewController *)audioCropperViewController
{
    UINavigationController *navigationController = [[UINavigationController alloc] initWithRootViewController:audioCropperViewController];
    
    navigationController.toolbarHidden = NO;
    navigationController.toolbar.translucent = YES;
    [navigationController.toolbar setBackgroundImage:[UIImage new] forToolbarPosition:UIBarPositionAny barMetrics:UIBarMetricsDefault];
    [navigationController.toolbar setShadowImage:[UIImage new] forToolbarPosition:UIBarPositionAny];
    
    navigationController.navigationBar.translucent = YES;
    [navigationController.navigationBar setBackgroundImage:[UIImage new] forBarMetrics:UIBarMetricsDefault];
    [navigationController.navigationBar setShadowImage:[UIImage new]];
    
    navigationController.modalPresentationStyle = UIModalPresentationOverCurrentContext;
    navigationController.modalTransitionStyle = UIModalTransitionStyleCrossDissolve;
    
    audioCropperViewController.barStyle = audioCropperViewController.barStyle;        //This line is used to refresh UI of Audio Recorder View Controller


    id<UIApplicationDelegate> app = [[UIApplication sharedApplication] delegate];
    [((UINavigationController*) app.window.rootViewController) presentViewController:navigationController animated:YES completion:nil];
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
  
