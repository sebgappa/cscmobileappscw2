package com.example.mynewsapp

class ArticleCardModel
{
    private var articleTitle: String? = null
    private var articleDescription: String? = null
    private var publisher: String? = null
    private var publishedDate: String? = null
    private var articleImagePointer = 0
    private var publisherImagePointer = 0


    fun getArticleTitle(): String {
        return this.articleTitle.toString()
    }

    fun getPublisher(): String {
        return this.publisher.toString()
    }

    fun getPublishedDate(): String {
        return this.publishedDate.toString()
    }

    fun getArticleImage(): Int {
        return articleImagePointer
    }

    fun getPublisherImage(): Int {
        return publisherImagePointer
    }

    fun getArticleDescription(): String {
        return articleDescription.toString()
    }

    fun setArticleTitle(articleTitle: String) {
        this.articleTitle = articleTitle
    }

    fun setPublisher(publisher: String) {
        this.publisher = publisher
    }

    fun setPublishedDate(publishedDate: String) {
        this.publishedDate = publishedDate
    }

    fun setArticleImage(imagePointer: Int) {
        this.articleImagePointer = imagePointer
    }

    fun setPublisherImage(imagePointer: Int) {
        this.publisherImagePointer = imagePointer
    }

    fun setArticleDescription(articleDescription: String) {
        this.articleDescription = articleDescription
    }
}