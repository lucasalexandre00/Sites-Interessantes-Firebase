package br.edu.ifsp.dmo.sitesinteressantes.view.viewmodel.tag;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import br.edu.ifsp.dmo.sitesinteressantes.model.TagSite;
import br.edu.ifsp.dmo.sitesinteressantes.model.dao.TagSiteDao;

public class TagViewModel extends ViewModel {

    private TagSiteDao tagSiteDao = new TagSiteDao();
    public LiveData<List<TagSite>> recuperateAll(){
        MutableLiveData<List<TagSite>> liveData;
        liveData = tagSiteDao.recuperateAll();
        return liveData;
    }

}
