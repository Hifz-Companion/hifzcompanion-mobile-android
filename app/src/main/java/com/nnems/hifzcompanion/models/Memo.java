package com.nnems.hifzcompanion.models;

import java.util.Map;

public class Memo {
    private String mPlan;
    private String mTarget;
    private Map<String, Object> mCards;

    public Memo(String plan, String target, Map<String, Object> cards) {
        mPlan = plan;
        mTarget = target;
        mCards = cards;
    }

    public Memo() {
    }

}
