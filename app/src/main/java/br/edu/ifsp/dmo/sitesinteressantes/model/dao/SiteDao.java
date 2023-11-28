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

    public MutableLiveData<Boolean> create(Site site){
        final String id  =  UUID.randomUUID().toString();
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

    public MutableLiveData<Boolean> delete(Site site) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();

        db.collection("sites")
                .document(site.getId())
                .delete()
                .addOnSuccessListener(
                        x ->  liveData.postValue(true)
                ).addOnFailureListener(
                        x ->  liveData.postValue(false)
                );

        return liveData;
    }

    public MutableLiveData<Boolean> update(Site siteAtualizado) {
        MutableLiveData<Boolean> liveData = new MutableLiveData<>();

        db.collection("sites")
                .document(siteAtualizado.getId())
                .update("title", siteAtualizado.getTitle(),
                        "url", siteAtualizado.getUrl(),
                        "tagId", siteAtualizado.getTag().getTagId())
                .addOnSuccessListener(
                        x ->  liveData.postValue(true)
                ).addOnFailureListener(
                        x ->  liveData.postValue(false)
                );

        return liveData;
    }
}
