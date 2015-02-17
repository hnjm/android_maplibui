/*
 * Project:  NextGIS Mobile
 * Purpose:  Mobile GIS for Android.
 * Author:   Dmitry Baryshnikov (aka Bishop), bishop.dev@gmail.com
 * *****************************************************************************
 * Copyright (c) 2012-2015. NextGIS, info@nextgis.com
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.nextgis.maplibui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nextgis.maplib.datasource.GeoPoint;
import com.nextgis.maplib.map.Layer;
import com.nextgis.maplib.api.MapEventListener;
import com.nextgis.maplib.map.MapEventSource;
import com.nextgis.maplibui.api.ILayerUI;

import static com.nextgis.maplib.util.Constants.*;


/**
 * An adapter to show layers as list
 */
public class LayersListAdapter
        extends BaseAdapter
        implements MapEventListener
{

    protected final MapEventSource mMap;
    protected final Context        mContext;


    public LayersListAdapter(
            Context context,
            MapEventSource map)
    {
        mMap = map;
        mContext = context;

        if(null != mMap)
            mMap.addListener(this);
    }


    @Override
    protected void finalize()
            throws Throwable
    {
        if(null != mMap)
            mMap.removeListener(this);
        super.finalize();
    }


    @Override
    public int getCount()
    {
        if(null != mMap)
            return mMap.getLayerCount();
        return 0;
    }


    @Override
    public Object getItem(int i)
    {
        int nIndex = getCount() - 1 - i;
        if(null != mMap)
            return mMap.getLayer(nIndex);
        return null;
    }


    @Override
    public long getItemId(int i)
    {
        if(i < 0 || i >= mMap.getLayerCount())
            return NOT_FOUND;
        Layer layer = (Layer) getItem(i);
        if(null != layer)
            return layer.getId();
        return NOT_FOUND;
    }


    @Override
    public View getView(
            int i,
            View view,
            ViewGroup viewGroup)
    {
        final Layer layer = (Layer) getItem(i);
        switch (layer.getType()) {
            case LAYERTYPE_REMOTE_TMS:
            default:
                return getStandardLayerView(layer, view);
        }
    }


    protected View getStandardLayerView(
            final Layer layer,
            View view)
    {
        View v = view;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.layout_layer_row, null);
        }

        final ILayerUI layerui;
        if (layer instanceof ILayerUI) {
            layerui = (ILayerUI) layer;
        } else {
            layerui = null;
        }


        if (layerui != null) {
            ImageView ivIcon = (ImageView) v.findViewById(R.id.ivIcon);
            ivIcon.setImageDrawable(layerui.getIcon());
        }

        TextView tvPaneName = (TextView) v.findViewById(R.id.tvLayerName);
        tvPaneName.setText(layer.getName());

        //final int id = layer.getId();

        ImageButton btShow = (ImageButton) v.findViewById(R.id.btShow);
        //Log.d(TAG, "Layer #" + id + " is visible " + layer.isVisible());
        btShow.setBackgroundResource(layer.isVisible()
                                     ? R.drawable.ic_action_visibility_on
                                     : R.drawable.ic_action_visibility_off);
        //btShow.refreshDrawableState();
        btShow.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                //Layer layer = mMap.getLayerById(id);
                layer.setVisible(!layer.isVisible());
                layer.save();
            }
        });

        ImageButton btSettings = (ImageButton) v.findViewById(R.id.btSettings);
        if (layerui != null) {
            btSettings.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View arg0)
                {
                    //Layer layer = mMap.getLayerById(id);
                    layerui.changeProperties();
                }
            });
        } else {
            btSettings.setEnabled(false);
        }
        ImageButton btDelete = (ImageButton) v.findViewById(R.id.btDelete);
        btDelete.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View arg0)
            {
                layer.delete();
                mMap.save();
            }
        });

        return v;
    }


    @Override
    public void onLayerAdded(int id)
    {
        notifyDataSetChanged();
    }


    @Override
    public void onLayerDeleted(int id)
    {
        notifyDataSetChanged();
    }


    @Override
    public void onLayerChanged(int id)
    {
        notifyDataSetChanged();
    }


    @Override
    public void onExtentChanged(
            float zoom,
            GeoPoint center)
    {

    }


    @Override
    public void onLayersReordered()
    {
        notifyDataSetChanged();
    }


    @Override
    public void onLayerDrawFinished(
            int id,
            float percent)
    {

    }

    public void swapElements(
            int originalPosition,
            int newPosition)
    {
        //Log.d(TAG,
        //      "Original position: " + originalPosition + " Destination position: " + newPosition);
        if(null == mMap)
            return;
        int newPositionFixed = getCount() - 1 - newPosition;
        mMap.moveLayer(newPositionFixed, (com.nextgis.maplib.api.ILayer) getItem(originalPosition));
        notifyDataSetChanged();
    }


    public void endDrag()
    {
        if(null == mMap)
            return;
        mMap.thaw();
        mMap.runDraw(null);
    }


    public void beginDrag()
    {
        if(null == mMap)
            return;
        mMap.freeze();
    }
}
