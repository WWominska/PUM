package pl.wroc.uni.ift.android.quizactivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.os.Parcelable;

import static android.app.Activity.RESULT_OK;

public class QuizActivityFragment extends Fragment
{

    private static final String QUESTION_ID = "question_id";
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";
    private static final String KEY_TOKENS = "TokensCount";
    private static final String KEY_QUESTIONS = "questions";

    private static final int CHEAT_REQEST_CODE = 0;
    private int mCheatTokens = 3;

    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPrevButton;
    private Button mListaButton;

    private TextView mQuestionTextView;
    private TextView mAPITextView;

    private Button mCheatButton;
    private QuestionBank mQuestionsBank = QuestionBank.getInstance(); //pobieranie

    private int mCurrentIndex = 0;

    public static QuizActivityFragment newInstance(int id)
    {
        Bundle args = new Bundle();
        args.putInt(QUESTION_ID, id);
        QuizActivityFragment fragment = new QuizActivityFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX);
            mCheatTokens = savedInstanceState.getInt(KEY_TOKENS);
            Log.i(TAG, String.format("onCreate(): Restoring saved index: %d", mCurrentIndex));
            //mQuestionsBank = (Question []) savedInstanceState.getParcelableArray(KEY_QUESTIONS);
            // sanity check
            if (mQuestionsBank == null)
            {
                Log.e(TAG, "Question bank array was not correctly returned from Bundle");

            }
            else
            {
                Log.i(TAG, "Question bank array was correctly returned from Bundle");
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        //super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.activity_quiz, container, false);
        Log.d(TAG, "onCreate() called");

        //setTitle(R.string.app_name);
        // inflating view objects
        //setContentView(R.layout.activity_quiz);

        // check for saved data


        mCheatButton = (Button) view.findViewById(R.id.button_cheat);
        mCheatButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                //boolean currentAnswer = mQuestionsBank[mCurrentIndex].isAnswerTrue();
                boolean currentAnswer = mQuestionsBank.getQuestion(mCurrentIndex).isAnswerTrue();
                boolean IsCheated =  mQuestionsBank.getQuestion(mCurrentIndex).getmIsCheated();
                Intent intent = CheatActivity.newIntent(getActivity(), currentAnswer, IsCheated, mCheatTokens); //wysylanie tokenow do CheatActivity
//
//                Intent intent = new Intent(QuizActivity.this, CheatActivity.class);
//                boolean currentAnswer = mQuestionsBank[mCurrentIndex].isAnswerTrue();
//                intent.putExtra("answer", currentAnswer);

                startActivityForResult(intent, CHEAT_REQEST_CODE);
            }
        });

        mAPITextView = (TextView) view.findViewById(R.id.textView_API);
        mAPITextView.setText("API Level: "+Integer.valueOf(android.os.Build.VERSION.SDK_INT).toString());

        mQuestionTextView = (TextView) view.findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.size();
                updateQuestion();
            }
        });



        mTrueButton = (Button) view.findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(
                new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        checkAnswer(true);
                    }
                }
        );

        mListaButton = (Button) view.findViewById(R.id.button_list);
        mListaButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), QuestionListActivity.class);
                        startActivity(intent);
                    }
                }
        );

        mFalseButton = (Button) view.findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton) view.findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.size();
                updateQuestion();
            }
        });

        mPrevButton = (ImageButton) view.findViewById(R.id.previous_button);
        mPrevButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                if (mCurrentIndex == 0)
                    mCurrentIndex = mQuestionsBank.size() - 1;
                else
                    mCurrentIndex = (mCurrentIndex - 1);
                updateQuestion();
            }
        });

        updateQuestion();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(resultCode != RESULT_OK)
        {
            return;
        }

        if (requestCode == CHEAT_REQEST_CODE)
        {
            if (data != null)
            {
                boolean answerWasShown = CheatActivity.wasAnswerShown(data);
                if (answerWasShown)
                {

                    Toast.makeText(getActivity(),
                            R.string.message_for_cheaters,
                            Toast.LENGTH_LONG)
                            .show();
                    mQuestionsBank.getQuestion(mCurrentIndex).SetmIsCheated(true);
                }
                mCheatTokens = CheatActivity.getCheatTokens(data);
                Log.d("Rezultat", Integer.toString(mCheatTokens));
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, String.format("onSaveInstanceState: current index %d ", mCurrentIndex) );

        //we still have to store current index to correctly reconstruct state of our app
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);

        // because Question is implementing Parcelable interface
        // we are able to store array in Bundle
        //savedInstanceState.putParcelableArray(KEY_QUESTIONS, mQuestionsBank);
        savedInstanceState.putInt(KEY_TOKENS, mCheatTokens);
    }


    private void updateQuestion()
    {
        int question = mQuestionsBank.getQuestion(mCurrentIndex).getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue)
    {
        boolean answerIsTrue = mQuestionsBank.getQuestion(mCurrentIndex).isAnswerTrue();

        int toastMessageId;

        if (userPressedTrue == answerIsTrue)
        {
            toastMessageId = R.string.correct_toast;
        }
        else
        {
            toastMessageId = R.string.incorrect_toast;
        }

        Toast.makeText(getActivity(), toastMessageId, Toast.LENGTH_SHORT).show();
    }
}
