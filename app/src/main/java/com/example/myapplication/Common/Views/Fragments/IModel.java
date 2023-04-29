package com.example.myapplication.Common.Views.Fragments;

public interface IModel {

    default String toPrettyString() {
        return toString();
    }

}
