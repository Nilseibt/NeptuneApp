package com.example.neptune.data.model.track.src

open class TrackList (val tracks: MutableList<Track>){
    constructor():this(ArrayList<Track>()){}

    fun removeTrack(index: Int){
        tracks.removeAt(index)
    }
    fun removeTrack(track: Track){
        for (index in tracks.indices){
            if(tracks[index].id == track.id){
                tracks.removeAt(index)
                break
            }
        }
    }
    fun addTrack(track: Track){
        tracks.add(track)
    }
    fun containsTrack(track: Track): Boolean{
        for (item in tracks){
            if (item.id == track.id){
                return true
            }
        }
        return false
    }
    fun popFirstTrack(): Track {
        var output  = tracks.first()
        tracks.removeAt(0);
        return output;
    }
    fun isEmpty(): Boolean{
        return tracks.isEmpty()
    }

    fun clear(){
        tracks.clear()
    }
    fun trackAt(index: Int):Track{
       return tracks[index]
    }

    override fun toString(): String {
        val output= StringBuilder()

        output.append("length: ${tracks.size}\n")
        for(track in tracks){
            output.append(track.toString())
            output.append("\n")
        }
        output.append("\n\n")
        return output.toString()
    }

}