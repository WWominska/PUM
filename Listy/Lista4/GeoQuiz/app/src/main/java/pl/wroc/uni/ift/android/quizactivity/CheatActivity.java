package pl.wroc.uni.ift.android.quizactivity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

public class CheatActivity extends AppCompatActivity
{

    private final static String EXTRA_KEY_ANSWER = "Answer";
    private final static String KEY_QUESTION = "QUESTION";
    private final static String EXTRA_KEY_SHOWN = "wasShown";
    private final static String KEY_TOKENS = "CheatTokens";
    TextView mTextViewAnswer;
    TextView mCheatTokensTextView; // napis o tokenach
    Button mButtonShow;


    boolean mAnswer;
    boolean wasShown;
    int mCheatTokens; //zmienna dls tokenow

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean("mAnswer", mAnswer);
        savedInstanceState.putBoolean("wasShown", wasShown);
        savedInstanceState.putInt("CheatTokens", mCheatTokens); //wysyłanie do stanu (po obrocie ekranu dane znikajo)
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        if(savedInstanceState != null)
        {
            setAnswerShown(savedInstanceState.getBoolean("wasShow"));//odbieranie ze stanu
        }
        else
        {
            setAnswerShown(false);
        }
        setContentView(R.layout.activity_cheat);
        mCheatTokensTextView = (TextView) findViewById(R.id.cheatTokensTextView);
        mCheatTokens = getIntent().getIntExtra(KEY_TOKENS, 0);
        updateTokenCount();
        mAnswer = getIntent().getBooleanExtra(EXTRA_KEY_ANSWER,false);
        mTextViewAnswer = (TextView) findViewById(R.id.text_view_answer);
        mButtonShow = (Button) findViewById(R.id.button_show_answer);
        mButtonShow.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                int toastMessageId;
                if (mCheatTokens <= 0) {
                    toastMessageId = R.string.NoTokens;
                } else {
                    if (mAnswer) {
                        mTextViewAnswer.setText("Prawda");
                    } else {
                        mTextViewAnswer.setText("Fałsz");
                    }
                    setAnswerShown(true);
                    wasShown = true;
                    toastMessageId = R.string.UsedToken;
                    mCheatTokens--;
                    updateTokenCount();
                }
                Toast.makeText(CheatActivity.this, toastMessageId, Toast.LENGTH_SHORT).show();
            }
        });

        if(getIntent().getBooleanExtra(EXTRA_KEY_SHOWN, false))
        {
            if(mAnswer)
            {
                mTextViewAnswer.setText("Prawda");
            }
            else
            {
                mTextViewAnswer.setText("Fałsz");
            }
        }
        setAnswerShown(false);
        wasShown=false;
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
        wasShown = savedInstanceState.getBoolean(EXTRA_KEY_SHOWN);
        if(wasShown)
        {
            if(mAnswer==true)
            {
                mTextViewAnswer.setText("Prawda");
            }
            else
            {
                mTextViewAnswer.setText("Fałsz");
            }
        }
        setAnswerShown(wasShown);
        mCheatTokens = savedInstanceState.getInt(KEY_TOKENS);
        updateTokenCount();
    }

    public static boolean wasAnswerShown(Intent data)
    {
        return data.getBooleanExtra(EXTRA_KEY_SHOWN, false);
    }

    public static Intent newIntent(Context context, boolean answerIsTrue, boolean wasShown, int CheatTokens)
    {
        Intent intent = new Intent(context, CheatActivity.class);
        intent.putExtra(EXTRA_KEY_ANSWER, answerIsTrue);
        intent.putExtra(EXTRA_KEY_SHOWN, wasShown);
        intent.putExtra(KEY_TOKENS, CheatTokens); //wysylanie
        return intent;

    }

    private void setAnswerShown (boolean isAnswerShown)
    {
        Intent data = new Intent();
        data.putExtra("wasShown", isAnswerShown);
        setResult(RESULT_OK, data);
    }
    
    private void updateTokenCount()
    {
        mCheatTokensTextView.setText("Dostępnych: "+Integer.toString(mCheatTokens)); //wyswietlanie tokenow
        Intent data = new Intent();
        data.putExtra(KEY_TOKENS, mCheatTokens);//wysylanie tokenow do QuizActivity
        setResult(RESULT_OK, data);

    }

    public static int getCheatTokens(Intent data)
    {
        int a = data.getIntExtra(KEY_TOKENS, 0);
        Log.d("Tokens get",Integer.toString(a));
        return a;

    }
}