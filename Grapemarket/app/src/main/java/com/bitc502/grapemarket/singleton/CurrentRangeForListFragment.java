package com.bitc502.grapemarket.singleton;

public class CurrentRangeForListFragment {
    private String currentRange;
    private Integer currentRangeInteger;
    private Boolean isSaveAction;

    private CurrentRangeForListFragment() {
        currentRange="5km ▼";
        currentRangeInteger = 5;
        isSaveAction = false;
    }

    private static CurrentRangeForListFragment instance = new CurrentRangeForListFragment();

    public static CurrentRangeForListFragment getInstance() {
        return instance;
    }

    public void setCurrentRange(String range) {
        this.currentRange = range;
    }
    public String getCurrentRange(){
        return this.currentRange;
    }

    public void setCurrentRangeInteger(Integer range){
        this.currentRangeInteger = range;
    }

    public Integer getCurrentRangeInteger(){
        return this.currentRangeInteger;
    }

    public void setIsSaveAction(Boolean aBoolean){
        this.isSaveAction = aBoolean;
    }

    public Boolean getIsSaveAction(){
        return this.isSaveAction;
    }
}
