package com.huizhi.manage.data;

import android.os.Environment;

/**
 * Created by CL on 2017/12/13.
 */

public class Constants {

    public static final String PACKAG_ENAME = "com.huizhi.manage";

    public static final int RESULT_SUCCESS = 1;
    public static final int MSG_SUCCESS = 200;
    public static final int MSG_SUCCESS_ONE = 201;
    public static final int MSG_SUCCESS_TWO = 202;
    public static final int MSG_SUCCESS_THREE = 203;
    public static final int MSG_SUCCESS_FOUR = 204;
    public static final int MSG_FAILURE = 300;
    public static final int MSG_RECEIVE = 1000;

    public static final int REQUEST_CODE = 1000;
    public static final int RESULT_CODE = 1100;

    public static final int TAKE_PICTURE = 800;
    public static final int SELECT_PICTURE = 801;
    public static final int SELECT_FILE = 802;
    public static final int CROP_SMALL_PICTURE = 803;

    public static final String TAKE_PICTURE_PATH = "/filename.jpg";

    private static final String Path = Environment.getExternalStorageDirectory().toString() + "/HuiZhi/";       //Environment.getExternalStorageDirectory()   /mnt/sdcard/HuiZhi/
    public static final String PATH_PIC = Path + "system/.Picture/";
    public static final String PATH_VIDEO = Path + "system/.Video/";
    public static final String PATH_WORD = Path + "system/.Word/";
    public static final String PATH_DOWNLOAD = Environment.getExternalStorageDirectory().toString() + "/Download/";     ///mnt/sdcard/Download/

    public static String DOWNLOAD_URL = "";
}
