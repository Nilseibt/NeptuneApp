package com.example.neptune.model.track
import  java.util.Date
class Track (val id: Int,
             val name: String,
             val artists: List<String>,
             val genres: List<String>,
             val imageUrl: String,
             var timeStamp:Date,
             var upvote: Int,
             var isUpvoted : Boolean) {
    fun addUpvote(){
        upvote += 1
    }
    fun removeUpvote(){
        upvote -= 1
    }
}
