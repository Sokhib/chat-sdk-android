/*
 * Created by Itzik Braun on 12/3/2015.
 * Copyright (c) 2015 deluge. All rights reserved.
 *
 * Last Modification at: 3/12/15 4:27 PM
 */

package co.chatsdk.ui.adapters;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import co.chatsdk.core.dao.User;
import co.chatsdk.core.defines.Availability;
import co.chatsdk.core.interfaces.UserListItem;
import co.chatsdk.core.session.ChatSDK;
import co.chatsdk.core.utils.Dimen;
import co.chatsdk.ui.R;
import co.chatsdk.ui.utils.AvailabilityHelper;
import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;


public class UsersListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    protected static final int TYPE_USER = 0;
    protected static final int TYPE_HEADER = 1;

    protected List<Object> items = new ArrayList<>();
    protected List<String> headers = new ArrayList<>();

    protected SparseBooleanArray selectedUsersPositions = new SparseBooleanArray();

    protected boolean multiSelectEnabled;
    protected final PublishSubject<Object> onClickSubject = PublishSubject.create();
    protected final PublishSubject<Object> onLongClickSubject = PublishSubject.create();
    protected final PublishSubject<List<UserListItem>> onToggleSubject = PublishSubject.create();

    protected class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView textView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_header);
        }
    }

    protected class UserViewHolder extends RecyclerView.ViewHolder {

        protected ImageView avatarImageView;
        protected TextView nameTextView;
        protected CheckBox checkBox;
        protected TextView statusTextView;
        protected ImageView availabilityImageView;

        public UserViewHolder(View view) {
            super(view);

            nameTextView = view.findViewById(R.id.text_name);
            statusTextView = view.findViewById(R.id.text_status);
            availabilityImageView = view.findViewById(R.id.image_availability);
            avatarImageView = view.findViewById(R.id.image_avatar);
            checkBox = view.findViewById(R.id.checkbox);

            // Clicks are handled at the list item level
            checkBox.setClickable(false);
        }

        public void setMultiSelectEnabled (boolean enabled) {
            if (enabled) {
                checkBox.setVisibility(View.VISIBLE);
                availabilityImageView.setVisibility(View.INVISIBLE);
            } else {
                availabilityImageView.setVisibility(View.VISIBLE);
                checkBox.setVisibility(View.INVISIBLE);
            }
        }
    }

    public UsersListAdapter() {
        this(null, false);
    }

    public UsersListAdapter(boolean multiSelectEnabled) {
        this(null, multiSelectEnabled);
    }

    public UsersListAdapter(List<UserListItem> users, boolean multiSelect) {
        if (users == null) {
            users = new ArrayList<>();
        }

        setUsers(users);

        this.multiSelectEnabled = multiSelect;
    }

    @Override
    public int getItemViewType(int position) {
        Object item = items.get(position);
        if (headers.contains(item)) {
            return TYPE_HEADER;
        }
        else {
            return TYPE_USER;
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_HEADER) {
            View row = inflater.inflate(R.layout.view_contact_row_header, null);
            return new HeaderViewHolder(row);
        }
        else if (viewType == TYPE_USER) {
            View row = inflater.inflate(R.layout.view_contact_row_body, null);
            return new UserViewHolder(row);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        int type = getItemViewType(position);
        final Object item = items.get(position);

        if (type == TYPE_HEADER) {
            HeaderViewHolder hh = (HeaderViewHolder) holder;
            String header = (String) item;
            hh.textView.setText(header);
        }

        if (type == TYPE_USER) {
            UserViewHolder userViewHolder = (UserViewHolder) holder;
            UserListItem userListItem = (UserListItem) item;

            userViewHolder.nameTextView.setText(userListItem.getName());

            userViewHolder.availabilityImageView.setImageResource(AvailabilityHelper.imageResourceIdForAvailability(userListItem.getAvailability()));
            userViewHolder.statusTextView.setText(userListItem.getStatus());

            Logger.debug("User: " + userListItem.getName() + " Availability: " + userListItem.getAvailability());

            Context context = ChatSDK.shared().context();

            int width = Dimen.from(context, R.dimen.small_avatar_width);
            int height = Dimen.from(context, R.dimen.small_avatar_height);

            if (userListItem instanceof User) {
                ((User) userListItem).loadAvatar(userViewHolder.avatarImageView, width, height);
            } else {
                Picasso.get().load(userListItem.getAvatarURL()).resize(width, height).into(userViewHolder.avatarImageView);
            }

            userViewHolder.setMultiSelectEnabled(multiSelectEnabled);

            if (multiSelectEnabled) {
                userViewHolder.checkBox.setChecked(selectedUsersPositions.get(position));
            }

        }

        holder.itemView.setOnClickListener(view -> onClickSubject.onNext(item));
        holder.itemView.setOnLongClickListener(view -> {
            onLongClickSubject.onNext(item);
            return true;
        });

    }

    public Object getItem(int i) {
        return items.get(i);
    }

    public void setUsers(List<UserListItem> users, boolean sort) {
        this.items.clear();

        if (sort) {
            sortList(users);
        }

        for (UserListItem item : users) {
            addUser(item);
        }

        notifyDataSetChanged();
    }

    public void addUser (UserListItem user) {
        addUser(user, false);
    }

    public List<Object> getItems () {
        return items;
    }

    public void addUser (UserListItem user, boolean notify) {
        addUser(user, -1, notify);
    }

    public void addUser (UserListItem user, int atIndex, boolean notify) {
        if (!items.contains(user)) {
            if (atIndex >= 0) {
                items.add(atIndex, user);
            }
            else {
                items.add(user);
            }
            if (notify) {
                notifyDataSetChanged();
            }
        }
    }

    public void addHeader (String header) {
        if (!items.contains(header)) {
            items.add(header);
            headers.add(header);
        }
    }

    public List<UserListItem> getSelectedUsers () {
        List<UserListItem> users = new ArrayList<>();
        for (int i = 0 ; i < getSelectedCount() ; i++) {
            int pos = getSelectedUsersPositions().keyAt(i);
            if (items.get(pos) instanceof UserListItem) {
                users.add(((UserListItem) items.get(pos)));
            }
        }
        return users;
    }

    public void setUsers(List<UserListItem> userItems) {
        setUsers(userItems, false);
    }

    /**
     *  Clear the list.
     * 
     *  Calls notifyDataSetChanged.
     * * */
    public void clear() {
        items.clear();
        clearSelection();
        notifyDataSetChanged();
    }

    public UserListItem userAtPosition (int position) {
        Object item = getItem(position);
        if (item instanceof UserListItem) {
            return (UserListItem) item;
        }
        else {
            return null;
        }
    }

    /**
     * Sorting a given list using the internal comparator.
     * 
     * This will be used each time after setting the user item
     * * */
    protected void sortList(List<UserListItem> list) {
        Comparator comparator = (Comparator<UserListItem>) (u1, u2) -> {
            boolean u1online = u1.getAvailability().equals(Availability.Available);
            boolean u2online = u2.getAvailability().equals(Availability.Available);
            if (u1online != u2online) {
                if (u1online) {
                    return -1;
                } else {
                    return 1;
                }
            } else {
                String s1 = u1.getName() != null ? u1.getName() : "";
                String s2 = u2.getName() != null ? u2.getName() : "";

                return s1.compareToIgnoreCase(s2);
            }
        };
        Collections.sort(list, comparator);
    }

    /**
     * Toggle the selection state of a list item for a given position
     * @param position the position in the list of the item that need to be toggled
     *                 
     * notifyDataSetChanged will be called.
     * * */
    public boolean toggleSelection(int position) {
        boolean selected = setViewSelected(position, !selectedUsersPositions.get(position));
        onToggleSubject.onNext(getSelectedUsers());
        return selected;
    }

    public boolean toggleSelection(Object object) {
        int position = items.indexOf(object);
        return toggleSelection(position);
    }

    /**
     * Set the selection state of a list item for a given position and value
     * @param position the position in the list of the item that need to be toggled.
     * @param selected pass true for selecting the view, false will remove the view from the selectedUsersPositions
     * * */
    public boolean setViewSelected(int position, boolean selected) {
        UserListItem user = userAtPosition(position);
        if (user != null) {
            if (selected) {
                selectedUsersPositions.put(position, true);
            }
            else {
                selectedUsersPositions.delete(position);
            }
            notifyItemChanged(position);

            return selected;
        }
        return false;
    }

    public SparseBooleanArray getSelectedUsersPositions() {
        return selectedUsersPositions;
    }

    /**
     * Get the amount of selected users.
     * * * */
    public int getSelectedCount() {
        return selectedUsersPositions.size();
    }

    /**
     * Select all users
     * 
     * notifyDataSetChanged will be called.
     */
    public void selectAll() {
        for (int i = 0; i < items.size(); i++) {
            setViewSelected(i, true);
        }

        notifyDataSetChanged();
    }

    /**
     * Clear the selection of all users.
     * 
     * notifyDataSetChanged will be called.
     */
    public void clearSelection() {
        selectedUsersPositions = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public Observable<Object> onClickObservable () {
        return onClickSubject;
    }

    public Observable<List<UserListItem>> onToggleObserver () {
        return onToggleSubject;
    }

    public Observable<Object> onLongClickObservable () {
        return onLongClickSubject;
    }

    public void setMultiSelectEnabled (boolean enabled) {
        multiSelectEnabled = enabled;
        notifyDataSetChanged();
    }

}