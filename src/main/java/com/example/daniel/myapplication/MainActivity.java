package com.example.daniel.myapplication;

import android.app.Activity;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;
    public ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends ListFragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static int selection;
        // Attributes
        private Context mContext;
        private LayoutInflater mLayoutInflater;
        private Cursor mCursor;

        // Elements
        public static ListView mListView;
        public static SimpleCursorAdapter mListAdapter;
        private FridgeOpenHelper mFridgeOpenHelper;


        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            selection = sectionNumber;
            return fragment;
        }

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Set our attributes
            mContext = getActivity();
            mLayoutInflater = inflater;


            // Let's inflate & return the view
            View rootView = mLayoutInflater.inflate(R.layout.fragment_main, container, false);
            mListView = (ListView)rootView.findViewById(android.R.id.list);

            // Get the database handler & the cursor
            mFridgeOpenHelper = new FridgeOpenHelper(mContext);
            if(selection == 1) mCursor = mFridgeOpenHelper.getAllItemsFridge();
            else if (selection == 2)mCursor = mFridgeOpenHelper.getAllItemsPantry();
            else mCursor = mFridgeOpenHelper.getAllItemsList();

            // Init
            init(rootView);

            // Return
            return rootView;
        }

        public void init(View v) {

            // Setup the listAdapter
            mListAdapter = new SimpleCursorAdapter(

                    mContext,
                    R.layout.list_item,
                    mCursor,
                    new String[] {"item" },
                    new int[] {R.id.list_text1 },
                    0
            );
            mListView.setAdapter(mListAdapter);

        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        @Override
        public void onResume() {
            super.onResume();
        }

        @Override
        public void onListItemClick(ListView l, final View v, final int position, long id) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Remove Item");
            TextView m_TextView = (TextView) mListView.getChildAt(position).findViewById(R.id.list_text1);
            final String m_Text = m_TextView.getText().toString().trim();
            if(selection != 3) {
                builder.setItems(new CharSequence[]
                                {"Remove", "Add to List", "Cancel"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                        if (m_Text.length() > 0) {
                                            if (selection == 1) {
                                                mFridgeOpenHelper.deleteItemFridge(m_Text);
                                                mCursor = mFridgeOpenHelper.getAllItemsFridge();
                                            } else if (selection == 2) {
                                                mFridgeOpenHelper.deleteItemPantry(m_Text);
                                                mCursor = mFridgeOpenHelper.getAllItemsPantry();
                                            }
                                        }
                                        mListAdapter.changeCursor(mCursor);
                                        break;
                                    case 1:
                                        if (m_Text.length() > 0) {
                                            mFridgeOpenHelper.addItemList(m_Text);
                                            if (selection == 1) {
                                                mFridgeOpenHelper.deleteItemFridge(m_Text);
                                                mCursor = mFridgeOpenHelper.getAllItemsFridge();
                                            } else if (selection == 2) {
                                                mFridgeOpenHelper.deleteItemPantry(m_Text);
                                                mCursor = mFridgeOpenHelper.getAllItemsPantry();
                                            }
                                        }
                                        mListAdapter.changeCursor(mCursor);
                                        break;
                                    case 2:
                                        dialog.cancel();
                                        break;
                                }
                            }
                        });
            } else {
                builder.setItems(new CharSequence[]
                                {"Remove", "Add to Fridge", "Add to Pantry", "Cancel"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // The 'which' argument contains the index position
                                // of the selected item
                                switch (which) {
                                    case 0:
                                        if (m_Text.length() > 0) {
                                            mFridgeOpenHelper.deleteItemList(m_Text);
                                            mCursor = mFridgeOpenHelper.getAllItemsList();
                                        }
                                        mListAdapter.changeCursor(mCursor);
                                        break;
                                    case 1:
                                        if (m_Text.length() > 0) {
                                            mFridgeOpenHelper.addItemFridge(m_Text);
                                            mFridgeOpenHelper.deleteItemList(m_Text);
                                            mCursor = mFridgeOpenHelper.getAllItemsList();
                                        }
                                        mListAdapter.changeCursor(mCursor);
                                        break;
                                    case 2:
                                        if (m_Text.length() > 0) {
                                            mFridgeOpenHelper.addItemPantry(m_Text);
                                            mFridgeOpenHelper.deleteItemList(m_Text);
                                            mCursor = mFridgeOpenHelper.getAllItemsList();
                                        }
                                        mListAdapter.changeCursor(mCursor);
                                        break;
                                    case 3:
                                        dialog.cancel();
                                        break;
                                }
                            }
                        });
            }
            builder.create().show();
            super.onListItemClick(l, v, position, id);
        }
    }

}
