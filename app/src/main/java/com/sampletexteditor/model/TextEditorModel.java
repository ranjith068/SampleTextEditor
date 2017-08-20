package com.sampletexteditor.model;

/**
 * Created by rajesh on 19/8/17.
 */

public class TextEditorModel {
    int _id;
    String _texts;

    public TextEditorModel() {

    }

    public TextEditorModel(String _texts) {
        this._texts = _texts;
    }

    public TextEditorModel(int _id, String _texts) {
        this._id = _id;
        this._texts = _texts;
    }

    public String get_texts() {
        return _texts;
    }

    public void set_texts(String _texts) {
        this._texts = _texts;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }
}
