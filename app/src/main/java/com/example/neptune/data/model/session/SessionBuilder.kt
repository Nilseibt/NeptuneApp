package com.example.neptune.data.model.session

class SessionBuilder {

    private var sessionType: SessionType? = null


    fun setSessionType(sessionType: SessionType) {
        this.sessionType = sessionType
    }

    fun getSessionType(): SessionType {
        if(sessionType == null){
            Exception("Session type must be set before getter is called")
        }
        return sessionType!!
    }


}