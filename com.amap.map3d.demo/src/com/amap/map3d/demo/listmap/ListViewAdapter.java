package com.amap.map3d.demo.listmap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.map3d.demo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shixin on 2018/4/9.
 */

public class ListViewAdapter extends BaseAdapter {
    private static final int VIEWTYPE_TEXT = 0;
    private static final int VIEWTYPE_MAP = 1;
    private Context context;
    private List<BaseEntity> list;
    private List<MapView> mapViews = new ArrayList<>();

    public ListViewAdapter(Context context, List<BaseEntity> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 3 || position == 5 || position == 9 || position == 17) {
            return VIEWTYPE_MAP;
        } else {
            return VIEWTYPE_TEXT;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        BaseEntity baseEntity = list.get(position);
        if (type == VIEWTYPE_MAP) {
            ViewHolder1 mapHolder;
            if (convertView == null) {
                mapHolder = new ViewHolder1();
                convertView = LayoutInflater.from(context).inflate(R.layout.activity_mapview_items, null, true);
                mapHolder.mapView = (MapView) convertView.findViewById(R.id.basicMap);
                mapHolder.mapView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 400));
                mapHolder.mapView.onCreate(null);
                mapViews.add(mapHolder.mapView);
                convertView.setTag(mapHolder);
            } else {
                mapHolder = (ViewHolder1) convertView.getTag();
            }
            MapEntity mapEntity = (MapEntity) baseEntity;
            addElementOnMap(mapHolder.mapView.getMap(), position, mapEntity);
            return convertView;
        } else {
            ViewHolder2 textHolder;
            if (convertView == null) {
                textHolder = new ViewHolder2();
                convertView = LayoutInflater.from(context).inflate(R.layout.feature, null, false);
                convertView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 300));
                textHolder.title = (TextView) convertView.findViewById(R.id.title);
                convertView.setTag(textHolder);
            } else {
                textHolder = (ViewHolder2) convertView.getTag();
            }
            TextEntity textEntity = (TextEntity) baseEntity;
            textHolder.title.setText(textEntity.text);
            return convertView;
        }
    }

    private void addElementOnMap(AMap map, int position, MapEntity mapEntity) {
        try {
            /**
             * 鍥犱负listview 涓殑mapview浼氬鐢紝鎵�浠ユ垜浠瘡娆℃竻绌轰笂涓�娆＄殑element
             */
            map.clear();
            if (position == 3) {
                addMarker(map, mapEntity.point);
            }
            if (position == 5) {
                addPolyline(map, mapEntity.polylinePoints);
            }
            if (position == 17) {
                addPolyline(map, mapEntity.polylinePoints);
            }
            if (position == 9) {
                addPolyline(map, mapEntity.polylinePoints);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addPolyline(AMap map, List<LatLng> polylinePoints) {
        map.addPolyline((new PolylineOptions())
                .addAll(polylinePoints)
                .width(10));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(polylinePoints.get(0), 15));
    }

    public void addMarker(AMap aMap, LatLng D) {
        Marker marker2 = aMap.addMarker(new MarkerOptions().position(D).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher)));
        Marker marker1 = aMap.addMarker(new MarkerOptions().position(new LatLng(D.latitude + 0.00015, D.longitude)).icon(BitmapDescriptorFactory.fromResource(R.drawable.car)));
        marker1.setVisible(true);
        marker2.setVisible(true);
        aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(D, 18));
    }

    static class ViewHolder2 {
        TextView title;
    }

    static class ViewHolder1 {
        MapView mapView;
    }


    /**
     * 鍒楄〃閲岄潰缂撳瓨浜嗗緢澶氬湴鍥惧璞★紝鎵�浠ヤ竴瀹氳璋冪敤閿�姣佹柟娉�
     */
    public void onDestroy() {
        for (MapView mapView : mapViews) {
            mapView.onDestroy();
        }
    }
}
