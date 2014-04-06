/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UI;

import java.util.ArrayList;
import javax.swing.AbstractListModel;

/**
 *
 * @author Herbert.Chow
 */
public class MonitorsListModel extends AbstractListModel {

    public ArrayList<MonitorEntry> activeMonitors = new ArrayList<>();

    @Override
    public int getSize() {
        return activeMonitors.size();
    }

    @Override
    public Object getElementAt(int index) {
        return (activeMonitors.get(index).EntryText);
    }

    public void Add(MonitorEntry entry) {
        activeMonitors.add(entry);
        this.fireContentsChanged(this, 0, activeMonitors.size());
    }

    public MonitorEntry Get(int index) {
        return activeMonitors.get(index);
    }

    public MonitorEntry RemoveEntry(int index) {
        MonitorEntry removed = activeMonitors.remove(index);
        this.fireContentsChanged(this, 0, activeMonitors.size());
        return removed;
    }
}
