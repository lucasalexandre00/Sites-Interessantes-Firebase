package br.edu.ifsp.dmo.sitesinteressantes.view.viewmodel.site;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import br.edu.ifsp.dmo.sitesinteressantes.model.Site;
import br.edu.ifsp.dmo.sitesinteressantes.model.dao.SiteDao;

public class SiteViewModel  extends ViewModel {

    private SiteDao siteDao = new SiteDao();

    public LiveData<List<Site>> recuperateAll() {
        return siteDao.recuperateAll();
    }
}