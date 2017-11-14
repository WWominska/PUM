package pl.wroc.uni.ift.android.quizactivity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jpola on 26.07.17.
 */

// Parcelable - daje możliwość przesłania całej klasy do Bundle
public class Question implements Parcelable
{

    private int mTextResId;
    private boolean mAnswerTrue;
    private boolean mIsCheated;

    protected Question(Parcel in)
    {
        mTextResId = in.readInt();
        mAnswerTrue = in.readByte() !=0;
        mIsCheated = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(mTextResId);
        dest.writeByte((byte) (mAnswerTrue ? 1 : 0));
        dest.writeByte((byte) (mIsCheated ? 1 : 0));
    }

    @Override
    public int describeContents()
    {
        return 0;
    }



    public static final Creator<Question> CREATOR = new Creator<Question>()
    {
        @Override
        public Question createFromParcel(Parcel in)
        {
            return new Question(in);
        }
        @Override
        public Question[] newArray(int size)
        {
            return new Question[size];
        }
    };

    public boolean  getmIsCheated()
    {
        return mIsCheated;
    }

    public void SetmIsCheated(boolean mIsCheated)
    {
        this.mIsCheated = mIsCheated;
    }

    public Question(int textResId, boolean answerTrue)
    {
        mTextResId = textResId;
        mAnswerTrue = answerTrue;
        mIsCheated = false;
    }

    public int getTextResId()
    {
        return mTextResId;
    }

    public void setTextResId(int textResId)
    {
        mTextResId = textResId;
    }

    public boolean isAnswerTrue()
    {
        return mAnswerTrue;
    }

    public void setAnswerTrue(boolean answerTrue)
    {
        mAnswerTrue = answerTrue;
    }
}