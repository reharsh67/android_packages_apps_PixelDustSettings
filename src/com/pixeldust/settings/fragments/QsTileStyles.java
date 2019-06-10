/*
 * Copyright (C) 2018 The Dirty Unicorns Project
 * Copyright (C) 2019 The PixelDust Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pixeldust.settings.fragments;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.om.IOverlayManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.ServiceManager;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.android.internal.logging.nano.MetricsProto;
import com.android.internal.util.pixeldust.PixeldustUtils;
import com.android.settings.R;
import com.android.settings.core.instrumentation.InstrumentedDialogFragment;

public class QsTileStyles extends InstrumentedDialogFragment implements OnClickListener {

    private static final String TAG_QS_TILE_STYLES = "qs_tile_style";

    private View mView;

    private IOverlayManager mOverlayManager;
    private int mCurrentUserId;
    private Context mContext;

    private static Fragment mParent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOverlayManager = IOverlayManager.Stub.asInterface(
                ServiceManager.getService(Context.OVERLAY_SERVICE));
        mCurrentUserId = ActivityManager.getCurrentUser();
        mContext = getActivity();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mView = LayoutInflater.from(getActivity()).inflate(R.layout.qs_styles_main, null);

        if (mView != null) {
            initView();
            setAlpha(mContext.getResources());
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mView)
                .setNegativeButton(R.string.cancel, this)
                .setNeutralButton(R.string.theme_accent_picker_default, this)
                .setCancelable(false);

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    private void initView() {
        LinearLayout square = mView.findViewById(R.id.QsTileStyleSquare);
        setLayout("1", square);

        LinearLayout roundedsquare = mView.findViewById(R.id.QsTileStyleRoundedSquare);
        setLayout("2", roundedsquare);

        LinearLayout squircle = mView.findViewById(R.id.QsTileStyleSquircle);
        setLayout("3", squircle);

        LinearLayout teardrop = mView.findViewById(R.id.QsTileStyleTearDrop);
        setLayout("4", teardrop);

        LinearLayout circlegradient = mView.findViewById(R.id.QsTileStyleCirclegradient);
        setLayout("5", circlegradient);

        LinearLayout circletrim = mView.findViewById(R.id.QsTileStyleCircletrim);
        setLayout("6", circletrim);

        LinearLayout dottedcircle = mView.findViewById(R.id.QsTileStyleDottedcircle);
        setLayout("7", dottedcircle);

        LinearLayout dualtonecircle = mView.findViewById(R.id.QsTileStyleDualtonecircle);
        setLayout("8", dualtonecircle);

        LinearLayout dualtonecircletrim = mView.findViewById(R.id.QsTileStyleDualtonecircletrim);
        setLayout("9", dualtonecircletrim);

        LinearLayout mountain = mView.findViewById(R.id.QsTileStyleMountain);
        setLayout("10", mountain);

        LinearLayout ninja = mView.findViewById(R.id.QsTileStyleNinja);
        setLayout("11", ninja);

        LinearLayout pokesign = mView.findViewById(R.id.QsTileStylePokesign);
        setLayout("12", pokesign);

        LinearLayout wavey = mView.findViewById(R.id.QsTileStyleWavey);
        setLayout("13", wavey);

        LinearLayout squircletrim = mView.findViewById(R.id.QsTileStyleSquircletrim);
        setLayout("14", squircletrim);

        LinearLayout cookie = mView.findViewById(R.id.QsTileStyleCookie);
        setLayout("15", cookie);

        LinearLayout oreo = mView.findViewById(R.id.QsTileStyleOreo);
        setLayout("16", oreo);

        LinearLayout oreocircletrim = mView.findViewById(R.id.QsTileStyleCircletrimOreo);
        setLayout("17", oreocircletrim);

        LinearLayout oreosquircletrim = mView.findViewById(R.id.QsTileStyleSquircletrimOreo);
        setLayout("18", oreosquircletrim);

        LinearLayout neonlike = mView.findViewById(R.id.QsTileStyleNeonLike);
        setLayout("19", neonlike);

        LinearLayout oxygen = mView.findViewById(R.id.QsTileStyleOOS);
        setLayout("20", oxygen);

        LinearLayout triangles = mView.findViewById(R.id.QsTileStyleTriangles);
        setLayout("21", triangles);

        LinearLayout divided = mView.findViewById(R.id.QsTileStyleDivided);
        setLayout("22", divided);

        LinearLayout cosmos = mView.findViewById(R.id.QsTileStyleCosmos);
        setLayout("23", cosmos);

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        ContentResolver resolver = getActivity().getContentResolver();

        if (which == AlertDialog.BUTTON_NEGATIVE) {
            dismiss();
        }
        if (which == AlertDialog.BUTTON_NEUTRAL) {
            Settings.System.putIntForUser(resolver,
                    Settings.System.QS_TILE_STYLE, 0, mCurrentUserId);
            ((ThemeFragment) mParent).setupQSTileStylesPref();
            dismiss();
        }
    }

    public static void show(Fragment parent) {
        if (!parent.isAdded()) return;

        final QsTileStyles dialog = new QsTileStyles();
        mParent = parent;
        dialog.setTargetFragment(parent, 0);
        dialog.show(parent.getFragmentManager(), TAG_QS_TILE_STYLES);
    }

    @Override
    public int getMetricsCategory() {
        return MetricsProto.MetricsEvent.PIXELDUST;
    }

    private void setLayout(final String style, final LinearLayout layout) {
        final ContentResolver resolver = getActivity().getContentResolver();
        if (layout != null) {
            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Settings.System.putIntForUser(resolver,
                            Settings.System.QS_TILE_STYLE, Integer.parseInt(style), mCurrentUserId);
                    ((ThemeFragment) mParent).setupQSTileStylesPref();
                    dismiss();
                }
            });
        }
    }

    private void setAlpha(Resources res) {
        LinearLayout square = mView.findViewById(R.id.QsTileStyleSquare);
        LinearLayout roundedsquare = mView.findViewById(R.id.QsTileStyleRoundedSquare);
        LinearLayout squircle = mView.findViewById(R.id.QsTileStyleSquircle);
        LinearLayout teardrop = mView.findViewById(R.id.QsTileStyleTearDrop);
        LinearLayout circlegradient = mView.findViewById(R.id.QsTileStyleCirclegradient);
        LinearLayout circletrim = mView.findViewById(R.id.QsTileStyleCircletrim);
        LinearLayout dottedcircle = mView.findViewById(R.id.QsTileStyleDottedcircle);
        LinearLayout dualtonecircle = mView.findViewById(R.id.QsTileStyleDualtonecircle);
        LinearLayout dualtonecircletrim = mView.findViewById(R.id.QsTileStyleDualtonecircletrim);
        LinearLayout mountain = mView.findViewById(R.id.QsTileStyleMountain);
        LinearLayout ninja = mView.findViewById(R.id.QsTileStyleNinja);
        LinearLayout pokesign = mView.findViewById(R.id.QsTileStylePokesign);
        LinearLayout wavey = mView.findViewById(R.id.QsTileStyleWavey);
        LinearLayout squircletrim = mView.findViewById(R.id.QsTileStyleSquircletrim);
        LinearLayout cookie = mView.findViewById(R.id.QsTileStyleCookie);
        LinearLayout oreo = mView.findViewById(R.id.QsTileStyleOreo);
        LinearLayout oreocircletrim = mView.findViewById(R.id.QsTileStyleCircletrimOreo);
        LinearLayout oreosquircletrim = mView.findViewById(R.id.QsTileStyleSquircletrimOreo);
        LinearLayout neonlike = mView.findViewById(R.id.QsTileStyleNeonLike);
        LinearLayout oxygen = mView.findViewById(R.id.QsTileStyleOOS);
        LinearLayout triangles = mView.findViewById(R.id.QsTileStyleTriangles);
        LinearLayout divided = mView.findViewById(R.id.QsTileStyleDivided);
        LinearLayout cosmos = mView.findViewById(R.id.QsTileStyleCosmos);

        if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 1 )) {
            setAlphaForAll(res);
            square.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 2 )) {
            setAlphaForAll(res);
            roundedsquare.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 3 )) {
            setAlphaForAll(res);
            squircle.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 4 )) {
            setAlphaForAll(res);
            teardrop.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 5 )) {
            setAlphaForAll(res);
            circlegradient.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 6 )) {
            setAlphaForAll(res);
            circletrim.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 7 )) {
            setAlphaForAll(res);
            dottedcircle.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 8 )) {
            setAlphaForAll(res);
            dualtonecircle.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 9 )) {
            setAlphaForAll(res);
            dualtonecircletrim.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 10 )) {
            setAlphaForAll(res);
            mountain.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 11 )) {
            setAlphaForAll(res);
            ninja.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 12 )) {
            setAlphaForAll(res);
            pokesign.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 13 )) {
            setAlphaForAll(res);
            wavey.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 14 )) {
            setAlphaForAll(res);
            squircletrim.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 15 )) {
            setAlphaForAll(res);
            cookie.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 16 )) {
            setAlphaForAll(res);
            oreo.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 17 )) {
            setAlphaForAll(res);
            oreocircletrim.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 18 )) {
            setAlphaForAll(res);
            oreosquircletrim.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 19 )) {
            setAlphaForAll(res);
            neonlike.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 20 )) {
            setAlphaForAll(res);
            oxygen.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 21 )) {
            setAlphaForAll(res);
            triangles.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 22 )) {
            setAlphaForAll(res);
            divided.setAlpha((float) 1.0);
        } else if (PixeldustUtils.isUsingQsTileStyles(mOverlayManager, mCurrentUserId, 23 )) {
            setAlphaForAll(res);
            cosmos.setAlpha((float) 1.0);
        } else {
            square.setAlpha((float) 1.0);
            roundedsquare.setAlpha((float) 1.0);
            squircle.setAlpha((float) 1.0);
            teardrop.setAlpha((float) 1.0);
            circlegradient.setAlpha((float) 1.0);
            circletrim.setAlpha((float) 1.0);
            dottedcircle.setAlpha((float) 1.0);
            dualtonecircle.setAlpha((float) 1.0);
            dualtonecircletrim.setAlpha((float) 1.0);
            mountain.setAlpha((float) 1.0);
            ninja.setAlpha((float) 1.0);
            pokesign.setAlpha((float) 1.0);
            wavey.setAlpha((float) 1.0);
            squircletrim.setAlpha((float) 1.0);
            cookie.setAlpha((float) 1.0);
            oreo.setAlpha((float) 1.0);
            oreocircletrim.setAlpha((float) 1.0);
            oreosquircletrim.setAlpha((float) 1.0);
            neonlike.setAlpha((float) 1.0);
            oxygen.setAlpha((float) 1.0);
            triangles.setAlpha((float) 1.0);
            divided.setAlpha((float) 1.0);
            cosmos.setAlpha((float) 1.0);
        }
    }

    private void setAlphaForAll(Resources res) {
        LinearLayout square = mView.findViewById(R.id.QsTileStyleSquare);
        LinearLayout roundedsquare = mView.findViewById(R.id.QsTileStyleRoundedSquare);
        LinearLayout squircle = mView.findViewById(R.id.QsTileStyleSquircle);
        LinearLayout teardrop = mView.findViewById(R.id.QsTileStyleTearDrop);
        LinearLayout circlegradient = mView.findViewById(R.id.QsTileStyleCirclegradient);
        LinearLayout circletrim = mView.findViewById(R.id.QsTileStyleCircletrim);
        LinearLayout dottedcircle = mView.findViewById(R.id.QsTileStyleDottedcircle);
        LinearLayout dualtonecircle = mView.findViewById(R.id.QsTileStyleDualtonecircle);
        LinearLayout dualtonecircletrim = mView.findViewById(R.id.QsTileStyleDualtonecircletrim);
        LinearLayout mountain = mView.findViewById(R.id.QsTileStyleMountain);
        LinearLayout ninja = mView.findViewById(R.id.QsTileStyleNinja);
        LinearLayout pokesign = mView.findViewById(R.id.QsTileStylePokesign);
        LinearLayout wavey = mView.findViewById(R.id.QsTileStyleWavey);
        LinearLayout squircletrim = mView.findViewById(R.id.QsTileStyleSquircletrim);
        LinearLayout cookie = mView.findViewById(R.id.QsTileStyleCookie);
        LinearLayout oreo = mView.findViewById(R.id.QsTileStyleOreo);
        LinearLayout oreocircletrim = mView.findViewById(R.id.QsTileStyleCircletrimOreo);
        LinearLayout oreosquircletrim = mView.findViewById(R.id.QsTileStyleSquircletrimOreo);
        LinearLayout neonlike = mView.findViewById(R.id.QsTileStyleNeonLike);
        LinearLayout oxygen = mView.findViewById(R.id.QsTileStyleOOS);
        LinearLayout triangles = mView.findViewById(R.id.QsTileStyleTriangles);
        LinearLayout divided = mView.findViewById(R.id.QsTileStyleDivided);
        LinearLayout cosmos = mView.findViewById(R.id.QsTileStyleCosmos);

        TypedValue typedValue = new TypedValue();
        res.getValue(R.dimen.qs_styles_layout_opacity, typedValue, true);
        float mLayoutOpacity = typedValue.getFloat();

        square.setAlpha(mLayoutOpacity);
        roundedsquare.setAlpha(mLayoutOpacity);
        squircle.setAlpha(mLayoutOpacity);
        teardrop.setAlpha(mLayoutOpacity);
        circlegradient.setAlpha(mLayoutOpacity);
        circletrim.setAlpha(mLayoutOpacity);
        dottedcircle.setAlpha(mLayoutOpacity);
        dualtonecircle.setAlpha(mLayoutOpacity);
        dualtonecircletrim.setAlpha(mLayoutOpacity);
        mountain.setAlpha(mLayoutOpacity);
        ninja.setAlpha(mLayoutOpacity);
        pokesign.setAlpha(mLayoutOpacity);
        wavey.setAlpha(mLayoutOpacity);
        squircletrim.setAlpha(mLayoutOpacity);
        cookie.setAlpha(mLayoutOpacity);
        oreo.setAlpha(mLayoutOpacity);
        oreocircletrim.setAlpha(mLayoutOpacity);
        oreosquircletrim.setAlpha(mLayoutOpacity);
        neonlike.setAlpha(mLayoutOpacity);
        oxygen.setAlpha(mLayoutOpacity);
        triangles.setAlpha(mLayoutOpacity);
        divided.setAlpha(mLayoutOpacity);
        cosmos.setAlpha(mLayoutOpacity);
    }
}
