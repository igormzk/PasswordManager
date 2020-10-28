package com.imeg.passwordmanager.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.imeg.passwordmanager.R;
import com.imeg.passwordmanager.db.SQLiteManager;
import com.imeg.passwordmanager.model.Spot;

import java.util.ArrayList;
import java.util.List;

public class SpotViewActivity extends AppCompatActivity {

    private ListView listView;
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_spot_view);
        listView = findViewById(R.id.listView);
        SQLiteManager sql = new SQLiteManager(this);
        sql.open();
        final List<Spot> listSpot = sql.loadSpot();
        final List listDisplay = new ArrayList();
        for (Spot s : listSpot) {
            listDisplay.add(s.toString());
        }
        sql.close();

        final ArrayAdapter<Spot> listAdapterSpot = new ArrayAdapter<Spot>(this, android.R.layout.simple_list_item_1, listDisplay){

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                if (convertView == null) {
                    convertView = new TextView(context);
                }
                if (position % 2 == 0) {
                    ((TextView)convertView).setBackgroundColor(Color.argb(255, 219,219,219));
                }
                Spannable word = new SpannableString(listDisplay.get(position).toString());
                word.setSpan(new ForegroundColorSpan(Color.BLUE), 0, word.toString().split("\n")[0].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                listDisplay.set(position, word);

                return super.getView(position, convertView, parent);
            }
        };

        listView.setAdapter(listAdapterSpot);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setTitle("Excluir");
                final Spot spot = listSpot.get(i);
                final Object obj = listDisplay.get(i);
                alert.setMessage("Quer deletar a senha " + spot.getDescription() + "?");
                alert.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SQLiteManager sql = new SQLiteManager(context);
                        sql.open();
                        long result = sql.deleteSpot(spot.getId());
                        sql.close();
                        String str;
                        if (result == -1) {
                            str = "Erro ao deletar senha";
                        } else {
                            str = "Senha excluída!";
                        }
                        Toast.makeText(getApplicationContext(), str, Toast.LENGTH_LONG).show();
                        listSpot.remove(spot);
                        listDisplay.remove(obj);
                        listAdapterSpot.notifyDataSetChanged();

                    }
                });
                alert.setNegativeButton("NÃO", null);
                alert.show();

                return false;
            }
        });

    }

}