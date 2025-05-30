package com.example.improvedpersonalisedlearningexperienceapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.widget.ArrayAdapter;
import com.example.improvedpersonalisedlearningexperienceapp.R;

import java.util.List;

public class QuizAdapter extends ArrayAdapter<String> {

    private final Context mContext;
    private final List<String> quizList;

    public QuizAdapter(@NonNull Context context, @NonNull List<String> quizItems) {
        super(context, 0, quizItems);
        this.mContext = context;
        this.quizList = quizItems;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String item = quizList.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.quiz_item_card, parent, false);
        }

        CardView cardView = convertView.findViewById(R.id.quizCard);
        TextView textView = convertView.findViewById(R.id.quizText);

        textView.setText(item);

        return convertView;
    }
}
