package com.example.avaloncs.firebase;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


/**
 * A simple {@link Fragment} subclass.
 */
public class ReportEventFragment extends Fragment {
    private final static String TAG = ReportEventFragment.class.getSimpleName();
    private EditText mTextViewLocation;
    private EditText getmTextViewDest;
    private Button mReportButton;
    private DatabaseReference database;
    private String username;


    public ReportEventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_report_event, container, false);
        mTextViewLocation = (EditText) view.findViewById(R.id.text_event_location);


        getmTextViewDest = (EditText) view.findViewById(R.id.text_event_description);
        mReportButton = (Button) view.findViewById(R.id.button_report);
        username = ((EventActivity)getActivity()).getUsername();
        database = FirebaseDatabase.getInstance().getReference();



        mReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = uploadEvent();
            }
        });
        return view;
    }

    /**
     * upload data and set path
     */
    private String uploadEvent() {
        String location = mTextViewLocation.getText().toString();
        String description = getmTextViewDest.getText().toString();
        if (location.equals("") || description.equals("")) {
            return "";
        }
        //create event instance
        Event event = new Event();
        event.setLocation(location);
        event.setDescription(description);
        event.setTime(System.currentTimeMillis());
        event.setUser(username);
        String key = database.child("events").push().getKey();
        event.setId(key);
        database.child("events").child(key).setValue(event, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    Toast toast = Toast.makeText(getContext(), "The event is failed, please check you network status.", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getContext(), "The event is reported", Toast.LENGTH_SHORT);
                    toast.show();
                    mTextViewLocation.setText("");
                    getmTextViewDest.setText("");
                }
            }
        });
        return key;
    }

}
