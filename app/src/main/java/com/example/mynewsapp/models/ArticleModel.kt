package com.example.mynewsapp.models

/**
 * All the relevant information we display for an article.
 * @author Sebastian Gappa
 */
class ArticleModel {
    private var title: String? = null
    private var description: String? = null
    private var publisher: String? = null
    private var publishedDate: String? = null
    private var urlToImage: String? = null
    private var url: String? = null
    private var preferenceModel: PreferenceModel? = null

    fun articleTitle(): String {
        return this.title.toString()
    }

    fun publisher(): String {
        return this.publisher.toString()
    }

    fun publishedDate(): String {
        return this.publishedDate.toString()
    }

    fun articleImage(): String {
        return urlToImage.toString()
    }

    fun description(): String {
        return description.toString()
    }

    fun url(): String {
        return url.toString()
    }

    fun setTitle(articleTitle: String?) {
        this.title = articleTitle
    }

    fun setPublisher(publisher: String?) {
        this.publisher = publisher
    }

    fun setPublishedDate(publishedDate: String?) {
        this.publishedDate = publishedDate
    }

    fun setUrlToImage(urlToImage: String?) {
        this.urlToImage = urlToImage
    }

    fun setDescription(articleDescription: String?) {
        this.description = articleDescription
    }

    fun setUrl(url: String?) {
        this.url = url
    }

    fun setPreferenceModel(preferenceName: String, preferenceType: String) {
        this.preferenceModel = PreferenceModel(preferenceName, preferenceType)
    }

    fun getPreferenceModel(): PreferenceModel? {
        return this.preferenceModel
    }
}