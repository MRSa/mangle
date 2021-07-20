package jp.osdn.gokigen.gokigenassets.camera.interfaces


/**
 *
 */
interface ICameraStatus
{
    fun getStatusList(key: String): List<String?>
    fun getStatus(key: String): String
    fun setStatus(key: String, value: String)

    companion object
    {
        var TAKE_MODE = "exposureMode"     // プログラムモード(P/A/S/M)
        var SHUTTER_SPEED = "tv"           // シャッタースピード
        var APERTURE = "av"                // 絞り値
        var EXPREV = "xv"                  // 露出補正値
        var CAPTURE_MODE = "captureMode"   // キャプチャーモード
        var ISO_SENSITIVITY = "sv"         // ISO感度
        var WHITE_BALANCE = "WBMode"       // ホワイトバランス
        var AE = "meteringMode"            // 測光モード

        var EFFECT = "effect"              // ピクチャーエフェクトモード
        var BATTERY = "battery"            // バッテリ残量


        var STATE = "state"
        var FOCUS_MODE = "focusMode"
        var AF_MODE = "AFMode"

        var RESOLUTION = "reso"
        var DRIVE_MODE = "shootMode"

        var AE_STATUS_MULTI = "multi"
        var AE_STATUS_ESP = "ESP"
        var AE_STATUS_SPOT = "spot"
        var AE_STATUS_PINPOINT = "Spot"
        var AE_STATUS_CENTER = "center"
        var AE_STATUS_CENTER2 = "Ctr-Weighted"

        var IMAGESIZE = "stillSize"
        var MOVIESIZE = "movieSize"

        var FLASH_XV = "flashxv"
        var SELF_TIMER = "selftimer"

        var TAKE_MODE_MOVIE = "movie"
    }
}
