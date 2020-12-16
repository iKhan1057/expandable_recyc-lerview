package com.e.expandablerecyclerview;

import java.util.ArrayList;

public class DataModel {
    public enum STATE {
        CLOSED,
        OPENED
    }

    private int level = 1;
    STATE state = STATE.CLOSED;
    private String name = "";
    private String id = "";
    private boolean showradio = false;
    private ArrayList<DataModel> models = new ArrayList<>();
    private boolean selected = false;

    public DataModel() {
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isShowradio() {
        return showradio;
    }

    public void setShowradio(boolean showradio) {
        this.showradio = showradio;
    }

    public ArrayList<DataModel> getModels() {
        return models;
    }

    public void setModels(ArrayList<DataModel> models) {
        this.models = models;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
