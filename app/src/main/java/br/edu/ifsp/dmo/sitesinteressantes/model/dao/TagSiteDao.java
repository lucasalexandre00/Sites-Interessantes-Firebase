package br.edu.ifsp.dmo.sitesinteressantes.model.dao;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.edu.ifsp.dmo.sitesinteressantes.model.TagSite;

public class TagSiteDao {
    private SQliteHelper mHelper;
    private SQLiteDatabase mDatabase;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TagSiteDao(){

    }

    public MutableLiveData<Boolean> create(TagSite tag){
        final String id  =  UUID.randomUUID().toString();
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        Map<String, Object> tagMap = new HashMap<>();
        tagMap.put("tagId", id);
        tagMap.put("tag", tag.getTag());
       db.collection("tags")
               .document(id)
               .set(tagMap).addOnSuccessListener(
                       x -> liveData.postValue(true)
               ).addOnFailureListener(
                       x -> liveData.postValue(false)
               );
       return liveData;
    }

    public MutableLiveData<Boolean> update(TagSite oldTagSite, TagSite newTagSite){
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();

        db.collection("tags")
                .document(oldTagSite.getTagId())
                .update("tag", newTagSite.getTag())
                .addOnSuccessListener(
                      x ->  liveData.postValue(true)
                ).addOnFailureListener(
                      x ->  liveData.postValue(false)
                );

        return liveData;
    }

    public MutableLiveData<List<TagSite>> recuperateAll(){
        MutableLiveData<List<TagSite>> liveData = new MutableLiveData<List<TagSite>>();

        db.collection("tags")
                .get()
                .addOnSuccessListener(
                        queryDocumentSnapshots -> {
                            List<TagSite> tags = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                TagSite tagSite = new TagSite(documentSnapshot.getString("tag"));
                                tagSite.setTagId(documentSnapshot.getString("tagId"));
                                tags.add(tagSite);
                            }
                            liveData.postValue(tags);
                        }
                );

        return liveData;
    }

    public int recuperateTagId(TagSite tag){
        int id;
        String columns[] = {DatabaseContracts.TableTag._ID};
        String where = DatabaseContracts.TableTag.COLUMN_TAG + " = ? ";
        String whereArgs[] = {tag.getTag()};

        Cursor cursor;

        mDatabase = mHelper.getReadableDatabase();
        cursor = mDatabase.query(
                DatabaseContracts.TableTag.TABLE_NAME,
                columns,
                where,
                whereArgs,
                null,
                null,
                null
        );

        cursor.moveToNext();
        id = cursor.getInt(0);
        cursor.close();
        mDatabase.close();
        return id;
    }

    public MutableLiveData<Boolean> delete(TagSite tag){
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();

        db.collection("tags")
                .document(tag.getTagId())
                .delete()
                .addOnSuccessListener(
                        x ->  liveData.postValue(true)
                ).addOnFailureListener(
                        x ->  liveData.postValue(false)
                );

        return liveData;
    }
}
