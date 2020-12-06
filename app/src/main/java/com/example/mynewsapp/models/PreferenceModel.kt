package com.example.mynewsapp.models

class PreferenceModel {
    var preferenceName: String? = null
    var type: String? = null

    constructor(preferenceName: String?, type: String?) {
        this.preferenceName = preferenceName
        this.type = type
    }
}