package pl.wroc.uni.ift.android.quizactivity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageButton;

public class QuizActivity extends AppCompatActivity {


    private Button mTrueButton;
    private Button mFalseButton;
    private ImageButton mNextButton;
    private ImageButton mPreviousButton;

    private TextView mQuestionTextView;

    private Question[] mQuestionsBank = new Question[]{
            new Question(R.string.question_stolica_polski, true),
            new Question(R.string.question_stolica_dolnego_slaska, false),
            new Question(R.string.question_sniezka, true),
            new Question(R.string.question_wisla, true)
    };

    private int mCurrentIndex = 0;
    private int mAnswerYup = 0; //przechowuje poprawne odpowiedzi
    private int mAnsweredAnswer = 0;
    //private boolean End = false; // sprawdza czy jest koniec programu

    //    Bundles are generally used for passing data between various Android activities.
    //    It depends on you what type of values you want to pass, but bundles can hold all
    //    types of values and pass them to the new activity.
    //    see: https://stackoverflow.com/questions/4999991/what-is-a-bundle-in-an-android-application
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.app_name);
        // inflating view objects
        setContentView(R.layout.activity_quiz);

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);
        mQuestionTextView.setOnClickListener(new View.OnClickListener() { //ustawienie po kliknięciu zmiany pytania
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                updateQuestion();
            }
        });

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkAnswer(true);
                    }
                }
        );

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        mNextButton = (ImageButton) findViewById(R.id.next_button);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionsBank.length;
                updateQuestion();
            }
        });

        mPreviousButton = (ImageButton) findViewById(R.id.previous_button);
        mPreviousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentIndex -= 1;
                if (mCurrentIndex<0) mCurrentIndex=mQuestionsBank.length-1;
                updateQuestion();
            }
        });

        updateQuestion();
    }

    private void updateQuestion() {
        int question = mQuestionsBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue)
    {
        Question question = mQuestionsBank[mCurrentIndex];
        boolean answerIsTrue = question.isAnswerTrue();

        int toastMessageId = 0;
        if (!question.checkIsAnswered() && mAnsweredAnswer != mQuestionsBank.length) {
            if (userPressedTrue == answerIsTrue) {
                toastMessageId = R.string.correct_toast;
                mAnswerYup++;
            } else {
                toastMessageId = R.string.incorrect_toast;
            }
            question.setAnswered(true);
            mAnsweredAnswer++;
        }
        else
        {
            toastMessageId = R.string.already_answered;
        }
            Toast toast = Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT); //zapisywanie do zmiennej powiadomienia
            toast.setGravity(Gravity.TOP, 0, 175); //ustawienie położenia
            toast.show(); //wyświetlanie powiedomienia

        if (mAnsweredAnswer==mQuestionsBank.length)
        {
            toastMessageId = R.string.final_notification;
            String message = getString(toastMessageId)+" "+mAnswerYup+" / "+mQuestionsBank.length;
            Toast toast1 = Toast.makeText(this, message, Toast.LENGTH_SHORT);
            toast1.setGravity(Gravity.BOTTOM,0,0);
            toast1.show();
        }
    }

}