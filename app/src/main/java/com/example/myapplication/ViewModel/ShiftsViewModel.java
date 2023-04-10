package com.example.myapplication.ViewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.Model.Profile;
import com.example.myapplication.Model.Shift;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ShiftsViewModel extends AndroidViewModel {


    private final MutableLiveData<ArrayList<Shift>> shiftsState =
            new MutableLiveData<ArrayList<Shift>>(new ArrayList<Shift>());
    private final RequestQueue               queue;

    final static private Gson gson = new Gson();

    public ShiftsViewModel(@NotNull Application application) {
        super(application);
        queue = Volley.newRequestQueue(getApplication());
    }

    public LiveData<ArrayList<Shift>> getShiftstate() {
        return shiftsState;
    }

    public void updateEntry(int index, Shift shift){
        shiftsState.getValue().set(index,shift);
        shiftsState.setValue(shiftsState.getValue());
    }

    public void addEntry(Shift shift){
        shiftsState.getValue().add(shift);
        shiftsState.setValue(shiftsState.getValue());
    }

    public void getData(){
        if(shiftsState.getValue().size() != 0)
            return;
        ArrayList<Shift> shiftsArrayList= new ArrayList<Shift>() {{
            add(new Shift("6-4-2023",4,new ArrayList<Profile>() {{
                add(new Profile("tal","tal","tal@gmail.com","050-1234567",0));
                add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567",1));
                //add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567",2));
                add(new Profile("tal","tal","tal@gmail.com","050-1234567",3));}}, 0, 5, 5));
            add(new Shift("6-4-2023",6,new ArrayList<Profile>() {{
                add(new Profile("tal","tal","tal@gmail.com","050-1234567",0));
                add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567",1));
                add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567",2));
                add(new Profile("tal","tal","tal@gmail.com","050-1234567",3));}}, 1, 10, 5));
            add(new Shift("8-4-2023",4,new ArrayList<Profile>() {{
                add(new Profile("tal","tal","tal@gmail.com","050-1234567",0));
            }}, 2, 15, 5));
            add(new Shift("9-4-2023",4,new ArrayList<Profile>() {{
                add(new Profile("tal","tal","tal@gmail.com","050-1234567",0));
                add(new Profile("Gal","Gal","Gal@gmail.com","050-1234567",1));
                add(new Profile("Yuval","Yuval","Yuval@gmail.com","050-1234567",2));
                add(new Profile("tal","tal","tal@gmail.com","050-1234567",3));}}, 3, 20, 5));
        }};

        shiftsState.setValue(shiftsArrayList);
    }
}
