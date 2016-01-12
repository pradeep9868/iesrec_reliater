package co.jlabs.cersei_retailer.custom_components;

import android.content.Context;
import android.widget.ListView;

public abstract class ScrollTabHolderListView extends ListView implements ScrollTabHolder {

	protected ScrollTabHolder mScrollTabHolder;

	public ScrollTabHolderListView(Context context) {
		super(context);
	}

	public void setScrollTabHolder(ScrollTabHolder scrollTabHolder) {
		mScrollTabHolder = scrollTabHolder;
	}
}