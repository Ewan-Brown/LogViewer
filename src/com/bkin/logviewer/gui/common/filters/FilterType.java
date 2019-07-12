package com.bkin.logviewer.gui.common.filters;

import com.bkin.logviewer.files.Line;
import com.bkin.logviewer.gui.texttable.TextTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains one checkbox for each line type (currently at 7), color coded with labels.
 * Filters based on line type. If a given line's type is unchecked it is not allowed.<br>
 * If the user right clicks on a checkbox it is enabled and all other boxes are disabled.
 */
public class FilterType extends Filter implements MouseListener {

    private List<FilterButton> m_filterButtons;

    public FilterType() {
        super();
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getSource() instanceof FilterButton && e.getButton() == MouseEvent.BUTTON3){
            for(FilterButton f : m_filterButtons){
                f.setSelected(false);
            }
            ((FilterButton)e.getSource()).setSelected(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }


    private class FilterButton extends JCheckBox{
        FilterButton(Line.LineType type){
            super(type.toString());
            setBackground(type.getColour());
        }
    }

    public void addTableListeners(TextTable a){
        for(FilterButton fB : m_filterButtons){
            fB.addActionListener(a);
            fB.addMouseListener(a);
        }

    }

    @Override
    public boolean isAllowed(Line l) {
        return m_filterButtons.get(l.getType().ordinal()).isSelected();
    }

    public void styleControls() {
        m_filterButtons = new ArrayList<>();
        for(int i = 0; i < Line.LineType.values().length;i++){
            Line.LineType lineType = Line.LineType.values()[i];
            FilterButton newButton = new FilterButton(lineType);
            newButton.addMouseListener(this);
            newButton.setToolTipText("Toggle filtering this line type. Right click to enable only this type");
            m_filterButtons.add(newButton);
        }
    }

    public void layoutControls(){
        setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        for(FilterButton filterButton : m_filterButtons){
            add(filterButton);
        }
    }

    public void clear(){
        for(FilterButton filterButton : m_filterButtons){
            filterButton.setSelected(true);
        }
    }
}
