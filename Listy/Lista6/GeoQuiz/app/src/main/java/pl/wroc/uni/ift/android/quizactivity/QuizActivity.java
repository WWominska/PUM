package pl.wroc.uni.ift.android.quizactivity;

import android.support.v4.app.Fragment;

public class QuizActivity extends SingleFragmentActivity
{

    @Override
    protected Fragment createFragment()
    {
        return new QuizActivityFragment();
    }
}