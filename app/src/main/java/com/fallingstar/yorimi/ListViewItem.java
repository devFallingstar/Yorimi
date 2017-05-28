package com.fallingstar.yorimi;

import java.util.HashMap;

/**
 * Created by Jiran on 2017-04-27.
 */

public class ListViewItem {
    private String buttonStr ;
    private String titleStr ;
    private String descStr ;

    public void setButtonStr(String str) {
        buttonStr = str ;
    }
    public void setTitle(String title) {
        titleStr = title ;
    }
    public void setDesc(String desc) {
        descStr = desc ;
    }

    public String getButtonStr() {
        return this.buttonStr ;
    }
    public String getTitle() {
        return this.titleStr ;
    }
    public String getDesc() {
        return this.descStr ;
    }

}
