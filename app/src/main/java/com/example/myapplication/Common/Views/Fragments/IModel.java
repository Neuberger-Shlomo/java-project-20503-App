package com.example.myapplication.Common.Views.Fragments;

/**
 * Interface that adds another version of toString,<br/>
 * This mostly use by the {@link DateListFragment} to make sure we don't use the more
 * technical toString
 */

public interface IModel {
    /**
     * This function should return a formatted version of the object
     *
     * @return A formatted string version of the object
     * @apiNote this function defaults to the {@code  toString()} function
     */
    default String toPrettyString() {
        return toString();
    }


}
