package com.example.myapplication.Common.Views.Fragments;

/**
 * interface to make sure we have to redefined toString in classes when we decide to,
 * and not implement when we choose not to
 * <p>
 * The presence of the method is enforced
 * (every function that implement IModel will have this method),
 * but the specific implementation is not
 * (a class can use the default or provide its own).
 */

public interface IModel {

    default public String toPrettyString() {
        return toString();
    }

    ; //-> default implementation OF THE METHOD

}
