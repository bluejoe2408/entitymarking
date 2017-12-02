package com.example.bluejoe.myapplication;

import android.util.Log;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.ArrayList;

/**
 * Created by Yunzhe on 2017/11/30.
 * Mention类，用于标注结果的保存和JSON格式输出
 * 包含原文本ID、本句ID、本句文本和ArrayList，用于存放命名实体的标注内容和实体关系的标注内容
 */

class Mention implements Serializable {
    private static final String TAG = "Mention";
    private String articleId;
    private int sentenceId;
    private String sentenceText;
    private ArrayList<EntityMention> entityMentions;
    private ArrayList<RelationMention> relationMentions;

    Mention(String article, int sentenceId, String sentenceText) {
        this.articleId = getMD5(article);
        this.sentenceId = sentenceId;
        this.sentenceText = sentenceText;
        this.entityMentions = new ArrayList<>();
        this.relationMentions = new ArrayList<>();
    }

    /**
     * EntityMention类，单个命名实体
     * 包含该命名实体的文本、类型和在句子中的位置
     */
    class EntityMention implements Serializable {
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

        int getStartIndex() {
            return startIndex;
        }
    }

    /**
     * RelationMention类，单条实体关系
     * 包含一对命名实体在句子中的位置和关系名称
     */
    class RelationMention implements Serializable {
        int firstEntityIndex;
        int secondEntityIndex;
        String relation;

        RelationMention(int firstEntityIndex, int secondEntityIndex, String relation) {
            this.firstEntityIndex = firstEntityIndex;
            this.secondEntityIndex = secondEntityIndex;
            this.relation = relation;
        }

        int getFirstEntityIndex() {
            return firstEntityIndex;
        }

        int getSecondEntityIndex() {
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

        Log.d(TAG, "removeRelation: " + this.relationMentions.size());
        for (int i = 0; i < this.relationMentions.size(); i++) {
            Log.d(TAG, "removingRelation: " + firstEntityIndex + " and " + secondEntityIndex);
            RelationMention element = this.relationMentions.get(i);
            if (element.getFirstEntityIndex() == firstEntityIndex
                    && element.getSecondEntityIndex() == secondEntityIndex) {
                this.relationMentions.remove(i);
                Log.d(TAG, "removedRelation: " + firstEntityIndex + " and " + secondEntityIndex);
                break;
            }
        }
    }

    String getArticleId() {
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

    String getEntityByIndex(int startIndex){
        String entity = null;
        for(int i = 0; i<entityMentions.size(); i++)
            if(entityMentions.get(i).getStartIndex()==startIndex){
                entity = entityMentions.get(i).getEntity();
                break;
            }
        return entity;
    }

    static String getMD5(String message) {
        String md5str = "";
        try {
            // 1 创建一个提供信息摘要算法的对象，初始化为md5算法对象
            MessageDigest md = MessageDigest.getInstance("MD5");

            // 2 将消息变成byte数组
            byte[] input = message.getBytes();

            // 3 计算后获得字节数组,这就是那128位了
            byte[] buff = md.digest(input);

            // 4 把数组每一字节（一个字节占八位）换成16进制连成md5字符串
            md5str = bytesToHex(buff);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5str;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder md5str = new StringBuilder();
        // 把数组每一字节换成16进制连成md5字符串
        int digital;
        for (byte aByte : bytes) {
            digital = aByte;

            if (digital < 0) {
                digital += 256;
            }
            if (digital < 16) {
                md5str.append("0");
            }
            md5str.append(Integer.toHexString(digital));
        }
        return md5str.toString().toUpperCase();
    }
}
