package lyon.model.view.viewmodel

import android.app.Application
import android.content.Context
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import lyon.model.view.viewmodel.DataModel.onDataReadyCallback


class MainViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * 使用ViewModel有一點需特別注意的是不要儲存Activity/Fragment的內容或context在ViewModel中，
     * 因為configuration changes時當前的Activity及其內容會被destroy，
     * 就會變成存放被destroy的內容在ViewModel中而產生memory leak。
     * 若需要在ViewModel中使用Context的話可以改成使用AndroidViewModel，
     * 其constructor帶有application供我們取得context
     */
    private var mContext: Context= application.getApplicationContext()
    private val dataModel = DataModel(mContext)

    /**
     * 加入ObservableField用來存放資料，
     * 以及ObservableBoolean控制ProgressBar的顯示與否，
     * 在refresh()中依照情況用set()更新它們的value，
     */
    var mData = ObservableField<String>()
    var isLoading = ObservableBoolean(false)
    fun refresh() {
        isLoading.set(true);
        dataModel.retrieveData(object : onDataReadyCallback {
            override fun onDataReady(data: String?) {
                mData.set(data);
                isLoading.set(false);
            }
        })
    }
}