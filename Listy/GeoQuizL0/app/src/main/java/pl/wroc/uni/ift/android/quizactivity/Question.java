package pl.wroc.uni.ift.android.quizactivity;

/**
 * Created by Yuuki K on 2017-10-23.
 */

public class Question {
    int mResourceId;
    private boolean mAnswerTrue;

    Question(int resourceId, boolean answerTrue ) // konstruktor
    {
        mResourceId=resourceId;
        mAnswerTrue=answerTrue;
    }

    public void setIsAnswerTrue(boolean answerTrue)
    {
        mAnswerTrue=answerTrue;
    }

    public boolean isAnswerTrue()
    {
        return mAnswerTrue;
    } //zwraca prawda lub fałsz

    public int getmResourceId()
    {
        return mResourceId;
    } //zwraca identyfikator pytania (id do którego można się odnieść)
}
