package brandy.advancevideo.utils;

import android.content.Context;
import android.media.AudioManager;

/**
 * Android中提供AudioManager控制系统音量，它也是一个系统服务，
 */
public class VolumeControl {
    //调高音量，注意此时yDelta是负值
    public static void turnUp(Context context, float yDelta, int screenWidth) {
        //获取AduioManager
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //获取媒体音量最大值
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //获取媒体音量当前值
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //计算出变化的值
        int change = (int) (0.05 * maxVolume * yDelta / screenWidth);
        //计算并设置最终值
        int finalVolume = Math.min(maxVolume, currentVolume - change);
        //参数：②即设置的最终值，③是设置时的一个flag，系统提供了可以显示UI的flag
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, finalVolume, AudioManager.FLAG_SHOW_UI);
    }

    public static void turnDown(Context context, float yDelta, int screenWidth) {
        //获取AduioManager
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        //获取媒体最大值
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        //获取媒体当前值
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        //计算出变化的值
        int change = (int) (0.05 * maxVolume * yDelta / screenWidth);
        //计算并设置最终值
        int finalVolume = Math.max(0, currentVolume - change);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, finalVolume, AudioManager.FLAG_SHOW_UI);
    }
}
