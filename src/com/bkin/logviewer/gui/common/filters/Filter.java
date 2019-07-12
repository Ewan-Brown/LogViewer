package com.bkin.logviewer.gui.common.filters;

import com.bkin.logviewer.files.Line;
import com.bkin.logviewer.gui.texttable.TextTable;

import javax.swing.*;

/**
 * Abstract class representing any visual, interactable component that has a filter rule to be applied to the currently displayed lines.
 * One Filter may have multiple components inside of it (see the {@link FilterType} filter for an example)
 *
 */
public abstract class Filter extends JPanel {

    Filter(){
        styleControls();
        layoutControls();
        clear();
    }

    protected abstract void styleControls();

    protected abstract void layoutControls();

    /**
     * Clears this filter's settings. (for example a text filter will revert to being empty, a checkbox will revert to it's defined default state)
     */
    public abstract void clear();

    /**Adds this panel's components to text table's listener list. This means when they receive user
     * input the table will be updated. <p>
     * In here put buttons, textboxes etc. Anything that should update the table when
     * interacted with.
     * It's not a great solution to the encapsulation problem but it's simple and works
     * @param a reference to the tabke
     */
    public abstract void addTableListeners(TextTable a);

    /**
     * Checks to see if the given line is allowed under this Filter's current rule.
     * @param l - line to be checked
     * @return whether or not this line is allowed
     */
    public abstract boolean isAllowed(Line l);

}



