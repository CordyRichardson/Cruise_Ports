package richardson.com.cruiseports;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaceListAdapter extends BaseAdapter {
    private ArrayList<Place> placeList;
    private LayoutInflater inflater;
    String placeWebsite;
    TelephonyManager tm;

    public PlaceListAdapter(Context context, ArrayList<Place>placeList){
        this.placeList = placeList;
        inflater = LayoutInflater.from(context);
        tm = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public int getCount(){
        return placeList.size();
    }

    public View getView(final int position, View convertView, ViewGroup parent){
        final ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.list_row, parent, false);
            holder = new ViewHolder();

            holder.nameTextView = (TextView)convertView.findViewById(R.id.place_name_textview);
            holder.addressTextView = (TextView)convertView.findViewById(R.id.place_address_textview);
            holder.phoneTextView = (TextView)convertView.findViewById(R.id.place_phone_textview);
            holder.webUrlTextView = (TextView)convertView.findViewById(R.id.place_website_textview);
            convertView.setTag(holder);

            holder.webUrlTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    placeWebsite = placeList.get(holder.position).website;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse(placeWebsite));
                    inflater.getContext().startActivity(intent);
                }
            });

        } else{
            holder = (ViewHolder)convertView.getTag();
        }

        holder.nameTextView.setText(placeList.get(position).name);
        holder.addressTextView.setText(placeList.get(position).address);
        holder.phoneTextView.setText(placeList.get(position).phoneNumber);
        holder.position = position;

        if(tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE){
            Linkify.addLinks(holder.phoneTextView, Linkify.PHONE_NUMBERS);
        }
        return convertView;
    }

    public Object getItem(int position){
        return placeList.get(position);
    }

    public long getItemId(int position){
        return position;

    }

    static class ViewHolder{
        TextView nameTextView,
                addressTextView,
                phoneTextView,
                webUrlTextView;
        int position;
    }

}
