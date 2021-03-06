package com.bignerdranch.android.criminalintent;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.criminalintent.infrastructure.Emotion;
import com.bignerdranch.android.criminalintent.infrastructure.EmotionLab;
import com.bignerdranch.android.criminalintent.infrastructure.EmotionSeries;

import java.util.ArrayList;

/**
 * Created by WuQiang on 2017/4/10.
 */

public class EmotionPageView extends Fragment {
   static private Callbacks callbacks;

    public interface Callbacks {
        void onSendEmotion(String emotionName);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        callbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callbacks = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_emotion_view_pager, container, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.emotion_pager);
        viewPager.setAdapter(new PageViewAdapter(getChildFragmentManager()));
        return view;
    }

    public static EmotionPageView newInstance() {
        return new EmotionPageView();
    }

    private class PageViewAdapter extends FragmentStatePagerAdapter {

        public PageViewAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public String getPageTitle(int position) {
            return "Item";
        }

        @Override
        public Fragment getItem(int position) {
            return new EmotionGridView().newInstance();
        }

        @Override
        public int getCount() {
            return 4;
        }
    }

    public static class EmotionGridView extends Fragment {
        public static EmotionGridView newInstance() {
            return new EmotionGridView();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_emotion_grid_view, container, false);
            RecyclerView emotionRecyclerView = (RecyclerView) view.findViewById(R.id.emotion_recycler_view);
            emotionRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
            emotionRecyclerView.setAdapter(new EmotionAdapter());
            return view;
        }

        private class EmotionAdapter extends RecyclerView.Adapter<EmotionViewHolder> {
            protected ArrayList<EmotionSeries> emotionSeries = new ArrayList<>();
            protected ArrayList<Emotion> emotions = new ArrayList<>();

            public EmotionAdapter() {
                emotionSeries = EmotionLab.getEmotionLab(getActivity()).getEmotionSeries();
                emotions = emotionSeries.get(1).getEmotions();
            }

            @Override
            public EmotionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = getActivity().getLayoutInflater().inflate(R.layout.emotion_thumbnail, parent, false);
                return new EmotionViewHolder(view);
            }

            @Override
            public void onBindViewHolder(EmotionViewHolder holder, int position) {
                holder.bind(emotions.get(position));
            }

            @Override
            public int getItemCount() {
                return emotions.size();
            }
        }

        private class EmotionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            ImageView emotionImageView;
            TextView emotionTextView;
            Emotion emotionn;

            public EmotionViewHolder(View itemView) {
                super(itemView);
                emotionImageView = (ImageView) itemView.findViewById(R.id.emotion_imageview);
                emotionTextView = (TextView) itemView.findViewById(R.id.emotion_textview);
                itemView.setOnClickListener(this);
            }


            public void bind(Emotion emotion) {
                this.emotionn = emotion;
                emotionTextView.setText(emotion.getDescription());
                emotionImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.imgdemo, null));
            }

            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), emotionn.getDescription(), Toast.LENGTH_SHORT).show();
                Log.d("WQ", emotionn.getDescription());
                callbacks.onSendEmotion(emotionn.getDescription());
            }

        }
    }
}
