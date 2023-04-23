package com.example.myapplication.Entry;

import com.example.myapplication.Common.Views.Fragments.IModel;

public class NavItem implements IModel {
    String name, description;
    Integer navTarget;

    private boolean loggedInOnly, loggedOutOnly;

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
