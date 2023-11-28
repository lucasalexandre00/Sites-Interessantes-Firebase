package br.edu.ifsp.dmo.sitesinteressantes.model.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.lifecycle.MutableLiveData;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import br.edu.ifsp.dmo.sitesinteressantes.model.Site;
import br.edu.ifsp.dmo.sitesinteressantes.model.TagSite;

public class SiteDao {

    private SQliteHelper mHelper;

    private SQLiteDatabase mDatabase;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public SiteDao(){}

    public MutableLiveData<Boolean> create(Site site){final String id  =  UUID.randomUUID().toString();
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();
        Map<String, Object> tagMap = new HashMap<>();
        tagMap.put("id", id);
        tagMap.put("title", site.getTitle());
        tagMap.put("url", site.getUrl());
        tagMap.put("tagId", site.getTag().getTagId());
        db.collection("sites")
                .document(id)
                .set(tagMap).addOnSuccessListener(
                        x -> liveData.postValue(true)
                ).addOnFailureListener(
                        x -> liveData.postValue(false)
                );
        return liveData;
    }

    public MutableLiveData<List<Site>> recuperateAll(){
        MutableLiveData<List<Site>> liveData = new MutableLiveData<List<Site>>();

        db.collection("sites")
                .get()
                .addOnSuccessListener(
                        queryDocumentSnapshots -> {
                            List<Site> sites = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots.getDocuments()){
                                db.collection("tags")
                                        .document(documentSnapshot.getString("tagId"))
                                        .get()
                                        .addOnSuccessListener(
                                                x -> {
                                                    Site site = new Site();
                                                    TagSite tagSite = new TagSite();
                                                    site.setId(documentSnapshot.getString("id"));
                                                    site.setTitle(documentSnapshot.getString("title"));
                                                    site.setUrl(documentSnapshot.getString("url"));
                                                    tagSite.setTagId(x.getString("tagId"));
                                                    tagSite.setTag(x.getString("tag"));
                                                    site.setTag(tagSite);
                                                    sites.add(site);
                                                    liveData.postValue(sites);
                                                }
                                        );
                            }
                        }
                );
        return liveData;
    }

    public void delete(Site site) {
        String where = DatabaseContracts.TableSite.COLUMN_TITLE + " = ? and " +
                DatabaseContracts.TableSite.COLUMN_URL + " = ? ";

        String whereArgs[] = {site.getTitle(), site.getUrl()};

        mDatabase = mHelper.getWritableDatabase();
        mDatabase.delete(DatabaseContracts.TableSite.TABLE_NAME,
                where,
                whereArgs);
        mDatabase.close();
    }

    public boolean update(Site siteDesatualizado, Site siteAtualizado) {
        boolean answer;
        TagSiteDao tagDao = new TagSiteDao(/*context*/);

        //int tagId = tagDao.recuperateTagId(siteAtualizado.getTag());
        ContentValues values = new ContentValues();
        values.put(DatabaseContracts.TableSite.COLUMN_TITLE, siteAtualizado.getTitle());
        values.put(DatabaseContracts.TableSite.COLUMN_URL, siteAtualizado.getUrl());
        //values.put(DatabaseContracts.TableSite.COLUMN_TAG_ID, tagId);

        String where = DatabaseContracts.TableSite.COLUMN_TITLE + " = ? and " +
                DatabaseContracts.TableSite.COLUMN_URL + " = ? ";

        String whereArgs[] = {siteDesatualizado.getTitle(), siteDesatualizado.getUrl()};

        try {
            mDatabase = mHelper.getWritableDatabase();
            mDatabase.update(DatabaseContracts.TableSite.TABLE_NAME,
                    values,
                    where,
                    whereArgs);
            answer = true;
        }catch (Exception e){
            e.printStackTrace();
            answer = false;
        }
        return answer;
    }
}
