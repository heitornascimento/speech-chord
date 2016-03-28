package com.unicap.tcc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class SpeechChordActivity extends Activity {

	private PackageManager packageManager;
	private Intent it;
	private int REQUEST_CODE_CHORD = 1;
	private ArrayList<String> matches;
	private MediaPlayer mp;
	private String msg;
	private View view;
	private ImageView chord;
	private ImageButton sound;
	private ImageView search;
	private int searchVisibilty = View.INVISIBLE;
	private Spinner spinnerNote;
	private Spinner spinnerQuality;

	private int KEY = 1;
	private int QUALITY = 2;

	private Chords chords;

	private HashMap<String, String> mapQualities;

	private String key;
	private String quality;

	private Button btnOk;
	private Button btnCancel;

	final Dialog dialog = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.main);

		init();

		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				startRecognition();
			}
		});

		/* Image Sound Listener */
		sound.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showResults();
			}
		});

		sound.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					((ImageButton) findViewById(R.id.repeat))
							.setImageResource(R.drawable.repeat_selected);
				}

				if (event.getAction() == MotionEvent.ACTION_UP) {
					((ImageButton) findViewById(R.id.repeat))
							.setImageResource(R.drawable.repeat);
				}

				return false;
			}
		});

		search.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (searchVisibilty == View.INVISIBLE) {
					setDialog();
				}
			}
		});

	}

	public void setDialog() {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_chord);
		dialog.setTitle("Escolha o Acorde");
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		alert.setTitle("Escolha o Acorde");

		final List<String> notes = new ArrayList<String>();
		notes.add("C");
		notes.add("D");
		notes.add("E");
		notes.add("F");
		notes.add("G");
		notes.add("A");
		notes.add("B");

		final List<String> qualities = new ArrayList<String>();
		qualities.add("Maior");
		qualities.add("Menor");

		spinnerNote = (Spinner) dialog.findViewById(R.id.spinner1);

		spinnerQuality = (Spinner) dialog.findViewById(R.id.spinner2);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				notes);

		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(
				getApplicationContext(), android.R.layout.simple_spinner_item,
				qualities);

		spinnerNote.setAdapter(adapter);
		spinnerQuality.setAdapter(adapter2);

		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinnerNote
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int posicao, long id) {
						setMsg(notes.get(posicao), KEY);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
					}

				});

		spinnerQuality
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int posicao, long id) {

						setMsg(qualities.get(posicao), QUALITY);
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});

		btnOk = (Button) dialog.findViewById(R.id.btnOK);
		btnCancel = (Button) dialog.findViewById(R.id.btnCancel);

		/* Dialog */
		btnOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				showResults();
				dialog.cancel();
			}
		});

		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.cancel();
			}
		});

		dialog.show();

	}

	private void init() {

		chord = (ImageView) findViewById(R.id.guitar);
		view = (View) findViewById(R.id.relativeLay);
		sound = (ImageButton) findViewById(R.id.repeat);
		search = (ImageView) findViewById(R.id.search);

		chords = new Chords();
		mapQualities = chords.getQualities();

	}

	public void startRecognition() {

		packageManager = getPackageManager();
		it = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		List<ResolveInfo> activities = packageManager.queryIntentActivities(it,
				0);
		mp = null;

		if (activities.size() == 0) {
			/* Aparelho N‹o Suporta Reconhecimento de Voz */
			Toast.makeText(this,
					getResources().getString(R.string.notSupported),
					Toast.LENGTH_SHORT).show();
		} else {
			it.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
					RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

			it.putExtra(RecognizerIntent.EXTRA_PROMPT, getResources()
					.getString(R.string.speak));

			it.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "pt-BR");

			startActivityForResult(it, REQUEST_CODE_CHORD);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (requestCode == REQUEST_CODE_CHORD && resultCode == RESULT_OK) {
			matches = data
					.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			startScan();

		}

	}

	public void startScan() {

		Parser parser = new Parser(matches, new Chords());
		try {
			msg = parser.startAnalysis();
		} catch (ChordNotFoundException e) {

			e.printStackTrace();
			msg = e.getMessage();
		}

		showResults();
	}

	public void setMsg(String word, int code) {

		if (code == KEY) {
			key = word;
		} else {
			quality = " " + mapQualities.get(word);
		}

		msg = key + quality;
		// showResults();

	}

	private void showResults() {

		if (msg != null) {

			if (msg.equals("A minor")) {

				playChord(R.raw.amenor, R.drawable.la_menor);

			} else if (msg.equals("G major")) {

				playChord(R.raw.gmaior, R.drawable.sol_maior);

			} else if (msg.equals("C major")) {

				playChord(R.raw.cmaior, R.drawable.do_maior);

			} else if (msg.equals("F major")) {
				playChord(R.raw.fmaior, R.drawable.fa_maior);

			} else if (msg.equals("E minor")) {
				playChord(R.raw.emenor, R.drawable.mi_menor);
			} else {
				Toast.makeText(getApplicationContext(),
						getResources().getString(R.string.chordNotFound),
						Toast.LENGTH_SHORT).show();
			}

		}

	}

	public void playChord(int sound, int img) {

		mp = MediaPlayer.create(this, sound);
		chord.setImageDrawable(getResources().getDrawable(img));

		if (mp != null) {
			mp.start();
		}

	}

}