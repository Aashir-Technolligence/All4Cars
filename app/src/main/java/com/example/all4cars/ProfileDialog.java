package com.example.all4cars;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ProfileDialog extends AppCompatDialogFragment {
    ImageView imageView;
    EditText editText;
    DatabaseReference dref = FirebaseDatabase.getInstance().getReference();
    String newName, newFilePath;
    private Uri filepath;
    int x = 0;
    ProgressBar progressBar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder( getActivity() );

        //getting layout of dialog
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate( R.layout.profile_dialog, null );
        imageView = (ImageView) view.findViewById( R.id.profileImage );
        editText = (EditText) view.findViewById( R.id.name );
        progressBar = (ProgressBar) view.findViewById( R.id.pro );

        progressBar.setVisibility( View.GONE );
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUser = user.getUid();
        dref.child( "Users" ).child( currentUser ).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                editText.setText( dataSnapshot.child( "Name" ).getValue().toString() );
                Picasso.get().load( dataSnapshot.child( "pic" ).getValue().toString() ).into( imageView );
                newFilePath = dataSnapshot.child( "pic" ).getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        imageView.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                startActivityForResult( intent, 2 );
            }
        } );


        alertDialogBuilder.setView( view ).setPositiveButton( "Save Changes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, final int which) {
                newName = editText.getText().toString();
                //Toast.makeText(view.getContext(), ""+newName, Toast.LENGTH_LONG).show();
                if (x == 0) {
                    progressBar.setVisibility( View.VISIBLE );
                    //filepath=Uri.parse(newFilePath);
                    dref.child( "Users" ).child( currentUser ).child( "Name" ).setValue( newName );
                    //dref.child("Users").child(currentUser).child("pic").setValue(filepath);
                    progressBar.setVisibility( View.GONE );
                    getActivity().finish();
                } else {
                    progressBar.setVisibility( View.VISIBLE );
                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child( "images/" + FirebaseDatabase.getInstance().getReference().child( "Users" ).push().getKey() );
                    storageReference.putFile( filepath ).addOnSuccessListener( new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            Uri downloadUri = uriTask.getResult();

                            dref.child( "Users" ).child( currentUser ).child( "Name" ).setValue( newName );
                            dref.child( "Users" ).child( currentUser ).child( "pic" ).setValue( downloadUri.toString() );
                            progressBar.setVisibility( View.GONE );
                            getActivity().finish();
                        }
                    } );
                }

            }
        } ).setNegativeButton( "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        } );
        return alertDialogBuilder.create();
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == requestCode && resultCode == resultCode
                && data != null && data.getData() != null) {

            filepath = data.getData();
            try {

                Bitmap bitmap = MediaStore.Images.Media.getBitmap( getContext().getContentResolver(), filepath );
                imageView.setImageBitmap( bitmap );
                x = 1;


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
