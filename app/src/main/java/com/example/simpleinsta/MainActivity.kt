package com.example.simpleinsta

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import com.parse.*
import java.io.File

private const val TAG = "MainActivity"
class MainActivity : AppCompatActivity() {
    val CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034
    val photoFileName = "photo.jpg"
    var photoFile: File? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var submitButton = findViewById<Button>(R.id.bt_submit)
        var takePictureButton = findViewById<Button>(R.id.bt_takePicture)
        var ed_description = findViewById<TextView>(R.id.ed_description)
        var iv_picture = findViewById<ImageView>(R.id.imageView)
        takePictureButton.setOnClickListener{
            onLaunchCamera()
        }

        submitButton.setOnClickListener{
            var postDesc = ed_description.text.toString()
            var user = ParseUser.getCurrentUser()
            if(photoFile!=null){
                submitPost(postDesc,user, photoFile!!)
                ed_description.text = ""
                iv_picture.setImageBitmap(null)
            } else {
                Log.i(TAG, "Image is null!")
                Toast.makeText(this, "Take Picture!", Toast.LENGTH_SHORT).show()
            }

        }


        queryPosts()
    }

    fun onLaunchCamera() {
        // create Intent to take a picture and return control to the calling application
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName)

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        if (photoFile != null) {
            val fileProvider: Uri =
                FileProvider.getUriForFile(this, "com.codepath.fileprovider", photoFile!!)
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.

            // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
            // So as long as the result is not null, it's safe to use the intent.
            if (intent.resolveActivity(packageManager) != null) {
                // Start the image capture intent to take photo
                startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE)
            }
        }
    }
    // Returns the File for a photo stored on disk given the fileName
    fun getPhotoFileUri(fileName: String): File {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        val mediaStorageDir =
            File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG)

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "failed to create directory")
        }

        // Return the file target for the photo based on filename
        return File(mediaStorageDir.path + File.separator + fileName)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // by this point we have the camera photo on disk
                val takenImage = BitmapFactory.decodeFile(photoFile!!.absolutePath)
                // RESIZE BITMAP, see section below
                // Load the taken image into a preview
                val ivPreview: ImageView = findViewById(R.id.imageView)
                ivPreview.setImageBitmap(takenImage)
            } else { // Result was a failure
                Toast.makeText(this, "Picture wasn't taken!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun submitPost(postDesc: String, usr: ParseUser, img: File ){ // Submits to the Parse server
        var post = Post()
        post.set_description(postDesc)
        post.set_user(usr)
        post.set_image(ParseFile(img))
        post.saveInBackground{ exception ->
            if(exception!=null){
                Toast.makeText(this, "Submit Failed!", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "Submit post failed!")
                exception.printStackTrace()
            } else {
                Toast.makeText(this, "Submit Successful!", Toast.LENGTH_SHORT).show()
                Log.i(TAG, "Submit post succeeded!")
            }
        }
    }

    // Query for all posts
    fun queryPosts(){
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        query.include(Post.KEY_USER) // will also get the user in this query
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if (e!=null){ // Failure
                    Log.e(TAG, "Getting Post query failed!")
                } else {
                    if(posts!=null) {
                        for (post in posts){
                            Log.i(TAG, "User: ${post.get_user()?.username} \n Post: " + post.get_description())
                        }
                    }
                }
            }

        })
    }
}