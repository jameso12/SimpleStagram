package com.example.simpleinsta

import com.parse.ParseClassName
import com.parse.ParseFile
import com.parse.ParseObject
import com.parse.ParseUser

/*
* user: pointer
* image: file
* description: string
* */
@ParseClassName("Post")
class Post: ParseObject() {

    fun get_user(): ParseUser?{
        return getParseUser(KEY_USER)
    }

    fun set_user(usr: ParseUser){
        put(KEY_USER, usr)
    }

    fun get_image(): ParseFile?{
        return getParseFile(KEY_IMAGE)
    }

    fun set_image(img: ParseFile){
        put(KEY_IMAGE, img)
    }

    fun get_description(): String?{
        return getString(KEY_DESCRIPTION)
    }

    fun set_description(description: String){
        put(KEY_DESCRIPTION, description)
    }

    companion object{
        const val KEY_IMAGE = "image"
        const val KEY_USER = "user"
        const val KEY_DESCRIPTION = "description"
    }
}