package fr.pastekweb.tchat.model;

import javax.swing.*;
import java.util.HashMap;

/**
 * @author Thomas TRIBOULT on 23/02/2015.
 */
public class PositionsList extends AbstractListModel<Position> {
    private HashMap<User, Position> list;

    public PositionsList()
    {
        list = new HashMap<>();
    }

    public void addPosition(User u, Position p)
    {
        list.put(u, p);

        fireContentsChanged(this, 0, getSize());
    }

    public void removePosition(User u)
    {
        list.remove(u);

        fireContentsChanged(this, 0, getSize());
    }

    public HashMap<User, Position> getHashMap()
    {
        return list;
    }

    @Override
    public int getSize() {
        return list.size();
    }

    @Override
    public Position getElementAt(int index) {
        return (Position) list.entrySet().toArray()[index];
    }
}
