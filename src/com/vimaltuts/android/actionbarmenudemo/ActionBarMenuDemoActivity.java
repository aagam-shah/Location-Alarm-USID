package com.vimaltuts.android.actionbarmenudemo;

import java.util.ArrayList;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ActionBarMenuDemoActivity extends ListActivity {
	private static final String[] items = { "Android", "Bluetooth", "Chrome",
			"Docs", "Email", "Facebook", "Google", "Hungary", "Iphone",
			"Korea", "Machintosh", "Nokia", "Orkut", "Picasa", "Singapore",
			"Talk", "Windows", "Youtube" };
	private ArrayList<String> words = null;

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		initAdapter();
		registerForContextMenu(getListView());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		new MenuInflater(this).inflate(R.menu.option, menu);

		EditText add = null;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			View v = menu.findItem(R.id.add).getActionView();

			if (v != null) {
				add = (EditText) v.findViewById(R.id.title);
			}
		}

		if (add != null) {
			add.setOnEditorActionListener(onSearch);
		}

		return (super.onCreateOptionsMenu(menu));
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		new MenuInflater(this).inflate(R.menu.context, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.add:
			add();
			return (true);

		case R.id.reset:
			initAdapter();
			return (true);

		case R.id.about:
			Toast.makeText(this, "Action Bar Menu Example", Toast.LENGTH_LONG)
					.show();
			return (true);
		}

		return (super.onOptionsItemSelected(item));
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();

		switch (item.getItemId()) {
		case R.id.cap:
			String word = words.get(info.position);

			word = word.toUpperCase();

			adapter.remove(words.get(info.position));
			adapter.insert(word, info.position);

			return (true);

		case R.id.remove:
			adapter.remove(words.get(info.position));

			return (true);
		}

		return (super.onContextItemSelected(item));
	}

	private void initAdapter() {
		words = new ArrayList<String>();

		for (String s : items) {
			words.add(s);
		}

		setListAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, words));
	}

	private void add() {
		final View addView = getLayoutInflater().inflate(R.layout.add, null);

		new AlertDialog.Builder(this).setTitle("Add a Word").setView(addView)
				.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						addWord((TextView) addView.findViewById(R.id.title));
					}
				}).setNegativeButton("Cancel", null).show();
	}

	@SuppressWarnings("unchecked")
	private void addWord(TextView title) {
		ArrayAdapter<String> adapter = (ArrayAdapter<String>) getListAdapter();

		adapter.add(title.getText().toString());
		Toast.makeText(ActionBarMenuDemoActivity.this,
				title.getText().toString() + " is Entered", Toast.LENGTH_LONG)
				.show();

		title.setText("");
	}

	private TextView.OnEditorActionListener onSearch = new TextView.OnEditorActionListener() {
		public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
			if (event == null || event.getAction() == KeyEvent.ACTION_UP) {
				addWord(v);

				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
			}

			return (true);
		}
	};
}
