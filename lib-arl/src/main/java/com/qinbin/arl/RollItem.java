package com.qinbin.arl;

/**
 * Created by teacher on 2016/3/28.
 */
public class RollItem implements IRollItem {
    String rollItemTitle;
    String rollItemImageUrl;

    public RollItem(String rollItemTitle, String rollItemImageUrl) {
        this.rollItemTitle = rollItemTitle;
        this.rollItemImageUrl = rollItemImageUrl;
    }

    @Override
    public String getRollItemTitle() {
        return rollItemTitle;
    }

    @Override
    public String getRollItemImageUrl() {
        return rollItemImageUrl;
    }
}
