package com.example.neptune.model.track.src
import java.sql.Timestamp
import  java.util.Date
class Track (val id: Int,
             val name: String,
             val artists: List<String>,
             val genres: List<String>,
             val imageUrl: String,
             var timeStamp:Timestamp,
             var upvote: Int,
             var isUpvoted : Boolean) {
    fun addUpvote(){
        upvote += 1
    }
    fun removeUpvote(){
        if (upvote > 0) {
            upvote -= 1
        }
    }
}
