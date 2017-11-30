package com.example.bluejoe.myapplication;

import java.util.ArrayList;

/**
 * Created by Yunzhe on 2017/11/30.
 *
 */

class Mention {
    private String articleId;
    private int sentenceId;
    private String sentenceText;
    private ArrayList<EntityMention> entityMentions;
    private ArrayList<RelationMention> relationMentions;

    Mention(String articleId, int sentenceId, String sentenceText) {
        this.articleId = articleId;
        this.sentenceId = sentenceId;
        this.sentenceText = sentenceText;
        this.entityMentions = new ArrayList<>();
        this.relationMentions = new ArrayList<>();
    }

    class EntityMention {
        String entity;
        String type;
        int startIndex;

        EntityMention(String entity, String type, int startIndex) {
            this.entity = entity;
            this.type = type;
            this.startIndex = startIndex;
        }

        public String getEntity() {
            return entity;
        }

        public String getType() {
            return type;
        }

        public int getStartIndex() {
            return startIndex;
        }
    }

    class RelationMention {
        int firstEntityIndex;
        int secondEntityIndex;
        String relation;

        RelationMention(int firstEntityIndex, int secondEntityIndex, String relation) {
            this.firstEntityIndex = firstEntityIndex;
            this.secondEntityIndex = secondEntityIndex;
            this.relation = relation;
        }

        public int getFirstEntityIndex() {
            return firstEntityIndex;
        }

        public int getSecondEntityIndex() {
            return secondEntityIndex;
        }

        public String getRelation() {
            return relation;
        }
    }

    void addEntity (String entity, String type, int startIndex) {
        this.entityMentions.add(new EntityMention(entity, type, startIndex));
    }

    void removeEntity (int startIndex) {
        for (int i = 0; i < this.entityMentions.size() - 1; i++) {
            if (this.entityMentions.get(i).getStartIndex() == startIndex) {
                this.entityMentions.remove(i);
                break;
            }
        }
        for (int i = 0; i < this.relationMentions.size() - 1; i++) {
            RelationMention element = this.relationMentions.get(i);
            if (element.getFirstEntityIndex() == startIndex
                    || element.getSecondEntityIndex() == startIndex) {
                this.relationMentions.remove(i);
            }
        }
    }

    void addRelation (int firstEntityIndex, int secondEntityIndex, String relation) {
        this.relationMentions.add(new RelationMention(firstEntityIndex, secondEntityIndex, relation));
    }

    void removeRelation (int firstEntityIndex, int secondEntityIndex) {
        for (int i = 0; i < this.relationMentions.size() - 1; i++) {
            RelationMention element = this.relationMentions.get(i);
            if (element.getFirstEntityIndex() == firstEntityIndex
                    && element.getSecondEntityIndex() == secondEntityIndex) {
                this.relationMentions.remove(i);
                break;
            }
        }
    }

    public String getArticleId() {
        return articleId;
    }

    public int getSentenceId() {
        return sentenceId;
    }

    public String getSentenceText() {
        return sentenceText;
    }

    public ArrayList<EntityMention> getEntityMentions() {
        return entityMentions;
    }

    public ArrayList<RelationMention> getRelationMentions() {
        return relationMentions;
    }
}
