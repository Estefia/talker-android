package ar.uba.fi.talker;

import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import ar.uba.fi.talker.action.userlog.TextDialogFragment;
import ar.uba.fi.talker.action.userlog.TextDialogFragment.TextDialogListener;
import ar.uba.fi.talker.component.ComponentType;
import ar.uba.fi.talker.fragment.EraseAllConfirmationDialogFragment;
import ar.uba.fi.talker.fragment.EraseAllConfirmationDialogFragment.EraseAllConfirmationDialogListener;
import ar.uba.fi.talker.fragment.InsertImageDialogFragment;
import ar.uba.fi.talker.fragment.InsertImageDialogFragment.InsertImageDialogListener;
import ar.uba.fi.talker.view.Scenario;

public class CanvasActivity extends ActionBarActivity implements
		TextDialogListener, InsertImageDialogListener, EraseAllConfirmationDialogListener {

	final String TAG = "CanvasActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.canvas_default);
		final Scenario scenario = (Scenario) findViewById(R.id.gestureOverlayView1);
		if(getIntent().hasExtra("BMP")) {
		    Bundle extras = getIntent().getExtras();
		    byte[] bytes = extras.getByteArray("BMP");
		    Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		    
		    scenario.setBackgroundImage(image);

		}

		ImageButton pencilOp = (ImageButton) findViewById(R.id.pencilOption);
		pencilOp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scenario.setActiveComponentType(ComponentType.PENCIL);
				scenario.invalidate();
			}
		});

		ImageButton eraserOp = (ImageButton) findViewById(R.id.eraserOption);
		eraserOp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scenario.setActiveComponentType(ComponentType.ERASER);
				scenario.invalidate();
			}
		});

		ImageButton eraseAllOp = (ImageButton) findViewById(R.id.eraseAllOption);
		eraseAllOp.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				DialogFragment newFragment = new EraseAllConfirmationDialogFragment();
				newFragment.show(getSupportFragmentManager(), "erase_all");
				scenario.invalidate();
			}
		});

		ImageButton textOp = (ImageButton) findViewById(R.id.textOption);
		textOp.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new TextDialogFragment();
				newFragment.show(getSupportFragmentManager(), "insert_text");
			}
		});

		ImageButton imageOption = (ImageButton) findViewById(R.id.insertImageOption);
		imageOption.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				scenario.setActiveComponentType(ComponentType.IMAGE);
				DialogFragment newFragment = new InsertImageDialogFragment();
				newFragment.show(getSupportFragmentManager(), "insert_image");
				scenario.invalidate();
			}
		});

	}

	@Override
	public void onDialogPositiveClickTextDialogListener(DialogFragment dialog) {
		Dialog dialogView = dialog.getDialog();
		EditText inputText = (EditText) dialogView
				.findViewById(R.id.insert_text_input);
		Scenario s = (Scenario) findViewById(R.id.gestureOverlayView1);
		s.setText(inputText.getText());
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onDialogPositiveClickInsertImageDialogListener(Uri uri) {

		final Scenario scenario = (Scenario) findViewById(R.id.gestureOverlayView1);
		
		Bitmap ima1;
		try {
			ima1 = Media.getBitmap(this.getContentResolver(), uri);
			scenario.addImage(ima1);
		} catch (FileNotFoundException e) {
			Toast.makeText(this, "Ocurrio un error con la imagen.",	Toast.LENGTH_SHORT).show();
			Log.e("CANVAS", "Unexpected error adding imagen.", e);
		} catch (IOException e) {
			Toast.makeText(this, "Ocurrio un error con la imagen.",	Toast.LENGTH_SHORT).show();
			Log.e("CANVAS", "Unexpected error adding imagen.", e);
		}
	}
	
	@Override
	public void onDialogPositiveClickEraseAllConfirmationListener(
			DialogFragment dialog) {
		final Scenario scenario = (Scenario) findViewById(R.id.gestureOverlayView1);
		scenario.clear();
	}

}
