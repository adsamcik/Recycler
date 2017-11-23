package com.adsamcik.tabletest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.adsamcik.table.AppendBehavior;
import com.adsamcik.table.Table;
import com.adsamcik.table.TableAdapter;

public class MainActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//buttons do not have background without this
		getApplicationContext().setTheme(R.style.AppTheme);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		ListView listView = findViewById(R.id.listview);
		TableAdapter adapter = new TableAdapter(this, 16, R.style.AppTheme);
		listView.setAdapter(adapter);

		Table first = new Table(this, 2, false, 16, AppendBehavior.Any);
		first.addData("hello", "world");
		first.addData("hi", "world");
		Table second = new Table(this, 2, true, 16, AppendBehavior.First);
		second.addData("numbered", "world");
		second.addButton("button", view -> {
		});


		Table third = new Table(this, 2, false, 16, AppendBehavior.Any);
		for (int i = 0; i < 10; i++)
			third.addData("data " + i, Integer.toString(i));
		third.setTitle("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum euismod ipsum vel fermentum vulputate. Nulla ultrices quam ut dolor bibendum semper. Quisque placerat cursus ipsum, sit amet rutrum diam porta sed. Aenean arcu est, scelerisque non neque sed, vulputate lacinia risus. In aliquet egestas ullamcorper. Phasellus vitae nunc aliquet, tincidunt metus ut, maximus magna. Nam fringilla porta enim euismod sagittis. Praesent placerat lacinia mauris id tempor. Nullam vulputate, nibh in tincidunt tempus, mauris libero sagittis arcu, a mollis libero tortor non ante. ");


		adapter.add(third);

		for (int i = 0; i < 10; i++) {
			adapter.add(first);
		}

		adapter.add(second);

		adapter.sort();
	}
}
