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
import java.util.List;

import br.edu.ifsp.dmo.sitesinteressantes.model.TagSite;

public class TagSiteDao {
    private SQliteHelper mHelper;
    private SQLiteDatabase mDatabase;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public TagSiteDao(){

    }

    public void create(TagSite tag){
        ContentValues values = new ContentValues();
        values.put(DatabaseContracts.TableTag.COLUMN_TAG, tag.getTag());

        mDatabase = mHelper.getWritableDatabase();
        mDatabase.insert(
                DatabaseContracts.TableTag.TABLE_NAME,
                null,
                values
        );

        mDatabase.close();
    }

    public boolean update(TagSite tagOld, TagSite tagNew){
        boolean answer;
        ContentValues values = new ContentValues();
        values.put(DatabaseContracts.TableTag.COLUMN_TAG, tagNew.getTag());

        String where = DatabaseContracts.TableTag.COLUMN_TAG + " = ? ";

        String whereArgs[] = {tagOld.getTag()};

        try {
            mDatabase = mHelper.getWritableDatabase();
            mDatabase.update(DatabaseContracts.TableTag.TABLE_NAME,
                    values,
                    where,
                    whereArgs);
            answer = true;
        }catch (Exception e){
            answer = false;
        }
        return answer;
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

    public void delete(TagSite tag){
        String where = DatabaseContracts.TableTag.COLUMN_TAG + " = ? ";
        String whereArgs[] = {tag.getTag()};

        mDatabase = mHelper.getWritableDatabase();
        mDatabase.delete(DatabaseContracts.TableTag.TABLE_NAME,
                where,
                whereArgs);
        mDatabase.close();
    }
}
