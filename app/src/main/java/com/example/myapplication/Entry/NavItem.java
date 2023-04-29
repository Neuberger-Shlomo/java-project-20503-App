package com.example.myapplication.Entry;

import com.example.myapplication.Common.Views.Fragments.IModel;
/**

 represents a navigation item in the application
 (implements the IModel interface, that defines methods for displaying data in a view)
 */
public class NavItem implements IModel {
    String name, description;
    Integer navTarget;

    private boolean loggedInOnly, loggedOutOnly;
    /**
     * Constructs a NavItem with the given name, description, navigation target, and logged-in/logged-out status.
     * @param name the name of the navigation item
     * @param description the description of the navigation item
     * @param navTarget the navigation target of the item
     * @param loggedInOnly whether the item should be displayed for logged-in users only
     * @param loggedOutOnly whether the item should be displayed for logged-out users only
     */
    public NavItem(String name, String description, Integer navTarget, boolean loggedInOnly,
                   boolean loggedOutOnly) {
        this.name          = name;
        this.description   = description;
        this.navTarget     = navTarget;
        this.loggedInOnly  = loggedInOnly;
        this.loggedOutOnly = loggedOutOnly;
    }

    @Override
    public String toString() {
        return "NavItem{" +
               "name='" + name + '\'' +
               ", description='" + description + '\'' +
               ", navTarget=" + navTarget +
               '}';
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    /**
     * @return the navigation target of the NavItem
     */
    public Integer getNavTarget() {
        return navTarget;
    }

    @Override
    public String toPrettyString() {
        if (description.isEmpty())
            return name;
        else
            return String.format("%s\n\n%s", name, description);
    }

    public boolean isLoggedInOnly() {
        return loggedInOnly;
    }

    public void setLoggedInOnly(boolean loggedInOnly) {
        this.loggedInOnly = loggedInOnly;
    }

    public boolean isLoggedOutOnly() {
        return loggedOutOnly;
    }

    public void setLoggedOutOnly(boolean loggedOutOnly) {
        this.loggedOutOnly = loggedOutOnly;
    }
}
