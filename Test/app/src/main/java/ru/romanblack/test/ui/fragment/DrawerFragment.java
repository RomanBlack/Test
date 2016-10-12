package ru.romanblack.test.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.romanblack.test.R;
import ru.romanblack.test.ui.activity.MainActivity;
import ru.romanblack.test.util.DimenUtils;

public class DrawerFragment extends BaseFragment {

    public static DrawerFragment crate(int selection) {
        DrawerFragment fragment = new DrawerFragment();

        Bundle args = new Bundle();
        args.putInt(SELECTION, selection);
        fragment.setArguments(args);

        return fragment;
    }

    private static final String SELECTION = "selection";

    public static final int MENU_LIST = 0;
    public static final int MENU_SCALING = 1;
    public static final int MENU_SERVICE = 2;
    public static final int MENU_MAP = 3;

    private int selectedItem = MENU_LIST;

    private View container;

    private View listButton;
    private View scalingButton;
    private View serviceButton;
    private View mapButton;

    private View currentSelected;

    private MainActivity mainActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof MainActivity) {
            mainActivity = (MainActivity) context;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        onSelect(selectedItem);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

        initializeData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drawer, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeUi(view);
    }

    @Override
    public void onDetach() {
        super.onDetach();

        mainActivity = null;
    }

    private void initializeData() {
        Bundle args = getArguments();

        if (args != null) {
            selectedItem = args.getInt(SELECTION, 0);
        }
    }

    private void initializeUi(View view) {
        container = view.findViewById(R.id.container);
        setupSize();

        listButton = view.findViewById(R.id.list_item);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListClicked();
            }
        });

        scalingButton = view.findViewById(R.id.scaling_item);
        scalingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onScalingClicked();
            }
        });

        serviceButton = view.findViewById(R.id.service_item);
        serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onServiceClicked();
            }
        });

        mapButton = view.findViewById(R.id.map_item);
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMapClicked();
            }
        });
    }

    private void setupSize() {
        ViewGroup.LayoutParams lp = container.getLayoutParams();
        lp.width = DimenUtils.calculateDrawerSize();
        container.setLayoutParams(lp);
    }

    private void onListClicked() {
        onSelect(MENU_LIST);
    }

    private void onScalingClicked() {
        onSelect(MENU_SCALING);
    }

    private void onServiceClicked() {
        onSelect(MENU_SERVICE);
    }

    private void onMapClicked() {
        onSelect(MENU_MAP);
    }

    private void onSelect(int item) {
        selectedItem = item;

        if (currentSelected != null) {
            currentSelected.setSelected(false);
            currentSelected = null;
        }

        switch (selectedItem) {
            case MENU_LIST:
                currentSelected = listButton;
                break;
            case MENU_SCALING:
                currentSelected = scalingButton;
                break;
            case MENU_SERVICE:
                currentSelected = serviceButton;
                break;
            case MENU_MAP:
                currentSelected = mapButton;
                break;
        }

        if (currentSelected != null) {
            currentSelected.setSelected(true);
        }

        if (mainActivity != null) {
            mainActivity.onSelected(item);
        }
    }
}
