package fr.pastekweb.tchat.model;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to represent a list of positions used by a room.
 *
 * @author Thomas TRIBOULT on 23/02/2015.
 */
public class PositionsList extends AbstractListModel<Position> {
    /**
     * The list of user's positions
     */
    private HashMap<User, Position> list;

    /**
     * Initialize an empty list
     */
    public PositionsList()
    {
        list = new HashMap<>();
    }

    /**
     * Add a user's position to the list and refresh the view
     * @param user The user which is positioned
     * @param position The position
     */
    public void addPosition(User user, Position position)
    {
        addPosition(user, position, true);
    }

    /**
     * Add a user's position to the list
     * @param user The user which is positioned
     * @param position The position
     * @param refreshDisplay Whether to refresh the view or not
     */
    public void addPosition(User user, Position position, boolean refreshDisplay) {
        list.put(user, position);

        if (refreshDisplay) {
            fireContentsChanged(this, 0, getSize());
        }
    }

    /**
     * Update the position of a given user
     * @param user The user which the position change
     * @param position The position
     */
    public void updatePosition(User user, Position position)
    {
        for (Map.Entry<User, Position> elmt : list.entrySet()) {
            if (elmt.getKey().getPseudo().equals(user.getPseudo())) {
                elmt.setValue(position);

                fireContentsChanged(this, 0, getSize());
                return;
            }
        }

        // Add the position if not already in the list
        addPosition(user, position);
    }

    /**
     * Remove a user position from the list
     * @param user The user who left
     */
    public void removePosition(User user)
    {
        for (Map.Entry<User, Position> elmt : list.entrySet()) {
            if (elmt.getKey().getPseudo().equals(user.getPseudo())) {
                list.remove(elmt.getKey());

                fireContentsChanged(this, 0, getSize());
                return;
            }
        }
    }

    /**
     * Get the list of positions
     * @return A list of user's positions
     */
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
