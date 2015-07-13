package com.dfjy.seal.activity;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

public class ApproveListActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ActionBar bar = getActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
        ActionBar.Tab approveTab = bar.newTab().setText("待审批");
        ActionBar.Tab approvedTab = bar.newTab().setText("已审批");
        TabListener approveFragment = new TabListener(new ApproveListFragment());
        TabListener approvedFragment = new TabListener(new ApprovedListFragment());
        approveTab.setTabListener(approveFragment);
        approvedTab.setTabListener(approvedFragment);
        bar.addTab(approveTab);
        bar.addTab(approvedTab);

        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getActionBar().getSelectedNavigationIndex());
    }

    public class TabListener implements ActionBar.TabListener {

        private Fragment mFragment;

        public TabListener(Fragment fragment ) {
             this.mFragment =fragment;
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.add(android.R.id.content, mFragment, null);
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            ft.remove(mFragment);
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

    }
}
