package com.example.admincalmable;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.admincalmable.Model.UploadSong;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        AdapterView.OnItemClickListener {

    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";

    TextView textViewImage;
    ProgressBar progressBar;
    Uri audioUri,imageUri;
    StorageReference mStorageref;
    StorageTask mUploadsTask;
    DatabaseReference referenceSongs;
    ImageView editSong;
    String songsCategory;
    Uri imageUrl;
    MediaMetadataRetriever metadataRetriever;
    FirebaseDatabase database;
    public static String selected_name;
    private String url;
    byte []art;
    String title1,artist1,album_art1 = "",duration1;
    TextView title,artist,durations,album,dataa;
    ImageView songimage;
    Button uploadBtn,showAllBtn;
    EditText Enterid;
    public static EditText EnterName;
    AppCompatButton buttonNotify;
//    private DatabaseReference root = FirebaseDatabase.getInstance().getReference().child("Songs");
//    private StorageReference reference = FirebaseStorage.getInstance().getReference().child("Songs");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewImage=findViewById(R.id.textViewSongsFilesSelected);
        progressBar=findViewById(R.id.progressbar);
        title=findViewById(R.id.title);
//        uploadBtn=findViewById(R.gameId.openAImageFiles);
        Enterid=findViewById(R.id.enterid);
        songimage = findViewById(R.id.songimage);
        EnterName=findViewById(R.id.entername);
        editSong=findViewById(R.id.editSgs);
        buttonNotify=findViewById(R.id.buttonNotify);
        mRequestQue= Volley.newRequestQueue(this);
        FirebaseMessaging.getInstance().subscribeToTopic("news");
//        artist=findViewById(R.gameId.artist);
//        durations=findViewById(R.gameId.duration);
//        album=findViewById(R.gameId.album);
//        dataa=findViewById(R.gameId.dataa);
//        album_art=findViewById(R.gameId.imageview);

        metadataRetriever = new MediaMetadataRetriever();
        referenceSongs = FirebaseDatabase.getInstance().getReference().child("Songs_Admin");

        mStorageref= FirebaseStorage.getInstance().getReference().child("Songs_Admin");

        //initialize the spinner
        Spinner spinner = findViewById(R.id.spinner1);

        spinner.setOnItemSelectedListener(this);

        List<String> categories = new ArrayList<>();
        ArrayList<String> songUrl = new ArrayList<>();

//        categories.add("Deep Relax");
//        categories.add("Nature Sound");
//        categories.add("Chill Out");
//        categories.add("Healing Music");
//        categories.add("Sound Scape");
//        categories.add("Sleep Stories");
//        categories.add("Instrumental Music");
//        categories.add("Love Music");
//        categories.add("Self Improvement");
//        categories.add("Meditate Music");
//        categories.add("Creativity");

        //get music categories from firebase
        database = FirebaseDatabase.getInstance();
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,categories);
        DatabaseReference reference = database.getReference("Songs_Admin");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categories.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    categories.add(snapshot1.getKey().toString());
                    songUrl.add(snapshot1.getValue().toString());

                }
                Log.i("TAG", categories + "");
                Log.i("TAG", songUrl + "");

                spinner.setAdapter(dataAdapter);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_name = adapterView.getItemAtPosition(i).toString();
                Log.d("selected name-----",selected_name);
                url = songUrl.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(dataAdapter);


        songimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");

                ImageResultLauncher.launch(galleryIntent);
            }
        });
        editSong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(getApplicationContext(), DisplayUploadedSongs.class));
                Intent myIntent = new Intent(MainActivity.this, DisplayUploadedSongs.class);
                myIntent.putExtra("selected_name", selected_name);
                startActivity(myIntent);
            }
        });

        buttonNotify.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sendNotification();
            }
        });

//        uploadBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (imageUrim != null){
//                    uploadToFirebase(imageUrim);
//
//
//                }else{
//                    Toast.makeText(MainActivity.this,"Please Select Image",Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });

    }

    private void sendNotification() {
        JSONObject mainobj = new JSONObject();
        try {
            mainobj.put("to","/topics/"+"news");
            JSONObject notificatioinObj = new JSONObject();
            notificatioinObj.put("title","Music");
            notificatioinObj.put("body","New music track was added!!");
            mainobj.put("notification",notificatioinObj);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
                    mainobj,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    },new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {

                }


            }

            ){
                @Override
                public Map<String,String> getHeaders()throws AuthFailureError {
                    Map<String,String> header = new HashMap<>();
                    header.put("content-type","application/json");
                    header.put("authorization", "key=AAAA0i3WXaQ:APA91bEO4QTUoHjZi1mLIRERhYmaE_ZG-_I1hI0bwjtG22s25NMJVPkNh52HZUTQvwT_Fx4bjDP-1tXqX9GZIZKRfWJdvFe49S1M54EATyU-GJxeKqU7FLcy_a_SLVW4hPA_atsTe9q7");
                    return header;
                }
            };
            mRequestQue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }





    private String getFileExtention(Uri muri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(muri));
    }




    ActivityResultLauncher<Intent> ImageResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();

                        imageUri = data.getData();
//
                        songimage.setImageURI(imageUri);


                    }
                }
            });

    //get selected item from spinner
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        songsCategory = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(this,"Selected "+songsCategory, Toast.LENGTH_SHORT).show();



    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
                        audioUri = data.getData();
                        // metadataRetrieverm.setDataSource(this,audioUrim);
                        String fileNames = getFileName(audioUri);
                        textViewImage.setText(fileNames);


//                        titlem.setText(metadataRetrieverm.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
//                        title1=metadataRetrieverm.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);

                    }
                }
            });

    public void openAudioFiles(View v) {
        Intent i = new Intent (Intent.ACTION_GET_CONTENT);
        i.setType("audio/*");
        someActivityResultLauncher.launch(i);
    }

//    public void openAudioFilesm(View v){
//        Intent i = new Intent (Intent.ACTION_GET_CONTENT);
//        i.setType("audio/*");
//        startActivityForResult(i, 101);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode ==101 && requestCode ==RESULT_OK && data.getData() != null){
//
//            audioUrim = data.getData();
//            String fileNames = getFileName(audioUrim);
//            textViewImagem.setText(fileNames);
//            metadataRetrieverm.setDataSource(this,audioUrim);
//
//
//
//            titlem.setText(metadataRetrieverm.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
//            title1=metadataRetrieverm.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
//
//        }
//
//    }

    @SuppressLint("Range")
    private String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")){
            Cursor cursor= getContentResolver().query(uri,null,null,null,null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

                }
            }
            finally{
                cursor.close();
            }
        }

        if(result ==null ){
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if(cut != -1){
                result = result.substring(cut +1);
            }
        }
        return result;




    }
    public void uploadFileTofirebase(View v){
        if(textViewImage.equals("No files selected")){
            Toast.makeText(this,"please selected an image",Toast.LENGTH_SHORT).show();

        }
        else{
            if (mUploadsTask != null && mUploadsTask.isInProgress()){
                Toast.makeText(this,"songs uploads in already progress !",Toast.LENGTH_SHORT);
            }else{
                uploadFiles();
            }
        }
    }
    private void uploadFiles(){
        StorageReference storageReferencea = FirebaseStorage.getInstance().getReference();
        StorageReference storageReference = storageReferencea.child("Songs/" + EnterName.getText().toString());
        //StorageReference storageReference=FirebaseStorage.getInstance().getReference(EnterName.getText().toString());
        StorageReference fileRef = storageReference.child("musicImage.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri imageUriUpload) {
                        Picasso.get().load(imageUriUpload).into(songimage);
                        imageUrl=imageUriUpload;
                        Log.d("Url", String.valueOf(imageUriUpload));
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Failed Uploading Image...", Toast.LENGTH_SHORT).show();
            }
        });


        if(audioUri != null){
            Toast.makeText(this,"Upload please wait !",Toast.LENGTH_SHORT);
            progressBar.setVisibility(View.VISIBLE);
            final StorageReference storageReference1 = mStorageref.child(System.currentTimeMillis()+"."+getfileextension(audioUri));
            mUploadsTask=storageReference.putFile(audioUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            UploadSong uploadSong = new UploadSong(uri.toString(),imageUrl.toString(),Enterid.getText().toString(),EnterName.getText().toString());
                            Log.d("ImagewUrl",imageUrl.toString());
                            String uploadId= referenceSongs.child(selected_name).push().getKey();
                            Log.d("UploadId",referenceSongs.push().getKey());
                            referenceSongs.child(selected_name).child(uploadId).setValue(uploadSong);



                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    double progress = (100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                    progressBar.setProgress((int) progress);
                }
            });


        }else{
            Toast.makeText(this,"No file Selected to uploads",Toast.LENGTH_SHORT).show();
        }




    }

    private String getfileextension(Uri audioUri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(audioUri));

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    public void openImageFiles(View view) {
    }
}