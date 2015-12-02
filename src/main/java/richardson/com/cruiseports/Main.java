package richardson.com.cruiseports;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;


public class Main extends Activity {
    GridView gridView;              //main activity grid of port names; used as menu
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;                             //will no longer need this; remove
        gridView = (GridView)findViewById(R.id.main_gridview);

        gridView.setAdapter(new ArrayAdapter<>(this, R.layout.gridview_item,
                getResources().getStringArray(R.array.port_names)));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                Intent intent = new Intent(context, PortDetailActivity.class); //2nd activity (tabs)
                intent.putExtra("port", pos);
                startActivity(intent);
            }
        });

    }
}
