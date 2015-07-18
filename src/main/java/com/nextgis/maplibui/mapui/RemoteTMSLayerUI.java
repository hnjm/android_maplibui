/*
 * Project:  NextGIS Mobile
 * Purpose:  Mobile GIS for Android.
 * Author:   Dmitry Baryshnikov (aka Bishop), bishop.dev@gmail.com
 * Author:   NikitaFeodonit, nfeodonit@yandex.com
 * Author:   Stanislav Petriakov, becomeglory@gmail.com
 * *****************************************************************************
 * Copyright (c) 2012-2015. NextGIS, info@nextgis.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.nextgis.maplibui.mapui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;

import com.nextgis.maplib.datasource.GeoEnvelope;
import com.nextgis.maplib.datasource.ngw.Connection;
import com.nextgis.maplib.map.RemoteTMSLayer;
import com.nextgis.maplibui.R;
import com.nextgis.maplibui.activity.RemoteTMSLayerSettingsActivity;
import com.nextgis.maplibui.api.ILayerUI;
import com.nextgis.maplibui.dialog.SelectNGWResourceDialog;
import com.nextgis.maplibui.dialog.SelectZoomLevelsDialog;

import java.io.File;


public class RemoteTMSLayerUI
        extends RemoteTMSLayer
        implements ILayerUI
{
    public RemoteTMSLayerUI(
            Context context,
            File path)
    {
        super(context, path);
    }


    @Override
    public Drawable getIcon()
    {
        return mContext.getResources().getDrawable(R.drawable.ic_remote_tms);
    }


    @Override
    public void changeProperties(Context context)
    {
        Intent settings = new Intent(context, RemoteTMSLayerSettingsActivity.class);
        settings.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        settings.putExtra(RemoteTMSLayerSettingsActivity.LAYER_ID_KEY, getId());
        context.startActivity(settings);
    }

    public void downloadTiles(Context context, GeoEnvelope env) {
        //TODO: set envelope and layer id
        FragmentActivity fragmentActivity = (FragmentActivity) context;
        SelectZoomLevelsDialog newFragment = new SelectZoomLevelsDialog();
        newFragment.show(fragmentActivity.getSupportFragmentManager(), "select_zoom_levels");
    }
}
