package com.walter.lesson14;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	ListView list;
	CustomAdapter adapter;
	ArrayList<Book> books_array;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		list = (ListView) findViewById(R.id.listView1);
		books_array = new ArrayList<Book>();
		adapter = new CustomAdapter(this, books_array);
		list.setAdapter(adapter);
		String url = "http://10.0.2.2/lesson13/fetch.php";
		AsyncHttpClient client = new AsyncHttpClient();
		client.get(url, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] response) {
				String json = new String(response);
				Log.d("JSON", json);
				Gson g = new Gson();
				Book[] books = g.fromJson(json, Book[].class);
				books_array.addAll(Arrays.asList(books));
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				Toast.makeText(getApplicationContext(), "Failed to Fetch",
						Toast.LENGTH_SHORT).show();
			}
		});
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				Book b = books_array.get(pos);
				Toast.makeText(getApplicationContext(),
						b.getTitle() + " " + b.getAuthor(), Toast.LENGTH_SHORT)
						.show();
			}
		});
        registerForContextMenu(list);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) {
		menu.add("Delete");
		menu.add("Share");
		menu.add("Edit");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{
		AdapterContextMenuInfo info=(AdapterContextMenuInfo)item.getMenuInfo();
		int pos = info.position;
		Book bb = books_array.get(pos);
		if(item.getTitle().equals("Delete"))
		{
		  books_array.remove(pos);
		  adapter.notifyDataSetChanged();
		}
		else if(item.getTitle().equals("Share"))
		{
		  Intent i=new Intent(Intent.ACTION_SEND);
		  i.setType("text/plain");
		  i.putExtra(Intent.EXTRA_TEXT, bb.getTitle()+" "+bb.getAuthor());
		  startActivity(i);
		}
		return super.onContextItemSelected(item);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
