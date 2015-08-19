package network;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by hyewon on 2015-06-26.
 */
class PostJsonRequest{
    final String content;
    ArrayList<String> filelist = new ArrayList<String>();

    PostJsonRequest(String content, ArrayList<String> filelist){
        this.content = content;
        this.filelist = filelist;
        Log.i("file", filelist.toString());
    }

    PostJsonRequest(String content){
        this.content = content;

    }
}
