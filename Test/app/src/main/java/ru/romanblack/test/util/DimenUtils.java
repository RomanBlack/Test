package ru.romanblack.test.util;

import android.content.res.Resources;

import ru.romanblack.test.App;
import ru.romanblack.test.R;

public class DimenUtils {

    public static int calculateDrawerSize() {
        Resources res = App.getInstance().getResources();

        int drawerMaxWidth = res.getDimensionPixelSize(R.dimen.common_drawer_max_width);
        int drawerEdgeMargin = res.getDimensionPixelSize(R.dimen.common_drawer_edge_margin);
        int displayWidth = res.getDisplayMetrics().widthPixels;

        int displayEdge = displayWidth - drawerEdgeMargin;

        return drawerMaxWidth > displayEdge ? displayEdge : drawerMaxWidth;
    }
}
