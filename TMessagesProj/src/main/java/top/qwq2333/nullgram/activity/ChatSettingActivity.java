package top.qwq2333.nullgram.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.EmptyCell;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.NotificationsCheckCell;
import org.telegram.ui.Cells.ShadowSectionCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextDetailSettingsCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.telegram.ui.ActionBar.BaseFragment;

@SuppressLint("NotifyDataSetChanged")
public class ChatSettingActivity extends BaseFragment {

    private RecyclerListView listView;
    private ListAdapter listAdapter;

    private int rowCount;

    private int chatRow;
    private int ignoreBlockedUserMessagesRow;
    private int hideGroupStickerRow;
    private int chat2Row;


    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();

        updateRows();

        return true;
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setTitle(LocaleController.getString("Chat", R.string.Chat));

        if (AndroidUtilities.isTablet()) {
            actionBar.setOccupyStatusBar(false);
        }
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        listAdapter = new ListAdapter(context);

        fragmentView = new FrameLayout(context);
        fragmentView.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));
        FrameLayout frameLayout = (FrameLayout) fragmentView;

        listView = new RecyclerListView(context);
        listView.setLayoutManager(
            new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        listView.setVerticalScrollBarEnabled(false);
        listView.setAdapter(listAdapter);
        ((DefaultItemAnimator) listView.getItemAnimator()).setDelayAnimations(false);
        frameLayout.addView(listView,
            LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setOnItemClickListener((view, position, x, y) -> {
            if (position == ignoreBlockedUserMessagesRow) {
                ConfigManager.putBoolean(Defines.ignoreBlockedUser,
                    !ConfigManager.getBooleanOrFalse(Defines.ignoreBlockedUser));
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(
                        ConfigManager.getBooleanOrFalse(Defines.ignoreBlockedUser));
                }
            } else if (position == hideGroupStickerRow) {
                ConfigManager.putBoolean(Defines.hideGroupSticker,
                    !ConfigManager.getBooleanOrFalse(Defines.hideGroupSticker));
                if (view instanceof TextCheckCell) {
                    ((TextCheckCell) view).setChecked(
                        ConfigManager.getBooleanOrFalse(Defines.hideGroupSticker));
                }
            }
        });

        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        try (Cursor cursor = getParentActivity().getContentResolver()
            .query(uri, new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
            }
        }
        return result;
    }


    private void updateRows() {
        rowCount = 0;

        chatRow = rowCount++;
        ignoreBlockedUserMessagesRow = rowCount++;
        hideGroupStickerRow = rowCount++;
        chat2Row = rowCount++;
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> themeDescriptions = new ArrayList<>();
        themeDescriptions.add(
            new ThemeDescription(listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR,
                new Class[]{EmptyCell.class, TextSettingsCell.class, TextCheckCell.class,
                    HeaderCell.class, TextDetailSettingsCell.class, NotificationsCheckCell.class},
                null, null, null, Theme.key_windowBackgroundWhite));
        themeDescriptions.add(
            new ThemeDescription(fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null,
                null, Theme.key_windowBackgroundGray));

        themeDescriptions.add(
            new ThemeDescription(actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null,
                null, Theme.key_avatar_backgroundActionBarBlue));
        themeDescriptions.add(
            new ThemeDescription(listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null,
                null, Theme.key_avatar_backgroundActionBarBlue));
        themeDescriptions.add(
            new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null,
                null, Theme.key_avatar_actionBarIconBlue));
        themeDescriptions.add(
            new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null,
                null, Theme.key_actionBarDefaultTitle));
        themeDescriptions.add(
            new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null,
                null, null, Theme.key_avatar_actionBarSelectorBlue));
        themeDescriptions.add(
            new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SUBMENUBACKGROUND, null, null,
                null, null, Theme.key_actionBarDefaultSubmenuBackground));
        themeDescriptions.add(
            new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SUBMENUITEM, null, null, null,
                null, Theme.key_actionBarDefaultSubmenuItem));

        themeDescriptions.add(
            new ThemeDescription(listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null,
                Theme.key_listSelector));

        themeDescriptions.add(
            new ThemeDescription(listView, 0, new Class[]{View.class}, Theme.dividerPaint, null,
                null, Theme.key_divider));

        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_BACKGROUNDFILTER,
            new Class[]{ShadowSectionCell.class}, null, null, null,
            Theme.key_windowBackgroundGrayShadow));

        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextSettingsCell.class},
            new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextSettingsCell.class},
            new String[]{"valueTextView"}, null, null, null,
            Theme.key_windowBackgroundWhiteValueText));

        themeDescriptions.add(
            new ThemeDescription(listView, 0, new Class[]{NotificationsCheckCell.class},
                new String[]{"textView"}, null, null, null,
                Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(
            new ThemeDescription(listView, 0, new Class[]{NotificationsCheckCell.class},
                new String[]{"valueTextView"}, null, null, null,
                Theme.key_windowBackgroundWhiteGrayText2));
        themeDescriptions.add(
            new ThemeDescription(listView, 0, new Class[]{NotificationsCheckCell.class},
                new String[]{"checkBox"}, null, null, null, Theme.key_switchTrack));
        themeDescriptions.add(
            new ThemeDescription(listView, 0, new Class[]{NotificationsCheckCell.class},
                new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked));

        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class},
            new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class},
            new String[]{"valueTextView"}, null, null, null,
            Theme.key_windowBackgroundWhiteGrayText2));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class},
            new String[]{"checkBox"}, null, null, null, Theme.key_switchTrack));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class},
            new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked));

        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{HeaderCell.class},
            new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader));

        themeDescriptions.add(
            new ThemeDescription(listView, 0, new Class[]{TextDetailSettingsCell.class},
                new String[]{"textView"}, null, null, null,
                Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(
            new ThemeDescription(listView, 0, new Class[]{TextDetailSettingsCell.class},
                new String[]{"valueTextView"}, null, null, null,
                Theme.key_windowBackgroundWhiteGrayText2));

        return themeDescriptions;
    }

    private class ListAdapter extends RecyclerListView.SelectionAdapter {

        private final Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            switch (holder.getItemViewType()) {
                case 1: {
                    if (position == chat2Row) {
                        holder.itemView.setBackground(
                            Theme.getThemedDrawable(mContext, R.drawable.greydivider_bottom,
                                Theme.key_windowBackgroundGrayShadow));
                    } else {
                        holder.itemView.setBackground(
                            Theme.getThemedDrawable(mContext, R.drawable.greydivider,
                                Theme.key_windowBackgroundGrayShadow));
                    }
                    break;
                }
                case 2: {
                    TextSettingsCell textCell = (TextSettingsCell) holder.itemView;
                    textCell.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
                    break;
                }
                case 3: {
                    TextCheckCell textCell = (TextCheckCell) holder.itemView;
                    textCell.setEnabled(true, null);
                    if (position == ignoreBlockedUserMessagesRow) {
                        textCell.setTextAndCheck(LocaleController.getString("ignoreBlockedUser",
                            R.string.ignoreBlockedUser), ConfigManager.getBooleanOrFalse(
                            Defines.ignoreBlockedUser), true);
                    } else if (position == hideGroupStickerRow) {
                        textCell.setTextAndCheck(LocaleController.getString("hideGroupSticker",
                            R.string.hideGroupSticker), ConfigManager.getBooleanOrFalse(
                            Defines.hideGroupSticker), true);
                    }
                    break;
                }
                case 4: {
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == chatRow) {
                        headerCell.setText(LocaleController.getString("Chat", R.string.Chat));
                    }
                    break;
                }
                case 5: {
                    NotificationsCheckCell textCell = (NotificationsCheckCell) holder.itemView;
                    break;
                }
            }
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int type = holder.getItemViewType();
            return type == 2 || type == 3 || type == 6 || type == 5;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType) {
                case 1:
                    view = new ShadowSectionCell(mContext);
                    break;
                case 2:
                    view = new TextSettingsCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 3:
                    view = new TextCheckCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 4:
                    view = new HeaderCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 5:
                    view = new NotificationsCheckCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 6:
                    view = new TextDetailSettingsCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case 7:
                    view = new TextInfoPrivacyCell(mContext);
                    view.setBackground(Theme.getThemedDrawable(mContext, R.drawable.greydivider,
                        Theme.key_windowBackgroundGrayShadow));
                    break;
            }
            //noinspection ConstantConditions
            view.setLayoutParams(
                new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT));
            return new RecyclerListView.Holder(view);
        }

        @Override
        public int getItemViewType(int position) {
            if (position == chat2Row) {
                return 1;
            } else if (position == chatRow) {
                return 4;
            }
            return 3;
        }
    }
}