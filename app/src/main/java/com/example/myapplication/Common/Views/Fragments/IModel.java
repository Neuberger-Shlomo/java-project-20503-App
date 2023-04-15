package com.example.myapplication.Common.Views.Fragments;

public interface IModel {

    default public String toPrettyString(){
        return toString();
    };

}
