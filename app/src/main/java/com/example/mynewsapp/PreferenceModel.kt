package com.example.mynewsapp

class PreferenceModel {
    var preferenceName: String? = null
    var type: String? = null

    constructor()

    constructor(preferenceName: String?, type: String?) {
        this.preferenceName = preferenceName
        this.type = type
    }
}