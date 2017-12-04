package pl.wroc.uni.ift.android.quizactivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class QuestionListFragment extends Fragment
{

    private RecyclerView mRecyclerView;
    private QuestionBank mQuestionBank = QuestionBank.getInstance();
    private List<Question> dataList = new ArrayList<>();

    private RecyclerView.Adapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)

    {
        //super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_question_list);
        View view = inflater.inflate(R.layout.activity_question_list, container, false);


        mRecyclerView = (RecyclerView) view.findViewById(R.id.Recycler);

        dataList = mQuestionBank.getQuestions();

        mRecyclerView.setHasFixedSize(true);

        // ustawiamy LayoutManagera
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // ustawiamy animatora, który odpowiada za animację dodania/usunięcia elementów listy
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // tworzymy adapter oraz łączymy go z RecyclerView
        mRecyclerView.setAdapter(new Adapter(dataList, mRecyclerView));
        return view;
    }

}