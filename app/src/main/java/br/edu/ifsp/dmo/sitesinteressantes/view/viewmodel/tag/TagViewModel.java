package br.edu.ifsp.dmo.sitesinteressantes.view.viewmodel.tag;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
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

    public MutableLiveData<Boolean> create(TagSite tagSite) {
        return tagSiteDao.create(tagSite);
    }

    public MutableLiveData<Boolean> update(TagSite oldtagSite, TagSite newTagSite) {
        return tagSiteDao.update(oldtagSite, newTagSite);
    }

    public MutableLiveData<Boolean> delete(TagSite tagSite) {
        return tagSiteDao.delete(tagSite);
    }
}
