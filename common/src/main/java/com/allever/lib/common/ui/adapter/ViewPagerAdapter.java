package com.allever.lib.common.ui.adapter;




import androidx.fragment.app.FragmentManager;

import com.allever.android.app.MyFragment;
import com.allever.android.app.MyFragmentPagerAdapter;

import java.util.List;

/**
 * @author Allever
 * @date 18/5/21
 */

public class ViewPagerAdapter extends MyFragmentPagerAdapter {
    private List<MyFragment> mFragmentList;

    public ViewPagerAdapter(FragmentManager fragmentManager, List<MyFragment> fragmentList) {
        super(fragmentManager);
        mFragmentList = fragmentList;
    }

    @Override
    public MyFragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
